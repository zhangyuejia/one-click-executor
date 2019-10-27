package com.zhangyj.replactor;

import com.zhangyj.config.CopyListConfig;
import com.zhangyj.config.SvnConfig;

/**
 * 规则替换接口
 * @author ZHANG
 */
public abstract class BaseCopyListConverter {

    protected final CopyListConfig copyListConfig;

    protected final SvnConfig svnConfig;

    public BaseCopyListConverter(CopyListConfig copyListConfig, SvnConfig svnConfig) {
        this.copyListConfig = copyListConfig;
        this.svnConfig = svnConfig;
    }

    /**
     * 替换内容
     * @param data 数据
     * @return 替换后内容
     */
    public abstract String convert(String data);
}
