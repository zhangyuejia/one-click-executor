package com.emp.replactor;

import com.emp.bean.Config;

/**
 * java文件路径替换器
 * @author ZHANG
 */
public class JavaPathReplactor implements PathReplactor {
    private static final Config CONFIG = Config.getInstance();

    @Override
    public String replace(String data) {
        return CONFIG.getTargetFilePrefix()+"\\WEB-INF\\classes"+data.substring(data.indexOf("/")).replaceAll("/", "\\\\").replace(".java", ".class");
    }
}
