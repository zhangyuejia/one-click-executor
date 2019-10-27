package com.zhangyj.monitor;

/**
 * 监控器
 * @author ZHANG-YJ
 */
public abstract class BaseMonitor {

    public <R> R monitor(BaseTask<R> task){
        taskStart(task);
        R result = task.doTask();
        taskEnd(task);
        return result;
    }

    protected abstract <R> void taskStart(BaseTask<R> task);

    protected abstract <R> void taskEnd(BaseTask<R> task);



}
