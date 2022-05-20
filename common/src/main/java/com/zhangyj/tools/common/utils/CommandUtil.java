package com.zhangyj.tools.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
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
        return new BufferedReader(new InputStreamReader(exec(command).getInputStream(), charset));
    }

    /**
     * 获取命令读取流
     * @param command 命令
     * @return 命令读取流
     * @throws IOException 异常
     */
    public static BufferedReader getCommandReader(Charset charset, String[] command, String dir) throws IOException {
        return new BufferedReader(new InputStreamReader(exec(command, new File(dir)).getInputStream(), charset));
    }

    public static List<String> getCommandOutput(Charset charset, String command) throws Exception {
        try (BufferedReader reader = CommandUtil.getCommandReader(charset, command)){
            return reader.lines().collect(Collectors.toList());
        }
    }

    public static Process exec(String command) throws IOException {
        log.info("执行命令：{}", command);
        Runtime runtime = Runtime.getRuntime();
        final Process process = runtime.exec(command);
        //noinspection AlibabaAvoidManuallyCreateThread
        runtime.addShutdownHook(new Thread(process::destroy));
        return process;
    }

    public static Process exec(String[] command, File dir) throws IOException {
        log.info("执行命令：{} 地址为：{}", Arrays.toString(command), dir.getCanonicalPath());
        Runtime runtime = Runtime.getRuntime();
        final Process process = runtime.exec(command, null, dir);
        //noinspection AlibabaAvoidManuallyCreateThread
        runtime.addShutdownHook(new Thread(process::destroy));
        return process;
    }
}
