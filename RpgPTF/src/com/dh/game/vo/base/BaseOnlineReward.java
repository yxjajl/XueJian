package com.dh.game.vo.base;

import java.util.List;

public class BaseOnlineReward {
	private int id;
	private int time;
	private int rewardid;
	private List<Reward> rewards;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getRewardid() {
		return rewardid;
	}

	public void setRewardid(int rewardid) {
		this.rewardid = rewardid;
	}

	public List<Reward> getRewards() {
		return rewards;
	}

	public void setRewards(List<Reward> rewards) {
		this.rewards = rewards;
	}

}
