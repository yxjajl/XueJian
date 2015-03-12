package com.dh.vo.user;

import java.util.ArrayList;
import java.util.List;

import com.dh.game.vo.user.PlayerKnapsackVO;

/**
 * 背包
 * 
 * @author Administrator
 * 
 */
public class UserKnapsack implements IClear{
	public transient int maxKnapsackId = -1; // 最大的物品id
	private List<PlayerKnapsackVO> knapsackList = new ArrayList<PlayerKnapsackVO>();

	public List<PlayerKnapsackVO> getKnapsackList() {
		return knapsackList;
	}

	public void setKnapsackList(List<PlayerKnapsackVO> knapsackList) {
		this.knapsackList = knapsackList;
	}

	public int getMaxKnapsackId() {
		return maxKnapsackId;
	}

	public void setMaxKnapsackId(int maxKnapsackId) {
		this.maxKnapsackId = maxKnapsackId;
	}

	public void clear() {
		maxKnapsackId = -1;
		if (knapsackList != null) {
			knapsackList.clear();
		}
		knapsackList = null;
	}
}
