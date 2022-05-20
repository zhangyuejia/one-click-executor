package com.zhangyj.tools.business.project.copyListmaker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ZHANG
 */
@Data
@Validated
@Component
@ConfigurationProperties(prefix = "copy-list-maker.svn")
public class SvnConfig {

    /**
     * svn路径
     */
    @NotBlank(message = "配置项[svn->path]不能为空")
    private String path;

    /**
     * svn起始版本号
     */
    @NotNull(message = "配置项[svn->revStart]不能为空")
    private Integer revStart;

    /**
     * svn终止版本号
     */
    private Integer revEnd;
}