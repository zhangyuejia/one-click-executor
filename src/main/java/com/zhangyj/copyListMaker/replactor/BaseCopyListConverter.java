package com.zhangyj.copyListMaker.replactor;

import com.zhangyj.copyListMaker.config.Config;
import com.zhangyj.common.utils.StringUtil;

import java.util.Set;
import java.util.stream.Collectors;

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
     * @throws Exception 异常
     */
    public final Set<String> toCopyListLines(String relativePath) throws Exception{
        return toCopyListRelativePath(relativePath)
                .stream()
                // 加上copyList前缀和替换斜杠为反斜杠
                .map(d -> StringUtil.replaceSlash(config.getCopyList().getPrefix() + d))
                .collect(Collectors.toSet());
    }

    /**
     * 获取copyList相对路径
     * @param relativePath 相对路径
     * @return copyList相对路径
     * @throws Exception 异常
     */
    protected abstract Set<String> toCopyListRelativePath(String relativePath) throws Exception;
}
