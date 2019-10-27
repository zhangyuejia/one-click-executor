package com.zhangyj;

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

    public static void main(String[] args){
        try {
            ApplicationContext context = SpringApplication.run(Application.class, args);
            Maker maker = context.getBean(CopyListMaker.class);
            maker.make().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
