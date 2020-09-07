package com.simple.servie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fangkun
 * @date 2020/9/6 18:26
 * @description:
 */
@Service
public class ServiceA {

	@Autowired
	private ServiceB serviceB;
	public void print() {
		System.out.println("this is a service print..." + serviceB == null ? "serviceB is null" : serviceB.hashCode());
	}


}
