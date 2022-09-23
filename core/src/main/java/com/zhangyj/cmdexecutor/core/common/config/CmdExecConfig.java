package com.zhangyj.cmdexecutor.core.common.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
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
@ConfigurationProperties(prefix = "cmd-executor")
public class CmdExecConfig extends AbstractCmdConfig {

    private String dir;

    private String charset;

    private String shellPath;

    /**
     * 是否为初始启动加载（与普通组件加载区分开）
     * 当配置为true时，dir属性不能为空；当为空或false时，dir取配置项[cmd-executor.dir]
     */
    private Boolean bootLoad = false;

    @Override
    public String getDesc() {
        return "命令执行功能";
    }
}
