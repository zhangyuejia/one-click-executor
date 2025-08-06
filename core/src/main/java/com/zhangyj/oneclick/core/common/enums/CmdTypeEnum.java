package com.zhangyj.oneclick.core.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 命令类型枚举
 * @author zhangyj
 */
@Getter
@RequiredArgsConstructor
@ToString
public enum CmdTypeEnum implements CodeEnum<String> {

    /**
     * shell命令
     */
    SHELL("shell"),

    /**
     * 组件命令
     */
    COMPONENT("component"),

    /**
     * 自定义变量命令，可用于变量值替换（1.cmd.sh替换 2.yml变量替换）
     */
    PARAM("param");

    private final String code;
}
