package com.zhangyj.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ZHANG
 */
@Data
@Component
@ConfigurationProperties(prefix = "emp")
public class EmpConfig {

    /**
     * emp项目编译输出路径
     */
    private String outPutPath;

}