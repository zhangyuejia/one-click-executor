package com.zhangyj.oneclick.core.entity.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 命令行解析对象
 * @author zhangyj
 */
@Getter
@Setter
public class CmdLinePO {

    /**
     * 命令类型
     */
    private CmdTypePO cmdType;
    /**
     * shell命令
     */
    private CmdNamePO cmdName;
    /**
     * 命令执行路径
     */
    private String dir;
    /**
     * 是否异步
     */
    private Boolean isAsync;
}
