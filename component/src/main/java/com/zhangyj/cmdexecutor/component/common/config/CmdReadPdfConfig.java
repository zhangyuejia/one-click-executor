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
     * 行程单pdf目录（支持高德和滴滴）
     */
    private String pdfDir;

    /**
     * 市内交通word输出地址
     */
    private String docOutPath;

    /**
     * 报销人
     */
    private String myName;

    @Override
    public String getDesc() {
        return "读取pdf功能";
    }
}
