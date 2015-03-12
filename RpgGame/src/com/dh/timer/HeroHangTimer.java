package com.dh.timer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.netty.NettyServerHandler;
import com.dh.processor.HeroHangProcessor;
import com.dh.vo.user.UserCached;

@Component
public class HeroHangTimer {
	private static Logger logger = Logger.getLogger(EveryDayTimer.class);
	@Resource
	private HeroHangProcessor heroHangProcessor;

	// private final static NettyMessageVO NETTYMESSAGEVO = new NettyMessageVO();
	//
	// static {
	// NETTYMESSAGEVO.setCommandCode(CSCommandConstant.SYS_SHOP_REFRESH);
	// }

	// 每２妙触发一次
	@Scheduled(cron = "0/3 * * * * ?")
	public void run() {
		// logger.debug("=============HeroHangTimer===========");
		// LocalCommandQueue.getInstance().put(NETTYMESSAGEVO);
		if (NettyServerHandler.System_status == 1) {
			return;
		}
		try {
			heroHang();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 英雄挂机主动回到队列
	 */
	public void heroHang() throws Exception {
		List<NettyMessageVO> commandList = new ArrayList<NettyMessageVO>();
		UserCached[] arrUserCached = ServerHandler.USERCACHEDMAP.values(new UserCached[0]);
		for (UserCached userCached : arrUserCached) {
			try {
				heroHangProcessor.hungryBack(userCached, commandList);
				if (commandList.size() > 0) {
					ServerHandler.sendMessageToPlayer(commandList, userCached.getPlayerId());
				}
			} catch (Exception e) {
				logger.error("TimerHandler.heroHang()", e);
			} finally {
				commandList.clear();
			}
		}
	}
}
