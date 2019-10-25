package com.zhangyj.maker.impl;

import com.google.common.collect.Sets;
import com.zhangyj.config.CopyListConfig;
import com.zhangyj.config.SvnConfig;
import com.zhangyj.constant.Constant;
import com.zhangyj.finder.impl.InnerClassFinder;
import com.zhangyj.maker.Maker;
import com.zhangyj.product.impl.CopyList;
import com.zhangyj.replactor.JavaReplacer;
import com.zhangyj.replactor.Replacer;
import com.zhangyj.replactor.ReplacerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLDecoder;
import java.util.Set;

/**
 * @author ZHANG
 */
@Component
@Slf4j
public class CopyListMaker implements Maker<CopyList> {

    private final CopyListConfig copyListConfig;

    private final SvnConfig svnConfig;

    private final InnerClassFinder innerClassFinder;

    public CopyListMaker(CopyListConfig copyListConfig, SvnConfig svnConfig, InnerClassFinder innerClassFinder) {
        this.copyListConfig = copyListConfig;
        this.svnConfig = svnConfig;
        this.innerClassFinder = innerClassFinder;
    }


    @Override
    public CopyList make() throws Exception {
        Set<String> datas = Sets.newHashSet();
        try (BufferedReader reader = getBufferedReader()){
            // 是否需要写入copylist
            boolean isWriteDist = false;
            String line;
            while ((line = reader.readLine()) != null){
                line = URLDecoder.decode(line, "utf-8");
                String fileName = line.substring(line.lastIndexOf("/") + 1);
                // 只处理修改和新增的记录
                if(!(line.startsWith(Constant.SVN_ADD_RECORD_PREFIX) || line.startsWith(Constant.SVN_MODIFY_RECORD_PREFIX))){
                    continue;
                }
                // 获取相对路径（svn命令返回的行前8个字符是修改类型）
                String relativePath = line.substring(8 + svnConfig.getPath().length() + 1);
                // 过滤非法文件路径、文件夹和SystemGlobals.properties
                if(!fileName.contains(".") || relativePath.endsWith(Constant.SYSTEM_GLOBALS_FILE_NAME)){
                    continue;
                }
                // 如果修改了webapp部分，则认为需要重新打dist
                if(relativePath.startsWith(Constant.RMS_WEBAPP_PREFIX)){
                    isWriteDist = true;
                    continue;
                }
                Replacer replactor = ReplacerFactory.getReplacer(relativePath);
                if(replactor == null){
                    continue;
                }
                String data = replactor.replace(relativePath);
                datas.add(data);
                // 检测是否有内部类
                if(replactor instanceof JavaReplacer){
                    datas.addAll(innerClassFinder.find(data));
                }
            }

            if(isWriteDist){
                log.info("检测到路径{}下有文件修改，加入dist路径，请勿忘记打包dist", Constant.RMS_WEBAPP_PREFIX);
                datas.add(copyListConfig.getPrefix() + "\\rms\\webapp\\dist\\*.*");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CopyList(datas, "");
    }



    /**
     * 获取字符输入流
     * @return 字符输入流
     * @throws IOException IO异常
     */
    private BufferedReader getBufferedReader() throws IOException {
        String command = String.format("svn diff -r %d:%d  --summarize %s", svnConfig.getRevStart() - 1, svnConfig.getRevEnd(), svnConfig.getPath());
        Process process = Runtime.getRuntime().exec(command);
        return new BufferedReader(new InputStreamReader(process.getInputStream()));
    }

}
