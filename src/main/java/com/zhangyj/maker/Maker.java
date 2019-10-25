package com.zhangyj.maker;

import com.zhangyj.product.Product;

/**
 * 制造者
 * @author ZHANG
 */
public interface Maker<T extends Product> {

    /**
     * 制造
     * @return 成品
     */
    T make() throws Exception;
}
