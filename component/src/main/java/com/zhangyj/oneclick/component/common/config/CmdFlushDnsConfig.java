package com.zhangyj.oneclick.component.common.config;

import com.zhangyj.oneclick.component.entity.bo.HostsInfoBO;
import com.zhangyj.oneclick.core.common.config.AbstractCmdConfig;
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
public class CmdFlushDnsConfig extends AbstractCmdConfig {

    /**
     * 网络hosts文件
     */
    private List<HostsInfoBO> hosts;

    /**
     * 本地hosts文件地址
     */
    private String hostsPath;
}
