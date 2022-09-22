package com.zhangyj.cmdexecutor.core.service;

import com.zhangyj.cmdexecutor.core.common.config.CmdExecConfig;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangyj
 */
public abstract class AbstractCmdService implements CmdService {

    @Autowired
    protected CmdExecConfig cmdExecConfig;
}
