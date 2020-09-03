package com.simple;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("bean.xml");
		User bean = context.getBean(User.class);
		System.out.println(bean.toString());
	}
}
