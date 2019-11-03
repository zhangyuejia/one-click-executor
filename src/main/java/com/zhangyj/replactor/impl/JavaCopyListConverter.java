package com.zhangyj.replactor.impl;

import com.google.common.collect.Sets;
import com.zhangyj.config.Config;
import com.zhangyj.constant.Const;
import com.zhangyj.pojo.JavaFilePath;
import com.zhangyj.replactor.BaseCopyListConverter;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * java文件路径替换器
 * @author ZHANG
 */
@Component
public class JavaCopyListConverter extends BaseCopyListConverter {

    public JavaCopyListConverter(Config config) {
        super(config);
    }

    @Override
    protected Set<String> toCopyListRelativePath(String relativePath) throws Exception {
        Set<String> data = Sets.newTreeSet();
        // class文件相对路径
        String classFileRelativePath = "/WEB-INF/classes"
                + relativePath.substring(relativePath.indexOf("/"), relativePath.length() - Const.JAVA.length())
                + Const.CLASS;
        // 加入主类copyList
        data.add(classFileRelativePath);
        // class文件绝对路径
        String classFilePositivePath = config.getEmp().getOutPutPath() + "/" + classFileRelativePath;
        // 内部类绝对路径
        Set<String> innerClassPaths = new JavaFilePath(classFilePositivePath).innerClassPaths();
        innerClassPaths.forEach(path ->
                data.add(path.substring(config.getEmp().getOutPutPath().length())));
        return data;
    }

}
