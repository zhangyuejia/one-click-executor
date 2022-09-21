package com.zhangyj.cmdexecutor.core.common.runner;

import com.zhangyj.cmdexecutor.core.common.config.CmdExecConfig;
import com.zhangyj.cmdexecutor.core.service.CmdExecService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author zhangyj
 */
@Component
@RequiredArgsConstructor
public class CmdExecRunner implements CommandLineRunner {

    private final CmdExecService cmdExecService;

    private final CmdExecConfig cmdExecConfig;

    @Override
    public void run(String... args) throws Exception {
        cmdExecService.exec(cmdExecConfig);
    }
}
