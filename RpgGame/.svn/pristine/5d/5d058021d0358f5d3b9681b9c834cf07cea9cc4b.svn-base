package com.dh.timer;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dh.game.constant.CSCommandConstant;
import com.dh.netty.NettyMessageVO;
import com.dh.queue.LocalCommandQueue;

/**
 * 每日凌晨定时任务
 * 
 * @author RickYu
 * 
 */
@Component
public class SysTipsTimer {
	private static Logger logger = Logger.getLogger(SysTipsTimer.class);

	private final static NettyMessageVO NETTYMESSAGEVO = new NettyMessageVO();

	static {
		NETTYMESSAGEVO.setCommandCode(CSCommandConstant.SYS_TIPS_NOTICE);
	}

	@Scheduled(cron = "0 0/15 * * * ?")
	public void run() {
		logger.debug("=============Sys tipTimer===========");
		LocalCommandQueue.getInstance().put(NETTYMESSAGEVO);
	}
}
