package com.dh.timer;

import io.netty.channel.Channel;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.game.constant.CSCommandConstant;
import com.dh.netty.NettyMessageVO;
import com.dh.queue.ArrayCommandQueue;
import com.dh.service.PlayerAccountService;
import com.dh.service.PlayerTimerService;
import com.dh.util.Tool;
import com.dh.vo.user.UserCached;

@Component
public class PlayerLogoutThread {
	private static final Logger LOGGER = Logger.getLogger(PlayerLogoutThread.class);
	public final static long OFFLINETIME = 1200 * 1000L;
	// public final static long OFFLINETIME = 60 * 1000L;
	@Resource
	private PlayerAccountService playerAccountService;
	@Resource
	private PlayerTimerService playerTimerService;

	@Scheduled(cron = "0 0/20 * * * ?")
	public void run() {
		LOGGER.info("=======================PlayerLogoutThread==============start========");
		try {
			processorOffLinePlayer();
			// LOGGER.info("====PlayerLogoutThread end");
		} catch (Exception e) {
			LOGGER.error("PlayerLogoutThread error" + e);
		}
		LOGGER.info("=======================PlayerLogoutThread==============end========" + ServerHandler.getOnlineCount());
	}

	public void processorOffLinePlayer() {
		Object[] values = ServerHandler.USERCACHEDMAP.values();
		ArrayList<Integer> idList = new ArrayList<Integer>();
		ArrayList<Channel> channelIdList = new ArrayList<Channel>();
		long curTime = System.currentTimeMillis();
		UserCached userCached = null;
		for (Object object : values) {
			try {
				userCached = (UserCached) object;
				if (userCached.getLastedAccessTime() != 0 && (curTime - userCached.getLastedAccessTime()) > OFFLINETIME) {

					// playerTimerService.addOnlineReward(userCached);
					// 用户离线太久，清理他
					idList.add(userCached.getPlayerVO().getPlayerId());
					channelIdList.add(userCached.getChannel());
				}
			} catch (Exception e) {
				System.out.println("PlayerLogoutThread processorOffLinePlayer1 error" + e);
			}
		}
		int i = 0;
		for (int value : idList) {
			try {
				LOGGER.info("用户id " + value + "踢出内存 强制离线");
				// ServerHandler.removePlayerChannelById(value);
				NettyMessageVO nettyMessageVO = packageOffLine(value);
				if (nettyMessageVO != null) {
					// LocalCommandQueue.getInstance().put(nettyMessageVO);
					int n = ArrayCommandQueue.getMod(channelIdList.get(i).hashCode());
					ArrayCommandQueue.arrQueue[n].put(nettyMessageVO);
				}
			} catch (Exception e) {
				System.out.println("PlayerLogoutThread processorOffLinePlayer2 error" + e);
			}
			i++;
		}

		idList.clear();
		idList = null;
		channelIdList.clear();
		channelIdList = null;
	}

	public NettyMessageVO packageOffLine(int playerId) {
		if (playerId < 1) {
			return null;
		}

		NettyMessageVO nettyMessageVO = new NettyMessageVO();
		nettyMessageVO.setCommandCode(CSCommandConstant.USER_LOGOUT);
		nettyMessageVO.setData(Tool.IntToByes(playerId));
		return nettyMessageVO;
	}

}
