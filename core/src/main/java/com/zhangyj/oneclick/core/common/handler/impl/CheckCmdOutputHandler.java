package com.zhangyj.oneclick.core.common.handler.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zhangyj.oneclick.core.common.config.AbstractCmdConfig;
import com.zhangyj.oneclick.core.common.constant.CoreConstant;
import com.zhangyj.oneclick.core.common.enums.CmdTypeParamEnum;
import com.zhangyj.oneclick.core.common.handler.CmdOutputHandler;
import com.zhangyj.oneclick.core.entity.bo.CmdProcessBo;
import com.zhangyj.oneclick.core.entity.bo.CmdTypeParamBO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;


/**
 * @author zhangyj
 */
@Slf4j
@RequiredArgsConstructor
public class CheckCmdOutputHandler implements CmdOutputHandler {

    private final AbstractCmdConfig cmdConfig;

    @Override
    public void handle(CmdProcessBo cmdProcessBo, String str) {
        CmdTypeParamBO cmdTypeParam = cmdConfig.getCmdLineBo().getCmdType().getCmdTypeParam();
        if (!cmdTypeParam.getBoolean(CmdTypeParamEnum.ENABLE_OUTPUT)) {
            return;
        }
        log.info(CoreConstant.PREFIX_CMD_OUT + "{}", cmdProcessBo.getCmd(), cmdProcessBo.getDir(), str);
        if (!cmdTypeParam.getBoolean(CmdTypeParamEnum.ENABLE_EXIT_MATCH_ERROR)) {
            return;
        }
        if (StringUtils.isBlank(str) || CollectionUtils.isEmpty(cmdConfig.getErrorLogWords())) {
            return;
        }
        for (String errorWord : cmdConfig.getErrorLogWords()) {
            if(!StringUtils.containsIgnoreCase(str, errorWord)){
                continue;
            }
            String currSkipKeyWord = null;
            if (CollUtil.isNotEmpty(cmdConfig.getSkipKeyWords())) {
                for (String skipKeyWord : cmdConfig.getSkipKeyWords()) {
                    if(StringUtils.containsIgnoreCase(str, skipKeyWord)){
                        currSkipKeyWord = skipKeyWord;
                        break;
                    }
                }
            }
            if (StrUtil.isNotEmpty(currSkipKeyWord)) {
                log.info("输出日志包含错误关键词:{}，匹配白名单关键字：{}，继续执行", errorWord, currSkipKeyWord);
            }else {
                log.error("输出日志包含错误关键词:{}，程序退出，当前命令：{}", errorWord, cmdConfig.getCmdLineBo().getOriginalValue());
                System.exit(0);
            }
        }
    }
}
