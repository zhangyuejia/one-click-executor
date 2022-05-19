package com.zhangyj.filesplicer.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件拼接器配置类
 * @author zhagnyj
 */
@Data
@Slf4j
@Component
@ConditionalOnProperty(prefix = "file-splicer", name = "enable", havingValue = "true")
@ConfigurationProperties(prefix = "file-splicer")
public class FileSplicerConfig {

    /**
     * 是否启用该功能
     */
    private Boolean enable;

    /**
     * 文件夹路径
     */
    private String path;

    /**
     * 文件名前缀
     */
    private String[] whitePattern;

    /**
     * 文件名后缀
     */
    private String[] blackPattern;

    /**
     * 文件名样式（正则表达式）
     */
    private String genFileName;

    /**
     * 生成文件之前，在path文件夹路径执行的cmd命令（如更新git或者svn）
     */
    private String command;
}
