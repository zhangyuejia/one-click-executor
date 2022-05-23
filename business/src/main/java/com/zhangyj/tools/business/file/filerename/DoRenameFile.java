package com.zhangyj.tools.business.file.filerename;

import com.zhangyj.tools.business.file.filerename.config.FileRenameConfig;
import com.zhangyj.tools.business.file.filerename.pojo.ReplaceWord;
import com.zhangyj.tools.common.base.AbstractFunExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class DoRenameFile extends AbstractFunExecutor<FileRenameConfig> {

    @Override
    protected void doExec() throws Exception {
        File sourceDir = new File(config.getPath());
        File targetDir = new File(config.getTargetPath());
        if(!sourceDir.isDirectory()) {
            throw new RuntimeException("源文件" + config.getPath() + "不是文件夹，中止执行");
        }
        if(targetDir.exists() && !targetDir.isDirectory()) {
            throw new RuntimeException("目标文件" + config.getTargetPath() + "不是文件夹，中止执行");
        }
        if(sourceDir.getPath().equals(targetDir.getPath())){
            throw new RuntimeException("源路径不能和目标路径一致！");
        }
        // 删除目标文件夹
        log.info("删除文件夹：" + targetDir.getPath());
        deleteDir(targetDir);
        // 复制并重命名源文件夹
        renameFiles(sourceDir, sourceDir, targetDir);
    }

    private void deleteDir(File dirFile) {
        if(!dirFile.exists()){
            return;
        }
        File[] files = dirFile.listFiles();
        if(files != null){
            for (File item : files) {
                if(item.isFile()){
                    if(!item.delete()){
                        throw new RuntimeException("删除文件失败！" + item.getPath());
                    }
                }else {
                    deleteDir(item);
                }
            }
        }
        if(!dirFile.delete()){
            throw new RuntimeException("删除文件失败！" + dirFile.getPath());
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
        for (ReplaceWord replaceWord : config.getReplaceWords()) {
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
            log.info("创建文件夹：" + parentFile.getPath());
        }
        log.info("复制文件 FROM：" + file.toPath());
        log.info("复制文件   TO：" + newFile.toPath());
        Files.copy(file.toPath(), newFile.toPath());
    }
}
