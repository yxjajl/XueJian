package com.dh.timer;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dh.game.constant.CSCommandConstant;
import com.dh.netty.NettyMessageVO;
import com.dh.queue.LocalCommandQueue;

@Component
public class PowerTimer {
	private static Logger logger = Logger.getLogger(PowerTimer.class);

	private final static NettyMessageVO NETTYMESSAGEVO = new NettyMessageVO();

	static {
		NETTYMESSAGEVO.setCommandCode(CSCommandConstant.SYS_POWER_FRESH);
	}

	// 每20分钟刷下体力
	@Scheduled(cron = "0 0/5 * * * ?")
	public void run() {
		logger.debug("=============powerTimer===========");
		LocalCommandQueue.getInstance().put(NETTYMESSAGEVO);
	}
}
