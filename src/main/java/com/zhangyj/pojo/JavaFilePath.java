package com.zhangyj.pojo;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 内部类查询者
 * @author ZHANG
 */
@Slf4j
public class JavaFilePath extends FilePath {

    public JavaFilePath(String path) {
        super(path);
    }

    /**
     * 查询类的内部类集合
     * @return 该类的内部类文件路径集合
     * @throws Exception 异常
     */
    public Set<String> innerClassPaths() throws Exception {
        String innerClassPathPrefix = getDir() + getFileName() + "$";
        return Files.list(Paths.get(getDir()))
                .filter(Files::isRegularFile)
                .filter((path -> path.toString().startsWith(innerClassPathPrefix)))
                .map(Path::toString).collect(Collectors.toSet());
    }



}
