package com.zhangyj.config;

import com.zhangyj.constant.DefaultConst;
import com.zhangyj.utils.SpringContextUtil;
import com.zhangyj.utils.StringUtil;
import com.zhangyj.utils.SvnUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

/**
 * 配置文件类
 * 用于处理配置项和打印配置信息
 * @author zhagnyj
 */
@Data
@Slf4j
@Component
public class Config {

    private final SvnConfig svn;

    private final CopyListConfig copyList;

    private final EmpConfig emp;

    public Config(SvnConfig svn, CopyListConfig copyList, EmpConfig emp) throws Exception {
        this.svn = svn;
        this.copyList = copyList;
        this.emp = emp;
        init();
    }

    private void init() throws Exception {
        // 处理配置信息
        processConfig();
        // 打印配置信息
        printConfig();
    }

    /**
     * 处理配置信息
     */
    private void processConfig() throws Exception {
        log.info("************************** 处理配置信息-开始 **************************");
        // 处理svn配置信息
        processSvnConfig();
        // 处理copyList配置信息
        processCopyListConfig();
        // 处理emp配置信息
        processEmpConfig();
        log.info("************************** 处理配置信息-结束 **************************");
    }

    /**
     * 处理emp配置信息
     */
    private void processEmpConfig() {
        emp.setOutPutPath(processPath(emp.getOutPutPath()));
        // 如果编译路径不存在或者不是文件夹则报错
        File outPutDir = new File(emp.getOutPutPath());
        if(!outPutDir.exists()){
            throw new RuntimeException("配置项[emp->outPutPath]路径不存在");
        }
        if(!outPutDir.isDirectory()){
            throw new RuntimeException("配置项[emp->outPutPath]不是文件夹");
        }

    }

    /**
     * 处理copyList配置信息
     */
    private void processCopyListConfig() throws IOException {
        // 设置为绝对路径
        String path = copyList.getPath();
        if(StringUtils.isEmpty(path)){
            path = DefaultConst.COPY_LIST_PATH;
        }
        copyList.setPath(new File(path).getCanonicalPath());

        if(StringUtils.isEmpty(copyList.getPrefix())){
            copyList.setPrefix(DefaultConst.COPY_LIST_PREFIX);
        }
    }

    /**
     * 处理svn配置信息
     */
    private void processSvnConfig() {
        if(svn.getRevEnd() == null){
            // 获取版本文件最新版本号
            Integer revEnd = getLatestVersion();
            log.info("配置项[svn->revEnd]为空，默认最新的版本号{}", revEnd);
            svn.setRevEnd(revEnd);
        }
        svn.setPath(processPath(svn.getPath()));
        if(svn.getRevStart() > svn.getRevEnd()){
            throw new RuntimeException("配置项[svn->revStart]不能大于[svn->revEnd]");
        }
    }

    /**
     * 获取版本文件最新版本号
     * @return 版本文件最新版本号
     */
    private Integer getLatestVersion() {
        String latestVerPrefix = "Last Changed Rev:";
        String lastChangeInfo = SvnUtil.getSvnInfo(svn.getPath(), line -> line.startsWith(latestVerPrefix)).get(0);
        return Integer.parseInt(lastChangeInfo.substring(latestVerPrefix.length()).trim());
    }

    /**
     * 打印配置信息
     */
    private void printConfig() {
        log.info("************************** 打印配置信息-开始 **************************");
        log.info("**************************** svn配置信息 ******************************");
        log.info("【svn】：path:{}", this.svn.getPath());
        log.info("【svn】：revStart:{}，备注信息↓↓↓", this.svn.getRevStart());
        SvnUtil.showLog(this.svn.getPath(), this.svn.getRevStart());
        log.info("【svn】：revEnd:{}，备注信息↓↓↓", this.svn.getRevEnd());
        SvnUtil.showLog(this.svn.getPath(), this.svn.getRevEnd());
        log.info("*************************** copyList配置信息 ***************************");
        log.info("【copyList】：{}", this.copyList);
        log.info("***************************** emp配置信息 ******************************");
        log.info("【emp】：{}", this.emp);
        log.info("************************** 打印配置信息-结束 **************************");
    }

    /**
     * 处理路径（替换反斜杠为斜杠， 最后一个字符如果为斜杠，将其去除）
     * @param path 路径
     * @return 处理后路径
     */
    private static String processPath(String path){
        // 替换反斜杠为斜杠
        String result = StringUtil.replaceBackslash(path);
        // 最后一个字符如果为斜杠，将其去除
        if(StringUtil.endWithsSlash(result)){
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
