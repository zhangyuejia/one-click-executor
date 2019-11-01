package com.zhangyj;

import com.zhangyj.maker.impl.CopyListMaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author ZHANG
 */
@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args){
        try {
            log.info("开始生成copyList");
            ApplicationContext context = SpringApplication.run(Application.class, args);
            CopyListMaker maker = context.getBean(CopyListMaker.class);
            log.info("生成copyList路径：{}", maker.make());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
