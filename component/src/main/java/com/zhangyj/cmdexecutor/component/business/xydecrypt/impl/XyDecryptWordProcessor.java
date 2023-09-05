package com.zhangyj.cmdexecutor.component.business.xydecrypt.impl;

import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.zhangyj.cmdexecutor.component.business.xydecrypt.AbstractXyDecryptProcessor;
import com.zhangyj.cmdexecutor.core.common.util.JacobUtil;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class XyDecryptWordProcessor extends AbstractXyDecryptProcessor {

    @Override
    public void processDecrypt(File file) {
        // 另存为dotm文档（允许宏文档）
        JacobUtil.processWord(file.getPath(), document -> {
            String doTmFilePath = getTempDir() + File.separator + file.getName().substring(0, file.getName().lastIndexOf('.') + 1) + "dotm";
            Dispatch.call(document, "SaveAs", doTmFilePath, new Variant(15));
        });
        // 运行宏
        JacobUtil.processWord(file.getPath(), document -> {
            Dispatch documentApplication = Dispatch.get(document, "Application").toDispatch();
            Dispatch.call(documentApplication, "Run", "SendDocumentToAPI");
        });
    }

    @Override
    protected void initFileExtensions() {
        this.fileExtensions.add("doc");
        this.fileExtensions.add("docx");
    }
}
