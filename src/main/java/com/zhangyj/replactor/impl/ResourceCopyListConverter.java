package com.zhangyj.replactor.impl;

import com.zhangyj.config.Config;
import com.zhangyj.replactor.BaseCopyListConverter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

/**
 * 资源文件路径替换器
 * @author ZHANG
 */
@Component
public class ResourceCopyListConverter extends BaseCopyListConverter {

    public ResourceCopyListConverter(Config config) {
        super(config);
    }

    @Override
    protected Set<String> toCopyListRelativePath(String relativePath) {
        return Collections.singleton("\\WEB-INF\\classes" + relativePath.substring(relativePath.indexOf("/")));
    }
}
