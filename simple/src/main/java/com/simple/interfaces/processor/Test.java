package com.simple.interfaces.processor;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author fangkun
 * @date 2020/9/11 19:26
 * @description:
 */
@Configuration
public class Test {
	@Resource(name = "processorBean")
	private SimpleBeanFactoryPostProcessor.ProcessorBean processorBean;

	@PostConstruct
	public void test() {
		System.out.println("Test ==" + processorBean);
	}
}
