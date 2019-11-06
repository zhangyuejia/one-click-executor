package com.zhangyj.maker.impl;

import com.google.common.collect.Sets;
import com.zhangyj.config.Config;
import com.zhangyj.constant.CharSetConst;
import com.zhangyj.constant.Const;
import com.zhangyj.maker.Maker;
import com.zhangyj.replactor.BaseCopyListConverter;
import com.zhangyj.replactor.ConverterFactory;
import com.zhangyj.utils.SvnUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author ZHANG
 */
@Component
@Slf4j
public class CopyListMaker implements Maker<String> {

    private final Config config;

    private final ConverterFactory converterFactory;

    public CopyListMaker(Config config, ConverterFactory converterFactory) {
        this.config = config;
        this.converterFactory = converterFactory;
    }

    @Override
    public String make() throws Exception {
        try (BufferedReader reader =
                     SvnUtil.getDiffRecordReader(config.getSvn().getPath(), config.getSvn().getRevStart(), config.getSvn().getRevEnd());
             BufferedWriter writer = Files.newBufferedWriter(Paths.get(config.getCopyList().getPath()), Charset.forName(CharSetConst.GBK))){
            Set<String> copyListLines = Sets.newTreeSet();
            reader.lines()
                    // utf-8转码，支持中文显示
                    .map(decodeUtf8())
                    // 过滤无效svn记录
                    .filter(validSvnRecord())
                    // 转化为相对路径
                    .map(this::toRelativePath)
                    .forEach(relativePath -> copyListLines.addAll(toCopyListLines(relativePath)));
            writeData(writer, copyListLines);
            return config.getCopyList().getPath();
        }
    }

    /**
     * 过滤无效svn记录
     * @return 过滤条件
     */
    private Predicate<String> validSvnRecord() {
        return svnRecord -> {
            if(config.getSvn().getShowRecord()){
                log.info("svn记录：{}", svnRecord);
            }
            return SvnUtil.isAddOrModifyRecord(svnRecord) && notSystemGlobals(svnRecord) && isFile(svnRecord);
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
     * 为EMP配置文件SystemGlobals的修改记录
     */
    private boolean isFile(String svnRecord){
        // svn记录前8个字符是修改类型
        int basPathLength = 8 + config.getSvn().getPath().length();
        String filePath;
        if(svnRecord.length() > basPathLength ){
            filePath = config.getEmp().getSourcePath() + svnRecord.substring(basPathLength);
            return new File(filePath).isFile();
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

    private void writeData(BufferedWriter writer, Collection<String> copyListLines) {
        copyListLines.forEach((line) ->{
            try {
                writer.write(line);
                writer.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
