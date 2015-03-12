package com.dh.processor;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.Cache.RedisMap;
import com.dh.Cache.ServerHandler;
import com.dh.constants.FriendConstant;
import com.dh.game.vo.BaseProto.FriendInfo;
import com.dh.game.vo.friend.FriendProto.FriendListResp;
import com.dh.game.vo.friend.FriendProto.FriendModResp;
import com.dh.game.vo.friend.FriendProto.FriendModResult;
import com.dh.game.vo.friend.FriendProto.FriendModType;
import com.dh.game.vo.user.PlayerArenaVO;
import com.dh.game.vo.user.PlayerFriendVO;
import com.dh.netty.NettyMessageVO;
import com.dh.service.ArenaService;
import com.dh.service.FriendService;
import com.dh.service.PlayerService;
import com.dh.sync.LockConstant;
import com.dh.sync.SyncLock;
import com.dh.sync.SyncUtil;
import com.dh.util.VOUtil;
import com.dh.vo.user.UserCached;
import com.dh.vo.user.UserFriend;

@Component
public class FriendProcessor {
	@Resource
	private PlayerService playerService;
	@Resource
	private ArenaService arenaService;
	@Resource
	private FriendService friendService;

	public void getFriendList(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserFriend userFriend = userCached.getUserFriend();
		FriendListResp.Builder resp = FriendListResp.newBuilder();
		for (PlayerFriendVO playerFriendVO : userFriend.getFriends()) {
			if (playerFriendVO.getPlayerVO() == null) {
				playerFriendVO.setPlayerVO(RedisMap.getPlayerVObyId(playerFriendVO.getOtherId()));
				PlayerArenaVO arenaVO = ArenaService.getPlayerArenaVOFromRedis(playerFriendVO.getOtherId() + "");
				playerFriendVO.setRank(arenaVO == null ? 0 : arenaVO.getOrdernum());
			}
			resp.addFriends(VOUtil.packFriendInfo(playerFriendVO));
		}
		for (PlayerFriendVO playerFriendVO : userFriend.getBlacks()) {
			if (playerFriendVO.getPlayerVO() == null) {
				playerFriendVO.setPlayerVO(RedisMap.getPlayerVObyId(playerFriendVO.getOtherId()));
				PlayerArenaVO arenaVO = ArenaService.getPlayerArenaVOFromRedis(playerFriendVO.getOtherId() + "");
				playerFriendVO.setRank(arenaVO == null ? 0 : arenaVO.getOrdernum());
			}
			resp.addBlacks(VOUtil.packFriendInfo(playerFriendVO));
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	public void friendMod(com.dh.game.vo.friend.FriendProto.FriendModReq.Builder req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserFriend userFriend = userCached.getUserFriend();
		int otherId = req.getPlayerId();
		String name = req.getName();

		if (otherId == 0 && name != null && !name.isEmpty()) {
			otherId = PlayerService.getPlayerVOByNick(name);
		}
		FriendModResp.Builder resp = FriendModResp.newBuilder();
		resp.setFriendModResult(FriendModResult.FRIEND_MOD_SUCC);
		if (otherId == 0) {
			resp.setFriendModResult(FriendModResult.PLAYER_NOT_EXIST);
		} else {
			FriendModType modType = req.getFriendModType();
			PlayerFriendVO playerFriendVO = null;
			boolean isRemove = false;
			SyncLock syncLock = SyncUtil.getInstance().getLock(LockConstant.LOCK_PLAYERVO + otherId);
			synchronized (syncLock) {
				if (modType == FriendModType.ADD) {
					if (userFriend.getFriends().size() >= FriendConstant.MAX_FRIEND_NUM) {
						resp.setFriendModResult(FriendModResult.FRIENDS_FULL);
					} else if (userFriend.isInFriend(false, otherId)) {
						resp.setFriendModResult(FriendModResult.FRIEND_EXIST);
					} else {
						playerFriendVO = friendService.AddFriend(modType.getNumber(), otherId, playerId);
						isRemove = userFriend.removeOldRelation(true, otherId);
						userFriend.getFriends().add(playerFriendVO);
						resp.setFriendModType(isRemove ? FriendModType.ADDFRIEND_DELBLACK : modType);
					}
				} else if (modType == FriendModType.DEL) {
					friendService.delFriend(otherId, userFriend, false);
					resp.setFriendModType(modType);
				} else if (modType == FriendModType.TO_BLACK) {
					if (userFriend.getFriends().size() >= FriendConstant.MAX_BLACK_NUM) {
						resp.setFriendModResult(FriendModResult.FRIENDS_FULL);
					} else if (userFriend.isInFriend(true, otherId)) {
						resp.setFriendModResult(FriendModResult.FRIEND_EXIST);
					} else {
						playerFriendVO = friendService.AddFriend(modType.getNumber(), otherId, playerId);
						isRemove = userFriend.removeOldRelation(false, otherId);
						userFriend.getBlacks().add(playerFriendVO);
						resp.setFriendModType(isRemove ? FriendModType.TO_BLACK_DELFRIEND : modType);
					}
				} else if (modType == FriendModType.OUT_BLACK) {
					friendService.delFriend(otherId, userFriend, true);
					resp.setFriendModType(modType);
				}
			}
			resp.setFriendInfo(playerFriendVO == null ? FriendInfo.newBuilder().setPlayerId(otherId).build() : VOUtil.packFriendInfo(playerFriendVO));
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}
}
