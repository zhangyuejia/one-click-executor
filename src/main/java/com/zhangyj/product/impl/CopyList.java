package com.zhangyj.product.impl;

import com.zhangyj.product.Product;
import com.zhangyj.utils.FileUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * 项目修改
 * @author zhangyj
 */
@Data
@Slf4j
public class CopyList implements Product {

    private Set<String> data;

    private String path;

    public CopyList(Set<String> data, String path) {
        this.data = data;
        this.path = path;
    }

    @Override
    public void build() throws Exception {
        FileUtil.save(path, data);
    }


}
