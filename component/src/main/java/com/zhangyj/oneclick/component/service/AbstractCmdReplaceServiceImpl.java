package com.zhangyj.oneclick.component.service;

import cn.hutool.core.collection.CollectionUtil;
import com.zhangyj.oneclick.component.common.config.CmdReplaceConfig;
import com.zhangyj.oneclick.component.entity.bo.ReplacePropertiesBO;
import com.zhangyj.oneclick.core.service.AbstractCmdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhangyj
 */
@Slf4j
@Service
@RequiredArgsConstructor
public abstract class AbstractCmdReplaceServiceImpl<T extends CmdReplaceConfig> extends AbstractCmdService<T> {

    /**
     * 剩下来没用到的，需要写入最后一个文件
     */
    protected final Map<String, Object> propertiesLeftMap = new HashMap<>(2);

    protected final Map<String, Object> currentPropertiesMap = new HashMap<>(2);

    protected List<String> currentUselessProperties;

    @Override
    public void exec(CmdReplaceConfig config) throws Exception {
        initConfig(config);
        List<ReplacePropertiesBO> replacePropertiesList = config.getReplaceKeys();
        // 是否检查只有一个replaceId
        if(config.getEnableRefId().size() > 1){
            throw new RuntimeException("配置项[enableRefId]配置个数不能大于1个");
        }
        for (ReplacePropertiesBO replaceProperties : replacePropertiesList) {
            if(!config.getEnableRefId().contains(replaceProperties.getRefId())){
                continue;
            }
            log.info("启用配置ID:{}", replaceProperties.getRefId());
            init(config, replaceProperties);
            replaceProperties(config);
            writeLeftProperties(config);
            clear();
        }
    }

    private void initConfig(CmdReplaceConfig config) {
        if (StringUtils.isBlank(config.getDir())) {
            config.setDir(cmdExecConfig.getDir());
        }
    }

    private void init(CmdReplaceConfig config, ReplacePropertiesBO replaceProperties) {
        List<String> uselessProperties = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(replaceProperties.getUselessProperties())) {
            uselessProperties.addAll(replaceProperties.getUselessProperties());
        }

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

    private void replaceProperties(CmdReplaceConfig config) throws IOException {
        List<String> filePaths = getAbsoluteFilePaths(config);
        for (String filePath : filePaths) {
            File file = new File(filePath);
            if(file.isDirectory()) {
                throw new RuntimeException("文件" + filePath + "不是文件，中止执行");
            }
            writePropertyFile(config, filePath);
        }

    }

    protected abstract void writePropertyFile(CmdReplaceConfig config, String filePath) throws IOException;

    private List<String> getAbsoluteFilePaths(CmdReplaceConfig config) {
        return config.getFilePaths().stream().map(v -> config.getDir() + File.separator + v).collect(Collectors.toList());
    }

    private void writeLeftProperties(CmdReplaceConfig config) throws IOException {
        if(CollectionUtils.isEmpty(propertiesLeftMap)){
            return;
        }
        List<String> filePaths = getAbsoluteFilePaths(config);
        writeLeftPropertiesFile(filePaths);
    }

    protected abstract void writeLeftPropertiesFile(List<String> filePaths) throws IOException ;



}
