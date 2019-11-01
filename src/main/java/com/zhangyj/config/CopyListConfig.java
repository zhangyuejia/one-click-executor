package com.zhangyj.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ZHANG
 */
@Data
@Component
@ConfigurationProperties(prefix = "copy-list")
public class CopyListConfig {

    /**
     * copyList路径
     */
    private String path;

    /**
     * copyList内容前缀
     */
    private String prefix;

}