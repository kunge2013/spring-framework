package com.simple.interfaces.replace;

import org.springframework.beans.factory.support.MethodReplacer;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author fangkun
 * @date 2020/9/12 17:23
 * @description:
 */

public  class SimpleMethodServiceImpl implements MethodReplacer {
	@Override
	public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
		SimpleDateFormat formate = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		Calendar c= Calendar.getInstance();
		c.add(Calendar.YEAR, 2);
		return formate.format(c.getTime());
	}
}