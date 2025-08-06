package com.zhangyj.oneclick.core.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * shell命令类型的参数
 * @author zhangyj
 */
@ToString
@Getter
@RequiredArgsConstructor
public enum CmdTypeParamEnum implements CodeEnum<String> {

    /**
     * 是否打印输出
     */
    ENABLE_OUTPUT("enableOutput", true),
    /**
     * 检测到错误输出是否退出
     */
    ENABLE_EXIT_MATCH_ERROR("enableExitMatchError", true);

    private final String code;
    private final Object defValue;
}
