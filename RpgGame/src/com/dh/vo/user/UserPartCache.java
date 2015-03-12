package com.dh.vo.user;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.game.vo.user.PlayerVO;

public class UserPartCache {
	private int playerId;//
	private AtomicInteger count;// 引用计数
	private PlayerVO playerVO;
	private List<PlayerHeroVO> heros;

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public AtomicInteger getCount() {
		return count;
	}

	public void setCount(AtomicInteger count) {
		this.count = count;
	}

	public PlayerVO getPlayerVO() {
		return playerVO;
	}

	public void setPlayerVO(PlayerVO playerVO) {
		this.playerVO = playerVO;
	}

	public List<PlayerHeroVO> getHeros() {
		return heros;
	}

	public void setHeros(List<PlayerHeroVO> heros) {
		this.heros = heros;
	}

}
