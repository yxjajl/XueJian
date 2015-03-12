package com.dh.s2s.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

public class BaseQueue<T> {
	private static Logger logger = Logger.getLogger(BaseQueue.class);
	/** 消息队列 */
	private final BlockingQueue<T> queue = new LinkedBlockingQueue<T>();

	public T take() {
		return queue.poll();
	}

	public T reallyTake() throws InterruptedException {
		return queue.take();
	}

	public void put(T t) {
		try {
			queue.put(t);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public int getQueueSize() {
		return queue.size();
	}

}
