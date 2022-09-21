package com.zhangyj.cmdexecutor.component.entity.bo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author zhangyj
 */
@Data
public class ReplacePropertiesBO {

    /**
     * id
     */
    private String refId;

    /**
     * 新关键字
     */
    private Map<String, String> propertiesMap;

    private List<String> uselessProperties;
}
