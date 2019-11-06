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
        String dir = getDir();
        String innerClassNamePrefix = getFileName() + "$";
        return Files.list(Paths.get(dir))
                .filter(Files::isRegularFile)
                .filter((path -> {
                    String classFileName = path.toString().substring(dir.length());
                    boolean isInnerClass = classFileName.startsWith(innerClassNamePrefix);
                    if(isInnerClass){
                        log.info("发现内部类，自动写入：{}", classFileName);
                    }
                    return isInnerClass;
                })).map(Path::toString).collect(Collectors.toSet());
    }



}
