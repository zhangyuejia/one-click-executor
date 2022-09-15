package com.zhangyj.tools.business.project.multimodule.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author zhangyj
 */
@Data
public class ModuleProperties {

    /**
     * id
     */
    private String refId;
    /**
     * 远程仓库名称
     */
    private String remoteRepoName;

    /**
     * 项目路径
     */
    private String projectPath;

    /**
     * 是否需要切换到本地分支
     */
    private Boolean enableCheckoutLocalBranch;

    private List<ModulesParam> modulesParams;

    @Data
    public static class ModulesParam {

        private String name;

        private String localBranch;

        private String[] remoteBranch;
    }
}
