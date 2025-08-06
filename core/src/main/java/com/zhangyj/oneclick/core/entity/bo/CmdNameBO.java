package com.zhangyj.oneclick.core.entity.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 命令名对象
 * @author zhangyj
 */
@Getter
@Setter
public class CmdNameBO {

    /**
     * 原始值
     */
    private String originalValue;

    /**
     * 命令名
     */
    private String value;

    /**
     * 命令类型参数
     */
    private Map<String, Object> paramMap;
}
