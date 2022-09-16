package com.zhangyj.tools.business.other;


import com.zhangyj.tools.business.other.config.OtherConfig;
import com.zhangyj.tools.business.other.reportsql.DoReportSql;
import com.zhangyj.tools.common.base.AbstractRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * 其他功能
 * @author zhangyj
 */
@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(OtherConfig.class)
public class DoOther extends AbstractRunner<OtherConfig> {

    @Override
    protected void doRun() throws Exception {
        new DoReportSql().exec();
    }

}
