package com.simple.interfaces.replace;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * @author fangkun
 * @date 2020/9/12 11:26
 * @description:  替换执行的方法
 */
public class SimpleMethodReplacer  {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("replacemethods.xml");
		SimpleMethodService bean = context.getBean(SimpleMethodService.class);
		System.out.println(bean.getTime());
	}
}
