package com.zhangyj.oneclick.component.entity.bo;

import lombok.Data;

import java.util.List;

/**
 * @author zhangyj
 */
@Data
public class ModulePropertiesBO {

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
    private String dir;

    /**
     * 是否需要切换到本地分支
     */
    private Boolean enableCheckoutLocalBranch;

    private List<ModulesParam> modulesParams;

    @Data
    public static class ModulesParam {

        private String name;

        private String localBranch;

        private List<String> remoteBranch;
    }
}
