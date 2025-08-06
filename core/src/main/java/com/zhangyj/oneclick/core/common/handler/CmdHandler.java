package com.zhangyj.oneclick.core.common.handler;

import com.zhangyj.oneclick.core.common.config.CmdExecConfig;
import com.zhangyj.oneclick.core.common.enums.CmdTypeEnum;

/**
 * @author zhangyj
 */
public interface CmdHandler {

    /**
     * 执行命令
     * @param config 配置
     * @throws Exception 异常
     */
    void handle(CmdExecConfig config) throws Exception;

    /**
     * 获取命令类型
     * @return 命令类型
     */
    CmdTypeEnum getCmdType();
}
