package com.zhangyj.cmdexecutor.core.service;


import com.zhangyj.cmdexecutor.core.common.config.CmdConfig;

/**
 * @author zhangyj
 */
public interface CmdService {

    /**
     * 执行命令
     * @param c 配置
     * @throws Exception 异常
     */
    void exec(CmdConfig c) throws Exception;

    /**
     * 获取配置class
     * @return 配置class
     */
    Class<? extends CmdConfig> getConfigClass();
}
