package com.dh.s2s.queue;

import org.apache.log4j.Logger;

import com.dh.netty.NettyMessageVO;

public abstract class AbstractCommandRunnable implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(AbstractCommandRunnable.class);
	private final static long SLEEPTIME = 50;
	private BaseQueue<NettyMessageVO> INSTANCE;
	private long lastRunTime = 0;
	private String threadName;

	@Override
	public void run() {
		while (true) {
			try {
				// System.out.println("Thread " + name + ": process");
				NettyMessageVO nettyMessageVO = null;
				while (INSTANCE.getQueueSize() > 0) {
					nettyMessageVO = INSTANCE.take();
					sendMessage(nettyMessageVO);
				}
				lastRunTime = System.currentTimeMillis();
				Thread.sleep(SLEEPTIME);
				// System.out.println(Thread.currentThread().getName() +
				// " runing -- ");
			} catch (Throwable th) {
				th.printStackTrace();
				LOGGER.error("AbstractCommandRunnable error " + this.getThreadName(), th);
			}
		}
	}

	public long getLastRunTime() {
		return lastRunTime;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public AbstractCommandRunnable(BaseQueue<NettyMessageVO> baseQueue, String _name) {
		INSTANCE = baseQueue;
		threadName = _name;
	}

	public abstract void sendMessage(NettyMessageVO nettyMessageVO);

}
