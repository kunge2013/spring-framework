package com.simple.annotations;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author fangkun
 * @date 2020/9/12 11:01
 * @description:
 */
public class LookupSimple {


	@Component
	public static abstract class SingletonBean  {

		@Bean(name = "commond1")
		public Commond createCommond1() {
			return new Commond("commond1");
		}


		@Bean(name = "commond2")
		public Commond createCommond2() {
			return new Commond("commond2");
		}

		@Bean(name = "commond3")
		public Commond createCommond3() {
			return new Commond("commond3");
		}

		public void print(int index) {
			Commond commond = null;
			if(index == 3) {
				commond = commond3();
			}
			if(index == 1) {
				commond = commond1();
			}
			if(index == 2) {
				commond = commond2();
			}

			if (commond != null) {
				System.out.println(commond.toString());
			}
		}

		@Lookup("commond1")
		protected abstract Commond commond1();
		@Lookup("commond2")
		protected abstract Commond commond2();
		@Lookup("commond3")
		protected abstract Commond commond3();

	}



	public static  class Commond {
		String commond;

		public Commond(String commond) {
			this.commond = commond;
		}

		@Override
		public String toString() {
			return "Commond{" +
					"commond='" + commond + '\'' +
					'}';
		}
	}





	public static void main(String[] args) {
		AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		SingletonBean bean = configApplicationContext.getBean(SingletonBean.class);
		bean.print(1);
		bean.print(2);
		bean.print(3);
	}

	@ComponentScan("com.simple.*")
	public static class AppConfig {

	}



}
