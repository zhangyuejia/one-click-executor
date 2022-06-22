package com.zhangyj.tools.business.project.copyListmaker.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author ZHANG
 */
@Data
@Component
@ConfigurationProperties(prefix = "copy-list-maker.emp")
@ConditionalOnProperty(prefix = "copy-list-maker", name = "enable", havingValue = "true")
@Validated
public class EmpConfig {

    /**
     * emp项目编译输出路径
     */
    @NotBlank(message = "配置项[emp->outPutPath]不能为空")
    private String outputPath;
}