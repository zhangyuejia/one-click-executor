package com.zhangyj.oneclick.core.service;

import com.zhangyj.oneclick.core.common.config.AbstractCmdConfig;
import com.zhangyj.oneclick.core.common.config.CmdExecConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangyj
 */
public abstract class AbstractCmdService<T extends AbstractCmdConfig> implements CmdService<T> {

    @Autowired
    protected CmdExecConfig cmdExecConfig;

    /**
     * 通过反射调用setter设置
     */
    @Getter
    @Setter
    protected T config;
}
