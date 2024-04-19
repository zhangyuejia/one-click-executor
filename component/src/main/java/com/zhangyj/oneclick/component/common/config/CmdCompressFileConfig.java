package com.zhangyj.oneclick.component.common.config;

import com.zhangyj.oneclick.core.common.config.AbstractCmdConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 压缩文件
 * @author zhagnyj
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class CmdCompressFileConfig extends AbstractCmdConfig {

    /**
     * 压缩目录
     */
    private String compressDir;

    /**
     * 压缩密码
     */
    private String compressPassword;
}
