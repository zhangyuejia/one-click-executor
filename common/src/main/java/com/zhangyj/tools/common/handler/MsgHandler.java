package com.zhangyj.tools.common.handler;

/**
 * @author zhangyj
 */
public interface MsgHandler {

    /**
     * 处理信息
     * @param msg 信息
     * @throws Exception 异常
     */
    void handle(String msg) throws Exception;
}
