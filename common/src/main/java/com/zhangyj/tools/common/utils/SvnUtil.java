package com.zhangyj.tools.common.utils;

import com.zhangyj.tools.common.constant.CharSets;
import lombok.extern.slf4j.Slf4j;

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
     * 打印版本号备注信息
     * @param svnPath svn路径
     * @param rev svn版本号
     */
    public static void showLog(String svnPath, Integer rev) throws Exception {
        String command = String.format("svn log %s -r %d", svnPath, rev);
        CommandUtil.execCommand(CharSets.CHARSET_GBK, command, null, log::info);
    }

    public static List<String> getSvnInfo(String svnPath, Predicate<String> predicate) throws Exception {
        String command = String.format("svn info %s",svnPath);
        return CommandUtil.execCommand(CharSets.CHARSET_GBK, command, null).stream().filter(predicate).collect(Collectors.toList());
    }

}
