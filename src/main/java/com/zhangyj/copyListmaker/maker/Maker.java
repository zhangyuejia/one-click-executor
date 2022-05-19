package com.zhangyj.copyListmaker.maker;

/**
 * 制造者
 * @author ZHANG
 */
public interface Maker<T> {

    /**
     * 制造
     * @return 成品
     * @throws Exception 异常
     */
    T make() throws Exception;
}
