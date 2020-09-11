package com.simple.current;

/**
 * @author fangkun
 * @date 2020/9/11 16:13
 * @description:
 */
public class ThreadLocalTest {

	public static void main(String[] args) {
//		ThreadLocal<Object> threadLocal = new ThreadLocalOwner<>("local");
//		threadLocal.set(new Object());
//		Object o = threadLocal.get();
//		threadLocal.remove();
//		System.out.println(o);
		new ThreadA().start();
		new ThreadA().start();

	}

	static class ThreadLocalOwner<T> extends ThreadLocal<T> {
		private String name;
		public ThreadLocalOwner(String name) {
			this.name = name;
		}
	}

	public static class ThreadA extends Thread {
		ThreadLocal<Object> threadLocal = new ThreadLocalOwner<>("ThreadA");

		public ThreadLocal<Object> getThreadLocal() {
			return threadLocal;
		}

		public void setThreadLocal(ThreadLocal<Object> threadLocal) {
			this.threadLocal = threadLocal;
		}

		@Override
		public void run() {
			threadLocal.set(new Object());
			Object o = threadLocal.get();
			threadLocal.remove();
			System.out.println(o);
		}
	}
}
