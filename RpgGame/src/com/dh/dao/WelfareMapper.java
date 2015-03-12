package com.dh.dao;

import java.util.List;

import com.dh.game.vo.base.DymicGiftVO;
import com.dh.game.vo.user.PlayerDymicGiftVO;
import com.dh.game.vo.user.PlayerOpenActVO;
import com.dh.game.vo.user.PlayerWelfareVO;

public interface WelfareMapper {
	public PlayerWelfareVO getPlayerWelfareVO(int playerId);

	public void insertPlayerWelfareVO(PlayerWelfareVO playerWelfareVO);

	public void updatePlayerWelfareVO(PlayerWelfareVO playerWelfareVO);

	// 自定义奖励
	public void addDymicGiftName(DymicGiftVO dymicGiftVO);

	public void updateDymicGiftName(DymicGiftVO dymicGiftVO);

	public List<DymicGiftVO> getAllDymicGiftName();

	//
	public List<PlayerDymicGiftVO> getPlayerDymicGift(int playerId);

	public void updatePlayerDymicGift(PlayerDymicGiftVO playerDymicGiftVO);

	public void insertPlayerDymicGift(PlayerDymicGiftVO playerDymicGiftVO);

	public List<PlayerOpenActVO> getPlayerOpenActVO();

	public void insertPlayerOpenActVO(PlayerOpenActVO playerOpenActVO);

	public void updatePlayerOpenActVO(PlayerOpenActVO playerOpenActVO);

}
