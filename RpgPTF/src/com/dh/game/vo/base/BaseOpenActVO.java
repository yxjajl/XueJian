package com.dh.game.vo.base;

/**
 * 开服活动配置
 * 
 * @author Administrator
 * 
 */
public class BaseOpenActVO {
	private int num; 
	private int stage; //阶段
	private int rewardtype;
	private int content;
	private int number;
	private int lowerlimit;
	private int superiorlimit;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public int getRewardtype() {
		return rewardtype;
	}

	public void setRewardtype(int rewardtype) {
		this.rewardtype = rewardtype;
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

	public int getLowerlimit() {
		return lowerlimit;
	}

	public void setLowerlimit(int lowerlimit) {
		this.lowerlimit = lowerlimit;
	}

	public int getSuperiorlimit() {
		return superiorlimit;
	}

	public void setSuperiorlimit(int superiorlimit) {
		this.superiorlimit = superiorlimit;
	}

}
