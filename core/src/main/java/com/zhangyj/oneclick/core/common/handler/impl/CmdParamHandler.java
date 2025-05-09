package com.zhangyj.oneclick.core.common.handler.impl;

import cn.hutool.json.JSONUtil;
import com.zhangyj.oneclick.core.common.config.CmdExecConfig;
import com.zhangyj.oneclick.core.common.constant.CoreConstant;
import com.zhangyj.oneclick.core.common.enums.CmdTypeEnum;
import com.zhangyj.oneclick.core.common.factory.CmdLinePoFactory;
import com.zhangyj.oneclick.core.common.handler.CmdHandler;
import com.zhangyj.oneclick.core.entity.bo.CmdNamePO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Component;

/**
 * 处理set命令，初始化自定义变量
 * @author zhangyj
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CmdParamHandler implements CmdHandler {

    @Override
    public void handle(CmdExecConfig config, String cmdLine) {

        CmdNamePO cmdName = CmdLinePoFactory.newInstance(cmdLine).getCmdName();
        cmdName.getParamMap().forEach((param, value) -> {
            CmdExecConfig.PARAM_MAP.put(param, value);
            log.info("新增变量：{}={}, 变量集合：{}", param, value, JSONUtil.toJsonStr(CmdExecConfig.PARAM_MAP));
        });
    }

    @Override
    public CmdTypeEnum getCmdType() {
        return CmdTypeEnum.PARAM;
    }
}
