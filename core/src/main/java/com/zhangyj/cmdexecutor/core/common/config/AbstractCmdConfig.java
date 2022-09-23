package com.zhangyj.cmdexecutor.core.common.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhangyj
 */
@Setter
@Getter
public abstract class AbstractCmdConfig {

    private List<String> errorLogWords;


    /**
     * 获取描述
     * @return 描述
     */
    public abstract String getDesc();
}
