package com.zhangyj.splicer;

import com.google.common.collect.Lists;
import com.zhangyj.splicer.config.SplicerConfig;
import com.zhangyj.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 拼接文件
 * @author zhangyj
 */
@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(SplicerConfig.class)
public class DoSplice extends BaseSplicer implements CommandLineRunner {

    private final SplicerConfig splicerConfig;

    private final List<String> data = Lists.newArrayList();

    @Override
    public void run(String... args) throws Exception {
        // 参数校验
        checkParam();
        // 删除上一次生成的文件
        deleteGenFile();
        // 读取数据文件
        readFile();
        // 写入文件
        writeFile();
    }

    private void checkParam() {
        if(StringUtil.isEmpty(splicerConfig.getGenFileName())){
            throw new IllegalArgumentException(getLog("生成文件名不能为空！"));
        }
    }

    private void writeFile() throws IOException {
        String genFileName = splicerConfig.getGenFileName();
        Path path = Paths.get(genFileName);
        logInfo("写入文件：" + path.toAbsolutePath());
        // 获取输入输出流
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private void deleteGenFile() throws IOException {
        String genFileName = splicerConfig.getGenFileName();
        File file = new File(genFileName);
        if(!file.exists()) {
            return;
        }
        if(file.delete()){
            logInfo("删除文件：" + file.getCanonicalPath());
        }else {
            logError("删除文件失败：" + file.getCanonicalPath());
        }
    }

    private void readFile() throws IOException {
        List<Path> paths = Files.list(Paths.get(splicerConfig.getPath())).collect(Collectors.toList());
        for (Path path : paths) {
            if(!StringUtil.isEmpty(splicerConfig.getSuffix()) && !path.getFileName().toString().endsWith(splicerConfig.getSuffix())){
                continue;
            }
            logInfo("读取文件：" + path.toString());
            Files.lines(path).forEach(data::add);
        }
    }
}