package com.zhangyj.cmdexecutor.core.entity.bo;

import lombok.Getter;
import lombok.Setter;

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
    private String type;
    /**
     * shell命令
     */
    private String cmd;
    /**
     * 命令执行路径
     */
    private String dir;


}
