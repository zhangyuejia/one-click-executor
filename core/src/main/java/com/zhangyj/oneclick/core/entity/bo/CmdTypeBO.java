package com.zhangyj.oneclick.core.entity.bo;

import com.zhangyj.oneclick.core.common.enums.CmdTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 命令类型解析对象
 * @author zhangyj
 */
@Getter
@Setter
public class CmdTypeBO {

    /**
     * 原始值
     */
    private String originalValue;

    /**
     * 命令类型
     */
    private CmdTypeEnum cmdType;

    /**
     * 命令类型参数
     */
    private CmdTypeParamBO cmdTypeParam;
}
