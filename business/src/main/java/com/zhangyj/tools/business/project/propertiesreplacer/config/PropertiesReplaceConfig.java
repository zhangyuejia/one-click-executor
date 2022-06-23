package com.zhangyj.tools.business.project.propertiesreplacer.config;

import com.zhangyj.tools.business.project.propertiesreplacer.pojo.ReplaceProperties;
import com.zhangyj.tools.common.base.AbstractConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 文件替换器配置类
 * @author zhagnyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@Component
@ConditionalOnProperty(prefix = "properties-replace", name = "enable", havingValue = "true")
@ConfigurationProperties(prefix = "properties-replace")
public class PropertiesReplaceConfig extends AbstractConfig {

    private List<String> enableReplaceId;

    /**
     * 配置文件路径
     */
    private List<String> filePaths;

    /**
     * 新关键字
     */
    private Map<String, String> propertiesMap;

    /**
     * 替换关键字
     */
    private List<ReplaceProperties> replaceKeys;

    @Override
    public String getFunctionName() {
        return "Properties配置项覆盖功能";
    }
}
