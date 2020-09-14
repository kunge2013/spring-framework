package com.simple.juc;

import java.util.concurrent.CountDownLatch;

/**
 * @author fangkun
 * @date 2020/9/14 9:09
 * @description:
 */
public class CountDownLatchSimple {

	public static void main(String[] args) throws InterruptedException {

		CountDownLatch latch = new CountDownLatch(5);

		Runnable simple = () -> {
			latch.countDown();
		};

		Runnable simple2 = () -> {
			latch.countDown();
		};

		Runnable simple4 = () -> {
			latch.countDown();
		};

		Runnable simple5 = () -> {
			latch.countDown();
		};

		Runnable simple3 = () -> {
			latch.countDown();
		};

		new Thread(simple).start();

		new Thread(simple2).start();

		new Thread(simple3).start();

		new Thread(simple4).start();

		new Thread(simple5).start();

		latch.await();


	}


}
