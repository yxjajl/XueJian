package com.dh.vo.user;

import java.util.ArrayList;
import java.util.List;

import com.dh.game.vo.base.Reward;

public class UserReward implements IClear {
	private int spReward = 0; // 碎片奖励
	private List<Reward> prepareReward;// 翻牌奖励
	private List<Reward> myReward = new ArrayList<Reward>(); // 我的奖励

	public List<Reward> getPrepareReward() {
		return prepareReward;
	}

	public void setPrepareReward(List<Reward> prepareReward) {
		this.prepareReward = prepareReward;
	}

	public List<Reward> getMyReward() {
		return myReward;
	}

	public void setMyReward(List<Reward> myReward) {
		this.myReward = myReward;
	}

	public int getSpReward() {
		return spReward;
	}

	public void setSpReward(int spReward) {
		this.spReward = spReward;
	}

	@Override
	public void clear() {
		if (prepareReward != null) {
			prepareReward.clear();
		}
		prepareReward = null;

		if (myReward != null) {
			myReward.clear();
		}
		
	}

}
