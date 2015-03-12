package com.dh.util;

import com.dh.Cache.RedisList;
import com.dh.Cache.ServerHandler;
import com.dh.constants.CommonConstants;
import com.dh.game.constant.RedisKey;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_UNIT_TYPE;
import com.dh.vo.user.UserCached;

public class MsgUtil {
	public static void addMsgNotice(int playerId, int type) {
		if (type == 1) {
			UserCached enemyCached;
			if ((enemyCached = ServerHandler.getUserCached2(playerId)) != null && enemyCached.getChannel().isActive()) {
				ServerHandler.sendMessageToPlayer(CommandUtil.packageAnyProperties(PLAYER_UNIT_TYPE.UNIT_PLAYER, playerId, PLAYER_PROPERTY.PROPERTY_STREET_NOTICE, type), playerId);
			} else {
				RedisList.addMsgNotice(playerId, RedisKey.MSG_NOTICE_STREET, CommonConstants.MSG_TYPE_STREET_SOMETING, "江湖被人攻打");
			}
		}

	}
}
