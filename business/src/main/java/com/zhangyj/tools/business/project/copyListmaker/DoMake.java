package com.zhangyj.tools.business.project.copyListmaker;

import com.zhangyj.tools.business.project.copyListmaker.config.Config;
import com.zhangyj.tools.business.project.copyListmaker.maker.impl.CopyListMaker;
import com.zhangyj.tools.common.base.AbstractRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * @author zhangyj
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(Config.class)
public class DoMake extends AbstractRunner<Config> {

    private final CopyListMaker copyListMaker;

    @Override
    protected void doRun() throws Exception {
        log.info("************************** 生成copyList-开始 **************************");
        copyListMaker.make();
        log.info("************************** 生成copyList-结束 **************************");
    }
}
