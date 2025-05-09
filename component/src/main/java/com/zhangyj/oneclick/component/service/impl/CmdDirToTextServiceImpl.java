package com.zhangyj.oneclick.component.service.impl;

import cn.hutool.core.util.IdUtil;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

    private static final String SEPARATOR = "::836E42B338674024B91CA5BE56CF747A::";

    private static final int PREFIX_LENGTH = IdUtil.fastSimpleUUID().length();

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
            String pathEncoded = Base64.getEncoder().encodeToString(relativePath.getBytes(StandardCharsets.UTF_8));
            byte[] fileContent = Files.readAllBytes(path);
            // Base64编码
            String contextEncoded = Base64.getEncoder().encodeToString(fileContent);
            // 写入格式：路径|编码内容

            writer.write(IdUtil.fastSimpleUUID().toUpperCase() + pathEncoded + SEPARATOR + IdUtil.fastSimpleUUID().toUpperCase() + contextEncoded);
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
                    parts[0] = parts[0].substring(PREFIX_LENGTH);
                    String path = new String(Base64.getDecoder().decode(parts[0]), StandardCharsets.UTF_8);
                    Path target = toDirPath.resolve(path);
                    Files.createDirectories(target.getParent());

                    parts[1] = parts[1].substring(PREFIX_LENGTH);
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
