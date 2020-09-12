package com.simple.aware;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author fangkun
 * @date 2020/9/12 20:16
 * @description:
 */
@Component
public class SimpleApplicationContextAware implements ApplicationContextAware {

	public ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

	public static void main(String[] args) {
		AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		//configApplicationContext.registerShutdownHook();
	}

	@ComponentScan("com.simple.*")
	public static class AppConfig {

	}
}
