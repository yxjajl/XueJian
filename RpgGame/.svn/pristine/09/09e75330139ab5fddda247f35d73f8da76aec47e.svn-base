package com.dh.vo.user;

import java.util.ArrayList;
import java.util.List;

import com.dh.game.vo.user.ActivityVO;
import com.dh.game.vo.user.PlayerActyVO;
import com.dh.game.vo.user.PlayerBossVO;
import com.dh.game.vo.user.PlayerDymicGiftVO;
import com.dh.game.vo.user.PlayerWelfareVO;

public class UserActivity implements IClear {
	// public final static int SLIDE_NUM = 0x;

	private PlayerWelfareVO playerWelfareVO; // 福利大厅
	private List<PlayerDymicGiftVO> playerDymicGiftList = new ArrayList<PlayerDymicGiftVO>(); // 已领取动态礼包
	private PlayerActyVO playerActyVO;

	private List<ActivityVO> activities = new ArrayList<ActivityVO>();
	private PlayerBossVO playerBossVO;

	public PlayerDymicGiftVO findDymicGift(int id, int state, int vip) {
		for (PlayerDymicGiftVO playerDymicGiftVO : playerDymicGiftList) {
			if (playerDymicGiftVO.getGifPlayerId() == id && playerDymicGiftVO.getVip() == vip && playerDymicGiftVO.getState() == state) {
				return playerDymicGiftVO;
			}
		}
		return null;
	}

	public PlayerWelfareVO getPlayerWelfareVO() {
		return playerWelfareVO;
	}

	public void setPlayerWelfareVO(PlayerWelfareVO playerWelfareVO) {
		this.playerWelfareVO = playerWelfareVO;
	}

	public List<PlayerDymicGiftVO> getPlayerDymicGiftList() {
		return playerDymicGiftList;
	}

	public void setPlayerDymicGiftList(List<PlayerDymicGiftVO> playerDymicGiftList) {
		this.playerDymicGiftList = playerDymicGiftList;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		playerDymicGiftList.clear();
		playerDymicGiftList = null;
	}

	public List<ActivityVO> getActivities() {
		return activities;
	}

	public void setActivities(List<ActivityVO> activities) {
		this.activities = activities;
	}

	public PlayerBossVO getPlayerBossVO() {
		return playerBossVO;
	}

	public void setPlayerBossVO(PlayerBossVO playerBossVO) {
		this.playerBossVO = playerBossVO;
	}

	public PlayerActyVO getPlayerActyVO() {
		return playerActyVO;
	}

	public void setPlayerActyVO(PlayerActyVO playerActyVO) {
		this.playerActyVO = playerActyVO;
	}

}
