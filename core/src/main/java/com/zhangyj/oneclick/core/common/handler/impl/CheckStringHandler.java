package com.zhangyj.oneclick.core.common.handler.impl;

import com.zhangyj.oneclick.core.common.config.AbstractCmdConfig;
import com.zhangyj.oneclick.core.common.handler.StringHandler;
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
                log.error("输出日志包含错误关键词" + errorWord + "，请检查是否正常");
                System.exit(0);
            }
        }
    }
}
