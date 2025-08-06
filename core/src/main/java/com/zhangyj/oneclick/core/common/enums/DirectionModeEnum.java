package com.zhangyj.oneclick.core.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 方向枚举
 * @author zhang.yuejia1
 */
@ToString
@Getter
@RequiredArgsConstructor
public enum DirectionModeEnum implements CodeEnum<String> {
    /**
     * 来
     */
    FROM("from"),
    /**
     * 去
     */
    TO("to");

    private final String code;
}
