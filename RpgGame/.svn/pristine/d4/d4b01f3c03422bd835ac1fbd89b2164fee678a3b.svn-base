package com.dh.timer;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dh.game.constant.CSCommandConstant;
import com.dh.netty.NettyMessageVO;
import com.dh.queue.LocalCommandQueue;

/**
 * 竞技场排名通知
 */
@Component
public class ArenaRankTimer {
	private static Logger logger = Logger.getLogger(ArenaRankTimer.class);

	private final static NettyMessageVO NETTYMESSAGEVO = new NettyMessageVO();

	static {
		NETTYMESSAGEVO.setCommandCode(CSCommandConstant.SYS_ARENA_RANK3);
	}

	@Scheduled(cron = "0 0 1,8,10,20,22 * * ?")
	public void run() {
		logger.debug("=============arenaRank===========");
		LocalCommandQueue.getInstance().put(NETTYMESSAGEVO);
	}
}
