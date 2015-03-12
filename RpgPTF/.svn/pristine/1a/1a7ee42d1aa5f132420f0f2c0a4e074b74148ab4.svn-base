package com.dh.game.vo.user;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.game.vo.user.PlayerVO;

/**
 * @author dingqu-pc100 部分玩家数据,增加引用计数功能,如果没有引用则会删除
 */
public class UserPartCache {
	private int playerId;//
	private AtomicInteger count;// 引用计数
	private int lifeTime;//
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

	public int getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}

}
