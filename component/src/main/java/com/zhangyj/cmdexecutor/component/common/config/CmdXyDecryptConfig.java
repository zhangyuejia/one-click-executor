package com.zhangyj.cmdexecutor.component.common.config;

import com.zhangyj.cmdexecutor.component.entity.bo.HostsInfoBO;
import com.zhangyj.cmdexecutor.core.common.config.AbstractCmdConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author zhagnyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class CmdXyDecryptConfig extends AbstractCmdConfig {

    private String path;

    private String dataServerUrl;

    @Override
    public String getDesc() {
        return "解密功能";
    }
}
