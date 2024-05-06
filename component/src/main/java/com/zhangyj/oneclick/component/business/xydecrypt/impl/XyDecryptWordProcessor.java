package com.zhangyj.oneclick.component.business.xydecrypt.impl;

import cn.hutool.core.io.FileUtil;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.zhangyj.oneclick.component.business.xydecrypt.AbstractXyDecryptProcessor;
import com.zhangyj.oneclick.component.common.config.CmdXyDecryptConfig;
import com.zhangyj.oneclick.core.common.util.FileUtils;
import com.zhangyj.oneclick.core.common.util.JacobUtil;
import com.zhangyj.oneclick.core.common.util.StrUtils;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhanglj
 */
@Component
public class XyDecryptWordProcessor extends AbstractXyDecryptProcessor {

    @Override
    public void processDecrypt(CmdXyDecryptConfig config, File file) {
        String doTmFilePath = TEMP_DIR + File.separator + file.getName().substring(0, file.getName().lastIndexOf('.') + 1) + "dotm";
        // 另存为dotm文档（允许宏文档）
        JacobUtil.processWord(file.getPath(), (word, document) -> Dispatch.call(document, "SaveAs", doTmFilePath, new Variant(15)));
        // 插入宏代码并运行
        JacobUtil.processWord(doTmFilePath, getMacroCode(file.getName(), config));
    }

    private String getMacroCode(String name, CmdXyDecryptConfig config) {
        String macrosCode = FileUtil.readString(FileUtils.getResourcePath("component/file/xydecrypt/macros-word.vb"), Charset.defaultCharset());
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("fileNameBase64", Base64Util.encode(name));
        paramMap.put("dataServerUrl", config.getDataServerUrl() + "/accept");
        return StrUtils.parseTplContent(macrosCode, paramMap);
    }

    @Override
    protected void initFileExtensions() {
        this.fileExtensions.add("doc");
        this.fileExtensions.add("docx");
    }
}
