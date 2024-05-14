package com.zhangyj.oneclick.component.service.impl;

import com.zhangyj.oneclick.component.common.config.CmdSleepConfig;
import com.zhangyj.oneclick.core.service.AbstractCmdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author zhangyj
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CmdSleepServiceImpl extends AbstractCmdService<CmdSleepConfig> {

    @Override
    public void exec() throws Exception {
        Integer seconds = config.getT();
        log.info("睡眠时间{}秒", seconds);
        TimeUnit.SECONDS.sleep(seconds);
    }

    @Override
    public String getDesc() {
        return "睡眠功能";
    }
}
