package com.zhangyj.tools.business.file.filesize.config;

import com.zhangyj.tools.common.base.AbstractConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 打印文件夹大小配置类
 * @author zhagnyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@Component
@ConditionalOnProperty(prefix = "file-sizer", name = "enable", havingValue = "true")
@ConfigurationProperties(prefix = "file-sizer")
public class FileSizeConfig extends AbstractConfig {

    /**
     * 文件夹路径
     */
    private String path;


    @Override
    public String getFunctionName() {
        return "打印文件夹大小功能";
    }
}
