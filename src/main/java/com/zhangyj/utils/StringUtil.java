package com.zhangyj.utils;

import org.springframework.util.StringUtils;

/**
 * @author ZHANG
 */
public class StringUtil extends StringUtils {

    /**
     * 替换反斜杠为斜杠
     * @param msg 内容
     * @return 替换后内容
     */
    public static String replaceBackslash(String msg){
        return msg.replaceAll("\\\\", "/");
    }

    /**
     * 是否以斜杠结尾
     * @param msg 内容
     * @return 判断结果
     */
    public static boolean endWithsSlash(String msg){
        return msg.endsWith("/");
    }

    /**
     * 替换斜杠为反斜杠
     * @param msg 内容
     * @return 替换后内容
     */
    public static String replaceSlash(String msg){
        return msg.replaceAll("/", "\\\\");
    }

    /**
     * 是否不为空
     * @param msg 内容
     * @return 判断结果
     */
    public static boolean isNotEmpty(String msg){
        return !isEmpty(msg);
    }
}
