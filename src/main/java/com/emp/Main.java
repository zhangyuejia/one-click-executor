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
import java.net.URLDecoder;
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
        if(CONFIG.isValid()){
            logger.info("开始生成copylist，svn路径：{}，目标文件路径：{}，目标文件前缀：{}，svn区间：[{}, {}]",
                    CONFIG.getSvnPath(), CONFIG.getTargetFilePath(), CONFIG.getTargetFilePrefix(), CONFIG.getSvnRevisionNumberStart(), CONFIG.getSvnRevisionNumberEnd());
            final boolean success = makeCopyList();
            logger.info("生成copylist" + (success ? "成功" :"失败"));
        }
    }

    /**
     * 生成copylist
     * @return 生成结果
     */
    private static boolean makeCopyList() {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            // 写入数据，使用Set进行去重
            Set<String> datas = new TreeSet<>();
            // 初始化字符输入输出流
            reader = getBufferedReader();
            writer = getBufferedWriter();
            // 是否需要写入copylist
            boolean isWriteDist = false;
            String line;
            while ((line = reader.readLine()) != null){
                line = URLDecoder.decode(line, "utf-8");
                if(CONFIG.getShowSvnRecord()){
                    logger.info("SVN记录：{}", line);
                }
                String fileName = line.substring(line.lastIndexOf("/") + 1);
                // 只处理修改和新增的记录
                if(!(line.startsWith(Constant.SVN_ADD_RECORD_PREFIX) || line.startsWith(Constant.SVN_MODIFY_RECORD_PREFIX))){
                    continue;
                }
                // 获取相对路径（svn命令返回的行前8个字符是修改类型）
                String relativePath = line.substring(8 + CONFIG.getSvnPath().length() + 1);
                // 过滤非法文件路径、文件夹和SystemGlobals.properties
                if(!fileName.contains(".") || relativePath.endsWith(Constant.SYSTEM_GLOBALS_FILE_NAME)){
                    continue;
                }
                // 如果修改了webapp部分，则认为需要重新打dist
                if(relativePath.startsWith(Constant.RMS_WEBAPP_PREFIX)){
                    isWriteDist = true;
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
            logger.info("copylist路径为：{}", new File(CONFIG.getTargetFilePath()).getCanonicalPath());
        } catch (Exception e) {
            logger.error( "生成copylist出现异常", e);
        }finally {
            FileUtils.close(reader);
            FileUtils.close(writer);
        }
        return true;
    }

    /**
     * 获取字符输出流
     * @return 字符输出流
     * @throws IOException IO异常
     */
    private static BufferedWriter getBufferedWriter() throws IOException {
        File f = new File(CONFIG.getTargetFilePath());
        if(f.exists()){
            final boolean delete = f.delete();
            if(!delete){
                throw new RuntimeException("删除已存在copylist文件（"+f.getCanonicalPath()+"）失败，请手动删除再运行程序！");
            }
        }
        return new BufferedWriter(new FileWriter(f));
    }

    /**
     * 获取字符输入流
     * @return 字符输入流
     * @throws IOException IO异常
     */
    private static BufferedReader getBufferedReader() throws IOException {
        String command = String.format("svn diff -r %s:%s  --summarize %s", String.valueOf(Integer.parseInt(CONFIG.getSvnRevisionNumberStart()) - 1), CONFIG.getSvnRevisionNumberEnd(), CONFIG.getSvnPath());
        Process process = Runtime.getRuntime().exec(command);
        return new BufferedReader(new InputStreamReader(process.getInputStream()));
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
