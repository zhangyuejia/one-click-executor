package com.zhangyj.cmdexecutor.core.service;

import com.zhangyj.cmdexecutor.core.common.config.CmdConfig;
import com.zhangyj.cmdexecutor.core.common.config.CmdExecConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangyj
 */
public abstract class AbstractCmdService<T extends CmdConfig> implements CmdService<T> {

    @Autowired
    protected CmdExecConfig cmdExecConfig;

    @Getter
    @Setter
    protected T config;
}
