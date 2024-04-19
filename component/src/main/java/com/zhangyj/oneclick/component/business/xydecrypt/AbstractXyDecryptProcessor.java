package com.zhangyj.oneclick.component.business.xydecrypt;

import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractXyDecryptProcessor implements XyDecryptProcessor, InitializingBean {

    protected List<String> fileExtensions = new ArrayList<>();

    @Override
    public boolean isMatch(File file) {
        return fileExtensions.contains(getFileExtension(file));
    }

    protected abstract void initFileExtensions();

    protected String getFileExtension(File file){
        return file.getName().substring(file.getName().lastIndexOf(".") + 1);
    }

    @Override
    public List<String> getFileExtensions() {
        return fileExtensions;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initFileExtensions();
    }
}
