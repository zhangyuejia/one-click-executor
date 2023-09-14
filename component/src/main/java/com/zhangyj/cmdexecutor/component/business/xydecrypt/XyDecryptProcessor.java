package com.zhangyj.cmdexecutor.component.business.xydecrypt;

import com.zhangyj.cmdexecutor.component.common.config.CmdXyDecryptConfig;
import com.zhangyj.cmdexecutor.core.common.util.FileUtils;

import java.io.File;
import java.util.List;

/**
 * xy处理器
 * @author zhanglj
 */
public interface XyDecryptProcessor {

    String TEMP_DIR = FileUtils.getTempDir("xyDecryptTemp");
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

    /**
     * 支持的文件拓展类型
     * @return 文件拓展类型
     */
    List<String> getFileExtensions();

}
