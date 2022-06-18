package com.zhangyj.tools.business.file.filesize;

import com.zhangyj.tools.business.file.filesize.config.PrintFileSizeConfig;
import com.zhangyj.tools.business.file.filesize.pojo.FileInfo;
import com.zhangyj.tools.common.base.AbstractRunner;
import com.zhangyj.tools.common.handler.ChainHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 打印文件夹大小功能
 * @author zhangyj
 */
@Slf4j
@Component
@ConditionalOnBean(PrintFileSizeConfig.class)
public class DoPrintFileSize extends AbstractRunner<PrintFileSizeConfig> {

    private final Map<String, Long> fileSizeMap = new HashMap<>();

    private final ExecutorService executorService  = new ThreadPoolExecutor(
            0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<>(), r -> {
                Thread thread = new Thread(r);
                thread.setName("DoPrintFileSize-");
                return thread;
            });

    private final ChainHandler<Long, String> fileSizeHandler;

    public DoPrintFileSize(@Qualifier("fileSizeHandler") ChainHandler<Long, String> fileSizeHandler) {
        this.fileSizeHandler = fileSizeHandler;
    }

    @Override
    protected void doRun() throws Exception {
        List<FileInfo> fileInfoList = getFileInfoList(config.getPath());
        printFileInfo(fileInfoList);
    }

    private void printFileInfo(List<FileInfo> fileInfoList) {
        if(CollectionUtils.isEmpty(fileInfoList)){
            log.info("文件夹路径内容为空:{}", config.getPath());
            return;
        }
        for (FileInfo fileInfo : fileInfoList) {
            printFileInfo(fileInfo);
        }
        long sum = fileInfoList.stream().mapToLong(FileInfo::getSize).sum();
        log.info("路径:{} 总大小:{}", config.getPath(), fileSizeHandler.handle(sum));
    }

    private void printFileInfo(FileInfo fileInfo) {
        log.info("{}：{} 大小：{}", fileInfo.getIsFile()? "文件名":"文件夹", fileInfo.getFileName(), printFileSize(fileInfo.getSize()));
    }

    private String printFileSize(Long size) {
        if(size == null || size == 0){
            return "0";
        }
        return fileSizeHandler.handle(size);
    }

    private List<FileInfo> getFileInfoList(String path) throws InterruptedException {
        File[] files = new File(path).listFiles();
        if(files == null){
            return null;
        }
        calculateFileSize(files);
        return Stream.of(files)
                .map(f -> FileInfo.builder()
                .fileName(f.getName())
                .isFile(f.isFile())
                .size(fileSizeMap.get(f.getName())).build())
                // 按大小倒序打印
                .sorted(Comparator.comparing(FileInfo::getSize).reversed())
                .collect(Collectors.toList());
    }

    private void calculateFileSize(File[] files) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(files.length);
        for (File file : files) {
             executorService.execute(() -> {
                Long fileSize = getFileSize(file);
                fileSizeMap.put(file.getName(), fileSize);
                latch.countDown();
            });
        }
        latch.await();
    }

    private Long getFileSize(File file) {
        return file.isFile()? file.length(): FileUtils.sizeOfDirectory(file);
    }
}
