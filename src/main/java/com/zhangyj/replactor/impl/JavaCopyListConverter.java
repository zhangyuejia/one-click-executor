package com.zhangyj.replactor.impl;

import com.zhangyj.config.Config;
import com.zhangyj.constant.Const;
import com.zhangyj.pojo.JavaFilePath;
import com.zhangyj.replactor.BaseCopyListConverter;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Set<String> toCopyListLines(String relativePath) throws Exception {
        String classFileRelativePath = "WEB-INF\\classes" + relativePath.substring(relativePath.indexOf("/"), relativePath.length() - Const.javaFileSuffix.length()) + Const.classFileSuffix;
        String classFilePositivePath = config.getEmp().getOutPutPath() + classFileRelativePath;
        Set<String> innerClassPaths = new JavaFilePath(classFilePositivePath).innerClassPaths();
        Set<String> data = Stream.of(config.getCopyList().getPrefix() + classFileRelativePath).collect(Collectors.toSet());
        if(innerClassPaths.size() > 0){
            data.addAll(innerClassPaths.stream().map(path -> path.substring(config.getEmp().getOutPutPath().length())).collect(Collectors.toSet()));
        }
        return data;
    }

}
