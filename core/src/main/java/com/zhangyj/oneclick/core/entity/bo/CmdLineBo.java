package com.zhangyj.oneclick.core.entity.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * 命令行解析对象
 * @author zhangyj
 */
@Getter
@Setter
public class CmdLineBo {
    /**
     * 原始值
     */
    private String originalValue;

    /**
     * 命令类型
     */
    private CmdTypeBO cmdType;
    /**
     * shell命令
     */
    private CmdNameBO cmdName;
    /**
     * 命令执行路径
     */
    private String dir;
    /**
     * 是否异步
     */
    private Boolean isAsync;
}
