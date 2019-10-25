package com.zhangyj.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ZHANG
 */
@Data
@Component
@ConfigurationProperties(prefix = "svn")
public class SvnConfig {

    /**
     * svn路径
     */
    private String path;

    /**
     * svn起始版本号
     */
    private Integer revStart;

    /**
     * svn终止版本号
     */
    private Integer revEnd;

}