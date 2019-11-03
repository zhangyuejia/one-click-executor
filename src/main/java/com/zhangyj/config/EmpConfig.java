package com.zhangyj.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author ZHANG
 */
@Data
@Component
@ConfigurationProperties(prefix = "emp")
@Validated
public class EmpConfig {

    /**
     * emp项目编译输出路径
     */
    @NotBlank(message = "配置项[emp->outPutPath]不能为空")
    private String outPutPath;

}