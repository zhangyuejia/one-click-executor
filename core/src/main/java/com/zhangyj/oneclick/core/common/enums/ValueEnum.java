package com.zhangyj.oneclick.core.common.enums;

/**
 * 值枚举
 * @author zhang.yuejia1
 */
public interface ValueEnum<T> {
    /**
     * 获取通用枚举值
     * @return 通用枚举值
     */
    T getValue();
}