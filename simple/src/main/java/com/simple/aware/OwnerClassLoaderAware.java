package com.simple.aware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * @author fangkun
 * @date 2020/9/11 11:48
 * @description:
 */
@Component
public class OwnerClassLoaderAware implements BeanClassLoaderAware , BeanFactoryAware {

	private BeanFactory factory;
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		classLoader = new OwnerClassLoader(classLoader);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.factory = beanFactory;
	}
}
