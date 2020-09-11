package com.simple;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.simple")
public class AppConfig {

	@Bean
	public User createBean() {
		User user = new User();
		user.setAddress("wuhan");
		//		user.setName("zhangsan");
		return user;
	}
}
