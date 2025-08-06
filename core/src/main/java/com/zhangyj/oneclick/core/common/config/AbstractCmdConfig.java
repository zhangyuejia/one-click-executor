package com.zhangyj.oneclick.core.common.config;

import com.zhangyj.oneclick.core.entity.bo.CmdLineBo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhangyj
 */
@Setter
@Getter
public abstract class AbstractCmdConfig {

    private String dir;

    private List<String> errorLogWords;

    private List<String> skipKeyWords;

    private transient CmdLineBo cmdLineBo;
}
