package com.zhangyj.cmdexecutor.core.entity.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * 命令行解析对象
 * @author zhangyj
 */
@Getter
@Setter
public class CmdTypePO {

    /**
     * 命令类型
     */
    private String type;

    /**
     * 命令类型参数
     */
    private CmdTypeParameterPO cmdTypeParameter;
}
