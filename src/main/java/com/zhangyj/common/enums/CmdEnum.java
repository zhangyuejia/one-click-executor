package com.zhangyj.common.enums;

import lombok.RequiredArgsConstructor;

/**
 * @author zhangyj
 */
@RequiredArgsConstructor
public enum CmdEnum {
    /**
     * ping百度一次
    */
    PING_ONE("ping www.baidu.com -n 1");


    private final String cmd;
}
