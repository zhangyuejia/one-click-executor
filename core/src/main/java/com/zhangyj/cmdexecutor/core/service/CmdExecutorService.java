package com.zhangyj.cmdexecutor.core.service;

/**
 * @author zhangyj
 */
public interface CmdExecutorService {

    /**
     * 是否匹配到本执行器
     * @param shell 命令
     * @return 匹配是否成功
     */
    boolean match(String shell);

    /**
     * 执行命令
     * @param shell 命令
     */
    void exec(String shell);
}
