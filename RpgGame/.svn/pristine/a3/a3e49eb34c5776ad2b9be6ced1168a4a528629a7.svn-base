package com.dh.vo.user;

import java.util.Date;

import com.dh.game.vo.hero.HeroProto.RECRUIT_TYPE;
import com.dh.game.vo.user.PlayerBuildVO;
import com.dh.game.vo.user.PlayerTimerVO;

public class UserTimer implements IClear {

	private PlayerTimerVO playerTimerVO;
	private PlayerBuildVO playerBuildVO;

	public Date getTimerDate(RECRUIT_TYPE type) {
		switch (type) {
		case TYPE_MONEY:
			return playerTimerVO.getZmD1();
		case TYPE_RMB:
			return playerTimerVO.getZmD2();
		case TYPE_RMB10:
			return playerTimerVO.getZmD3();
		case TYPE_MONEY10:
			return playerTimerVO.getZmD4();
		}
		return null;
	}

	@Override
	public void clear() {

	}

	public PlayerTimerVO getPlayerTimerVO() {
		return playerTimerVO;
	}

	public void setPlayerTimerVO(PlayerTimerVO playerTimerVO) {
		this.playerTimerVO = playerTimerVO;
	}

	public PlayerBuildVO getPlayerBuildVO() {
		return playerBuildVO;
	}

	public void setPlayerBuildVO(PlayerBuildVO playerBuildVO) {
		this.playerBuildVO = playerBuildVO;
	}

}
