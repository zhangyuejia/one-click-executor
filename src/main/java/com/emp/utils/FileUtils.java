package com.emp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 文件操作工具类
 * @author ZHANG
 */
public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    /**
     * 如果该路径文件存在，则返回file对象
     * @param path 文件路径
     * @return 文件对象
     */
    public static File isExisted(String path){
        File file = new File(path);
        return file.exists()? file: null;
    }

    /**
     * 获取当前jar的绝对路径
     * @return
     */
    public static String getJarPath() {
        String jarWholePath = FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            jarWholePath = java.net.URLDecoder.decode(jarWholePath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.toString());
        }
        return new File(jarWholePath).getParentFile().getAbsolutePath();
    }

    /**
     * 关闭字符流
     * @param reader 字符流
     */
    public static void close(Reader reader) {
        if(reader != null){
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭字符流
     * @param writer 字符流
     */
    public static void close(Writer writer) {
        if(writer != null){
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取某个目录下的文件名
     * @param dir 路径
     * @return 文件名集合
     */
    public static Set<String> getFileNames(String dir){
        final Set<String> fileNames = new HashSet<>();
        new File(dir).listFiles((File pathname) ->{
            if(pathname.isFile()){
                fileNames.add(pathname.getName());
                return true;
            }
            return false;
        });
        return fileNames;
    }

    public static void main(String[] args) {
        getFileNames("E:\\mw_zhangyj\\项目\\20180816-富信通");
    }
}
