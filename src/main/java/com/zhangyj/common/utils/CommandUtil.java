package com.zhangyj.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 命令执行器
 * @author zhangyj
 */
@Slf4j
public class CommandUtil {

    /**
     * 获取命令读取流
     * @param command 命令
     * @return 命令读取流
     * @throws IOException 异常
     */
    public static BufferedReader getCommandReader(Charset charset, String command) throws IOException {
        return new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(command).getInputStream(), charset));
    }

    /**
     * 获取命令读取流
     * @param command 命令
     * @return 命令读取流
     * @throws IOException 异常
     */
    public static BufferedReader getCommandReader(Charset charset, String[] command, String dir) throws IOException {
        return new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(command, null, new File(dir)).getInputStream(), charset));
    }

    public static List<String> getCommandOutput(Charset charset, String command) throws Exception {
        log.info("执行命令：{}", command);
        try (BufferedReader reader = CommandUtil.getCommandReader(charset, command)){
            return reader.lines().collect(Collectors.toList());
        }
    }
}
