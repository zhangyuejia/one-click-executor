package com.zhangyj.tools.common.base;

import com.zhangyj.tools.common.utils.TaskUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 * @author zhangyj
 */
@Slf4j
public abstract class AbstractRunner<T extends AbstractConfig> implements CommandLineRunner {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected T config;

    /**
     * 默认实现
     * @param args 程序参数
     */
    @Override
    public void run(String... args) {
        try {
            log.info("执行：" + config.getFunctionName());
            TaskUtil.watch(config.getFunctionName(), this::doRun);
        } catch (Exception e) {
            log.error("抛出异常", e);
        }
    }

    /**
     * 供子类实现
     * @throws Exception 异常
     */
    protected abstract void doRun() throws Exception;
}
