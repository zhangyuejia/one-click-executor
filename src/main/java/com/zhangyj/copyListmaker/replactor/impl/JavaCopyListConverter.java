package com.zhangyj.copyListmaker.replactor.impl;

import com.google.common.collect.Sets;
import com.zhangyj.copyListmaker.config.Config;
import com.zhangyj.common.constant.Const;
import com.zhangyj.copyListmaker.pojo.JavaFilePath;
import com.zhangyj.copyListmaker.replactor.BaseCopyListConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * java文件路径替换器
 * @author ZHANG
 */
@Slf4j
@Component
@ConditionalOnBean(Config.class)
public class JavaCopyListConverter extends BaseCopyListConverter {

    public JavaCopyListConverter(Config config) {
        super(config);
    }

    @Override
    protected Set<String> toCopyListRelativePath(String relativePath) throws Exception {
        Set<String> data = Sets.newTreeSet();
        // class文件相对路径
        String classFileRelativePath = Const.WEB_INF_CLASSES
                + relativePath.substring(relativePath.indexOf("/"), relativePath.length() - Const.JAVA.length())
                + Const.CLASS;
        // 加入主类copyList
        data.add(classFileRelativePath);
        // class文件绝对路径
        String classFilePositivePath = config.getEmp().getOutputPath() + "/" + classFileRelativePath;
        // 内部类绝对路径
        Set<String> innerClassPaths = new JavaFilePath(classFilePositivePath).innerClassPaths();
        innerClassPaths.forEach(path -> data.add(path.substring(config.getEmp().getOutputPath().length())));
        return data;
    }

}
