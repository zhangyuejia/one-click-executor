package com.zhangyj.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * svn记录
 * @author zhangyj
 */
public class SvnUtil {

    /**
     * 不是svn修改记录
     */
    /**
     *
     * @param svnRecord
     * @return
     */
    public static boolean notAddOrModifyRecord(String svnRecord){
        final String addPrefix ="A", modifyPrefix ="M";
        return !svnRecord.startsWith(addPrefix) && !svnRecord.startsWith(modifyPrefix);
    }

    /**
     * 获取字符输入流
     * @param svnPath svn路径
     * @param revStart svn开始版本号
     * @param revEnd svn结束版本号
     * @return 字符输入流
     * @throws IOException IO异常
     */
    public static BufferedReader getDiffRecordReader(String svnPath, Integer revStart, Integer revEnd) throws IOException {
        String command = String.format("svn diff -r %d:%d  --summarize %s", revStart - 1, revEnd, svnPath);
        Process process = Runtime.getRuntime().exec(command);
        return new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

    /**
     * 为EMP配置文件SystemGlobals的修改记录
     */
    public static boolean isSystemGlobalsDiffRecord(String svnRecord){
        final String systemGlobalsSuffix ="SystemGlobals.properties";
        return svnRecord.endsWith(systemGlobalsSuffix);
    }
}
