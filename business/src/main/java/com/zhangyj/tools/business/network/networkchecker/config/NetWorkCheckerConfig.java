package com.zhangyj.tools.business.network.networkchecker.config;

import com.zhangyj.tools.common.base.AbstractConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 网络检测功能配置类
 * @author zhagnyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@Component
@ConditionalOnProperty(prefix = "network-checker", name = "enable", havingValue = "true")
@ConfigurationProperties(prefix = "network-checker")
public class NetWorkCheckerConfig extends AbstractConfig {

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

    @Override
    public String getFunctionName() {
        return "网络检测功能";
    }
}
