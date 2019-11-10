package com.zhangyj.config;

import com.zhangyj.constant.DefaultConst;
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
    private void processSvnConfig() throws IOException {
        if(svn.getRevEnd() == null){
            if(StringUtil.isEmpty(svn.getVersionFile())){
                svn.setVersionFile(DefaultConst.VERSION_FILE);
                log.info("配置项[svn->versionFile]为空，使用默认值{}", svn.getVersionFile());
            }
            // 获取版本文件最新版本号
            Integer revEnd = getLatestVersionFileRev();
            log.info("配置项[svn->revEnd]为空，默认版本文件{}最新的版本号{}", svn.getVersionFile(), revEnd);
            svn.setRevEnd(revEnd);

        }
        svn.setPath(processPath(svn.getPath()));
        if(svn.getRevStart() > svn.getRevEnd()){
            throw new RuntimeException("配置项[svn->revStart]不能大于[svn->revEnd]");
        }
        if(svn.getShowRecord() == null){
            svn.setShowRecord(false);
        }
    }

    /**
     * 获取版本文件最新版本号
     * @return 版本文件最新版本号
     * @throws IOException 异常
     */
    private Integer getLatestVersionFileRev() throws IOException {
        // svn用户名
        String userName = SvnUtil.getSvnUserName(svn.getPath());
        // 版本文件svn路径
        String versionFileSvnPath = StringUtil.replaceBackslash(svn.getPath() + File.separator + svn.getVersionFile());
        // 命令
        String command = String.format("svn log %s --search %s -l 50", versionFileSvnPath, userName);
        BufferedReader reader = SvnUtil.getCommandReader(command, Charset.forName("GBK"));
        String s = reader.lines().filter(line -> line.contains(userName)).collect(Collectors.toList()).get(0);
        return Integer.parseInt(s.substring(1, s.indexOf("|")-1));
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

    public static void main(String[] args) {
        String s = "https://192.169.1.81/svn/cmmi/04javaemp/01dev/05code/emp_branches/emp_7.2.0.452m_linux --search zhanglj001 -l 3|findS";
        System.out.println(s.substring(0, s.indexOf("svn")-1));
    }
}
