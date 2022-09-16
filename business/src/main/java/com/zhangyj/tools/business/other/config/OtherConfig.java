package com.zhangyj.tools.business.other.config;

import com.zhangyj.tools.common.base.AbstractConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 临时功能
 * @author zhagnyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@Component
@ConditionalOnProperty(prefix = "other", name = "enable", havingValue = "true")
@ConfigurationProperties(prefix = "other")
public class OtherConfig extends AbstractConfig {


    @Override
    public String getFunctionName() {
        return "临时功能";
    }
}
