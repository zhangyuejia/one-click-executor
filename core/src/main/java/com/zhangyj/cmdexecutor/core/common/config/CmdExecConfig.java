package com.zhangyj.cmdexecutor.core.common.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 命令执行功能
 * @author zhagnyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "cmd-executor")
public class CmdExecConfig extends AbstractCmdConfig {

    private String dir;

    private String charset;

    private String shellPath;

    public static final Map<String, String> PARAM_MAP = new HashMap<>();
}
