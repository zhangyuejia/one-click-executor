package com.zhangyj.cmdexecutor.core.common.handler.impl;

import cn.hutool.json.JSONUtil;
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
        CmdExecConfig.PARAM_MAP.put(split[0], split[1]);
        log.info("全部变量：{}", JSONUtil.toJsonStr(CmdExecConfig.PARAM_MAP));
    }

    @Override
    public CmdTypeEnum getCmdType() {
        return CmdTypeEnum.PARAM;
    }
}
