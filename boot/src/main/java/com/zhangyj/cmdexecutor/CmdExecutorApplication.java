package com.zhangyj.cmdexecutor;

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
public class CmdExecutorApplication {

    public static void main(String[] args){
        SpringApplication.run(CmdExecutorApplication.class, args);
    }
}
