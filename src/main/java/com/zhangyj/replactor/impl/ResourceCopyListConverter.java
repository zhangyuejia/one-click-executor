package com.zhangyj.replactor.impl;

import com.zhangyj.config.CopyListConfig;
import com.zhangyj.config.SvnConfig;
import com.zhangyj.replactor.BaseCopyListConverter;
import org.springframework.stereotype.Component;

/**
 * 资源文件路径替换器
 * @author ZHANG
 */
@Component
public class ResourceCopyListConverter extends BaseCopyListConverter {

    public ResourceCopyListConverter(CopyListConfig copyListConfig, SvnConfig svnConfig) {
        super(copyListConfig, svnConfig);
    }

    @Override
    public String convert(String svnRecord) {
        return copyListConfig.getPrefix()
                + "\\WEB-INF\\classes"
                + svnRecord.substring(svnRecord.indexOf("/")).replaceAll("/", "\\\\");
    }
}
