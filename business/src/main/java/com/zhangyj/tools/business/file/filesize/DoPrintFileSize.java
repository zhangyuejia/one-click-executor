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

    private List<FileInfo> fileInfoList;

    private final ChainHandler<Long, String> fileSizeHandler;

    public DoPrintFileSize(@Qualifier("fileSizeHandler") ChainHandler<Long, String> fileSizeHandler) {
        this.fileSizeHandler = fileSizeHandler;
    }

    @Override
    protected void doRun() throws Exception {
        loadFileInfoList();
        printFileInfoList();
    }

    private void printFileInfoList() {
        for (FileInfo fileInfo : fileInfoList) {
            log.info("{}：{} 大小：{}", fileInfo.getIsFile()? "单文件":"文件夹", fileInfo.getFileName(), getFileSizeDesc(fileInfo.getSize()));
        }
        long sum = fileInfoList.stream().mapToLong(FileInfo::getSize).sum();
        log.info("路径:{} 总大小:{}", config.getPath(), fileSizeHandler.handle(sum));
        executorService.shutdownNow();
    }

    private String getFileSizeDesc(Long size) {
        if(size == null || size == 0){
            return "0";
        }
        return fileSizeHandler.handle(size);
    }

    private void loadFileInfoList() throws InterruptedException {
        File file = new File(config.getPath());
        if(!file.exists()){
            throw new RuntimeException("文件路径不存在，" + config.getPath());
        }
        File[] files = file.listFiles();
        if(files == null){
            throw new RuntimeException("文件路径内容为空，" + config.getPath());
        }
        // 计算文件大小
        calculateFileSize(files);
        // 转换对象
        transferToFileInfoList(files);
    }

    private void transferToFileInfoList(File[] files) {
        this.fileInfoList = Stream.of(files)
                .map(f -> FileInfo.builder()
                        .fileName(f.getAbsolutePath())
                        .isFile(f.isFile())
                        .size(fileSizeMap.get(f.getName())).build())
                // 按大小倒序打印
                .sorted(Comparator.comparing(FileInfo::getSize).reversed())
                .collect(Collectors.toList());
    }

    private void calculateFileSize(File[] files) throws InterruptedException {
        // 多线程计算文件大小
        CountDownLatch latch = new CountDownLatch(files.length);
        for (File file : files) {
             executorService.execute(() -> {
                 long fileSize = file.isFile() ? file.length() : FileUtils.sizeOfDirectory(file);
                 fileSizeMap.put(file.getName(), fileSize);
                latch.countDown();
            });
        }
        latch.await();
    }
}
