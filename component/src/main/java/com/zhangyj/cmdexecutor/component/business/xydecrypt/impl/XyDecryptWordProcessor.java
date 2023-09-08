package com.zhangyj.cmdexecutor.component.business.xydecrypt.impl;

import cn.hutool.core.io.FileUtil;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.zhangyj.cmdexecutor.component.business.xydecrypt.AbstractXyDecryptProcessor;
import com.zhangyj.cmdexecutor.component.common.config.CmdXyDecryptConfig;
import com.zhangyj.cmdexecutor.core.common.util.FileUtils;
import com.zhangyj.cmdexecutor.core.common.util.JacobUtil;
import com.zhangyj.cmdexecutor.core.common.util.StrUtils;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Component
public class XyDecryptWordProcessor extends AbstractXyDecryptProcessor {

    @Override
    public void processDecrypt(CmdXyDecryptConfig config, File file) {
        String doTmFilePath = getTempDir() + File.separator + file.getName().substring(0, file.getName().lastIndexOf('.') + 1) + "dotm";
        // 另存为dotm文档（允许宏文档）
        JacobUtil.processWord(file.getPath(), (word, document) -> {
            Dispatch.call(document, "SaveAs", doTmFilePath, new Variant(15));
        });
        // 插入宏代码并运行
        JacobUtil.processWord(doTmFilePath, (word, document) -> {
            String macroCode = getMacroCode(file.getName(), config);
            Dispatch vbaProject = Dispatch.get(document, "VBProject").toDispatch();
            Dispatch vbaModule = Dispatch.call(vbaProject, "VBComponents", new Variant(1)).toDispatch();
            Dispatch codeModule = Dispatch.get(vbaModule, "CodeModule").toDispatch();
            int startLine = Dispatch.call(codeModule, "CountOfLines").getInt() + 1;
            Dispatch.call(codeModule, "InsertLines", startLine, macroCode);
            // 运行宏
            Dispatch.call(word, "Run", "SendDocumentToAPI");
            // 保存文档
            Dispatch.call(document, "Save");
        });
    }

    private String getMacroCode(String name, CmdXyDecryptConfig config) {
        String macrosCode = FileUtil.readString(FileUtils.getResourcePath("component/file/xydecrypt/macros.vb"), Charset.defaultCharset());
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("docNameBase64", Base64Util.encode(name));
        paramMap.put("dataServerUrl", config.getDataServerUrl());
        return StrUtils.parseTplContent(macrosCode, paramMap);
    }

    @Override
    protected void initFileExtensions() {
        this.fileExtensions.add("doc");
        this.fileExtensions.add("docx");
    }
}
