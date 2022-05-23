package com.zhangyj.tools.business.file.filerename.config;

import com.zhangyj.tools.business.file.filerename.pojo.ReplaceWord;
import com.zhangyj.tools.common.base.AbstractConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文件拼接器配置类
 * @author zhagnyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@Component
@ConditionalOnProperty(prefix = "file-rename", name = "enable", havingValue = "true")
@ConfigurationProperties(prefix = "file-rename")
public class FileRenameConfig extends AbstractConfig {

    /**
     * 文件夹路径
     */
    private String path;

    /**
     * 目标文件夹路径
     */
    private String targetPath;

    /**
     * 替换关键字
     */
    private List<ReplaceWord> replaceWords;

    @Override
    public String getFunctionName() {
        return "文件夹文件重命名功能";
    }
}
