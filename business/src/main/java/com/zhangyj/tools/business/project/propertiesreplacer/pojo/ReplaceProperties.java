package com.zhangyj.tools.business.project.propertiesreplacer.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author zhangyj
 */
@Data
public class ReplaceProperties {

    /**
     * id
     */
    private String replaceId;;

    /**
     * 新关键字
     */
    private Map<String, String> propertiesMap;

    private List<String> uselessProperties;
}
