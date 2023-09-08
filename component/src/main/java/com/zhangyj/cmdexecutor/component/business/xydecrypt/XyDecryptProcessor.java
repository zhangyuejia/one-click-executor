package com.zhangyj.cmdexecutor.component.business.xydecrypt;

import com.zhangyj.cmdexecutor.component.common.config.CmdXyDecryptConfig;

import java.io.File;

/**
 * xy处理器
 * @author zhanglj
 */
public interface XyDecryptProcessor {

    /**
     * 是否匹配到
     * @param file 原文件
     * @return 匹配结果
     */
    boolean isMatch(File file);

    /**
     * 处理
     * @param config 配置对象
     * @param file   原文件
     */
    void processDecrypt(CmdXyDecryptConfig config, File file);

}
