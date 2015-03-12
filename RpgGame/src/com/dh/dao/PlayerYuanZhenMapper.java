package com.dh.dao;

import java.util.List;

import com.dh.game.vo.user.PlayerYuanZhenVO;

public interface PlayerYuanZhenMapper {
	public List<PlayerYuanZhenVO> getPlayerYuanZhenVO(int playerId);

	public void updatePlayerYuanZhenVO(PlayerYuanZhenVO playerYuanZhenVO);
	
	public void insertPlayerYuanZhenVO(PlayerYuanZhenVO playerYuanZhenVO);
	
	public void delPlayerYuanZhenVO(int playerId);
}
