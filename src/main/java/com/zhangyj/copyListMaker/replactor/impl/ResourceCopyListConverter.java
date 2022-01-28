package com.zhangyj.copyListMaker.replactor.impl;

import com.zhangyj.copyListMaker.config.Config;
import com.zhangyj.common.constant.Const;
import com.zhangyj.copyListMaker.replactor.BaseCopyListConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

/**
 * 资源文件路径替换器
 * @author ZHANG
 */
@Component
@ConditionalOnBean(Config.class)
public class ResourceCopyListConverter extends BaseCopyListConverter {

    public ResourceCopyListConverter(Config config) {
        super(config);
    }

    @Override
    protected Set<String> toCopyListRelativePath(String relativePath) {
        return Collections.singleton(Const.WEB_INF_CLASSES + relativePath.substring(relativePath.indexOf("/")));
    }
}
