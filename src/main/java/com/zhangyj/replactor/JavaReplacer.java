package com.zhangyj.replactor;

import com.zhangyj.config.CopyListConfig;

/**
 * java文件路径替换器
 * @author ZHANG
 */
public class JavaReplacer implements Replacer {
    private final CopyListConfig copyListConfig;

    public JavaReplacer(CopyListConfig copyListConfig) {
        this.copyListConfig = copyListConfig;
    }
    @Override
    public String replace(String data) {
        return copyListConfig.getPrefix()+"\\WEB-INF\\classes"+data.substring(data.indexOf("/")).replaceAll("/", "\\\\").replace(".java", ".class");
    }
}
