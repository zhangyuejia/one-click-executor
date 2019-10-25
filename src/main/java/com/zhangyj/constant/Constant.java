package com.zhangyj.constant;

/**
 * 常量类
 * @author ZHANG
 */
public interface Constant {
    /**
     * GBK编码
     */
    String CHARSET_GBK = "GBK";
    /**
     * 字符串分隔符（逗号）
     */
    String SPLIT_SIGN = ",";
    /**
     * 替换器-WebRoot文件夹前缀
     */
    String REPLACTOR_WEB_ROOT_PREFIX = "WebRoot";
    /**
     * 替换器-资源文件前缀
     */
    String REPLACTOR_PRESOURCE_REFIX = "properties";
    /**
     * 替换器-java文件后缀
     */
    String REPLACTOR_JAVA_SUFFIX = ".java";
    /**
     * emp总配置文件
     */
    String SYSTEM_GLOBALS_FILE_NAME = "SystemGlobals.properties";

    String RMS_WEBAPP_PREFIX = "WebRoot/rms/webapp";
    /**
     * 默认copylist前缀
     */
    String DEFAULT_COPYLIST_PREFIX="emp_sta";
    /**
     * 默认copylist路径
     */
    String DEFAULT_COPYLIST_PATH="copylist_new.txt";
    /**
     * svn修改记录前缀
     */
    String SVN_MODIFY_RECORD_PREFIX= "M       ";
    /**
     * svn新增记录前缀
     */
    String SVN_ADD_RECORD_PREFIX= "A       ";
    /**
     * 反斜杠
     */
    String BACK_SLASH = "\\";
    /**
     * 斜杠
     */
    String SLASH = "/";
    /**
     * 1
     */
    String ONE = "1";
    /**
     * dist路径
     */
    String DIST_PATH = "\\rms\\webapp\\dist\\*.*";
}
