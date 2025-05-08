package com.zhangyj.oneclick.core.common.handler.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.zhangyj.oneclick.core.common.config.CmdExecConfig;
import com.zhangyj.oneclick.core.common.constant.CoreConstant;
import com.zhangyj.oneclick.core.common.enums.CmdTypeEnum;
import com.zhangyj.oneclick.core.common.factory.CmdLinePoFactory;
import com.zhangyj.oneclick.core.common.handler.CmdHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
        log.info(MessageFormatter.format(CoreConstant.CMD_LOG_BEFORE, "新增变量").getMessage());

        String[] split = CmdLinePoFactory.newInstance(cmdLine).getCmd().substring(SET.length()).split("=");
        String[] paramCmdSplit = Arrays.copyOf(split, 2);
        String param = split[0].trim();
        String value = (paramCmdSplit[1] != null ? split[1]: StrUtil.EMPTY).trim();
        CmdExecConfig.PARAM_MAP.put(param, value);
        log.info("新增变量：{}={}, 变量集合：{}", param, value, JSONUtil.toJsonStr(CmdExecConfig.PARAM_MAP));
    }

    @Override
    public CmdTypeEnum getCmdType() {
        return CmdTypeEnum.PARAM;
    }
}
