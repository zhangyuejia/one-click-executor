package com.zhangyj.cmdexecutor.component.business.xydecrypt;

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
     * @param file 原文件
     */
    void processDecrypt(File file);

}
