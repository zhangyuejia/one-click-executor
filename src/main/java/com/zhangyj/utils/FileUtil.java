package com.zhangyj.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
/**
 * @author zhangyj
 */
@Slf4j
public class FileUtil {

    /**
     * 保存文件
     * @param path 路径
     * @param data 数据
     */
    public static void save(String path, Collection<String> data) throws Exception {
        if(CollectionUtils.isEmpty(data)){
            log.error("数据为空，无需写入文件!");
            return;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path))){
            for (String d : data) {
                writer.write(d);
                writer.newLine();
            }
        }
    }
}
