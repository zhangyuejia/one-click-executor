package com.zhangyj.cmdexecutor.common.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Map;

/**
 * @author ZHANG
 */
public class StrUtils extends StringUtils {

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

    public static String parseTplContent(String tplContent, Object paramObj) {
        Map<String, String> paramMap = JSON.parseObject(JSON.toJSONString(paramObj), new TypeReference<Map<String, String>>() {});
        TemplateParserContext parserContext = new TemplateParserContext();
        return new SpelExpressionParser().parseExpression(tplContent, parserContext).getValue(paramMap, String.class);
    }

    public static String toCamel(String cmd) {
        String cmdCamelStr = CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, cmd);
        return cmdCamelStr.substring(0, 1).toUpperCase() + cmdCamelStr.substring(1);
    }

}
