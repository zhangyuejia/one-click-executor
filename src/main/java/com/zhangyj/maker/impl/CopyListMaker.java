package com.zhangyj.maker.impl;

import com.google.common.collect.Lists;
import com.zhangyj.product.impl.CopyList;
import com.zhangyj.maker.Maker;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

/**
 * @author ZHANG
 */
@Component
public class CopyListMaker implements Maker<CopyList> {

    @Override
    public CopyList make() {
        List<String> data = Lists.newArrayList();
        try (BufferedReader reader = getBufferedReader(); BufferedWriter writer = getBufferedWriter()){

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CopyList(data);
    }

    /**
     * 获取字符输出流
     * @return 字符输出流
     * @throws IOException IO异常
     */
    private static BufferedWriter getBufferedWriter() throws IOException {
        File f = new File(CONFIG.getTargetFilePath());
        if(f.exists() && !f.delete()){
            throw new RuntimeException("删除已存在copylist文件（"+f.getCanonicalPath()+"）失败，请手动删除再运行程序！");
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
}
