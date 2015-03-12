package com.dh.game.vo.user;

public class PlayerAccountVO {
	private int playerId;
	private int gmMoney; // 游戏币
	private int gmRmb; // 增值币
	private int exploit; // 功勋
	private int power; // 行动力
	private int knapsack; // 背包格子数
	private int knapsack2;
	private int lifeTime;// 体力加1 剩余时间
	private int pvp;
	private int expc;
	private int score;// 积分
	private int hisrecharge;
	private int giftgod;// 礼金

	public int getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getGmMoney() {
		return gmMoney;
	}

	public void setGmMoney(int gmMoney) {
		this.gmMoney = gmMoney;
	}

	public int getGmRmb() {
		return gmRmb;
	}

	public void setGmRmb(int gmRmb) {
		this.gmRmb = gmRmb;
	}

	public int getExploit() {
		return exploit;
	}

	public void setExploit(int exploit) {
		this.exploit = exploit;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getKnapsack() {
		return knapsack;
	}

	public void setKnapsack(int knapsack) {
		this.knapsack = knapsack;
	}

	public int getPvp() {
		return pvp;
	}

	public void setPvp(int pvp) {
		this.pvp = pvp;
	}

	public int getExpc() {
		return expc;
	}

	public void setExpc(int expc) {
		this.expc = expc;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getKnapsack2() {
		return knapsack2;
	}

	public void setKnapsack2(int knapsack2) {
		this.knapsack2 = knapsack2;
	}

	public int getHisrecharge() {
		return hisrecharge;
	}

	public void setHisrecharge(int hisrecharge) {
		this.hisrecharge = hisrecharge;
	}

	public int getGiftgod() {
		return giftgod;
	}

	public void setGiftgod(int giftgod) {
		this.giftgod = giftgod;
	}

}
