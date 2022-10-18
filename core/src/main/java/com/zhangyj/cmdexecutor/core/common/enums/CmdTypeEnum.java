package com.zhangyj.cmdexecutor.core.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 命令类型枚举
 * @author zhangyj
 */
@Getter
@RequiredArgsConstructor
public enum CmdTypeEnum {

    /**
     * shell命令
     */
    SHELL("shell"),

    /**
     * 组件命令
     */
    COMPONENT("component"),

    /**
     * 注释命令
     */
    COMMENT("#"),

    /**
     * 初始化变量
     */
    PARAM("param");

    private final String flag;
}
