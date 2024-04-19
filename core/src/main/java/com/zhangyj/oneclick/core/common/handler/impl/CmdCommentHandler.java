package com.zhangyj.oneclick.core.common.handler.impl;

import com.zhangyj.oneclick.core.common.config.CmdExecConfig;
import com.zhangyj.oneclick.core.common.enums.CmdTypeEnum;
import com.zhangyj.oneclick.core.common.handler.CmdHandler;
import org.springframework.stereotype.Component;

/**
 * 注释命令handler
 * @author zhangyj
 */
@Component
public class CmdCommentHandler implements CmdHandler {

    @Override
    public void handle(CmdExecConfig config, String cmdLine) {

    }

    @Override
    public CmdTypeEnum getCmdType() {
        return CmdTypeEnum.COMMENT;
    }
}
