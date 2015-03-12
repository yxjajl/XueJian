package com.dh.game.vo.base;

/**
 * 单笔充值奖励
 * 
 * @author Administrator
 * 
 */
public class BaseOneRechargeVO {
	private int rechargenumber;
	private int type;
	private int content;
	private int number;

	public int getRechargenumber() {
		return rechargenumber;
	}

	public void setRechargenumber(int rechargenumber) {
		this.rechargenumber = rechargenumber;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getContent() {
		return content;
	}

	public void setContent(int content) {
		this.content = content;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
