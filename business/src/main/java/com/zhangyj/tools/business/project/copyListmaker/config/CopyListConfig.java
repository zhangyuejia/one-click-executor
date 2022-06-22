package com.zhangyj.tools.business.project.copyListmaker.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ZHANG
 */
@Data
@Component
@ConfigurationProperties(prefix = "copy-list-maker")
@ConditionalOnProperty(prefix = "copy-list-maker", name = "enable", havingValue = "true")
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