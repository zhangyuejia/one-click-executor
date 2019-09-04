package com.emp.replactor;

import com.emp.bean.Config;

/**
 * 资源文件替换器
 * @author ZHANG
 */
public class ResourcePathReplactor implements PathReplactor {
    private static final Config CONFIG = Config.getInstance();

    @Override
    public String replace(String data) {
        return CONFIG.getTargetFilePrefix()+"\\WEB-INF\\classes"+data.substring(data.indexOf("/")).replaceAll("/", "\\\\");
    }
}
