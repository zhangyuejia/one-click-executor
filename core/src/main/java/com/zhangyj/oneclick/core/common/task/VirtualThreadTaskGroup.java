package com.zhangyj.oneclick.core.common.task;

import java.util.ArrayList;
import java.util.List;

public class VirtualThreadTaskGroup implements AsyncTaskGroup {
    private final List<Thread> threads = new ArrayList<>();

    @Override
    public void execAsync(Runnable task) {
        Thread t = Thread.startVirtualThread(task);
        threads.add(t);
    }

    @Override
    public void join() {
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("等待任务结束时被中断", e);
            }
        }
        threads.clear();
    }
}
