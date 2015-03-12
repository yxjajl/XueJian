package com.dh.timer;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.queue.LocalCommandRunnable;

@Component
public class ThreadMonitor {
	private static Logger logger = Logger.getLogger(ThreadMonitor.class);
	private final static long TT = 20 * 60 * 1000L;

	@Scheduled(cron = "0 0/10 * * * ?")
	public void run() {
		long curTime = System.currentTimeMillis();
		for (LocalCommandRunnable localCommandRunnable : ServerHandler.businessThreadList) {
			if (curTime - localCommandRunnable.getLastRunTime() > TT) {
				logger.error(localCommandRunnable.getThreadName() + "已经卡死了");
			}
		}
	}
}
