package com.dh.game.vo.base;

import java.util.List;

/**
 * 宝箱
 * 
 * @author dingqu-pc100
 * 
 */
public class BaseBoxVO {
	private int id;
	private int url;
	private int level;
	private int rewardid;
	private int openlevel;
	private int star;
	private List<Reward> rewards;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUrl() {
		return url;
	}

	public void setUrl(int url) {
		this.url = url;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRewardid() {
		return rewardid;
	}

	public void setRewardid(int rewardid) {
		this.rewardid = rewardid;
	}

	public int getOpenlevel() {
		return openlevel;
	}

	public void setOpenlevel(int openlevel) {
		this.openlevel = openlevel;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public List<Reward> getRewards() {
		return rewards;
	}

	public void setRewards(List<Reward> rewards) {
		this.rewards = rewards;
	}
}
