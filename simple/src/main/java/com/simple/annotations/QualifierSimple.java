package com.simple.annotations;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author fangkun
 * @date 2020/9/11 18:02
 * @description:
 */
@Component
public class QualifierSimple implements InitializingBean {


	@Autowired
	@Qualifier("yyy")
	private QualifierBean bean;

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println(bean.toString());
	}


	public static class QualifierBean {

		private String name;

		public QualifierBean(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "QualifierBean{" +
					"name='" + name + '\'' +
					'}';
		}
	}


	@Bean("xxx")
	public QualifierBean createBean() {
		return new QualifierBean("XXXX");
	}

	@Bean("yyy")
	public QualifierBean createBeanY() {
		return new QualifierBean("yyy");
	}
}
