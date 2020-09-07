package com.simple.servie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fangkun
 * @date 2020/9/6 18:26
 * @description:
 */
@Service
public class ServiceB {

//	@Autowired
	private ServiceA serviceA;


	public void print() {
		System.out.println("print service b");
	}
}
