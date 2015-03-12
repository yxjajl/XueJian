package com.dh.vo.user;

import java.util.ArrayList;
import java.util.List;

import com.dh.game.vo.user.PlayerHeroDefVO;
import com.dh.game.vo.user.PlayerYuanZhenVO;

public class UserYuanZhen implements IClear {
	private List<PlayerYuanZhenVO> yzList = new ArrayList<PlayerYuanZhenVO>();

	private List<PlayerHeroDefVO> playerHeroDefList = new ArrayList<PlayerHeroDefVO>(); //

	private int lastIndex = 1;

	public PlayerYuanZhenVO findPlayerYuanZhenVO(int index) {
		if (index < 1 || index > yzList.size() ) {
			return null;
		}
		return yzList.get(index-1);
	}

	@Override
	public void clear() {
		yzList.clear();
	}

	public List<PlayerYuanZhenVO> getYzList() {
		return yzList;
	}

	public void setYzList(List<PlayerYuanZhenVO> yzList) {
		this.yzList = yzList;
	}

	public List<PlayerHeroDefVO> getPlayerHeroDefList() {
		return playerHeroDefList;
	}

	public void setPlayerHeroDefList(List<PlayerHeroDefVO> playerHeroDefList) {
		this.playerHeroDefList = playerHeroDefList;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public PlayerHeroDefVO findPlayerHeroDefVO(int id) {
		for (PlayerHeroDefVO playerHeroDefVO : playerHeroDefList) {
			if (playerHeroDefVO.getId() == id) {
				return playerHeroDefVO;
			}
		}
		return null;
	}

}
