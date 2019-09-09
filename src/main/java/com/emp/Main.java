package com.emp;

import com.emp.bean.Config;
import com.emp.bean.Constant;
import com.emp.replactor.JavaPathReplactor;
import com.emp.replactor.PathReplactor;
import com.emp.replactor.PathReplactorFactory;
import com.emp.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Set;
import java.util.TreeSet;

/**
 * jar包入口
 * @author ZHANG
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final Config CONFIG = Config.getInstance();

    public static void main(String[] args) {
        if(CONFIG.validateField()){
            logger.info("开始生成copylist，源文件路径：{}，源文件前缀：{}，目标文件路径：{}，目标文件前缀：{}",
                    CONFIG.getSourceFilePath(), CONFIG.getSourceFilePrefix(), CONFIG.getTargetFilePath(), CONFIG.getTargetFilePrefix());
            final boolean success = makeCopyList();
            logger.info("生成copylist" + (success ? "成功" :"失败"));
        }
    }

    /**
     * 生成copylist
     * @return 生成结果
     */
    private static boolean makeCopyList() {
        final File file = FileUtils.isExisted(CONFIG.getSourceFilePath());
        if(file == null){
            logger.error("源文件路径不存在:{}", CONFIG.getSourceFilePath());
            return false;
        }
        // 写入数据，使用Set进行去重
        Set<String> datas = new TreeSet<>();
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            // 是否需要写入copylist
            boolean isWriteDist = false;
            reader = new BufferedReader(new FileReader(file));
            File copylist = new File(CONFIG.getTargetFilePath());
            writer = new BufferedWriter(new FileWriter(copylist));
            String line;

            while ((line = reader.readLine()) != null){
                String fileName = line.substring(line.lastIndexOf("/") + 1);
                // 获取相对路径
                String relativePath = line.substring(CONFIG.getSourceFilePrefix().length() + 1);
                // 过滤非法文件路径、文件夹和SystemGlobals.properties
                if(!line.startsWith(CONFIG.getSourceFilePrefix()) || !fileName.contains(".") || relativePath.endsWith(Constant.SYSTEM_GLOBALS_FILE_NAME)){
                    continue;
                }
                // 如果修改了webapp部分，则认为需要重新打dist
                if(relativePath.startsWith(Constant.RMS_WEBAPP_PREFIX)){
                    isWriteDist = true;
                    continue;
                }
                // 过滤svn删除记录(如果在源码中不存在，认为该记录为删除记录)
                if(isNotExistedInSourceCode(relativePath)){
                    logger.info("检测到路径svn删除记录:{}", line);
                    continue;
                }
                PathReplactor replactor = PathReplactorFactory.getReplator(relativePath);
                if(replactor == null){
                    continue;
                }
                String data = replactor.replace(relativePath);
                datas.add(data);
                // 检测是否有内部类
                if(replactor instanceof JavaPathReplactor){
                    datas.addAll(getInnerClassPaths(data));
                }
            }

            if(isWriteDist){
                logger.info("检测到路径{}下有文件修改，加入dist路径，请勿忘记打包dist", Constant.RMS_WEBAPP_PREFIX);
                datas.add(CONFIG.getTargetFilePrefix() + "\\rms\\webapp\\dist\\*.*");
            }
            writeDatas(writer, datas);
            logger.info("copylist路径为：{}", copylist.getCanonicalPath());
        } catch (Exception e) {
            logger.error( "文件IO异常", e);
        }finally {
            FileUtils.close(reader);
            FileUtils.close(writer);
        }
        return true;

    }

    /**
     * 过滤svn删除记录
     * @param pathData 相对路径
     * @return 是否存在
     */
    private static boolean isNotExistedInSourceCode(String pathData) {
        // 绝对路径
        String absolutePath = CONFIG.getSourceCodePath() + "\\" + pathData;
        return FileUtils.isExisted(absolutePath) == null;
    }

    /**
     * 获取内部类路径
     * @param javaClassPath 主类路径
     * @return 内部类路径
     */
    private static Set<String> getInnerClassPaths(String javaClassPath) {
        Set<String> innerClassPaths = new TreeSet<>();
        // 获取绝对路径
        String dirPath = CONFIG.getEmpWebOutputPath() + javaClassPath.substring(CONFIG.getTargetFilePrefix().length(), javaClassPath.lastIndexOf("\\"));
        // 主类名
        String javaClassName = javaClassPath.substring(javaClassPath.lastIndexOf("\\") + 1, javaClassPath.lastIndexOf("."));
        Set<String> fileNames = FileUtils.getFileNames(dirPath);
        for (String fileName : fileNames) {
            if(fileName.startsWith(javaClassName + "$")){
                logger.info("发现内部类{}，自动写入路径", fileName);
                String innnerClassPath = javaClassPath.substring(0, javaClassPath.lastIndexOf("\\") + 1) + fileName;
                innerClassPaths.add(innnerClassPath);
            }
        }
        return innerClassPaths;
    }

    /**
     * 写文件
     * @param writer 写入类
     * @param datas 数据
     * @throws IOException 异常
     */
    private static void writeDatas(BufferedWriter writer, Set<String> datas) throws IOException {
        for (String data : datas) {
            writer.write(data, 0, data.length());
            writer.newLine();
        }
    }


}
