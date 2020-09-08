package com.simple.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author fangkun
 * @date 2020/9/7 19:52
 * @description: aop切面配置
 */
@Configuration
@EnableAspectJAutoProxy
@Aspect
public class AopAspect {

	@Pointcut("execution(* com.simple.servie.*.*(..))")
	public void point() {

	}

	@Before("point()")
	public void beforeService() {
		System.out.println("beforeService");
	}

	@After("point()")
	public void afterService() {
		System.out.println("afterService");
	}

	@Around("point()")
	public Object  aroundService(ProceedingJoinPoint proceedingJoinPoint) {
		Object proceed = null;
		System.out.println("before proceed ");
		try {
			proceed = proceedingJoinPoint.proceed();
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		System.out.println("after proceed");
		return proceed;
	}
}
