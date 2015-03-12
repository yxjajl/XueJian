package com.dh.processor;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.constants.GMConstants;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.vo.message.MessageProto.ChatRequest;
import com.dh.game.vo.message.MessageProto.ChatResponse;
import com.dh.game.vo.message.MessageProto.CommonType;
import com.dh.game.vo.message.MessageProto.EChannel;
import com.dh.netty.NettyMessageVO;
import com.dh.service.ActivityService;
import com.dh.service.GMService;
import com.dh.service.KnapsackService;
import com.dh.util.BadWordsFilter;
import com.dh.util.CommonUtils;
import com.dh.util.DateUtil;
import com.dh.vo.user.UserCached;

@Component
public class ChatProcessor {
	@Resource
	private KnapsackService knapsackService;
	@Resource
	private GMService gmService;
	@Resource
	private ActivityService activityService;

	public void chatSend(ChatRequest req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		if (userCached.getPlayerVO().getLevel() < 10) {
			throw new GameException(AlertEnum.ROLE_LEVEL_NO);
		}
		if (GMConstants.canntSpeak(userCached.getPlayerVO().getGmGroup())) {// 被禁言
			return;
		}

		String msg = req.getMsg().trim();
		if (msg.isEmpty()) {
			return;
		}
		// if (msg.charAt(0) == '#' && gmService.gm(msg, userCached,
		// commandList)) {
		// return;
		// }

		EChannel eChannel = req.getEChannel();
		int commonId = req.getCommonId();
		CommonType commonType = req.getCommonType();
		boolean persiod = DateUtil.isOverTime(userCached.getUserTimer().getPlayerTimerVO().getChatTime(), 10);
		if (msg == null || msg.isEmpty()) {
			return;
		}
		msg = BadWordsFilter.filter(msg);
		msg = CommonUtils.cutWordsLenth(msg, 160);
		ChatResponse.Builder resp = ChatResponse.newBuilder().setEChannel(eChannel).setMsg(msg).setName(userCached.getPlayerVO().getName());
		resp.setCommonType(commonType);
		resp.setCommonId(playerId);
		if (eChannel == EChannel.CHAT_WORLD) {
			nettyMessageVO.setData(resp.build().toByteArray());
			if (persiod) {
				throw new GameException(AlertEnum.CHAT_TOO_MANY);
			} else {
				userCached.getUserTimer().getPlayerTimerVO().setChatTime(DateUtil.getNow());
			}
			ServerHandler.sendToAllOnlinePlayerWithBlack(nettyMessageVO, playerId);
			activityService.addDayDone(userCached, 12, commandList);
		} else if (eChannel == EChannel.CHAT_ROOM) {// 私聊
			resp.setDateTime(DateUtil.getNow());
			if (!userCached.getUserFriend().isInFriend(true, commonId)) {
				nettyMessageVO.setData(resp.build().toByteArray());
				ServerHandler.sendMessageToPlayer(nettyMessageVO, playerId, commonId);
				commandList.add(nettyMessageVO);
			} else {
				throw new GameException(AlertEnum.FRIEND_IN_BLACK);
			}
		} else if (eChannel == EChannel.CHAT_SPEAK) {// 喇叭
			nettyMessageVO.setData(resp.build().toByteArray());
			if (!knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), 24018, 1)) {
				throw new GameException(AlertEnum.ITEM_NUM_NOT);
			}
			knapsackService.removeItem(userCached, 24018, 1, commandList);
			ServerHandler.sendToAllOnlinePlayer(nettyMessageVO);
		} else if (eChannel == EChannel.CHAT_LEGION) {// 血盟
			resp.setDateTime(DateUtil.getNow());
			nettyMessageVO.setData(resp.build().toByteArray());
			int legionId = userCached.getPlayerVO().getLegionId();
			if (legionId > 0) {
				ServerHandler.broadcastLegion(legionId, nettyMessageVO);
			}
		}
	}
}
