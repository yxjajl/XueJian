package com.dh.game.vo.user;

/**
 * 江湖保卫日志
 * 
 */
public class StreetDefendLogVO implements Comparable<StreetDefendLogVO> {
	private int id;
	private int playerId;
	private int enemyId;
	private String enemyName;
	private int atkTime;
	private int resId;// 攻击位置{0:总部,1:铸铁坊,2:树林,3:粮仓}
	private int isSucc;// 战斗结果[1 成功]
	private String rewards;// 使用与邮件奖励相同方式
	private int expc;// 扣经验
	private int money;// 扣钱
	private int isAtk;// 是否是攻击方,如果为1则为攻击方
	private int isReward;

	public int getEnemyId() {
		return enemyId;
	}

	public void setEnemyId(int enemyId) {
		this.enemyId = enemyId;
	}

	public String getEnemyName() {
		return enemyName;
	}

	public void setEnemyName(String enemyName) {
		this.enemyName = enemyName;
	}

	public int getAtkTime() {
		return atkTime;
	}

	public void setAtkTime(int atkTime) {
		this.atkTime = atkTime;
	}

	public int getIsSucc() {
		return isSucc;
	}

	public void setIsSucc(int isSucc) {
		this.isSucc = isSucc;
	}

	public String getRewards() {
		return rewards;
	}

	public void setRewards(String rewards) {
		this.rewards = rewards;
	}

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	@Override
	public int compareTo(StreetDefendLogVO arg0) {
		return arg0.getAtkTime() - this.getAtkTime();
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getExpc() {
		return expc;
	}

	public void setExpc(int expc) {
		this.expc = expc;
	}

	public int getIsAtk() {
		return isAtk;
	}

	public void setIsAtk(int isAtk) {
		this.isAtk = isAtk;
	}

	public int getIsReward() {
		return isReward;
	}

	public void setIsReward(int isReward) {
		this.isReward = isReward;
	}

}
