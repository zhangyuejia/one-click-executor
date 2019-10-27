package com.zhangyj.maker.impl;

import com.google.common.collect.Sets;
import com.zhangyj.config.CopyListConfig;
import com.zhangyj.config.EmpConfig;
import com.zhangyj.config.SvnConfig;
import com.zhangyj.maker.Maker;
import com.zhangyj.pojo.JavaFilePath;
import com.zhangyj.product.impl.CopyList;
import com.zhangyj.replactor.BaseCopyListConverter;
import com.zhangyj.replactor.ConverterFactory;
import com.zhangyj.replactor.impl.JavaCopyListConverter;
import com.zhangyj.utils.SvnUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.net.URLDecoder;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ZHANG
 */
@Component
@Slf4j
public class CopyListMaker implements Maker<CopyList> {

    private final CopyListConfig copyListConfig;

    private final SvnConfig svnConfig;

    private final EmpConfig empConfig;

    private final ConverterFactory converterFactory;

    public CopyListMaker(CopyListConfig copyListConfig, SvnConfig svnConfig, EmpConfig empConfig, ConverterFactory converterFactory) {
        this.copyListConfig = copyListConfig;
        this.svnConfig = svnConfig;
        this.empConfig = empConfig;
        this.converterFactory = converterFactory;
    }

    @Override
    public CopyList make() throws Exception {
        try (BufferedReader reader = SvnUtil.getDiffRecordReader(svnConfig.getPath(), svnConfig.getRevStart(), svnConfig.getRevEnd())){
            Set<String> data = Sets.newTreeSet();
            reader.lines()
                    .filter(svnRecord ->
                            SvnUtil.notAddOrModifyRecord(svnRecord) || SvnUtil.isSystemGlobalsDiffRecord(svnRecord))
                    .map(svnRecord -> {
                        try {
                            String relativePath = svnRecord.substring(8 + svnConfig.getPath().length() + 1);
                            return URLDecoder.decode(relativePath, "utf-8");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(relativePath -> data.addAll(toCopyListLines(relativePath)));
            return new CopyList(data, copyListConfig.getPath());
        }
    }

    /**
     * 将svn修改记录转化为copyList行
     * @param relativePath 文件相对路径
     * @return copyList行
     */
    private Set<String> toCopyListLines(String relativePath) {
        try {
            BaseCopyListConverter converter = converterFactory.getConverter(relativePath);
            if(converter == null){
                return null;
            }
            Set<String> data = Stream.of(converter.convert(relativePath)).collect(Collectors.toSet());
            if(converter instanceof JavaCopyListConverter){
                String positivePath = empConfig.getOutPutPath() + relativePath;
                Set<String> innerClassPaths = new JavaFilePath(positivePath).innerClassPaths();
                data.addAll(innerClassPaths.stream().map(path -> path.substring(empConfig.getOutPutPath().length())).collect(Collectors.toSet()));
            }
            return data.stream().map(d -> copyListConfig.getPrefix() + d).collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException(String.format("将svn修改记录(%s)转化为copyList行报错!", relativePath), e);
        }
    }




}
