package com.zhangyj.fileRename;

import com.zhangyj.fileRename.config.FileRenameConfig;
import com.zhangyj.fileRename.pojo.ReplaceWord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author zhangyj
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(FileRenameConfig.class)
public class DoRenameFile implements CommandLineRunner {

    private String dirPath;

    private final FileRenameConfig fileRenameConfig;

    @Override
    public void run(String... args) throws IOException {
        renameFiles(getDirFile());
    }

    private File getDirFile() {
        File dirFile = new File(fileRenameConfig.getPath());
        if(dirFile.isFile()){
            throw new RuntimeException("文件" + fileRenameConfig.getPath() + "不是文件夹，中止执行");
        }
        this.dirPath = dirFile.getPath();
        return dirFile;
    }

    public void renameFiles(File file) throws IOException {
        if(file.isFile()){
            renameFile(file);
            return;
        }
        File[] files = file.listFiles();
        if(files == null){
            return;
        }
        for (File item : files) {
            renameFiles(item);
        }
    }

    private void renameFile(File file) throws IOException {
        String newUri = file.getPath().substring(dirPath.length());
        for (ReplaceWord replaceWord : fileRenameConfig.getReplaceWords()) {
            newUri = newUri.replaceAll(replaceWord.getWord(), replaceWord.getNewWord());
        }
        File newFile = new File(dirPath + newUri);
        if(newFile.getPath().equals(file.getPath())){
            return;
        }
        File parentFile = newFile.getParentFile();
        if(!parentFile.exists()){
            boolean fail = !parentFile.mkdirs();
            if(fail){
                throw new RuntimeException("创建路径失败：" + parentFile.getPath());
            }
        }
        Files.copy(file.toPath(), newFile.toPath());
    }
}
