package com.zhangyj.tools.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangyj
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "tools")
public class ToolsConfig {

    /**
     * 是否检查只有一个功能运行
     */
    private Boolean checkOneFunction;
}
