package com.dh.timer;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dh.netty.NettyServerHandler;
import com.dh.service.GMService;

@Component
public class GMTimer {
	private static Logger logger = Logger.getLogger(GMTimer.class);
	@Resource
	private GMService gmService;

	@Scheduled(cron = "0  0/10 * * * ?")
	public void run() {
		logger.info("enter " + this.getClass().getName());
		if (NettyServerHandler.System_status == 1) {
			return;
		}

		try {
			gmService.getAndUpOnline();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("enter " + this.getClass().getName());
	}
}
