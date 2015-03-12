package com.dh.game.vo.base;

import com.dh.game.vo.hero.HeroProto.RECRUIT_TYPE;

public class BaseHeroRecruitVO {
	private RECRUIT_TYPE type;
	private int price;
	private long cd;
	private int rewardId;

	public RECRUIT_TYPE getType() {
		return type;
	}

	public void setType(RECRUIT_TYPE type) {
		this.type = type;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public long getCd() {
		return cd;
	}

	public void setCd(long cd) {
		this.cd = cd;
	}

	public int getRewardId() {
		return rewardId;
	}

	public void setRewardId(int rewardId) {
		this.rewardId = rewardId;
	}

}
