package com.dh.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.Cache.RedisMap;
import com.dh.dao.PlayerFriendMapper;
import com.dh.dao.PlayerMapper;
import com.dh.game.vo.friend.FriendProto.FriendType;
import com.dh.game.vo.user.PlayerArenaVO;
import com.dh.game.vo.user.PlayerFriendVO;
import com.dh.game.vo.user.PlayerVO;
import com.dh.sqlexe.SqlSaveThread;
import com.dh.util.DateUtil;
import com.dh.util.SqlBuild;
import com.dh.vo.user.UserCached;
import com.dh.vo.user.UserFriend;

@Component
public class FriendService {
	@Resource
	private PlayerFriendMapper playerFriendMapper;
	@Resource
	private PlayerMapper playerMapper;
	@Resource
	private SqlSaveThread saveThread;
	@Resource
	private SqlBuild sqlBuild;

	public void LoadFriendList(UserCached userCached) {
		List<PlayerFriendVO> friends = playerFriendMapper.getFriendList(userCached.getPlayerId());
		UserFriend userFriend = userCached.getUserFriend();
		for (PlayerFriendVO playerFriendVO : friends) {
			playerFriendVO.setPlayerVO(RedisMap.getPlayerVObyId(playerFriendVO.getOtherId()));
			PlayerArenaVO arenaVO = ArenaService.getPlayerArenaVOFromRedis(playerFriendVO.getOtherId() + "");
			if (arenaVO != null) {
				playerFriendVO.setRank(arenaVO.getOrdernum());
			}

			int type = playerFriendVO.getType();
			if (type == FriendType.BLACK_VALUE) {
				userFriend.getBlacks().add(playerFriendVO);
			} else if (type == FriendType.FRIEND_VALUE) {
				userFriend.getFriends().add(playerFriendVO);
			}
		}
	}

	public void delFriend(int otherId, UserFriend userFriend, boolean isBlack) {
		if (isBlack) {
			for (PlayerFriendVO playerFriendVO : userFriend.getBlacks()) {
				if (playerFriendVO.getOtherId() == otherId) {
					saveThread.putSql(playerFriendVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerFriendMapper.delFriend", playerFriendVO));
					userFriend.getBlacks().remove(playerFriendVO);
					return;
				}
			}
		} else {
			for (PlayerFriendVO playerFriendVO : userFriend.getFriends()) {
				if (playerFriendVO.getOtherId() == otherId) {
					saveThread.putSql(playerFriendVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerFriendMapper.delFriend", playerFriendVO));
					userFriend.getFriends().remove(playerFriendVO);
					return;
				}
			}
		}
	}

	public PlayerFriendVO AddFriend(int type, int otherId, int playerId) {
		PlayerFriendVO playerFriendVO = new PlayerFriendVO();
		playerFriendVO.setFrDate(DateUtil.getNow());
		playerFriendVO.setOtherId(otherId);
		playerFriendVO.setPlayerId(playerId);
		PlayerArenaVO arenaVO = ArenaService.getPlayerArenaVOFromRedis(otherId + "");
		playerFriendVO.setRank(arenaVO == null ? 0 : arenaVO.getOrdernum());
		playerFriendVO.setType(type);
		PlayerVO playerVO = RedisMap.getPlayerVObyId(otherId);
		playerFriendVO.setPlayerVO(playerVO);
		playerFriendVO.setName(playerVO.getName());
		saveThread.putSql(playerFriendVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerFriendMapper.addFriend", playerFriendVO));
		return playerFriendVO;
	}
}
