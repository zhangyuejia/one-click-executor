package com.zhangyj.propertiesreplacer.config;

import com.zhangyj.propertiesreplacer.pojo.ReplaceProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文件替换器配置类
 * @author zhagnyj
 */
@Data
@Slf4j
@Component
@ConditionalOnProperty(prefix = "properties-replace", name = "enable", havingValue = "true")
@ConfigurationProperties(prefix = "properties-replace")
public class PropertiesReplaceConfig {

    /**
     * 是否启用该功能
     */
    private Boolean enable;

    /**
     * 替换关键字
     */
    private List<ReplaceProperties> replaceKeys;
}
