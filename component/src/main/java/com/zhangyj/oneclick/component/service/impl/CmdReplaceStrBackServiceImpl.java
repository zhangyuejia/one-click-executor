package com.zhangyj.oneclick.component.service.impl;

import com.zhangyj.oneclick.component.common.config.CmdReplaceConfig;
import com.zhangyj.oneclick.component.common.config.CmdReplaceStrBackConfig;
import com.zhangyj.oneclick.component.service.AbstractCmdReplaceServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyj
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CmdReplaceStrBackServiceImpl extends AbstractCmdReplaceServiceImpl<CmdReplaceStrBackConfig> {

    @Override
    protected void writePropertyFile(CmdReplaceConfig config, String filePath) throws IOException {
        log.info("读取文件：{}", filePath);
        Path path = Paths.get(filePath);

        List<String> lines = Files.readAllLines(path);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String placedLine = line;
                boolean isMatch = false;
                for (Map.Entry<String, Object> entry : this.currentPropertiesMap.entrySet()) {
                    Object value = entry.getValue();
                    if (placedLine.contains(value.toString())) {
                        isMatch = true;
                        String key = entry.getKey();
                        placedLine = placedLine.replace(value.toString(), key);
                    }
                }
                if (isMatch && !line.equals(placedLine)) {
                    log.info("替换结果:{} -> {}", line, placedLine);
                }
                writer.write(placedLine);
                if (i != lines.size() - 1) {
                    writer.newLine();
                }
            }
        }
    }

    @Override
    protected void writeLeftPropertiesFile(List<String> filePaths) throws IOException {

    }

    @Override
    public String getDesc() {
        return "文本反向替换功能";
    }
}
