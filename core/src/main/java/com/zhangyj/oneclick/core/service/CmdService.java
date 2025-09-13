package com.zhangyj.oneclick.core.service;


import com.zhangyj.oneclick.core.common.config.AbstractCmdConfig;

/**
 * @author zhangyj
 */
public interface CmdService<T extends AbstractCmdConfig> {

    /**
     * 执行命令
     * @throws Exception 异常
     */
    void exec(T config) throws Exception;

    /**
     * 获取描述
     * @return 描述
     */
    String getDesc();
}
