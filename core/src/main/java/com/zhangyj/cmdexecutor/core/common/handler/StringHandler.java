package com.zhangyj.cmdexecutor.core.common.handler;

/**
 * @author zhangyj
 */
public interface StringHandler {

    /**
     * 处理信息
     * @param str 信息
     * @throws Exception 异常
     */
    void handle(String str) throws Exception;
}
