package com.zhangyj.splicer;

import com.zhangyj.enums.AppFunEnum;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author zhangyj
 */
@Slf4j
public abstract class BaseSplicer {

    protected void logInfo(String info) {
        log.info(getLog(info));
    }

    protected void logError(String info) {
        log.error(getLog(info));
    }

    protected String getLog(String info){
        return "[" + AppFunEnum.FILE_SPLICER.name() + "]" + info;
    }
}
