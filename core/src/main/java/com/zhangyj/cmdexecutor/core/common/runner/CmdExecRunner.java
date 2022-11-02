package com.zhangyj.cmdexecutor.core.common.runner;

import com.zhangyj.cmdexecutor.core.common.config.CmdExecConfig;
import com.zhangyj.cmdexecutor.core.common.util.FileUtils;
import com.zhangyj.cmdexecutor.core.common.util.TimeUtils;
import com.zhangyj.cmdexecutor.core.service.CmdExecService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @author zhangyj
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CmdExecRunner implements CommandLineRunner {

    private final CmdExecService cmdExecService;

    private final CmdExecConfig cmdExecConfig;

    @Override
    public void run(String... args) throws Exception {
        if (StringUtils.isBlank(cmdExecConfig.getDir())) {
            throw new IllegalArgumentException("配置项[cmd-executor.dir]不能为空");
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        cmdExecConfig.setShellPath(FileUtils.getResourcePath("cmd.sh"));
        cmdExecService.setConfig(cmdExecConfig);
        cmdExecService.exec();
        stopWatch.stop();
        log.info("总耗时：{}", TimeUtils.formatInterval(stopWatch.getTotalTimeMillis()));

    }
}
