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
}
