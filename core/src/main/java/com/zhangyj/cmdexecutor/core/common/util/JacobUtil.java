package com.zhangyj.cmdexecutor.core.common.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class JacobUtil {
    public interface JacobProcessor{

        void process(ActiveXComponent word, Dispatch document);
    }

    public static void processWord(String filePath, JacobProcessor jacobProcessor){
        ComThread.InitSTA();
        ActiveXComponent word = new ActiveXComponent("Word.Application");
        try {
            word.setProperty("Visible", new Variant(false));
            Dispatch documents = word.getProperty("Documents").toDispatch();
            Dispatch document = Dispatch.call(documents, "Open", filePath).toDispatch();
            jacobProcessor.process(word, document);
            Dispatch.call(document, "Close", new Variant(false));
        } finally {
            word.invoke("Quit", new Variant(false));
            word.safeRelease();
            ComThread.Release();
        }
    }

    public static void processWord(String filePath, String macroCode){
        JacobUtil.processWord(filePath, (word, document) -> {
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
}
