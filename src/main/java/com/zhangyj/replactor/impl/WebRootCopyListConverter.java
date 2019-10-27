package com.zhangyj.replactor.impl;

import com.zhangyj.config.CopyListConfig;
import com.zhangyj.config.SvnConfig;
import com.zhangyj.replactor.BaseCopyListConverter;
import org.springframework.stereotype.Component;

/**
 * WebRoot路径替换器
 * @author ZHANG
 */
@Component
public class WebRootCopyListConverter extends BaseCopyListConverter {

    public WebRootCopyListConverter(CopyListConfig copyListConfig, SvnConfig svnConfig) {
        super(copyListConfig, svnConfig);
    }

    @Override
    public String convert(String svnRecord) {
        return svnRecord.replaceFirst("WebRoot", copyListConfig.getPrefix()).replaceAll("/", "\\\\");
    }
}
