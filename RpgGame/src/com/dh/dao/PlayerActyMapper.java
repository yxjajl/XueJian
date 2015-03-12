package com.dh.dao;

import com.dh.game.vo.user.PlayerActyVO;

public interface PlayerActyMapper {
	public void updatePlayerActy(PlayerActyVO playerActyVO);

	public PlayerActyVO getPlayerActy(int playerId);

	public void insertPlayerActy(PlayerActyVO playerActyVO);
}
