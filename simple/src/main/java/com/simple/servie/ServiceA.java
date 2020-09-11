package com.simple.servie;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author fangkun
 * @date 2020/9/6 18:26
 * @description:
 */
@Service
//@Scope("prototype")
public class ServiceA  implements BeanFactoryAware, BeanNameAware, InitializingBean, DisposableBean, BeanClassLoaderAware {

	@Autowired
	private ServiceB serviceB;

	public void print() {
		System.out.println("this is a service print..." + serviceB == null ? "serviceB is null" : serviceB.hashCode());
	}


	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("set beanFactory ");
	}

	@Override
	public void setBeanName(String name) {
		System.out.println("BeanNameAware set name = " + name);
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("destroy = destroy");
	}

	@PostConstruct
	public void testPostCut() {
		System.out.println("PostConstruct  postConstruct");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("InitializingBean afterPropertiesSet()");
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		System.out.println(classLoader);
	}
}
