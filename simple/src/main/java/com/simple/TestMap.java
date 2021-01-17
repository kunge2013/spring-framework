package com.simple;

import java.util.HashMap;

/**
 * @author fangkun
 * @date 2021/1/6 14:16
 * @description:
 */
public class TestMap {

	public static void main(String[] args) {
		HashMap<Integer, Integer> hashMap = new HashMap<>();
		for (int  i= 0 ;  i< 65 ; i++) {
			if (i == 64 || i == 12) {
				hashMap.put(i, i);
			} else {
				hashMap.put(i, i);
			}

		}
	}

}
