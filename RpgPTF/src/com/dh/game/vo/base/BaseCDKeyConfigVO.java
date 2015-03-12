package com.dh.game.vo.base;

import java.util.ArrayList;
import java.util.List;

public class BaseCDKeyConfigVO {
	private int id;
	private int type;
	private int item;
	private String keybegin;
	private String describe;
	private List<Reward> rewardList = new ArrayList<Reward>(1);

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

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public String getKeybegin() {
		return keybegin;
	}

	public void setKeybegin(String keybegin) {
		this.keybegin = keybegin;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public List<Reward> getRewardList() {
		return rewardList;
	}

	public void setRewardList(List<Reward> rewardList) {
		this.rewardList = rewardList;
	}

}
