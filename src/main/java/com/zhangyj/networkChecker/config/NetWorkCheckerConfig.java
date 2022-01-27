package com.zhangyj.networkChecker.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件拼接器配置类
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
     * 【必填】定时校验时间间隔(单位：秒)
     */
    private Integer checkPeriod;

    /**
     * 网卡名称（如 以太网）
     */
    private String networkName;

    /**
     * 断网执行的cmd命令
     */
    private String command;
}
