package com.zhangyj.cmdexecutor.core.common.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 命令执行功能
 * @author zhagnyj
 */
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "cmd-executor")
public class CmdExecConfig implements CmdConfig {

    private String dir;

    private String charset;

    private String filePath;

    @Override
    public String getDesc() {
        return "命令执行功能";
    }
}
