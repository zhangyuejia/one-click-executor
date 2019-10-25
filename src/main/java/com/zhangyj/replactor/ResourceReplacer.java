package com.zhangyj.replactor;

import com.zhangyj.config.CopyListConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 资源文件路径替换器
 * @author ZHANG
 */
@Component
public class ResourceReplacer implements Replacer {

    private final CopyListConfig copyListConfig;

    public ResourceReplacer(CopyListConfig copyListConfig) {
        this.copyListConfig = copyListConfig;
    }

    @Override
    public String replace(String data) {
        return copyListConfig.getPrefix()+"\\WEB-INF\\classes"+data.substring(data.indexOf("/")).replaceAll("/", "\\\\");
    }
}
