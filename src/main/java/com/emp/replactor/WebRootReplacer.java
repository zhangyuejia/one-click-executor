package com.emp.replactor;

import com.emp.bean.Config;

/**
 * WebRoot路径替换器
 * @author ZHANG
 */
public class WebRootReplacer implements Replacer {

    @Override
    public String replace(String data) {
        return data.replaceFirst("WebRoot", Config.getInstance().getTargetFilePrefix()).replaceAll("/", "\\\\");
    }
}
