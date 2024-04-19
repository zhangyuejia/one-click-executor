package com.zhangyj.oneclick.core.common.config;

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
}
