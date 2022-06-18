package com.zhangyj.tools.common.handler;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangyj
 */
@Slf4j
public abstract class ChainHandler<R, P> {

    @Getter
    @Setter
    private ChainHandler<R, P> nextHandler;

    public P handle(R r){
        if(isHandle(r)){
            return getResponse(r);
        }else if (nextHandler != null){
            return nextHandler.handle(r);
        }else {
            log.error("匹配不到Handler!{}", r);
            return null;
        }
    }

    protected abstract P getResponse(R r);

    protected abstract boolean isHandle(R r);
}
