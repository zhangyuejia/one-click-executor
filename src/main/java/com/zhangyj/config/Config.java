package com.zhangyj.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhagnyj
 */
@Data
@Slf4j
@Component
public class Config {

    private final SvnConfig svn;

    private final CopyListConfig copyList;

    private final EmpConfig emp;

    public Config(CopyListConfig copyListConfig, SvnConfig svnConfig, EmpConfig empConfig) {
        this.copyList = copyListConfig;
        this.svn = svnConfig;
        this.emp = empConfig;
        loadConfig();
    }

    private void loadConfig() {
        this.validateConfig();
        this.printConfig();
    }

    private void printConfig() {
        log.info("************************** 配置信息-开始 **************************");
        log.info("svn配置信息：{}", this.svn);
        log.info("copyList配置信息：{}", this.copyList);
        log.info("emp配置信息：{}", this.emp);
        log.info("************************** 配置信息-结束 **************************");
    }

    private void validateConfig() {
        log.info("校验配置文件");
    }


}
