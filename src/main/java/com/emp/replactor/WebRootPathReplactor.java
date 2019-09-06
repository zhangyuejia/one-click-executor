package com.emp.replactor;

import com.emp.bean.Config;

/**
 * WebRoot路径替换器
 * @author ZHANG
 */
public class WebRootPathReplactor implements PathReplactor {

    private static final Config CONFIG = Config.getInstance();

    @Override
    public String replace(String data) {
        return data.replaceFirst("WebRoot", CONFIG.getTargetFilePrefix()).replaceAll("/", "\\\\");
    }
}
