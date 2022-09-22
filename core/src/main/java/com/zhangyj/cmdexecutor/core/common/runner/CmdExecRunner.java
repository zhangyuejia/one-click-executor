package com.zhangyj.cmdexecutor.core.common.runner;

import com.zhangyj.cmdexecutor.core.common.config.CmdExecConfig;
import com.zhangyj.cmdexecutor.core.service.CmdExecService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
        if (StringUtils.isBlank(cmdExecConfig.getDir())) {
            throw new IllegalArgumentException("配置项[cmd-executor.dir]不能为空");
        }
        cmdExecConfig.setBootLoad(true);
        cmdExecService.exec(cmdExecConfig);
    }
}
