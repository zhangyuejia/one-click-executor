package com.zhangyj.cmdexecutor.core.entity.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangyj
 */
@Setter
@Getter
public class CmdTypeParameterPO {

    /**
     * 直接执行shell时，是否打印输出
     */
    private Boolean enableOutput = true;
}
