package com.zhangyj.tools.common.utils;

import com.zhangyj.tools.common.constant.CharSets;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * svn记录
 * @author zhangyj
 */
@Slf4j
public class SvnUtil {

    /**
     * 是否为新增或者修改记录
     * @param svnRecord svn路径
     * @return 判断结果
     */
    public static boolean isAddOrModifyRecord(String svnRecord){
        final String addPrefix ="A", modifyPrefix ="M";
        return svnRecord.startsWith(addPrefix) || svnRecord.startsWith(modifyPrefix);
    }

    /**
     * 获取svn变更记录字符输入流
     * @param svnPath svn路径
     * @param revStart svn开始版本号
     * @param revEnd svn结束版本号
     * @return 字符输入流
     * @throws IOException IO异常
     */
    public static BufferedReader getDiffRecordReader(String svnPath, Integer revStart, Integer revEnd) throws IOException {
        String command = String.format("svn diff -r %d:%d  --summarize %s", revStart - 1, revEnd, svnPath);
        return CommandUtil.getCommandReader(CharSets.CHARSET_GBK, command);
    }

    /**
     * 获取svn记录字符输入流
     * @param rev 版本号
     * @param svnPath svn路径
     * @return svn记录字符输入流
     * @throws IOException 异常
     */
    private static BufferedReader getLogReader(String svnPath, Integer rev) throws IOException {
        String command = String.format("svn log %s -r %d", svnPath, rev);
        return CommandUtil.getCommandReader(CharSets.CHARSET_GBK, command);
    }

    /**
     * 打印版本号备注信息
     * @param svnPath svn路径
     * @param rev svn版本号
     */
    public static void showLog(String svnPath, Integer rev){
        try (BufferedReader reader = getLogReader(svnPath, rev)){
            reader.lines().filter(StringUtil::isNotEmpty).forEach(log::info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getSvnInfo(String svnPath, Predicate<String> predicate){
        String command = String.format("svn info %s",svnPath);
        try (BufferedReader reader = CommandUtil.getCommandReader(CharSets.CHARSET_GBK, command)){
            return reader.lines().filter(predicate).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("执行命令出错："+command);
        }
    }

}
