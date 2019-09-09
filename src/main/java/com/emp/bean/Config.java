package com.emp.bean;

import lombok.Data;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
/**
 * @author ZHANG
 */
@Data
public class Config {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    private static final String CONFIG_FILE_URL = "config.properties";


    /**
     * 单例实例化
     */
    private static class ConfigHolder {
        static final Config INSTANCE = new Config();
    }

    public static Config getInstance() {
        return ConfigHolder.INSTANCE;
    }

    private Config(){
        init();
    }

    private Properties properties;

    /**
     * 初始化配置
     */
    private void init() {
        loadProperties();
        loadField();
    }

    /**
     * 校验配置参数
     */
    public boolean validateField() {
        boolean result = false;
        if(StringUtils.isBlank(this.svnPath)){
            logger.error("配置项svnPath(项目svn路径)不能为空！");
        }else if(StringUtils.isBlank(this.svnRevisionNumberStart)){
            logger.error("配置项svnRevisionNumberStart(项目svn起始版本号)不能为空！");
        }else if(StringUtils.isBlank(this.svnRevisionNumberEnd)){
            logger.error("配置项svnRevisionNumberEnd(项目svn终止版本号)不能为空！");
        }else if(StringUtils.isBlank(this.empWebOutputPath)){
            logger.error("配置项empWebOutputPath(emp web项目编译路径)不能为空！");
        }else {
            result = true;
        }
        return result;
    }

    /**
     *  初始化成员对象
     */
    private void loadField() {
        this.targetFilePath = getPropertiesValue("targetFilePath", Constant.DEFAULT_COPYLIST_PATH);
        this.targetFilePrefix = getPropertiesValue("targetFilePrefix", Constant.DEFAULT_COPYLIST_PREFIX);
        this.empWebOutputPath = getPropertiesValue("empWebOutputPath", "");
        this.svnPath = getPropertiesValue("svnPath", "");
        this.svnRevisionNumberStart = getPropertiesValue("svnRevisionNumberStart", "");
        this.svnRevisionNumberEnd = getPropertiesValue("svnRevisionNumberEnd", "");
    }

    /**
     * 获取值，如果该值为null则取默认值
     * @param key 键
     * @param defaultValue 默认值
     * @return  获取值
     */
    private String getPropertiesValue(String key, String defaultValue) {
        Object value = this.properties.get(key);
        return value != null? String.valueOf(this.properties.get(key)).trim() : defaultValue;
    }

    /**
     * 获取配置参数
     * @return 配置参数
     */
    private void loadProperties() {
        properties = new Properties();
        try {
            properties.load(getConfigReader());
        } catch (Exception e) {
            logger.error("读取配置文件出错", e);
        }
    }

    /**
     * 获取配置流
     * @return 配置流
     * @throws UnsupportedEncodingException
     */
    private Reader getConfigReader() throws UnsupportedEncodingException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_URL);
        return new InputStreamReader(in, Constant.CHARSET_GBK);
    }

    /**
     * 项目svn路径
     */
    private String svnPath;
    /**
     * 项目svn起始版本号
     */
    private String svnRevisionNumberStart;
    /**
     * 项目svn终止版本号
     */
    private String svnRevisionNumberEnd;
    /**
     * 目标文件路径
     */
    private String targetFilePath;
    /**
     * 目标文件前缀
     */
    private String targetFilePrefix;
    /**
     * emp web输出路径
     */
    private String empWebOutputPath;
}
