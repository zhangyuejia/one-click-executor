package com.zhangyj.cmdexecutor.component.service.impl;

import com.zhangyj.cmdexecutor.component.common.config.CmdReplacePropertiesConfig;
import com.zhangyj.cmdexecutor.component.service.AbstractCmdReplaceServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyj
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CmdReplacePropertiesServiceImpl extends AbstractCmdReplaceServiceImpl<CmdReplacePropertiesConfig> {

    @Override
    protected void writePropertyFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        List<String> lines = Files.readAllLines(path);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (String line : lines) {
                line = line.trim();
                // 先判断是否需要注释，再判断是否需要替换配置项
                String newLine = replaceUselessProperty(line);
                if(newLine == null){
                    newLine = replaceProperty(line);
                }
                writer.write(newLine == null? line: newLine);
                writer.newLine();
            }
        }
    }

    @Override
    protected void writeLeftPropertiesFile(List<String> filePaths) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePaths.get(filePaths.size() - 1)), StandardOpenOption.APPEND)){
            for (Map.Entry<String, String> entry : propertiesLeftMap.entrySet()) {
                String line = entry.getKey() + "=" + entry.getValue();
                log.info("写入配置项：{}", line);
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private String replaceUselessProperty(String line) {
        List<String> uselessProperties = this.currentUselessProperties;
        for (String uselessProperty : uselessProperties) {
            if(line.startsWith(uselessProperty + "=")){
                log.info("注释配置项：{}", uselessProperty);
                return "#" + line;
            }
        }
        return null;
    }

    private String replaceProperty(String line) {
        // 替换关键字
        String leftStr;
        for (String property : currentPropertiesMap.keySet()) {
            if(line.startsWith(property)){
                leftStr = line.substring(property.length()).trim();
                if(leftStr.startsWith("=")){
                    log.info("修改配置项：{} 为{}", property, currentPropertiesMap.get(property));
                    propertiesLeftMap.remove(property);
                    return property + "=" + currentPropertiesMap.get(property);
                }
            }
        }
        return null;
    }

    @Override
    public String getDesc() {
        return "Properties配置项替换功能";
    }
}
