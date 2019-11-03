package com.zhangyj.replactor.impl;

import com.zhangyj.config.Config;
import com.zhangyj.constant.EmpConst;
import com.zhangyj.replactor.BaseCopyListConverter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

/**
 * @author ZHANG
 */
@Component
public class RmsCopyListConverter extends BaseCopyListConverter {

    public RmsCopyListConverter(Config config) {
        super(config);
    }

    @Override
    protected Set<String> toCopyListRelativePath(String relativePath) {
        return Collections.singleton(EmpConst.DIST_PATH);
    }
}
