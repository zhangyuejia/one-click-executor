package com.zhangyj.cmdexecutor.core.common.handler.impl;

import com.zhangyj.cmdexecutor.core.common.config.AbstractCmdConfig;
import com.zhangyj.cmdexecutor.core.common.handler.StringHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * @author zhangyj
 */
@Slf4j
@RequiredArgsConstructor
public class CheckStringHandler implements StringHandler {

    private final AbstractCmdConfig cmdConfig;

    @Override
    public void handle(String str) {
        log.info(str);
        if (StringUtils.isBlank(str) || CollectionUtils.isEmpty(cmdConfig.getErrorLogWords())) {
            return;
        }
        for (String errorWord : cmdConfig.getErrorLogWords()) {
            if(StringUtils.containsIgnoreCase(str, errorWord)){
                try {
                    throw new RuntimeException("输出日志包含错误关键词" + errorWord + "，请检查是否正常");
                }catch (Exception e){
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }
    }
}
