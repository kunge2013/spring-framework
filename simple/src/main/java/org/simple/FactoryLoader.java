package org.simple;

import com.simple.AppConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author fangkun
 * @date 2020/9/14 20:28
 * @description:
 */
public class FactoryLoader implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("init success !!!");
	}

	public static void main(String[] args) {
		AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

	}
}
