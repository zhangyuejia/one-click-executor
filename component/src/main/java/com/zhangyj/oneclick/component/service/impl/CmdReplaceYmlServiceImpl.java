package com.zhangyj.oneclick.component.service.impl;

import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.zhangyj.oneclick.component.common.config.CmdReplaceConfig;
import com.zhangyj.oneclick.component.common.config.CmdReplaceYmlConfig;
import com.zhangyj.oneclick.component.service.AbstractCmdReplaceServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangyj
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CmdReplaceYmlServiceImpl extends AbstractCmdReplaceServiceImpl<CmdReplaceYmlConfig> {

    @Override
    protected void writePropertyFile(CmdReplaceConfig config, String filePath) throws IOException {
        CmdReplaceYmlConfig replaceYmlConfig = (CmdReplaceYmlConfig) config;
        log.info("读取yml文件：{}", filePath);
        Dict dict = YamlUtil.loadByPath(filePath);
        boolean isMatch = false;
        boolean enableAddItemIfNotExists = BooleanUtil.isTrue(replaceYmlConfig.getEnableAddItemIfNotExists());
        for (Map.Entry<String, Object> entry : this.currentPropertiesMap.entrySet()) {
            String key = entry.getKey();
            String pathValue = dict.getByPath(key, String.class);
            if (StrUtil.isNotEmpty(pathValue)) {
                Object value = entry.getValue();
                if (pathValue.equals(value)) {
                    log.info("匹配配置{}，当前值：{}，无需修改", key, pathValue);
                }else {
                    BeanPath.create(key).set(dict, value);
                    log.info("替换配置{}：{} -> {}", key, pathValue, value);
                    isMatch = true;
                }
            }else {
                if (enableAddItemIfNotExists) {
                    log.info("新增配置{}：{} -> {}", dict, key, entry.getValue());
                    setByPath(dict, key, entry.getValue());
                    isMatch = true;
                }
            }
        }
        if (!isMatch) {
            return;
        }
        YamlUtil.dump(dict, Files.newBufferedWriter(Paths.get(filePath)));
    }

    private static void setByPath(Map<String, Object> map, String path, Object value) {
        String[] keys = path.split("\\.");
        Map<String, Object> current = map;

        for (int i = 0; i < keys.length - 1; i++) {
            String key = keys[i];
            // 如果当前层级不存在则创建新Map
            //noinspection unchecked
            current = (Map<String, Object>) current.computeIfAbsent(key, k -> new LinkedHashMap<>());
        }
        // 设置最终层级的值
        current.put(keys[keys.length - 1], value);
    }

    @Override
    protected void writeLeftPropertiesFile(List<String> filePaths) throws IOException {

    }

    @Override
    public String getDesc() {
        return "Yml配置项替换功能";
    }
}
