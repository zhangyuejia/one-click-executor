package com.zhangyj.cmdexecutor.component.common.config;

import com.zhangyj.cmdexecutor.core.common.config.AbstractCmdConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件拼接器配置类
 * @author zhagnyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class CmdSpliceFileConfig extends AbstractCmdConfig {

    /**
     * 文件夹路径
     */
    private String path;

    /**
     * 文件名前缀
     */
    private String[] whitePattern;

    /**
     * 文件名后缀
     */
    private String[] blackPattern;

    /**
     * 文件名样式（正则表达式）
     */
    private String genFileName;

    /**
     * 生成文件模式: new or append
     */
    private String genMode;

    /**
     * 生成文件之前，在path文件夹路径执行的cmd命令（如更新git或者svn）
     */
    private String command;

    @Override
    public String getDesc() {
        return "文件拼接功能";
    }
}
