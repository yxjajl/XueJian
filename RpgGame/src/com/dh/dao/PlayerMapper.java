package com.dh.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.dh.game.vo.user.PlayerVO;

public interface PlayerMapper {
	public List<PlayerVO> getAllPlayerVO();

	public PlayerVO getPlayerVOById(int playerId);

	public void updatePlayer(PlayerVO playerVO);

	public void updatePlayerNick(PlayerVO playerVO);

	// public void registerPlayer(HashMap<String, Object> hashMap);

	public int getPlayerIdByAccount(String username);

	public void registerPlayer(PlayerVO playerVO);

	public void updatePlayerOnline(PlayerVO playerVO);

	public void updatePlayerGM(PlayerVO playerVO);

	public int getOnlineNum();

	public void updatePlayerVOForLegion(PlayerVO playerVO);

	public void initAllPlayerOnline();

}
