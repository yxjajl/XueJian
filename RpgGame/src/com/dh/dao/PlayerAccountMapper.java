package com.dh.dao;

import com.dh.game.vo.user.PlayerAccountVO;

public interface PlayerAccountMapper {
	// @Select("set @r = 0")
	public PlayerAccountVO getPlayerAccount(int playerid);

	public PlayerAccountVO insertPlayerAccountVO(PlayerAccountVO playerAccountVO);

	public void updatePlayerAccountMoney(PlayerAccountVO playerAccountVO);

	public void updatePlayerAccountPower(PlayerAccountVO playerAccountVO);

	public void updatePlayerAccountKnapsack(PlayerAccountVO playerAccountVO);

	public void updatePlayerAccount(PlayerAccountVO playerAccountVO);

	public void updatePlayerAccountPVP(PlayerAccountVO playerAccountVO);

	public void updatePlayerAccountExpc(PlayerAccountVO playerAccountVO);

	public void updatePlayerAccountScore(PlayerAccountVO playerAccountVO);

}
