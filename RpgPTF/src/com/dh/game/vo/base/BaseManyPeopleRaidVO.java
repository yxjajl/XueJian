package com.dh.game.vo.base;

//多人副本
public class BaseManyPeopleRaidVO {
	// 副本id 副本名称 开启等级 副本奖励 所需竞技值 成员上限

	private int raidid;
	private String name;
	private int req_level;
	private int reward;
	private int integral;// 所需竞技值
	private int member;

	public int getRaidid() {
		return raidid;
	}

	public void setRaidid(int raidid) {
		this.raidid = raidid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getReq_level() {
		return req_level;
	}

	public void setReq_level(int req_level) {
		this.req_level = req_level;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public int getMember() {
		return member;
	}

	public void setMember(int member) {
		this.member = member;
	}

}
