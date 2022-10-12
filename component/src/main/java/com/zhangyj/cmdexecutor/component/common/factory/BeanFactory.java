package com.zhangyj.cmdexecutor.component.common.factory;


import com.zhangyj.cmdexecutor.component.common.enums.FileSizeEnum;
import com.zhangyj.cmdexecutor.core.common.handler.ChainHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhangyj
 */
@Configuration
public class BeanFactory {

    @Bean(name = "fileSizeHandler")
    public ChainHandler<Long, String> getFileSizeHandler(){
        // 转换为文件大小处理器
        List<ChainHandler<Long, String>> handlers = getFileSizeHandlers();
        // 组装链式文件大小处理器
        composeFileSizeHandler(handlers);
        return handlers.get(0);
    }

    private void composeFileSizeHandler(List<ChainHandler<Long, String>> handlers) {
        ChainHandler<Long, String> currentHandler = handlers.get(0);
        for (int i = 0; i < handlers.size(); i++) {
            if(i == 0){
                continue;
            }
            currentHandler.setNextHandler(handlers.get(i));
            currentHandler = currentHandler.getNextHandler();
        }
    }

    private List<ChainHandler<Long, String>> getFileSizeHandlers() {
        return Stream.of(FileSizeEnum.values())
                    // 按大小倒序排
                    .sorted(Comparator.comparing(FileSizeEnum::getUnit).reversed())
                    .map(item -> new ChainHandler<Long, String>() {
                @Override
                protected String getResponse(Long aLong) {
                    return String.format("%.2f%s", (float) aLong / item.getUnit(), item.name());
                }

                @Override
                protected boolean checkHandle(Long aLong) {
                    return aLong > item.getUnit();
                }

            }).collect(Collectors.toList());
    }
}
