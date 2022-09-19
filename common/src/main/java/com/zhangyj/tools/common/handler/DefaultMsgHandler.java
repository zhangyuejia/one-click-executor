package com.zhangyj.tools.common.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangyj
 */
@Slf4j
public class DefaultMsgHandler implements MsgHandler{

    @Override
    public void handle(String msg) {
        log.info(msg);
    }
}
