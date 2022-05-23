package com.zhangyj.tools.common.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 * @author zhangyj
 */
@Slf4j
public abstract class AbstractFunExecutor<T extends AbstractConfig> implements CommandLineRunner {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
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
            doExec();
        } catch (Exception e) {
            log.error("抛出异常", e);
        }
    }

    /**
     * 供子类实现
     * @throws Exception 异常
     */
    protected abstract void doExec() throws Exception;
}
