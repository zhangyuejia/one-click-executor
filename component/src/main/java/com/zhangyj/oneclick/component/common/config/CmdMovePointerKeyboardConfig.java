package com.zhangyj.oneclick.component.common.config;

import com.zhangyj.oneclick.core.common.config.AbstractCmdConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 模拟键盘鼠标移动配置
 * @author zhagnyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class CmdMovePointerKeyboardConfig extends AbstractCmdConfig {

    /**
     * 停止时间
     */
    private String stopTime;
    /**
     * 睡眠间隙
     */
    private Integer sleepInterval;
}
