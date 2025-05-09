package com.zhangyj.oneclick.core.common.util;

import com.zhangyj.oneclick.core.common.enums.ValueEnum;

/**
 * @author zhang.yuejia1
 */
public class EnumUtils {

    public static <E extends Enum<E> & ValueEnum<T>, T> E getByValue(Class<E> enumClass, T value) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException("枚举" + enumClass.getSimpleName() + "非法枚举值: " + value);
    }

    public static boolean equalsAny(Object obj, ValueEnum... values) {
        for (ValueEnum<?> value : values) {
            if (value.getValue().equals(obj)) {
                return true;
            }
        }
        return false;
    }
}
