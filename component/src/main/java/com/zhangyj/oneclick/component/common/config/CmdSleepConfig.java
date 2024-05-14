package com.zhangyj.oneclick.component.common.config;

import com.zhangyj.oneclick.core.common.config.AbstractCmdConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhagnyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class CmdSleepConfig extends AbstractCmdConfig {

    private Integer t;
}
