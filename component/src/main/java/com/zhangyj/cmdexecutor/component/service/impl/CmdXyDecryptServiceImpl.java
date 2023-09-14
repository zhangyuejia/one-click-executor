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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zhangyj
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CmdXyDecryptServiceImpl extends AbstractCmdService<CmdXyDecryptConfig> {

    private final List<XyDecryptProcessor> xyDecryptProcessors;

    @Override
    public void exec() throws Exception {
        if (checkParam()){
            return;
        }
        printInfo();
        mkTempDir();
        processFiles(new File(config.getPath()));
    }

    private void printInfo() {
        List<String> fileExtensions = new ArrayList<>();
        for (XyDecryptProcessor xyDecryptProcessor : xyDecryptProcessors) {
            fileExtensions.addAll(xyDecryptProcessor.getFileExtensions());
        }
        log.info("支持的文件类型：{}", String.join(",", fileExtensions));
    }

    private void processFiles(File dirFile) {
        File[] files = dirFile.listFiles();
        if(files == null){
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                processFiles(file);
            }else {
                for (XyDecryptProcessor xyDecryptProcessor : xyDecryptProcessors) {
                    if (xyDecryptProcessor.isMatch(file)) {
                        log.info("匹配到文件处理器：{}", file.getPath());
                        xyDecryptProcessor.processDecrypt(config, file);
                        break;
                    }
                }
            }
        }
    }

    private static void mkTempDir() {
        String tempDir = XyDecryptProcessor.TEMP_DIR;
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
        return false;
    }
}
