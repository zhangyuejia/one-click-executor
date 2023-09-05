package com.zhangyj.cmdexecutor.component.business.xydecrypt;

import cn.hutool.core.collection.CollectionUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractXyDecryptProcessor implements XyDecryptProcessor {

    protected List<String> fileExtensions = new ArrayList<>();

    @Override
    public boolean isMatch(File file) {
        if(CollectionUtil.isEmpty(fileExtensions)){
            initFileExtensions();
        }
        return fileExtensions.contains(getFileExtension(file));
    }

    protected abstract void initFileExtensions();

    protected String getFileExtension(File file){
        return file.getName().substring(file.getName().lastIndexOf(".") + 1);
    }

    protected String getTempDir(){
        return System.getProperty("java.io.tmpdir") + File.separator +  "xyDecryptTemp";
    }
}
