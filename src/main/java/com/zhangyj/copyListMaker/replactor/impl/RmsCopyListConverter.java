package com.zhangyj.copyListMaker.replactor.impl;

import com.zhangyj.copyListMaker.config.Config;
import com.zhangyj.common.constant.Const;
import com.zhangyj.copyListMaker.replactor.BaseCopyListConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

/**
 * @author ZHANG
 */
@Component
@ConditionalOnBean(Config.class)
public class RmsCopyListConverter extends BaseCopyListConverter {

    public RmsCopyListConverter(Config config) {
        super(config);
    }

    @Override
    protected Set<String> toCopyListRelativePath(String relativePath) {
        return Collections.singleton(Const.DIST_PATH);
    }
}
