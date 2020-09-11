package com.simple.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;


/**
 * @author fangkun
 * @date 2020/9/11 18:58
 * @description:
 */
@Component
public class SimpleBeanFactoryPostProcessor implements BeanFactoryPostProcessor, InitializingBean {


	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if (beanFactory instanceof DefaultListableBeanFactory) {
//			MutablePropertyValues tsPvs = new MutablePropertyValues();
//			tsPvs.add("targetBeanName", "person");
			RootBeanDefinition tsBd = new RootBeanDefinition(ProcessorBean.class);
//			tsBd.setPropertyValues(tsPvs);
			((DefaultListableBeanFactory) beanFactory).registerBeanDefinition("processorBean", tsBd);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("SimpleBeanFactoryPostProcessor ==");
	}

	public static class ProcessorBean {
		public ProcessorBean() {
			System.out.println("ProcessorBean  create success !");
		}
	}

}
