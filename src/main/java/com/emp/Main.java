package com.emp;

import com.emp.bean.Config;
import com.emp.bean.Constant;
import com.emp.replactor.JavaPathReplactor;
import com.emp.replactor.PathReplactor;
import com.emp.replactor.PathReplactorFactory;
import com.emp.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * jar包入口
 * @author ZHANG
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final Config CONFIG = Config.getInstance();

    public static void main(String[] args) {
        logger.info("开始生成copylist，源文件路径：{}，源文件前缀：{}，目标文件路径：{}，目标文件前缀：{}",
                CONFIG.getSourceFilePath(), CONFIG.getSourceFilePrefix(), CONFIG.getTargetFilePath(), CONFIG.getTargetFilePrefix());
        final boolean success = makeCopyList();
        logger.info("生成copylist" + (success ? "成功" :"失败"));

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
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            boolean isWriteDist = false;
            reader = new BufferedReader(new FileReader(file));
            File copylist = new File(CONFIG.getTargetFilePath());
            writer = new BufferedWriter(new FileWriter(copylist));
            String line;
            // 写入路径数据
            List<String> datas = new ArrayList<String>();
            while ((line = reader.readLine()) != null){
                String fileName = line.substring(line.lastIndexOf("/") + 1);
                // 删除前缀
                String data = line.substring(CONFIG.getSourceFilePrefix().length() + 1);
                // 过滤非法文件路径、文件夹和SystemGlobals.properties
                if(!line.startsWith(CONFIG.getSourceFilePrefix()) || !fileName.contains(".") || data.endsWith(Constant.SYSTEM_GLOBALS_FILE_NAME)){
                    continue;
                }
                // 如果修改了webapp部分，则认为需要重新打dist
                if(data.startsWith(Constant.RMS_WEBAPP_PREFIX)){
                    isWriteDist = true;
                    continue;
                }
                PathReplactor replactor = PathReplactorFactory.getInstance(data);
                if(replactor == null){
                    continue;
                }
                datas.clear();
                datas.add(replactor.replace(data));
                // 检测是否有内部类
                if(replactor instanceof JavaPathReplactor){
                    datas.addAll(getInnerClassPaths(datas.get(0)));
                }
                writeDatas(writer, datas);
            }
            if(isWriteDist){
                logger.info("检测到路径{}下有文件修改，加入dist路径，请勿忘记打包dist", Constant.RMS_WEBAPP_PREFIX);
                writeDatas(writer, Collections.singletonList(CONFIG.getTargetFilePrefix() + "\\rms\\webapp\\dist\\*.*"));
            }
            logger.info("copylist路径为：{}", copylist.getCanonicalPath());
        } catch (FileNotFoundException e) {
            logger.error( "文件找不到异常", e);
        } catch (IOException e) {
            logger.error( "文件IO异常", e);
        }finally {
            FileUtils.close(reader);
            FileUtils.close(writer);
        }
        return true;

    }

    /**
     * 获取内部类路径
     * @param javaClassPath 主类路径
     * @return 内部类路径
     */
    private static List<String> getInnerClassPaths(String javaClassPath) {
        ArrayList<String> innerClassPaths = new ArrayList<String>();
        String dirPath = CONFIG.getEmpWebOutputPath() + javaClassPath.substring(CONFIG.getTargetFilePrefix().length(), javaClassPath.lastIndexOf("\\"));
        String javaClassName = javaClassPath.substring(javaClassPath.lastIndexOf("\\") + 1, javaClassPath.lastIndexOf("."));
        List<String> fileNames = FileUtils.getFileNames(dirPath);
        for (String fileName : fileNames) {
            if(fileName.startsWith(javaClassName + "$")){
                logger.info("发现内部类{}，自动写入路径", fileName);
                String innnerClassPath = javaClassPath.substring(0, javaClassPath.lastIndexOf("\\") - 1) + fileName;
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
    private static void writeDatas(BufferedWriter writer, List<String> datas) throws IOException {
        for (String data : datas) {
            writer.write(data, 0, data.length());
            writer.newLine();
        }
    }


}
