package com.simple.interfaces.processor;

import org.springframework.context.LifecycleProcessor;

/**
 * @author fangkun
 * @date 2020/9/12 20:13
 * @description:
 */
public class LifecycleProcessorSimple implements LifecycleProcessor {
	@Override
	public void onRefresh() {

	}

	@Override
	public void onClose() {

	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	@Override
	public boolean isRunning() {
		return false;
	}
}
