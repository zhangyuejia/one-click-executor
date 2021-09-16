package com.zhangyj.fileSplicer;

import com.zhangyj.fileSplicer.config.FileSplicerConfig;
import com.zhangyj.utils.CommandUtil;
import com.zhangyj.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
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
public class DoSplice implements CommandLineRunner {

    private Pattern[] whitePattern;

    private Pattern[] blackPattern;

    private final FileSplicerConfig fileSplicerConfig;

    @Override
    public void run(String... args) throws Exception {
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
        if(StringUtil.isEmpty(fileSplicerConfig.getGenFileName())){
            throw new IllegalArgumentException("生成文件名不能为空！");
        }
    }

    private void init() {
        this.whitePattern = getPatterns(fileSplicerConfig.getWhitePattern());
        this.blackPattern = getPatterns(fileSplicerConfig.getBlackPattern());
        execCommand();
    }

    private void execCommand() {
        String command = fileSplicerConfig.getCommand();
        if(StringUtil.isEmpty(command)){
            return;
        }
        log.info("执行命令：{} 执行路径：{}", command, fileSplicerConfig.getPath());
        String[] commands = {"cmd", "/c",  command};
        try (BufferedReader reader = CommandUtil.getCommandReader(StandardCharsets.UTF_8, commands, fileSplicerConfig.getPath())){
            reader.lines().filter(StringUtil::isNotEmpty).forEach(log::info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteGenFile() throws IOException {
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
