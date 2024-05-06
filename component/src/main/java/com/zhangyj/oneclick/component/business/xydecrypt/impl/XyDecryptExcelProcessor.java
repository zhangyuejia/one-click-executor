package com.zhangyj.oneclick.component.business.xydecrypt.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.zhangyj.oneclick.component.business.xydecrypt.AbstractXyDecryptProcessor;
import com.zhangyj.oneclick.component.common.config.CmdXyDecryptConfig;
import com.zhangyj.oneclick.core.common.util.FileUtils;
import com.zhangyj.oneclick.core.common.util.JacobUtil;
import com.zhangyj.oneclick.core.common.util.StrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhanglj
 */
@Slf4j
@Component
public class XyDecryptExcelProcessor extends AbstractXyDecryptProcessor {

    private String newDotFilePath;

    private String xmlFilePath;

    @Override
    public void processDecrypt(CmdXyDecryptConfig config, File file) {
        try {
            String fileName = file.getName();
            if (fileName.startsWith("~")) {
                log.info("跳过文件：" + fileName);
                return;
            }
            // 创建文件
            createNewDotFile();
            JacobUtil.processWord(this.newDotFilePath, getMacroCode(file.getPath()));
            requestDataServer(config, fileName);
        }catch (Exception e){
            log.error("抛出异常", e);
        }finally {
            if (StrUtil.isNotEmpty(xmlFilePath)) {
                FileUtil.del(xmlFilePath);
            }
        }
    }

    private void requestDataServer(CmdXyDecryptConfig config, String fileName) {
        Map<String, Object> map = new HashMap<>();
        map.put("xmlContent", FileUtil.readString(xmlFilePath, Charset.defaultCharset()));
        map.put("fileName", fileName);
        HttpResponse httpResponse = HttpRequest.post(config.getDataServerUrl() + "/acceptExcel")
                .body(JSONUtil.toJsonStr(map))
                .execute();
        httpResponse.close();
    }

    private void createNewDotFile() throws Exception {
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
        this.newDotFilePath = file.getPath();
    }

    private String getMacroCode(String filePath) {
        xmlFilePath = TEMP_DIR + File.separator + System.currentTimeMillis() + ".xml";
        String macrosCode = FileUtil.readString(FileUtils.getResourcePath("component/file/xydecrypt/macros-excel-to-xml.vb"), Charset.defaultCharset());
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("filePath", filePath);
        paramMap.put("xmlFilePath", xmlFilePath);
        return StrUtils.parseTplContent(macrosCode, paramMap);
    }

    @Override
    protected void initFileExtensions() {
        this.fileExtensions.add("xls");
        this.fileExtensions.add("xlsx");
    }
}
