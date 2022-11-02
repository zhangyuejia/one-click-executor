package com.zhangyj.cmdexecutor.core.common.handler.impl;

import com.alibaba.fastjson.JSON;
import com.zhangyj.cmdexecutor.core.common.config.CmdExecConfig;
import com.zhangyj.cmdexecutor.core.common.enums.CmdTypeEnum;
import com.zhangyj.cmdexecutor.core.common.factory.CmdLinePoFactory;
import com.zhangyj.cmdexecutor.core.common.handler.CmdHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 处理set命令，初始化自定义变量
 * @author zhangyj
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CmdParamHandler implements CmdHandler {

    private final static String SET = "set ";

    @Override
    public void handle(CmdExecConfig config, String cmdLine) {
        String[] split = CmdLinePoFactory.newInstance(cmdLine).getCmd().substring(SET.length()).split("=");
        log.info("新增变量：{}={}", split[0], split[1]);
        config.getParamMap().put(split[0], split[1]);
        log.info("全部变量：{}", JSON.toJSONString(config.getParamMap()));
    }

    @Override
    public CmdTypeEnum getCmdType() {
        return CmdTypeEnum.PARAM;
    }
}
