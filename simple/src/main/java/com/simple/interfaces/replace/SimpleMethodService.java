package com.simple.interfaces.replace;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author fangkun
 * @date 2020/9/12 17:24
 * @description:
 */

public class SimpleMethodService {

	public String getTime() {
		SimpleDateFormat formate = new SimpleDateFormat("yy-MM-dd");
		return formate.format(new Date());
	}
}