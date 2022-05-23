package com.zhangyj.tools.business.project.propertiesreplacer;

import com.zhangyj.tools.business.project.propertiesreplacer.config.PropertiesReplaceConfig;
import com.zhangyj.tools.business.project.propertiesreplacer.pojo.ReplaceProperties;
import com.zhangyj.tools.common.base.AbstractRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyj
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(PropertiesReplaceConfig.class)
public class DoReplaceProperties extends AbstractRunner<PropertiesReplaceConfig> {

    /**
     * 剩下来没用到的，需要写入最后一个文件
     */
    private final Map<String, String> propertiesLeftMap = new HashMap<>(2);

    @Override
    protected void doRun() throws Exception {
        List<ReplaceProperties> replacePropertiesList = config.getReplaceKeys();
        for (ReplaceProperties replaceProperties : replacePropertiesList) {
            if(!replaceProperties.getEnable()){
                continue;
            }
            propertiesLeftMap.putAll(replaceProperties.getPropertiesMap());
            List<String> filePaths = replaceProperties.getFilePaths();
            for (String filePath : filePaths) {
                replaceFileKey(filePath, replaceProperties);
            }
            handlePropertiesLeftMap(replaceProperties);
        }
    }

    private void replaceFileKey(String filePath, ReplaceProperties replaceProperties) throws IOException {
        File file = new File(filePath);
        if(file.isDirectory()) {
            throw new RuntimeException("文件" + filePath + "不是文件，中止执行");
        }
        Path path = Paths.get(filePath);

        List<String> lines = Files.readAllLines(path);
        try (BufferedWriter writer = Files.newBufferedWriter(path)){
            for (String line : lines) {
                // 判断是否为应该注释的关键字
                String newLine = replaceUselessProperty(line, replaceProperties);
                if(newLine == null){
                    newLine = replaceProperty(line, replaceProperties);
                }
                writer.write(newLine == null? line: newLine);
                writer.newLine();
            }
        }
    }

    private void handlePropertiesLeftMap(ReplaceProperties replaceProperties) throws IOException {
        if(CollectionUtils.isEmpty(propertiesLeftMap)){
            return;
        }
        List<String> filePaths = replaceProperties.getFilePaths();
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePaths.get(filePaths.size() - 1)), StandardOpenOption.APPEND)){
            for (Map.Entry<String, String> entry : propertiesLeftMap.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        }
        propertiesLeftMap.clear();
    }

    private String replaceUselessProperty(String line, ReplaceProperties replaceProperties) {
        List<String> uselessProperties = replaceProperties.getUselessProperties();
        for (String uselessProperty : uselessProperties) {
            if(line.startsWith(uselessProperty + "=")){
                return "#" + line;
            }
        }
        return null;
    }

    private String replaceProperty(String line, ReplaceProperties replaceProperties) {
        // 替换关键字
        Map<String, String> propertiesMap = replaceProperties.getPropertiesMap();
        for (String property : propertiesMap.keySet()) {
            if(line.startsWith(property + "=")){
                propertiesLeftMap.remove(property);
                return property + "=" + propertiesMap.get(property);
            }
        }
        return null;
    }
}
