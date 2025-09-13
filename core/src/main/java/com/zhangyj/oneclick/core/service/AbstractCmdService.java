package com.zhangyj.oneclick.core.service;

import com.zhangyj.oneclick.core.common.config.AbstractCmdConfig;
import com.zhangyj.oneclick.core.common.config.CmdExecConfig;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangyj
 */
public abstract class AbstractCmdService<T extends AbstractCmdConfig> implements CmdService<T> {

    @Autowired
    protected CmdExecConfig cmdExecConfig;
}
