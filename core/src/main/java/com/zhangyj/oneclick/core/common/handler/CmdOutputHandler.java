package com.zhangyj.oneclick.core.common.handler;

import com.zhangyj.oneclick.core.entity.bo.CmdProcessBo;

/**
 * @author zhangyj
 */
public interface CmdOutputHandler {

    /**
     * 处理信息
     * @param str 信息
     * @param cmdProcessBo cmd进程业务类
     * @throws Exception 异常
     */
    void handle(CmdProcessBo cmdProcessBo, String str) throws Exception;
}
