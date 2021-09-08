package com.zhangyj.copyListMaker;

import com.zhangyj.copyListMaker.config.Config;
import com.zhangyj.copyListMaker.maker.impl.CopyListMaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * @author zhangyj
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(Config.class)
public class DoMake implements CommandLineRunner {

    private final CopyListMaker copyListMaker;

    @Override
    public void run(String... args) throws Exception {
        log.info("************************** 生成copyList-开始 **************************");
        copyListMaker.make();
        log.info("************************** 生成copyList-结束 **************************");
    }
}
