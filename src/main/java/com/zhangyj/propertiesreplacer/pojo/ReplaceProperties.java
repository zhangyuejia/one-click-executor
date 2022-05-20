package com.zhangyj.propertiesreplacer.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author zhangyj
 */
@Data
public class ReplaceProperties {

    /**
     * 是否替换改配置
     */
    private Boolean enable;

    /**
     * 关键字
     */
    private List<String> filePaths;

    /**
     * 新关键字
     */
    private Map<String, String> propertiesMap;

    private List<String> uselessProperties;
}
