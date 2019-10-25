package com.zhangyj.product.impl;

import com.zhangyj.product.Product;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * 项目修改
 * @author zhangyj
 */
@Data
@Slf4j
public class CopyList implements Product {

    private Set<String> data;

    public CopyList(Set<String> data) {
        this.data = data;
    }

    @Override
    public void build() {
        if(CollectionUtils.isEmpty(data)){
            log.error("CopyList数据为空，无需写入文件");
            return;
        }
        data.forEach((d) ->{

        });
    }
}
