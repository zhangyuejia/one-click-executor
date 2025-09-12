package com.zhangyj.oneclick.core.common.task;

public interface AsyncTaskGroup {
    /**
     * 异步执行一个任务
     */
    void execAsync(Runnable task);

    /**
     * 等待所有任务执行结束
     */
    void join();
}
