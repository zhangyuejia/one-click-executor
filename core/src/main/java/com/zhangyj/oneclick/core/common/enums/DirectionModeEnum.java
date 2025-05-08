package com.zhangyj.oneclick.core.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 方向枚举
 * @author zhang.yuejia1
 */
@Getter
@RequiredArgsConstructor
public enum DirectionModeEnum implements ValueEnum<String>{
    /**
     * 来
     */
    FROM("from"),
    /**
     * 去
     */
    TO("to");

    private final String value;
}
