package com.simple;

import com.simple.servie.ServiceA;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class App {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//		User bean = context.getBean(User.class);
//		System.out.println(bean.toString());
		ServiceA serviceA = context.getBean(ServiceA.class);
		//serviceA.print();
		//AutowireCapableBeanFactory autowireCapableBeanFactory = context.getAutowireCapableBeanFactory();
	}

}
