package com.zhangyj.tools.business.file.filesplicer;

import com.zhangyj.tools.business.file.filesplicer.config.FileSplicerConfig;
import com.zhangyj.tools.business.file.filesplicer.enums.GenModeEnum;
import com.zhangyj.tools.common.base.AbstractRunner;
import com.zhangyj.tools.common.handler.DefaultMsgHandler;
import com.zhangyj.tools.common.utils.CommandUtil;
import com.zhangyj.tools.common.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 拼接文件
 * @author zhangyj
 */
@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(FileSplicerConfig.class)
public class DoSplice extends AbstractRunner<FileSplicerConfig> {

    private Pattern[] whitePattern;

    private Pattern[] blackPattern;

    private final FileSplicerConfig fileSplicerConfig;

    @Override
    protected void doRun() throws Exception {
        log.info("执行文件拼接功能");
        // 参数校验
        checkParam();
        // 初始化
        init();
        // 删除上一次生成的文件
        deleteGenFile();
        // 拼接数据文件
        spliceFile();
    }

    private void checkParam() {
        if(fileSplicerConfig.getGenFileName() == null){
            throw new IllegalArgumentException("生成文件名不能为空！");
        }
    }

    private void init() throws Exception {
        this.whitePattern = getPatterns(fileSplicerConfig.getWhitePattern());
        this.blackPattern = getPatterns(fileSplicerConfig.getBlackPattern());
        execCommand();
    }

    private void execCommand() throws Exception {
        String command = fileSplicerConfig.getCommand();
        if(command == null){
            return;
        }
        log.info("执行命令：{} 执行路径：{}", command, fileSplicerConfig.getPath());
        CommandUtil.execCommand(StandardCharsets.UTF_8, command, fileSplicerConfig.getPath(), new DefaultMsgHandler());
    }

    private void deleteGenFile() throws IOException {
        String genMode = StringUtil.isNotEmpty(fileSplicerConfig.getGenMode())? fileSplicerConfig.getGenMode(): GenModeEnum.NEW.name();
        log.info("生成文件模式为：{}", genMode);
        if(GenModeEnum.APPEND.name().equalsIgnoreCase(genMode)){
            return;
        }
        String genFileName = fileSplicerConfig.getGenFileName();
        File file = new File(genFileName);
        if(!file.exists()) {
            return;
        }
        if(file.delete()){
            log.info("删除文件：" + file.getCanonicalPath());
        }else {
            throw new RuntimeException("删除文件失败：" + file.getCanonicalPath());
        }
    }

    private void spliceFile() throws IOException {
        List<Path> readFilePaths = Files.list(Paths.get(fileSplicerConfig.getPath()))
                .filter(this::filterReadFile).collect(Collectors.toList());
        // 获取输入输出流
        Path genFilePath = Paths.get(fileSplicerConfig.getGenFileName());
        try (BufferedWriter writer = Files.newBufferedWriter(genFilePath)){
            for (Path readFilePath : readFilePaths) {
                log.info("读取文件：" + readFilePath.toString());
                List<String> lines = Files.readAllLines(readFilePath);
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
        log.info("写入文件：" + genFilePath.toAbsolutePath());

    }

    private boolean filterReadFile(Path readFilePath) {
        String fileName = readFilePath.getFileName().toString();
        // 过滤文件夹
        if(readFilePath.toFile().isDirectory()){
            return false;
        }
        // 黑名单只要有一个匹配就跳过
        if(this.blackPattern != null && matchAnyPattern(fileName, this.blackPattern)){
            return false;
        }
        // 白名单只要有一个匹配就不跳过
        return this.whitePattern == null || matchAnyPattern(fileName, this.whitePattern);
    }

    private boolean matchAnyPattern(String fileName, Pattern[] patterns) {
        for (Pattern p : patterns) {
            if (p.matcher(fileName).matches()) {
                return true;
            }
        }
        return false;
    }

    private Pattern[] getPatterns(String[] patternArr) {
        if(patternArr == null || patternArr.length == 0){
            return null;
        }
        Pattern[] patterns = new Pattern[patternArr.length];
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = Pattern.compile(patternArr[i]);
        }
        return patterns;
    }
}
