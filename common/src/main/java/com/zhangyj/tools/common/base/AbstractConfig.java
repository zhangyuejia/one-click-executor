package com.zhangyj.tools.common.base;

/**
 * @author zhangyj
 */
public abstract class AbstractConfig {

    /**
     * 是否启用该功能
     */
    private Boolean enable;

    /**
     * 获取功能名称
     * @return 功能名称
     */
    public abstract String getFunctionName();
}
