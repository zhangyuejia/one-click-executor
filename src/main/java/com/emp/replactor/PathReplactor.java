package com.emp.replactor;

/**
 * 规则替换接口
 * @author ZHANG
 */
public interface PathReplactor {
    /**
     * 替换内容
     * @param data 数据
     * @return 替换后内容
     */
    String replace(String data);
}
