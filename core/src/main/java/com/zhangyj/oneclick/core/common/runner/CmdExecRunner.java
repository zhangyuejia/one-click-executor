package com.zhangyj.oneclick.core.common.runner;

import cn.hutool.core.collection.CollUtil;
import com.zhangyj.oneclick.core.common.config.CmdExecConfig;
import com.zhangyj.oneclick.core.common.util.FileUtils;
import com.zhangyj.oneclick.core.common.util.TimeUtils;
import com.zhangyj.oneclick.core.service.CmdExecService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author zhangyj
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CmdExecRunner implements CommandLineRunner {

    private final CmdExecService cmdExecService;

    private final CmdExecConfig cmdExecConfig;

    public static final List<CompletableFuture<Void>> FUTURE_LIST = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void run(String... args) throws Exception {
        if (StringUtils.isBlank(cmdExecConfig.getDir())) {
            throw new IllegalArgumentException("配置项[one-click-executor.dir]不能为空");
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        cmdExecConfig.setShellPath(FileUtils.getResourcePath("cmd.sh"));
        cmdExecService.setConfig(cmdExecConfig);
        cmdExecService.exec();

        if (CollUtil.isNotEmpty(FUTURE_LIST)) {
            CompletableFuture.allOf(FUTURE_LIST.toArray(new CompletableFuture[0])).join();
        }
        stopWatch.stop();
        log.info("总耗时：{}", TimeUtils.formatInterval(stopWatch.getTotalTimeMillis()));

    }

    public static void execTask(Boolean isAsync, Runnable exec) {
        if (isAsync) {
            CmdExecRunner.FUTURE_LIST.add(CompletableFuture.runAsync(exec));
        }else {
            exec.run();
        }
    }
}
