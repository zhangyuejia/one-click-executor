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
     * svn起始版本号
     */
    private String path;

    /**
     * svn终止版本号
     */
    private String prefix;

}