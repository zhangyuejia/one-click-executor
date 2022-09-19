package com.zhangyj.tools.common.utils;

import cn.hutool.core.thread.ThreadUtil;
import com.zhangyj.tools.common.handler.MsgHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 命令执行器
 * @author zhangyj
 */
@Slf4j
public class CommandUtil {

    public static void execCommand(Charset charset, String command, String dir, MsgHandler handler) throws Exception{
        handleExecCommand(charset, exec(command, dir), handler);
    }

    public static List<String> execCommand(Charset charset, String command) throws Exception {
        return execCommand(charset, exec(command, null));
    }

    public static List<String> execCommand(Charset charset, String command, String dir) throws Exception {
        return execCommand(charset, exec(command, dir));
    }

    private static List<String> execCommand(Charset charset, Process process) throws InterruptedException {
        List<String> list = new ArrayList<>();
        handleExecCommand(charset, process, list::add);
        return list;
    }

    private static void handleExecCommand(Charset charset, Process process, MsgHandler handler) throws InterruptedException {
        if(handler == null){
            return;
        }
        InputStream[] inputStreams = {process.getInputStream(), process.getErrorStream()};
        CountDownLatch countDownLatch = new CountDownLatch(inputStreams.length);
        for (InputStream inputStream : inputStreams) {
            // 设置为守护线程，主线程执行完自动结束
            ThreadUtil.execAsync(() -> {
                handleExecCommand(charset, inputStream, handler);
                countDownLatch.countDown();
            }, true);
        }
        countDownLatch.await();
    }

    private static void handleExecCommand(Charset charset, InputStream inputStream, MsgHandler handler){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))){
            String line;
            while ((line = reader.readLine()) != null){
                handler.handle(line);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static Process exec(String command, String dir) throws IOException {
        log.info("执行命令：{}" + (dir != null? "地址：" + dir: ""), command);
        Runtime runtime = Runtime.getRuntime();
        final Process process = dir == null? runtime.exec(command): runtime.exec(command, null, new File(dir));
        //noinspection AlibabaAvoidManuallyCreateThread
        runtime.addShutdownHook(new Thread(process::destroy));
        return process;
    }

}
