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

    private final FileRenameConfig fileRenameConfig;

    @Override
    public void run(String... args) throws IOException {
        log.info("执行文件重命名功能");
        File sourceDir = new File(fileRenameConfig.getPath());
        File targetDir = new File(fileRenameConfig.getTargetPath());
        if(sourceDir.isFile() || targetDir.isFile()) {
            throw new RuntimeException("文件" + fileRenameConfig.getPath() + "不是文件夹，中止执行");
        }
        if(sourceDir.getPath().equals(targetDir.getPath())){
            throw new RuntimeException("源路径不能和目标路径一致！");
        }
        // 删除目标文件夹
        deleteDir(targetDir);
        // 复制并重命名源文件夹
        renameFiles(sourceDir, sourceDir, targetDir);
    }

    private void deleteDir(File dirFile) {
        File[] files = dirFile.listFiles();
        if(!dirFile.exists() || files == null){
            return;
        }
        for (File item : files) {
            if(item.isFile()){
                if(!item.delete()){
                    throw new RuntimeException("删除文件失败！" + item.getPath());
                }
            }else {
                deleteDir(item);
            }
        }
        if(!dirFile.delete()){
            throw new RuntimeException("删除文件夹失败！" + dirFile.getPath());
        }
    }

    public void renameFiles(File currentFile, File sourceDir, File targetDir) throws IOException {
        if(currentFile.isFile()){
            renameFile(currentFile, sourceDir, targetDir);
            return;
        }
        File[] files = currentFile.listFiles();
        if(files == null){
            return;
        }
        for (File item : files) {
            renameFiles(item, sourceDir, targetDir);
        }
    }

    private void renameFile(File file, File sourceDir, File targetDir) throws IOException {
        String newUri = file.getPath().substring(sourceDir.getPath().length());
        for (ReplaceWord replaceWord : fileRenameConfig.getReplaceWords()) {
            newUri = newUri.replaceAll(replaceWord.getWord(), replaceWord.getNewWord());
        }
        File newFile = new File(targetDir.getPath() + newUri);
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
