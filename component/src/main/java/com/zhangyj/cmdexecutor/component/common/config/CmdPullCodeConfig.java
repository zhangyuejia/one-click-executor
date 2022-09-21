package com.zhangyj.cmdexecutor.component.common.config;

import com.zhangyj.cmdexecutor.component.entity.bo.ModulePropertiesBO;
import com.zhangyj.cmdexecutor.core.common.config.CmdConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 多模块代码更新配置
 * @author zhagnyj
 */
@Data
@Slf4j
public class CmdPullCodeConfig implements CmdConfig {

    private List<String> enableRefId;

    /**
     * 替换关键字
     */
    private List<ModulePropertiesBO> modulesProperties;

    private List<String> errorLogWords;

    @Override
    public String getDesc() {
        return "多模块代码更新配置";
    }
}
