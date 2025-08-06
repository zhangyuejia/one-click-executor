package com.zhangyj.oneclick.core.common.util;

import cn.hutool.core.thread.ThreadUtil;
import com.zhangyj.oneclick.core.common.handler.CmdOutputHandler;
import com.zhangyj.oneclick.core.entity.bo.CmdProcessBo;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 命令执行器
 * @author zhangyj
 */
@Slf4j
public class CommandUtils {

    public static void execCommand(String charset, String command, String dir, CmdOutputHandler handler) throws Exception{
        handleExecCommand(charset, exec(command, dir), handler);
    }

    public static List<String> execCommand(String charset, String command, String dir) throws Exception {
        List<String> list = new ArrayList<>();
        CmdProcessBo cmdProcessBo = exec(command, dir);
        handleExecCommand(charset, cmdProcessBo, (cmdProcess, str) -> list.add(str));
        return list;
    }

    private static void handleExecCommand(String charset, CmdProcessBo cmdProcessBo, CmdOutputHandler handler) throws InterruptedException {
        Process process = cmdProcessBo.getProcess();
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
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))){
                    String line;
                    while ((line = reader.readLine()) != null){
                        handler.handle(cmdProcessBo, line);
                    }
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
                countDownLatch.countDown();
            }, true);
        }
        // waitFor必须加载读取输入输出流后面,不然会阻塞
        process.waitFor();
        countDownLatch.await();
    }

    private static CmdProcessBo exec(String command, String dir) throws Exception {
        log.info("执行命令：{}{}", command, dir != null ? " 地址：" + dir : "");
        Runtime runtime = Runtime.getRuntime();
        final Process process = dir == null? runtime.exec(command): runtime.exec(command, null, new File(dir));
        //noinspection AlibabaAvoidManuallyCreateThread
        runtime.addShutdownHook(new Thread(process::destroy));
        return new CmdProcessBo(command, dir, process);
    }

}
