package com.simple.annotations;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author fangkun
 * @date 2020/9/11 18:20
 * @description:
 */
@Component
@Order(1)
//@DependsOn("qualifierSimple")
public class PrimarySimple implements InitializingBean {

	@Autowired
	private PrimaryBean primaryBean;

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println(primaryBean.toString());
	}



	@Primary
	@Bean
	public PrimaryBean createX() {
		return new PrimaryBean("XXX");
	}


	@Bean
	public PrimaryBean createY() {
		return new PrimaryBean("YYY");
	}


	public static class PrimaryBean {

		private String name;

		public PrimaryBean(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "PrimaryBean{" +
					"name='" + name + '\'' +
					'}';
		}
	}
}
