package com.zhangyj.oneclick.component.common.config;

import com.zhangyj.oneclick.core.common.config.AbstractCmdConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 多模块代码更新配置
 * @author zhagnyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class CmdDirToTextConfig extends AbstractCmdConfig {

    /**
     * 方向模式
     */
    private String directionMode;
    /**
     * 源目录路径
     */
    private String fromDir;

    /**
     * txt文件路劲
     */
    private String textPath;

    /**
     * 还原的目录路径
     */
    private String toDir;
}
