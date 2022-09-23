package com.zhangyj.cmdexecutor.component.common.config;

import com.zhangyj.cmdexecutor.component.entity.bo.ReplacePropertiesBO;
import com.zhangyj.cmdexecutor.core.common.config.AbstractCmdConfig;
import com.zhangyj.cmdexecutor.core.common.config.AbstractCmdConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 文件替换器配置类
 * @author zhagnyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class CmdReplacePropertiesConfig extends AbstractCmdConfig {

    private String dir;

    private List<String> enableRefId;

    /**
     * 配置文件路径
     */
    private List<String> filePaths;

    /**
     * 新关键字
     */
    private Map<String, String> propertiesMap;

    /**
     * 需要注释的配置项，优先级最高
     */
    private List<String> uselessProperties;

    /**
     * 替换关键字
     */
    private List<ReplacePropertiesBO> replaceKeys;

    @Override
    public String getDesc() {
        return "Properties配置项覆盖功能";
    }
}
