package com.zhangyj.copyListMaker.maker.impl;

import com.google.common.collect.Sets;
import com.zhangyj.common.constant.CharSetConst;
import com.zhangyj.common.constant.CharSets;
import com.zhangyj.common.constant.Const;
import com.zhangyj.copyListMaker.config.Config;
import com.zhangyj.copyListMaker.maker.Maker;
import com.zhangyj.copyListMaker.replactor.BaseCopyListConverter;
import com.zhangyj.copyListMaker.replactor.ConverterFactory;
import com.zhangyj.common.utils.SvnUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * copyList生成器类
 * @author ZHANG
 */
@Component
@Slf4j
@ConditionalOnBean(Config.class)
public class CopyListMaker implements Maker<String> {

    private final Config config;

    private final ConverterFactory converterFactory;

    private final Set<String> copyListLines = Sets.newTreeSet();

    public CopyListMaker(Config config, ConverterFactory converterFactory) {
        this.config = config;
        this.converterFactory = converterFactory;
    }

    @Override
    public String make() throws Exception {
        // 读取copyList数据
        readCopyList();
        // 将copyList数据写入文件
        writeCopyList();
        return config.getCopyList().getPath();
    }

    private void readCopyList() throws IOException {
        // 获取输入输出流
        try (BufferedReader reader = SvnUtil.getDiffRecordReader(config.getSvn().getPath(), config.getSvn().getRevStart(), config.getSvn().getRevEnd())){
            reader.lines()
                    // utf-8转码，支持中文显示
                    .map(decodeUtf8())
                    // 过滤无效svn记录
                    .filter(validSvnRecord())
                    // 转化为相对路径
                    .map(this::toRelativePath)
                    // 将相对路径转化为copyList行
                    .forEach(relativePath -> copyListLines.addAll(toCopyListLines(relativePath)));
        }
    }

    private void writeCopyList() throws IOException {
        // 获取输入输出流
        try (BufferedWriter writer =
                     Files.newBufferedWriter(Paths.get(config.getCopyList().getPath()), CharSets.CHARSET_GBK)){
            // 将copyList数据写入文件
            copyListLines.forEach((line) ->{
                try {
                    writer.write(line);
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    /**
     * 过滤无效svn记录
     * @return 过滤条件
     */
    private Predicate<String> validSvnRecord() {
        return svnRecord -> {
            try {
                return SvnUtil.isAddOrModifyRecord(svnRecord) && notSystemGlobals(svnRecord) && isFile(svnRecord);
            } catch (Exception e) {
               throw new RuntimeException("过滤无效svn记录异常");
            }
        };
    }

    /**
     * utf-8转码，支持中文显示
     * @return 函数
     */
    private Function<String, String> decodeUtf8() {
        return svnRecord -> {
            try {
                return URLDecoder.decode(svnRecord, CharSetConst.UTF8);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * 为EMP配置文件SystemGlobals的修改记录
     */
    private boolean notSystemGlobals(String svnRecord){
        return !svnRecord.endsWith(Const.SYSTEM_GLOBALS);
    }

    /**
     * 判断是不是文件
     * @param svnRecord svn记录
     * @return 判断结果
     */
    private boolean isFile(String svnRecord){
        // svn记录前8个字符是修改类型
        int basePathLength = 8 + config.getSvn().getPath().length();
        if(svnRecord.length() > basePathLength ){
            String fileName = svnRecord.substring(svnRecord.lastIndexOf("/"));
            return fileName.contains(".");
        }
        return false;
    }

    /**
     * 将svn修改记录转为svn相对路径
     * @param svnRecord svn修改记录
     * @return svn相对路径
     */
    private String toRelativePath(String svnRecord) {
        // svn记录前8个字符是修改类型
        int basPathLength = 8 + config.getSvn().getPath().length();
        if(svnRecord.length() <= basPathLength){
            return null;
        }
        return svnRecord.substring(basPathLength + 1);
    }

    /**
     * 将文件相对路径转化为copyList行
     * @param relativePath 文件相对路径
     * @return copyList行
     */
    private Set<String> toCopyListLines(String relativePath) {
        try {
            BaseCopyListConverter converter = converterFactory.getConverter(relativePath);
            if(converter == null){
                return Collections.emptySet();
            }
            return converter.toCopyListLines(relativePath);
        } catch (Exception e) {
            throw new RuntimeException(String.format("将svn修改记录(%s)转化为copyList行报错!", relativePath), e);
        }
    }
}
