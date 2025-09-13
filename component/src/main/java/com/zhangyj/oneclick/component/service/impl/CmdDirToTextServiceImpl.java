package com.zhangyj.oneclick.component.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
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
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author zhang.yuejia1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CmdDirToTextServiceImpl extends AbstractCmdService<CmdDirToTextConfig> {

    private static final String PATH_PREFIX = "#P#";
    private static final String ENCRYPT_KEY = "ThisIsASecretKey";
    private static final int BUFFER_SIZE = 4096 * 64;
    private static Path targetPath;
    private final SymmetricCrypto symmetricCrypto = SecureUtil.aes(ENCRYPT_KEY.getBytes(StandardCharsets.UTF_8));

    @Override
    public void exec(CmdDirToTextConfig config) throws Exception {
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

    private void exportToText(String fromDir, String outputFile) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile))) {
            Path fromDirPath = Paths.get(fromDir);
            try (Stream<Path> paths = Files.walk(fromDirPath)) {
                paths.filter(Files::isRegularFile)
                        .forEach(path -> processFile(fromDirPath, path, writer));
            }
        }
    }

    private void processFile(Path fromDirPath, Path path, BufferedWriter writer) {
        try (InputStream is = Files.newInputStream(path)) {
            // 处理相对路径
            writeLine(writer, PATH_PREFIX, fromDirPath.relativize(path).toString().getBytes(StandardCharsets.UTF_8));

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                if (bytesRead < BUFFER_SIZE) {
                    buffer = Arrays.copyOf(buffer, bytesRead);
                }
                writeLine(writer, StrUtil.EMPTY, buffer);
            }
        } catch (Exception e) {
            throw new RuntimeException("文件处理失败: " + path, e);
        }
    }

    private void writeLine(BufferedWriter writer, String prefix, byte[] data) throws Exception{
        writer.write(prefix + symmetricCrypto.encryptBase64(data));
        writer.newLine();
    }

    private void restoreFromText(String toDir, String inputFile) throws Exception {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFile))) {
            String line;
            Path toDirPath = Paths.get(toDir);
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(PATH_PREFIX)) {
                    line = line.substring(PATH_PREFIX.length());
                    String relativePath = new String(symmetricCrypto.decrypt(line), StandardCharsets.UTF_8);
                    Path target = toDirPath.resolve(relativePath);
                    Files.createDirectories(target.getParent());
                    targetPath = target;
                }else {
                    byte[] decoded = symmetricCrypto.decrypt(line);
                    Files.write(targetPath, decoded, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
                }
            }
        }
    }

    @Override
    public String getDesc() {
        return "大文件友好的文件夹文本互转";
    }
}