package com.zhangyj.cmdexecutor.core.common.handler;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 责任链模式
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

    /**
     * 获取响应结果
     * @param r 请求
     * @return 响应结果
     */
    protected abstract P getResponse(R r);

    /**
     * 是否进行处理
     * @param r 请求
     * @return 是否进行处理
     */
    protected abstract boolean isHandle(R r);
}
