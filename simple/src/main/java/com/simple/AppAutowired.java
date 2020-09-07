package com.simple;

import com.simple.servie.ServiceA;
import com.simple.servie.ServiceB;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author fangkun
 * @date 2020/9/6 18:14
 * @description: 通过beanFactory 设置循环依赖为false
 */
public class AppAutowired {


	public static void main(String[] args) {
		GenericApplicationContext configApplicationContext = new AnnotationConfigApplicationContext();
		configApplicationContext.setAllowCircularReferences(true);
		((AnnotationConfigApplicationContext) configApplicationContext).register(AppConfig.class);
		configApplicationContext.refresh();
		ServiceB serviceB = configApplicationContext.getBean(ServiceB.class);
		serviceB.print();
	}
}
