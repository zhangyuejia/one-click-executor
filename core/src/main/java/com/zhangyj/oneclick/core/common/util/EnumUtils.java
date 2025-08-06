package com.zhangyj.oneclick.core.common.util;

import com.zhangyj.oneclick.core.common.enums.CodeEnum;

/**
 * @author zhang.yuejia1
 */
public class EnumUtils {

    public static <E extends Enum<E> & CodeEnum<T>, T> E getByValue(Class<E> enumClass, T value) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.getCode().equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException("枚举" + enumClass.getSimpleName() + "非法枚举值: " + value);
    }

    public static boolean equalsAny(Object obj, CodeEnum... values) {
        for (CodeEnum<?> value : values) {
            if (obj instanceof CodeEnum) {
                if (value.equals(obj)) {
                    return true;
                }
            }else {
                if (value.getCode().equals(obj)) {
                    return true;
                }
            }
        }
        return false;
    }
}
