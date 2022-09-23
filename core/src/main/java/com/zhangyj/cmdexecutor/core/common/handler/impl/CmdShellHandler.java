package com.zhangyj.cmdexecutor.core.common.handler.impl;

import com.alibaba.fastjson.JSON;
import com.zhangyj.cmdexecutor.core.common.config.CmdExecConfig;
import com.zhangyj.cmdexecutor.core.common.enums.CmdTypeEnum;
import com.zhangyj.cmdexecutor.core.common.factory.CmdLinePoFactory;
import com.zhangyj.cmdexecutor.core.common.handler.CmdHandler;
import com.zhangyj.cmdexecutor.core.common.util.CommandUtils;
import com.zhangyj.cmdexecutor.core.entity.bo.CmdLinePO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

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
        log.info("cmd:" + JSON.toJSONString(cmdLinePo));
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
