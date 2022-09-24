package com.zhangyj.cmdexecutor.core.entity.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * cmd变量对象
 * @author zhangyj
 */
@Getter
@Setter
public class CmdParameterPO {

    /**
     * 命令执行根路径
     */
    private String dir;


    /**
     * 类资源路径
     */
    private String classpath;
}
