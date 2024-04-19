package com.zhangyj.oneclick.component.service.impl;

import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.yaml.YamlUtil;
import com.zhangyj.oneclick.component.common.config.CmdReplaceYmlConfig;
import com.zhangyj.oneclick.component.service.AbstractCmdReplaceServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    protected void writePropertyFile(String filePath) throws IOException {
        log.info("读取yml文件：{}", filePath);
        Dict dict = YamlUtil.loadByPath(filePath);
        boolean isMatch = false;
        for (Map.Entry<String, String> entry : this.currentPropertiesMap.entrySet()) {
            String key = entry.getKey();
            String pathValue = dict.getByPath(key, String.class);
            if (StrUtil.isNotEmpty(pathValue)) {
                String value = entry.getValue();
                if (pathValue.equals(value)) {
                    log.info("匹配配置{}，当前值：{}，无需修改", key, pathValue);
                }else {
                    BeanPath.create(key).set(dict, value);
                    log.info("替换配置{}：{} -> {}", key, pathValue, value);
                    isMatch = true;
                }
            }
        }
        if (!isMatch) {
            return;
        }
        YamlUtil.dump(dict, Files.newBufferedWriter(Paths.get(filePath)));
    }

    @Override
    protected void writeLeftPropertiesFile(List<String> filePaths) throws IOException {

    }

    @Override
    public String getDesc() {
        return "Yml配置项替换功能";
    }
}
