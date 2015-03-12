package com.dh.dao;

import java.util.List;

import com.dh.game.vo.user.PlayerFriendVO;

public interface PlayerFriendMapper {
	public List<PlayerFriendVO> getFriendList(int playerId);

	public void addFriend(PlayerFriendVO friendVO);

	public void delFriend(PlayerFriendVO friendVO);
}
