package com.zhangyj.tools.business.project.multimodule;

import com.google.common.collect.Lists;
import com.zhangyj.tools.business.project.multimodule.config.MultiModulePullCodeConfig;
import com.zhangyj.tools.business.project.multimodule.pojo.ModuleProperties;
import com.zhangyj.tools.common.base.AbstractRunner;
import com.zhangyj.tools.common.constant.CharSets;
import com.zhangyj.tools.common.utils.CommandUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangyj
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(MultiModulePullCodeConfig.class)
public class DoMultiModulePullCode extends AbstractRunner<MultiModulePullCodeConfig> {

    private final static String CURRENT_BRANCH_FLAG = "* ";

    private final static String LOCAL_BRANCH_FLAG = "  ";

    @Override
    protected void doRun() throws IOException {
        // 是否检查只有一个replaceId
        if(config.getEnableRefId().size() > 1){
            throw new RuntimeException("配置项[multi-module-pull.enableReplaceId]配置个数不能大于1个");
        }
        for (ModuleProperties moduleProperties : config.getModulesProperties()) {
            if(!config.getEnableRefId().contains(moduleProperties.getRefId())){
                continue;
            }
            log.info("启用配置ID:{}", moduleProperties.getRefId());
            for (ModuleProperties.ModulesParam modulesParam : moduleProperties.getModulesParams()) {
                // 切换到本地分支
                checkoutLocalBranch(moduleProperties, modulesParam);
                // 更新代码
                pullCode(moduleProperties, modulesParam);
            }
        }
    }

    private void checkOutput(List<String> commandOutput) {
        for (String output : commandOutput) {
            for (String errorWord : config.getErrorLogWords()) {
                if(output.contains(errorWord)){
                    throw new RuntimeException("输出日志包含错误关键词" + errorWord + "，请检查是否正常");
                }
            }
        }
    }

    private void pullCode(ModuleProperties moduleProperties, ModuleProperties.ModulesParam modulesParam) throws IOException {
        String modulePath = moduleProperties.getProjectPath() + File.separator + modulesParam.getName();
        for (String remoteBranch : modulesParam.getRemoteBranch()) {
            logOutput(CommandUtil.getCommandOutput(CharSets.CHARSET_GBK, new String[]{"git", "pull", moduleProperties.getRemoteRepoName(), remoteBranch}, modulePath));
        }
    }

    private void checkoutLocalBranch(ModuleProperties moduleProperties, ModuleProperties.ModulesParam modulesParam) throws IOException {
        if (!moduleProperties.getEnableCheckoutLocalBranch()) {
            log.info("未开启本地分支切换开关，无需切换");
            return;
        }
        String modulePath = moduleProperties.getProjectPath() + File.separator + modulesParam.getName();
        List<String> commandOutput = CommandUtil.getCommandOutput(CharSets.CHARSET_GBK, new String[]{"git", "branch"}, modulePath);
        String currentBranch = commandOutput.stream().filter(v -> v.startsWith(CURRENT_BRANCH_FLAG)).collect(Collectors.toList()).get(0).substring(CURRENT_BRANCH_FLAG.length());
        String localBranch = modulesParam.getLocalBranch().trim();
        if(currentBranch.equals(localBranch)){
            log.info("{}仓库处于本地分支{}，无需切换", modulesParam.getName(), modulesParam.getLocalBranch());
        } else if(commandOutput.contains(LOCAL_BRANCH_FLAG + localBranch)){
            log.info("{}仓库处于其他分支{}，需要切换为{}", modulesParam.getName(), localBranch, modulesParam.getLocalBranch());
            logOutput(CommandUtil.getCommandOutput(CharSets.CHARSET_GBK, new String[]{"git", "checkout", localBranch}, modulePath));
        }else {
            log.info("{}仓库不存在分支，从远程仓库{}检出分支{}", modulesParam.getName(), moduleProperties.getRemoteRepoName(), modulesParam.getLocalBranch());
            logOutput(CommandUtil.getCommandOutput(CharSets.CHARSET_GBK, new String[]{"git", "checkout", "-b", localBranch, moduleProperties.getRemoteRepoName() + "/" + modulesParam.getLocalBranch()}, modulePath));
        }
    }

    private void logOutput(List<String> commandOutput) {
        commandOutput.forEach(log::info);
        checkOutput(commandOutput);
    }
}
