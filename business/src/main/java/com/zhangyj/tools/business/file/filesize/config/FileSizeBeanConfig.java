package com.zhangyj.tools.business.file.filesize.config;

import com.zhangyj.tools.common.enums.FileSizeEnum;
import com.zhangyj.tools.common.handler.ChainHandler;
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
public class FileSizeBeanConfig {

    @Bean(name = "fileSizeHandler")
    public ChainHandler<Long, String> getFileSizeHandler(){
        List<ChainHandler<Long, String>> handlers = Stream.of(FileSizeEnum.values())
                .sorted(Comparator.comparing(FileSizeEnum::getUnit).reversed())
                .map(item -> new ChainHandler<Long, String>() {
            @Override
            protected String getResponse(Long aLong) {
                return String.format("%.2f%s", (float) aLong / item.getUnit(), item.name());
            }

            @Override
            protected boolean isHandle(Long aLong) {
                return aLong > item.getUnit();
            }

        }).collect(Collectors.toList());

        ChainHandler<Long, String> currentHandler = handlers.get(0);
        for (int i = 0; i < handlers.size(); i++) {
            if(i == 0){
                continue;
            }
            currentHandler.setNextHandler(handlers.get(i));
            currentHandler = currentHandler.getNextHandler();
        }
        return handlers.get(0);
    }
}
