package com.dh.game.vo.base;

import java.util.ArrayList;
import java.util.List;

/**
 * 黄钻礼包信息
 * 
 * @author RickYu
 * 
 */
public class YellowRewardVO {
	private int id; // 当type为４时　　id-400＝　等级
	private int type; // 1 黄钻每日礼包 2年费黄钻每日礼包 3黄钻新手礼包 4新手成长礼包
	private int reward_type1;
	private int content1;
	private int number1;
	private int reward_type2;
	private int content2;
	private int number2;
	private int reward_type3;
	private int content3;
	private int number3;

	private List<Reward> rewards = new ArrayList<Reward>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getReward_type1() {
		return reward_type1;
	}

	public void setReward_type1(int reward_type1) {
		this.reward_type1 = reward_type1;
	}

	public int getContent1() {
		return content1;
	}

	public void setContent1(int content1) {
		this.content1 = content1;
	}

	public int getNumber1() {
		return number1;
	}

	public void setNumber1(int number1) {
		this.number1 = number1;
	}

	public int getReward_type2() {
		return reward_type2;
	}

	public void setReward_type2(int reward_type2) {
		this.reward_type2 = reward_type2;
	}

	public int getContent2() {
		return content2;
	}

	public void setContent2(int content2) {
		this.content2 = content2;
	}

	public int getNumber2() {
		return number2;
	}

	public void setNumber2(int number2) {
		this.number2 = number2;
	}

	public int getReward_type3() {
		return reward_type3;
	}

	public void setReward_type3(int reward_type3) {
		this.reward_type3 = reward_type3;
	}

	public int getContent3() {
		return content3;
	}

	public void setContent3(int content3) {
		this.content3 = content3;
	}

	public int getNumber3() {
		return number3;
	}

	public void setNumber3(int number3) {
		this.number3 = number3;
	}

	public List<Reward> getRewards() {
		return rewards;
	}

	public void setRewards(List<Reward> rewards) {
		this.rewards = rewards;
	}

}
