package com.zhangyj.replactor;

import com.zhangyj.config.CopyListConfig;

/**
 * WebRoot路径替换器
 * @author ZHANG
 */
public class WebRootReplacer implements Replacer {
    private final CopyListConfig copyListConfig;

    public WebRootReplacer(CopyListConfig copyListConfig) {
        this.copyListConfig = copyListConfig;
    }
    @Override
    public String replace(String data) {
        return data.replaceFirst("WebRoot", copyListConfig.getPrefix()).replaceAll("/", "\\\\");
    }
}
