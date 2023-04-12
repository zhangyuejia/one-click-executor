package com.zhangyj.cmdexecutor.component.common.config;

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
public class CmdReadPdfConfig extends AbstractCmdConfig {
    /**
     * 输入pdf目录
     */
    private String pdfDir;

    /**
     * 输出的docx
     */
    private String docOutPath;

    /**
     * 输出的txt
     */
    private String txtOutPath;

    @Override
    public String getDesc() {
        return "读取pdf功能";
    }
}
