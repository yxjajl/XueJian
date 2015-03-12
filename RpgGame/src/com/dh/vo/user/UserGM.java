package com.dh.vo.user;

import com.dh.game.vo.user.PlayerGMVO;

public class UserGM implements IClear {

	private PlayerGMVO playerGMVO = new PlayerGMVO();

	public PlayerGMVO getPlayerGMVO() {
		return playerGMVO;
	}

	public void setPlayerGMVO(PlayerGMVO playerGMVO) {
		this.playerGMVO = playerGMVO;
	}

	@Override
	public void clear() {

	}

}
