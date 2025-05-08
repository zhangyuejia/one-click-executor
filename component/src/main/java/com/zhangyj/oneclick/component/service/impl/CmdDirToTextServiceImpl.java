package com.zhangyj.oneclick.component.service.impl;

import com.zhangyj.oneclick.component.common.config.CmdDirToTextConfig;
import com.zhangyj.oneclick.core.common.enums.DirectionModeEnum;
import com.zhangyj.oneclick.core.common.util.EnumUtils;
import com.zhangyj.oneclick.core.service.AbstractCmdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.stream.Stream;

/**
 * @author zhangyj
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CmdDirToTextServiceImpl extends AbstractCmdService<CmdDirToTextConfig> {

    private static final String SEPARATOR = "::BASE64::";

    @Override
    public void exec() throws Exception {
        DirectionModeEnum modeEnum = EnumUtils.getByValue(DirectionModeEnum.class, config.getDirectionMode());
        switch (modeEnum) {
            case FROM:
                exportToText(config.getFromDir(), config.getTextPath());
                break;
            case TO:
                restoreFromText(config.getToDir(), config.getTextPath());
                break;
            default:
        }
    }

    private static void exportToText(String fromDir, String outputFile) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile))) {
            Path fromDirPath = Paths.get(fromDir);
            try (Stream<Path> paths = Files.walk(fromDirPath)) {
                paths.filter(Files::isRegularFile)
                        .forEach(path -> processFile(fromDirPath, path, writer));
            }
        }
    }

    private static void processFile(Path fromDirPath, Path path, BufferedWriter writer) {
        try {
            // 获取相对路径
            String relativePath = fromDirPath.relativize(path).toString();
            byte[] fileContent = Files.readAllBytes(path);
            // Base64编码
            String encoded = Base64.getEncoder().encodeToString(fileContent);
            // 写入格式：路径|编码内容
            writer.write(relativePath + SEPARATOR + encoded);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void restoreFromText(String toDir, String inputFile) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFile))) {
            String line;
            Path toDirPath = Paths.get(toDir);
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(SEPARATOR, 2);
                if (parts.length == 2) {
                    Path target = toDirPath.resolve(parts[0]);
                    Files.createDirectories(target.getParent());

                    byte[] decoded = Base64.getDecoder().decode(parts[1]);
                    Files.write(target, decoded);
                }
            }
        }
    }

    @Override
    public String getDesc() {
        return "文件夹文本互转";
    }
}
