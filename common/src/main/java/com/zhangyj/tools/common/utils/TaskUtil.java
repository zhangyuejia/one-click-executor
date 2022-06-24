package com.zhangyj.tools.common.utils;

import com.zhangyj.tools.common.bean.pojo.ITask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

/**
 * @author zhangyj
 */
@Slf4j
public class TaskUtil {

    public static void watch(String taskName, ITask task) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        task.exec();
        stopWatch.stop();
        log.info("执行任务[{}]耗时：{}ms", taskName, stopWatch.getTotalTimeMillis());
    }
}
