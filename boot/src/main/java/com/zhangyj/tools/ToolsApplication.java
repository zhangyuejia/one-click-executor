package com.zhangyj.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ZHANG
 */
@Slf4j
@EnableScheduling
@SpringBootApplication
public class ToolsApplication {

    public static void main(String[] args){
        SpringApplication.run(ToolsApplication.class, args);
    }
}
