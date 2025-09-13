package com.zhangyj.oneclick.core.common.factory;

import com.zhangyj.oneclick.core.common.task.AsyncTaskGroup;
import com.zhangyj.oneclick.core.common.task.VirtualThreadTaskGroup;

public class TaskGroupFactory {

    public static AsyncTaskGroup newAsyncTaskGroup() {
        return new VirtualThreadTaskGroup();
    }
}
