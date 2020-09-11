package com.simple.aware;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.stereotype.Component;

/**
 * @author fangkun
 * @date 2020/9/11 11:46
 * @description:
 */

public class OwnerClassLoader  extends ClassLoader {


	public OwnerClassLoader(ClassLoader parentclass) {
		super(parentclass);
	}

}
