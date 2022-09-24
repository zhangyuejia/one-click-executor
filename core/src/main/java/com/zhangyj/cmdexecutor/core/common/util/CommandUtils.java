package com.zhangyj.cmdexecutor.core.common.util;

import cn.hutool.core.thread.ThreadUtil;
import com.zhangyj.cmdexecutor.core.common.handler.StringHandler;
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
public class CommandUtils {

    public static void execCommand(Charset charset, String command, String dir, StringHandler handler) throws Exception{
        handleExecCommand(charset, exec(command, dir), handler);
    }

    public static List<String> execCommand(Charset charset, String command, String dir) throws Exception {
        return execCommand(charset, exec(command, dir));
    }

    private static List<String> execCommand(Charset charset, Process process) throws InterruptedException {
        List<String> list = new ArrayList<>();
        handleExecCommand(charset, process, list::add);
        return list;
    }

    private static void handleExecCommand(Charset charset, Process process, StringHandler handler) throws InterruptedException {
        if(handler == null){
            // 此处是为了执行cmd -c start弹出cmd黑框,不等待不会弹出
            process.waitFor();
            return;
        }
        InputStream[] inputStreams = {process.getInputStream(), process.getErrorStream()};
        CountDownLatch countDownLatch = new CountDownLatch(inputStreams.length);
        for (InputStream inputStream : inputStreams) {
            // 异步读取输入流,不然会阻塞
            // 设置为守护线程，主线程执行完自动结束,不然执行完命令程序不会自动结束
            ThreadUtil.execAsync(() -> {
                handleExecCommand(charset, inputStream, handler);
                countDownLatch.countDown();
            }, true);
        }
        // waitFor必须加载读取输入输出流后面,不然会阻塞
        process.waitFor();
        countDownLatch.await();
    }

    private static void handleExecCommand(Charset charset, InputStream inputStream, StringHandler handler){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))){
            String line;
            while ((line = reader.readLine()) != null){
                handler.handle(line);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static Process exec(String command, String dir) throws Exception {
        log.info("执行命令：{}" + (dir != null? " 地址：" + dir: ""), command);
        Runtime runtime = Runtime.getRuntime();
        final Process process = dir == null? runtime.exec(command): runtime.exec(command, null, new File(dir));
        //noinspection AlibabaAvoidManuallyCreateThread
        runtime.addShutdownHook(new Thread(process::destroy));
        return process;
    }

}
