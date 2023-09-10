package com.zhangyj.cmdexecutor.component.business.xydecrypt.impl;

import cn.hutool.core.io.FileUtil;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.zhangyj.cmdexecutor.component.business.xydecrypt.AbstractXyDecryptProcessor;
import com.zhangyj.cmdexecutor.component.business.xydecrypt.XyDecryptProcessor;
import com.zhangyj.cmdexecutor.component.common.config.CmdXyDecryptConfig;
import com.zhangyj.cmdexecutor.core.common.util.FileUtils;
import com.zhangyj.cmdexecutor.core.common.util.JacobUtil;
import com.zhangyj.cmdexecutor.core.common.util.StrUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Component
public class XyDecryptPdfProcessor extends AbstractXyDecryptProcessor {

    private String newDotmFilePath;

    @Override
    public void processDecrypt(CmdXyDecryptConfig config, File file) {
        try {
            // 创建文件
            createNewDotmFile();
            // 插入宏代码并运行
            JacobUtil.processWord(this.newDotmFilePath, getMacroCode(file.getName(), file.getPath(), config));
        }catch (Exception e){
            log.error("抛出异常", e);
        }
    }

    private void createNewDotmFile() throws Exception {
        File file = new File(TEMP_DIR + File.separator + System.currentTimeMillis() + ".dotm");
        if(file.exists()){
            boolean success = file.delete();
            if(!success && file.exists()){
                throw new RuntimeException("删除文件失败：" + file.getPath());
            }
        }
        boolean success = file.createNewFile();
        if(!success && !file.exists()){
            throw new RuntimeException("创建文件失败：" + file.getPath());
        }
        this.newDotmFilePath = file.getPath();
    }

    private String getMacroCode(String fileName, String pdfFilePath, CmdXyDecryptConfig config) {
        String macrosCode = FileUtil.readString(FileUtils.getResourcePath("component/file/xydecrypt/macros-pdf.vb"), Charset.defaultCharset());
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("pdfFilePath", pdfFilePath);
        paramMap.put("fileNameBase64", Base64Util.encode(fileName));
        paramMap.put("dataServerUrl", config.getDataServerUrl());
        return StrUtils.parseTplContent(macrosCode, paramMap);
    }

    @Override
    protected void initFileExtensions() {
        this.fileExtensions.add("pdf");
    }
}
