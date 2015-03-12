package com.dh.timer;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dh.game.constant.CSCommandConstant;
import com.dh.netty.NettyMessageVO;
import com.dh.queue.LocalCommandQueue;

/**
 * 系统通知检查
 * 
 * @author RickYu
 * 
 */
@Component
public class SystemNoticeTimer {
	private static Logger logger = Logger.getLogger(SystemNoticeTimer.class);

	private final static NettyMessageVO NETTYMESSAGEVO = new NettyMessageVO();

	static {
		NETTYMESSAGEVO.setCommandCode(CSCommandConstant.SYS_GM_NOTICE);
	}

	@Scheduled(cron = "0 0/1 * * * ?")
	public void run() {
		logger.debug("=============SystemNoticeTimer===========");
		LocalCommandQueue.getInstance().put(NETTYMESSAGEVO);
	}
}
