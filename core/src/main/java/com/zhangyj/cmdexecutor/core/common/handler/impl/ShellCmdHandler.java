package com.zhangyj.cmdexecutor.core.common.handler.impl;

import com.zhangyj.cmdexecutor.core.common.config.CmdExecConfig;
import com.zhangyj.cmdexecutor.core.common.enums.CmdTypeEnum;
import com.zhangyj.cmdexecutor.core.common.handler.CmdHandler;
import com.zhangyj.cmdexecutor.core.common.util.CommandUtils;
import com.zhangyj.cmdexecutor.core.entity.bo.CmdLinePO;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * shell命令handler
 * @author zhangyj
 */
@Component
public class ShellCmdHandler implements CmdHandler {

    @Override
    public void handle(CmdExecConfig config, CmdLinePO cmdLinePo) throws Exception {
        if(cmdLinePo.getDir() == null){
            cmdLinePo.setDir(config.getDir());
        }
        CommandUtils.execCommand(Charset.forName(config.getCharset()), cmdLinePo.getCmd(), cmdLinePo.getDir(), new DefaultStringHandler());
    }

    @Override
    public CmdTypeEnum getCmdType() {
        return CmdTypeEnum.SHELL;
    }
}
