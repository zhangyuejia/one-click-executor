package com.zhangyj.hostrefresher.config;

import com.zhangyj.hostrefresher.pojo.HostsInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 网络检测功能配置类
 * @author zhagnyj
 */
@Data
@Slf4j
@Component
@ConditionalOnProperty(prefix = "host-refresher", name = "enable", havingValue = "true")
@ConfigurationProperties(prefix = "host-refresher")
public class HostsRefresherConfig {

    /**
     * 是否启用该功能
     */
    private Boolean enable;

    /**
     * 【必填】定时校验定时corn表达式
     */
    private String corn;

    /**
     * 网络hosts文件
     */
    private List<HostsInfo> hosts;

    /**
     * 本地hosts文件地址
     */
    private String hostsPath;
}
