package com.zhangyj.tools.business.project.cmdexecutor.config;

import com.zhangyj.tools.common.base.AbstractConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 命令执行功能
 * @author zhagnyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@Component
@ConditionalOnProperty(prefix = "cmd-executor", name = "enable", havingValue = "true")
@ConfigurationProperties(prefix = "cmd-executor")
public class CmdExecutorConfig extends AbstractConfig {

    private String cmdFilePath;

    private String execPath;

    private String charset;

    @Override
    public String getFunctionName() {
        return "命令执行功能";
    }
}
