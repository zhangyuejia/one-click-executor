package com.zhangyj.tools.business.project.multimodule.config;

import com.zhangyj.tools.business.project.multimodule.pojo.ModuleProperties;
import com.zhangyj.tools.common.base.AbstractConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 多模块代码更新配置
 * @author zhagnyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@Component
@ConditionalOnProperty(prefix = "multi-module-pull-code", name = "enable", havingValue = "true")
@ConfigurationProperties(prefix = "multi-module-pull-code")
public class MultiModulePullCodeConfig extends AbstractConfig {

    private List<String> enableRefId;

    /**
     * 替换关键字
     */
    private List<ModuleProperties> modulesProperties;

    @Override
    public String getFunctionName() {
        return "多模块代码更新配置";
    }
}
