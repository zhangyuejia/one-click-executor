package com.zhangyj.oneclick.core.common.handler.impl;

import com.zhangyj.oneclick.core.common.config.CmdExecConfig;
import com.zhangyj.oneclick.core.common.constant.CoreConstant;
import com.zhangyj.oneclick.core.common.enums.CmdShellParamaterEnum;
import com.zhangyj.oneclick.core.common.enums.CmdTypeEnum;
import com.zhangyj.oneclick.core.common.factory.CmdLinePoFactory;
import com.zhangyj.oneclick.core.common.handler.CmdHandler;
import com.zhangyj.oneclick.core.common.handler.StringHandler;
import com.zhangyj.oneclick.core.common.runner.CmdExecRunner;
import com.zhangyj.oneclick.core.common.util.CommandUtils;
import com.zhangyj.oneclick.core.entity.bo.CmdLinePO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.stereotype.Component;

/**
 * shell命令handler
 * @author zhangyj
 */
@Slf4j
@Component
public class CmdShellHandler implements CmdHandler {

    @Override
    public void handle(CmdExecConfig config, String cmdLine) throws Exception {
        CmdLinePO cmdLinePo = CmdLinePoFactory.newInstance(cmdLine);
        if(cmdLinePo.getDir() == null){
            cmdLinePo.setDir(CmdExecConfig.PARAM_MAP.get(CoreConstant.PARAM_DIR).toString());
        }
        StringHandler stringHandler = null;
        Object enableOutputObj = cmdLinePo.getCmdType().getParamMap().get(CmdShellParamaterEnum.ENABLE_OUTPUT.getValue());
        if(Boolean.TRUE.toString().equals(enableOutputObj)){
            stringHandler = new CheckStringHandler(config);
        }
        CommandUtils.execCommand(config.getCharset(), cmdLinePo.getCmdName().getValue(), cmdLinePo.getDir(), stringHandler);
    }

    @Override
    public CmdTypeEnum getCmdType() {
        return CmdTypeEnum.SHELL;
    }
}
