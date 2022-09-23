package com.zhangyj.cmdexecutor.core.service;

import com.zhangyj.cmdexecutor.core.common.config.AbstractCmdConfig;
import com.zhangyj.cmdexecutor.core.common.config.CmdExecConfig;
import com.zhangyj.cmdexecutor.core.entity.bo.CmdParameterPO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangyj
 */
public abstract class AbstractCmdService<T extends AbstractCmdConfig> implements CmdService<T> {

    @Autowired
    protected CmdExecConfig cmdExecConfig;

    protected CmdParameterPO cmdParameter;

    /**
     * 通过反射调用setter设置
     */
    @Getter
    @Setter
    protected T config;
}
