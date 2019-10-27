package com.zhangyj.monitor;

import lombok.Data;

@Data
public abstract class BaseTask<R> {

    private long timeout;

    private boolean taskDone;

    abstract R doTask();

}
