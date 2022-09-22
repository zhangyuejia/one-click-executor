package com.zhangyj.cmdexecutor.component.service.impl;

import com.zhangyj.cmdexecutor.component.common.config.CmdReplacePropertiesConfig;
import com.zhangyj.cmdexecutor.component.entity.bo.ReplacePropertiesBO;
import com.zhangyj.cmdexecutor.core.common.config.CmdConfig;
import com.zhangyj.cmdexecutor.core.service.AbstractCmdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangyj
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CmdReplacePropertiesServiceImpl extends AbstractCmdService {

    /**
     * 剩下来没用到的，需要写入最后一个文件
     */
    private final Map<String, String> propertiesLeftMap = new HashMap<>(2);

    private final Map<String, String> currentPropertiesMap = new HashMap<>(2);

    private List<String> currentUselessProperties;

    @Override
    public void exec(CmdConfig c) throws Exception {
        CmdReplacePropertiesConfig config = getConfig(c);
        List<ReplacePropertiesBO> replacePropertiesList = config.getReplaceKeys();
        // 是否检查只有一个replaceId
        if(config.getEnableRefId().size() > 1){
            throw new RuntimeException("配置项[properties-replace.enableReplaceId]配置个数不能大于1个");
        }
        for (ReplacePropertiesBO replaceProperties : replacePropertiesList) {
            if(!config.getEnableRefId().contains(replaceProperties.getRefId())){
                continue;
            }
            log.info("启用配置ID:{}", replaceProperties.getRefId());
            init(replaceProperties, config);
            replaceProperties(config);
            writeLeftProperties(config);
            clear();
        }
    }

    private CmdReplacePropertiesConfig getConfig(CmdConfig c) {
        CmdReplacePropertiesConfig config = (CmdReplacePropertiesConfig) c;
        if (StringUtils.isBlank(config.getDir())) {
            config.setDir(cmdExecConfig.getDir());
        }
        return config;
    }

    @Override
    public Class<? extends CmdConfig> getConfigClass() {
        return CmdReplacePropertiesConfig.class;
    }

    private void init(ReplacePropertiesBO replaceProperties, CmdReplacePropertiesConfig config) {
        List<String> uselessProperties = new ArrayList<>(replaceProperties.getUselessProperties());
        uselessProperties.addAll(config.getUselessProperties());
        this.currentUselessProperties = uselessProperties.stream().distinct().collect(Collectors.toList());

        this.currentPropertiesMap.putAll(replaceProperties.getPropertiesMap());
        this.currentPropertiesMap.putAll(config.getPropertiesMap());
        this.propertiesLeftMap.putAll(this.currentPropertiesMap);
    }

    private void clear() {
        this.currentPropertiesMap.clear();
        this.currentUselessProperties.clear();
        this.propertiesLeftMap.clear();
    }

    private void replaceProperties(CmdReplacePropertiesConfig config) throws IOException {
        List<String> filePaths = getAbsoluteFilePaths(config);
        for (String filePath : filePaths) {
            File file = new File(filePath);
            if(file.isDirectory()) {
                throw new RuntimeException("文件" + filePath + "不是文件，中止执行");
            }
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

    }

    private List<String> getAbsoluteFilePaths(CmdReplacePropertiesConfig config) {
       return config.getFilePaths().stream().map(v -> config.getDir() + File.separator + v).collect(Collectors.toList());
    }

    private void writeLeftProperties(CmdReplacePropertiesConfig config) throws IOException {
        if(CollectionUtils.isEmpty(propertiesLeftMap)){
            return;
        }
        List<String> filePaths = getAbsoluteFilePaths(config);
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
        for (String property : currentPropertiesMap.keySet()) {
            if(line.startsWith(property + "=")){
                log.info("修改配置项：{} 为{}", property, currentPropertiesMap.get(property));
                propertiesLeftMap.remove(property);
                return property + "=" + currentPropertiesMap.get(property);
            }
        }
        return null;
    }
}
