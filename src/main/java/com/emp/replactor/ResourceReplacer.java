package com.emp.replactor;

import com.emp.bean.Config;

/**
 * 资源文件路径替换器
 * @author ZHANG
 */
public class ResourceReplacer implements Replacer {

    @Override
    public String replace(String data) {
        return Config.getInstance().getTargetFilePrefix()+"\\WEB-INF\\classes"+data.substring(data.indexOf("/")).replaceAll("/", "\\\\");
    }
}
