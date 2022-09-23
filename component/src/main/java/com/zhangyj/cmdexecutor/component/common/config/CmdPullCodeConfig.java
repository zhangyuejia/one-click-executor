package com.zhangyj.cmdexecutor.component.common.config;

import com.zhangyj.cmdexecutor.component.entity.bo.ModulePropertiesBO;
import com.zhangyj.cmdexecutor.core.common.config.AbstractCmdConfig;
import com.zhangyj.cmdexecutor.core.common.config.AbstractCmdConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 多模块代码更新配置
 * @author zhagnyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class CmdPullCodeConfig extends AbstractCmdConfig {

    private List<String> enableRefId;

    /**
     * 替换关键字
     */
    private List<ModulePropertiesBO> modulesProperties;

    @Override
    public String getDesc() {
        return "多模块代码更新配置";
    }
}
