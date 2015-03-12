package com.dh.dao;

import java.util.List;

import com.dh.game.vo.user.PlayerHeroHangVO;

public interface PlayerHeroHangMapper {
	List<PlayerHeroHangVO> getPlayerHang(int playerId);

	public void updatePlayerHang(PlayerHeroHangVO playerHeroHangVO);

	public void addPlayerHang(PlayerHeroHangVO playerHeroHangVO);

}
