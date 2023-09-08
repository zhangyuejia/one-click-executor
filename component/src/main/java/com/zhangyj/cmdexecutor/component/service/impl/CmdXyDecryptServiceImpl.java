package com.zhangyj.cmdexecutor.component.service.impl;

import cn.hutool.core.io.FileUtil;
import com.zhangyj.cmdexecutor.component.business.xydecrypt.XyDecryptProcessor;
import com.zhangyj.cmdexecutor.component.common.config.CmdXyDecryptConfig;
import com.zhangyj.cmdexecutor.core.service.AbstractCmdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 *
 * @author zhangyj
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CmdXyDecryptServiceImpl extends AbstractCmdService<CmdXyDecryptConfig> {

    private File[] files;

    private final List<XyDecryptProcessor> xyDecryptProcessors;

    @Override
    public void exec() throws Exception {
        if (checkParam()){
            return;
        }
        refreshTempDir();
        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }
            for (XyDecryptProcessor xyDecryptProcessor : xyDecryptProcessors) {
                if (xyDecryptProcessor.isMatch(file)) {
                    xyDecryptProcessor.processDecrypt(config, file);
                }
            }
        }
    }

    private static void refreshTempDir() {
        String tempDir = System.getProperty("java.io.tmpdir") + File.separator +  "xyDecryptTemp";
        if (FileUtil.exist(tempDir)) {
            FileUtil.del(tempDir);
        }
        FileUtil.mkdir(tempDir);
    }

    private boolean checkParam() {
        if (StringUtils.isBlank(config.getPath()) || !FileUtil.exist(config.getPath())) {
            return true;
        }
        File[] files = new File(config.getPath()).listFiles();
        if (files == null) {
            log.info("文件夹无文件：" + config.getPath());
            return true;
        }
        this.files = files;
        return false;
    }
}
