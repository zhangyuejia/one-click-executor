package com.zhangyj.replactor;

import com.zhangyj.config.Config;

import java.util.Set;

/**
 * 规则替换接口
 * @author ZHANG
 */
public abstract class BaseCopyListConverter {

    protected final Config config;

    public BaseCopyListConverter(Config config) {
        this.config = config;
    }

    /**
     * 替换内容
     * @param relativePath 相对路径
     * @return 替换后内容
     */
    public abstract Set<String> toCopyListLines(String relativePath) throws Exception;
}
