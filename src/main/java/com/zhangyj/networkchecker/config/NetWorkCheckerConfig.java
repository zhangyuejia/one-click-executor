package com.zhangyj.networkchecker.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 网络检测功能配置类
 * @author zhagnyj
 */
@Data
@Slf4j
@Component
@ConditionalOnProperty(prefix = "network-checker", name = "enable", havingValue = "true")
@ConfigurationProperties(prefix = "network-checker")
public class NetWorkCheckerConfig {

    /**
     * 是否启用该功能
     */
    private Boolean enable;

    /**
     * 【必填】定时校验定时corn表达式
     */
    private String corn;

    /**
     * wifi名称（断网重连）
     */
    private String wifiName;
}
