package com.zhangyj.cmdexecutor.component.service.impl;

import com.zhangyj.cmdexecutor.component.common.config.CmdSpliceFileConfig;
import com.zhangyj.cmdexecutor.component.common.enums.GenModeEnum;
import com.zhangyj.cmdexecutor.core.common.handler.impl.CheckStringHandler;
import com.zhangyj.cmdexecutor.core.common.util.CommandUtils;
import com.zhangyj.cmdexecutor.core.service.AbstractCmdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
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
public class CmdSpliceFileServiceImpl extends AbstractCmdService<CmdSpliceFileConfig> {

    private Pattern[] whitePattern;

    private Pattern[] blackPattern;

    @Override
    public void exec() throws Exception {
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
        if(config.getGenFileName() == null){
            throw new IllegalArgumentException("生成文件名不能为空！");
        }
    }

    private void init() throws Exception {
        this.whitePattern = getPatterns(config.getWhitePattern());
        this.blackPattern = getPatterns(config.getBlackPattern());
        execCommand();
    }

    private void execCommand() throws Exception {
        String command = config.getCommand();
        if(command == null){
            return;
        }
        log.info("执行命令：{} 执行路径：{}", command, config.getPath());
        CommandUtils.execCommand(cmdExecConfig.getCharset(), command, config.getPath(), new CheckStringHandler(config));
    }

    private void deleteGenFile() throws IOException {
        String genMode = StringUtils.isNotEmpty(config.getGenMode())? config.getGenMode(): GenModeEnum.NEW.name();
        log.info("生成文件模式为：{}", genMode);
        if(GenModeEnum.APPEND.name().equalsIgnoreCase(genMode)){
            return;
        }
        String genFileName = config.getGenFileName();
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
        List<Path> readFilePaths = Files.list(Paths.get(config.getPath()))
                .filter(this::filterReadFile).collect(Collectors.toList());
        // 获取输入输出流
        Path genFilePath = Paths.get(config.getGenFileName());
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

    @Override
    public String getDesc() {
        return "文件拼接功能";
    }
}
