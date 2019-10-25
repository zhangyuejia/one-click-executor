package com.zhangyj.finder;

import java.io.IOException;

/**
 * 查询者
 * @param <M> 查询参数
 * @param <R> 查询结果
 */
public interface Finder<M, R> {

    /**
     * 查询内容
     * @param msg 参数
     * @return 结果
     */
    R find(M msg) throws IOException, Exception;

}
