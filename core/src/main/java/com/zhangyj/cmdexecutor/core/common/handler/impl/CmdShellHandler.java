package com.zhangyj.cmdexecutor.core.common.handler.impl;

import com.zhangyj.cmdexecutor.core.common.config.CmdExecConfig;
import com.zhangyj.cmdexecutor.core.common.constant.CoreConstant;
import com.zhangyj.cmdexecutor.core.common.enums.CmdTypeEnum;
import com.zhangyj.cmdexecutor.core.common.factory.CmdLinePoFactory;
import com.zhangyj.cmdexecutor.core.common.handler.CmdHandler;
import com.zhangyj.cmdexecutor.core.common.handler.StringHandler;
import com.zhangyj.cmdexecutor.core.common.util.CommandUtils;
import com.zhangyj.cmdexecutor.core.entity.bo.CmdLinePO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.zhangyj.cmdexecutor.core.common.constant.CoreConstant.CMD_LOG_BEFORE;

/**
 * shell命令handler
 * @author zhangyj
 */
@Slf4j
@Component
public class CmdShellHandler implements CmdHandler {

    @Override
    public void handle(CmdExecConfig config, String cmdLine) throws Exception {
        log.info(CoreConstant.CMD_LOG_BEFORE);
        log.info("解析命令：" + cmdLine);
        CmdLinePO cmdLinePo = CmdLinePoFactory.newInstance(cmdLine);
        if(cmdLinePo.getDir() == null){
            cmdLinePo.setDir(config.getDir());
        }
        StringHandler stringHandler = null;
        if(cmdLinePo.getCmdType().getCmdTypeParameter().getEnableOutput()){
            stringHandler = new CheckStringHandler(config);
        }
        CommandUtils.execCommand(config.getCharset(), cmdLinePo.getCmd(), cmdLinePo.getDir(), stringHandler);
    }

    @Override
    public CmdTypeEnum getCmdType() {
        return CmdTypeEnum.SHELL;
    }
}
