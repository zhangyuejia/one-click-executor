package com.zhangyj.oneclick.core.common.enums;

/**
 * 值枚举
 * @author zhang.yuejia1
 */
public interface CodeEnum<T> {
    /**
     * 获取通用枚举值
     * @return 通用枚举值
     */
    T getCode();

    /**
     * 编码是否一致
     * @param codeObj 编码对象
     * @return 是否一致
     */
    default boolean codeEquals(Object codeObj) {
        return getCode().toString().equals(codeObj.toString());
    }
}