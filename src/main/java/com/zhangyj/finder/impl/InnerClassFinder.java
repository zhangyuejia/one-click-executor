package com.zhangyj.finder.impl;

import com.zhangyj.finder.Finder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
@Component
public class InnerClassFinder implements Finder<String, Set<String>> {

    @Override
    public Set<String> find(String classPath) throws Exception {
        String innerClassPathPrefix = getInnerClassPathPrefix(classPath);
        return Files.list(Paths.get(classPath))
                .filter(Files::isRegularFile)
                .filter((path -> path.toString().startsWith(innerClassPathPrefix)))
                .map(Path::toString).collect(Collectors.toSet());
    }

    private String getInnerClassPathPrefix(String classPath) {
        return null;
    }

}
