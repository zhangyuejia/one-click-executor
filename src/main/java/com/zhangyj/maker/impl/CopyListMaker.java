package com.zhangyj.maker.impl;

import com.zhangyj.config.CopyListConfig;
import com.zhangyj.config.EmpConfig;
import com.zhangyj.config.SvnConfig;
import com.zhangyj.constant.CharSetConst;
import com.zhangyj.maker.Maker;
import com.zhangyj.pojo.JavaFilePath;
import com.zhangyj.replactor.BaseCopyListConverter;
import com.zhangyj.replactor.ConverterFactory;
import com.zhangyj.replactor.impl.JavaCopyListConverter;
import com.zhangyj.utils.SvnUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ZHANG
 */
@Component
@Slf4j
public class CopyListMaker implements Maker<String> {

    private final CopyListConfig copyListConfig;

    private final SvnConfig svnConfig;

    private final ConverterFactory converterFactory;

    public CopyListMaker(CopyListConfig copyListConfig, SvnConfig svnConfig, ConverterFactory converterFactory) {
        this.copyListConfig = copyListConfig;
        this.svnConfig = svnConfig;
        this.converterFactory = converterFactory;
    }

    @Override
    public String make() throws Exception {

        try (BufferedReader reader =
                     SvnUtil.getDiffRecordReader(svnConfig.getPath(), svnConfig.getRevStart(), svnConfig.getRevEnd());
             BufferedWriter writer = Files.newBufferedWriter(Paths.get(copyListConfig.getPath()))){
            reader.lines()
                    .filter(svnRecord -> SvnUtil.isAddOrModifyRecord(svnRecord) || SvnUtil.notSystemGlobalsDiffRecord(svnRecord))
                    .map(this::toRelativePath)
                    .forEach(relativePath -> writeData(writer, toCopyListLines(relativePath)));

            return copyListConfig.getPath();
        }
    }

    private String toRelativePath(String svnRecord) {
        try {
            String relativePath = svnRecord.substring(8 + svnConfig.getPath().length() + 1);
            return URLDecoder.decode(relativePath, CharSetConst.UTF8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
