package com.zhangyj.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author ZHANG
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext ac;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
    }

    public static String getActiveProfile(){
        return ac.getEnvironment().getActiveProfiles()[0];
    }
}
