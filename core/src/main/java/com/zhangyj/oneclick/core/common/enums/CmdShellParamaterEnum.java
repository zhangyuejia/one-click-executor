package com.zhangyj.oneclick.core.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * shell命令类型的参数
 * @author zhangyj
 */
@Getter
@RequiredArgsConstructor
public enum CmdShellParamaterEnum implements ValueEnum<String> {

    /**
     * 是否打印输出
     */
    ENABLE_OUTPUT("enableOutput");

    private final String value;

}
