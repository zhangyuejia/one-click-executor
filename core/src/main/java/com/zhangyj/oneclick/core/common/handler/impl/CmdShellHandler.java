package com.zhangyj.oneclick.core.common.handler.impl;

import com.zhangyj.oneclick.core.common.config.CmdExecConfig;
import com.zhangyj.oneclick.core.common.constant.CoreConstant;
import com.zhangyj.oneclick.core.common.enums.CmdTypeEnum;
import com.zhangyj.oneclick.core.common.handler.CmdHandler;
import com.zhangyj.oneclick.core.common.runner.CmdExecRunner;
import com.zhangyj.oneclick.core.common.util.CommandUtils;
import com.zhangyj.oneclick.core.entity.bo.CmdLineBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * shell命令handler
 * @author zhangyj
 */
@Slf4j
@Component
public class CmdShellHandler implements CmdHandler {

    @Override
    public void handle(CmdExecConfig config) throws Exception {
        CmdLineBo cmdLineBo = config.getCmdLineBo();
        if(cmdLineBo.getDir() == null){
            cmdLineBo.setDir(CmdExecConfig.PARAM_MAP.get(CoreConstant.PARAM_DIR).toString());
        }
        CmdExecRunner.execTask(cmdLineBo.getIsAsync(),
                () -> {
                    try {
                        CommandUtils.execCommand(config.getCharset(), cmdLineBo.getCmdName().getValue(),
                                cmdLineBo.getDir(), new CheckCmdOutputHandler(config));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public CmdTypeEnum getCmdType() {
        return CmdTypeEnum.SHELL;
    }
}
