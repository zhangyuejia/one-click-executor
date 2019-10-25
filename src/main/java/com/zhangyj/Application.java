package com.zhangyj;

import com.zhangyj.config.CopyListConfig;
import com.zhangyj.maker.Maker;
import com.zhangyj.maker.impl.CopyListMaker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author ZHANG
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        Maker maker = context.getBean(CopyListMaker.class);
        maker.make().build();
    }
}
