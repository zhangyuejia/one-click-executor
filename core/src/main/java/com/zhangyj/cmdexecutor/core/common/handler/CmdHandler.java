package com.zhangyj.cmdexecutor.core.common.handler;

import com.zhangyj.cmdexecutor.core.common.config.CmdExecConfig;
import com.zhangyj.cmdexecutor.core.common.enums.CmdTypeEnum;
import com.zhangyj.cmdexecutor.core.entity.bo.CmdLinePO;

/**
 * @author zhangyj
 */
public interface CmdHandler {

    /**
     * 是否匹配到本执行器
     * @param cmd 命令
     * @return 匹配是否成功
     */
    default boolean match(String cmd){
        return cmd.startsWith(getCmdType().getFlag());
    }

    /**
     * 执行命令
     * @param config 配置
     * @param cmdLine 命令
     * @throws Exception 异常
     */
    void handle(CmdExecConfig config, String cmdLine) throws Exception;

    /**
     * 获取命令类型
     * @return 命令类型
     */
    CmdTypeEnum getCmdType();
}
