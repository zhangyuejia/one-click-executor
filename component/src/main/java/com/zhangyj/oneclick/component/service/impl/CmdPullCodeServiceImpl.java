package com.zhangyj.oneclick.component.service.impl;

import cn.hutool.core.util.StrUtil;
import com.zhangyj.oneclick.component.common.config.CmdPullCodeConfig;
import com.zhangyj.oneclick.component.entity.bo.ModulePropertiesBO;
import com.zhangyj.oneclick.core.common.handler.impl.CheckCmdOutputHandler;
import com.zhangyj.oneclick.core.common.util.CommandUtils;
import com.zhangyj.oneclick.core.common.util.StrUtils;
import com.zhangyj.oneclick.core.service.AbstractCmdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author zhangyj
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CmdPullCodeServiceImpl extends AbstractCmdService<CmdPullCodeConfig> {

    private final static String CURRENT_BRANCH_FLAG = "* ";

    private final static String LOCAL_BRANCH_FLAG = "  ";

    private final static String BRANCH_SP = "/";

    private final static String REMOTE_BRANCH_FLAG = LOCAL_BRANCH_FLAG + "remotes" + BRANCH_SP;

    private final List<CompletableFuture<Void>> futureList = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void exec() throws Exception {
        initConfig();
        for (ModulePropertiesBO moduleProperties : config.getModulesProperties()) {
            if(!config.getCurrentRefId().equals(moduleProperties.getRefId())){
                continue;
            }

            log.info("启用配置ID:{}", moduleProperties.getRefId());
            if (Boolean.TRUE.equals(config.getEnablePullAsync())) {
                pullModuleCodeAsync(moduleProperties);
            }else {
                pullModuleCodeSync(moduleProperties);
            }
        }
    }

    private void pullModuleCodeAsync(ModulePropertiesBO moduleProperties) {
        for (ModulePropertiesBO.ModulesParam modulesParam : moduleProperties.getModulesParams()) {
            futureList.add(CompletableFuture.runAsync(() -> {
                try {
                    pullModuleCode(moduleProperties, modulesParam);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
    }

    private void pullModuleCodeSync(ModulePropertiesBO moduleProperties) throws Exception{
        for (ModulePropertiesBO.ModulesParam modulesParam : moduleProperties.getModulesParams()) {
            pullModuleCode(moduleProperties, modulesParam);
        }
    }

    private void pullModuleCode(ModulePropertiesBO moduleProperties, ModulePropertiesBO.ModulesParam modulesParam) throws Exception {
        String modulePath = getModulePath(moduleProperties, modulesParam);
        if(!new File(modulePath).exists()){
            log.error("模块{}路径不存在，跳过:{}", modulesParam.getName(), modulePath);
            return;
        }
        fetchRepo(moduleProperties, modulesParam);
        // 切换到本地分支
        checkoutLocalBranch(moduleProperties, modulesParam);
        // 更新代码
        pullCode(moduleProperties, modulesParam);
    }

    private void initConfig() {
        if (CollectionUtils.isEmpty(config.getModulesProperties())) {
            return;
        }
        for (ModulePropertiesBO modulesProperty : config.getModulesProperties()) {
            if (StringUtils.isBlank(modulesProperty.getDir())) {
                modulesProperty.setDir(cmdExecConfig.getDir());
            }
        }
    }

    private void fetchRepo(ModulePropertiesBO moduleProperties, ModulePropertiesBO.ModulesParam modulesParam) throws Exception {
        String command = "git fetch --all";
        logExecCmdOutput(moduleProperties, modulesParam, command);
    }

    private void pullCode(ModulePropertiesBO moduleProperties, ModulePropertiesBO.ModulesParam modulesParam) throws Exception {
        for (String remoteBranch : modulesParam.getRemoteBranch()) {
            String command = "git pull " + moduleProperties.getRemoteRepoName() + " " + remoteBranch;
            logExecCmdOutput(moduleProperties, modulesParam, command);
        }
    }

    private void checkoutLocalBranch(ModulePropertiesBO moduleProperties, ModulePropertiesBO.ModulesParam modulesParam) throws Exception {
        if (!moduleProperties.getEnableCheckoutLocalBranch()) {
            log.info("未开启本地分支切换开关，无需切换");
            return;
        }

        String modulePath = getModulePath(moduleProperties, modulesParam);
        CommandUtils.execCommand(cmdExecConfig.getCharset(), "git remote -vv", modulePath, new CheckCmdOutputHandler(config));
        List<String> commandOutput = CommandUtils.execCommand(cmdExecConfig.getCharset(), "git branch -a", modulePath);
        String currentBranch = commandOutput.stream().filter(v -> v.startsWith(CURRENT_BRANCH_FLAG)).collect(Collectors.toList()).get(0).substring(CURRENT_BRANCH_FLAG.length());
        String localBranch = modulesParam.getLocalBranch().trim();
        if (StrUtil.isBlank(moduleProperties.getCheckoutRemoteRepoName())) {
            throw new RuntimeException("配置项【checkoutRemoteRepoName】未配置");
        }
        if(currentBranch.equals(localBranch)){
            log.debug("{}仓库处于本地分支{}，无需切换", modulesParam.getName(), modulesParam.getLocalBranch());
        } else if(commandOutput.contains(LOCAL_BRANCH_FLAG + localBranch)){
            log.info("{}仓库处于其他分支{}，需要切换为{}", modulesParam.getName(), currentBranch, modulesParam.getLocalBranch());
            String command = "git checkout " + localBranch;
            logExecCmdOutput(moduleProperties, modulesParam, command);
        }else if(commandOutput.contains(REMOTE_BRANCH_FLAG + moduleProperties.getCheckoutRemoteRepoName() + BRANCH_SP + localBranch)){
            log.info("{}仓库从远程仓库{}检出新分支{}", modulesParam.getName(), moduleProperties.getCheckoutRemoteRepoName(), modulesParam.getLocalBranch());
            String command = "git checkout -b " + localBranch + " " + moduleProperties.getCheckoutRemoteRepoName() + BRANCH_SP + modulesParam.getLocalBranch();
            logExecCmdOutput(moduleProperties, modulesParam, command);
        }else {
            throw new RuntimeException("远程仓库不存在分支" + modulesParam.getLocalBranch() + ",无法进行检出");
        }
    }

    private void logExecCmdOutput(ModulePropertiesBO moduleProperties, ModulePropertiesBO.ModulesParam modulesParam, String cmdArr) throws Exception {
        String modulePath = getModulePath(moduleProperties, modulesParam);
        CommandUtils.execCommand(cmdExecConfig.getCharset(), cmdArr, modulePath, new CheckCmdOutputHandler(config));
    }

    private String getModulePath(ModulePropertiesBO moduleProperties, ModulePropertiesBO.ModulesParam modulesParam){
        return moduleProperties.getDir() + File.separator + modulesParam.getName();
    }

    @Override
    public String getDesc() {
        return "多模块代码更新配置";
    }
}
