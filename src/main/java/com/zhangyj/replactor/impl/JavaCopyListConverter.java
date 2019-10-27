package com.zhangyj.replactor.impl;

import com.zhangyj.config.CopyListConfig;
import com.zhangyj.config.SvnConfig;
import com.zhangyj.replactor.BaseCopyListConverter;
import org.springframework.stereotype.Component;

/**
 * java文件路径替换器
 * @author ZHANG
 */
@Component
public class JavaCopyListConverter extends BaseCopyListConverter {

    public JavaCopyListConverter(CopyListConfig copyListConfig, SvnConfig svnConfig) {
        super(copyListConfig, svnConfig);
    }

    @Override
    public String convert(String svnRecord) {
        return copyListConfig.getPrefix()
                + "\\WEB-INF\\classes"
                + svnRecord.substring(svnConfig.getPath().length())
                .replaceAll("/", "\\\\").replace(".java", ".class");
    }
}
