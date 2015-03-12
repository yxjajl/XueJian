package com.dh.game.vo.user;

public class PlayerWelfareVO {
	private int playerId;
	private int lvrew;
	private int sevrew;
	private int rechargerew;
	private int firstrecharge;

	private int signFreshTime;// 上次刷新时间,一个月刷一次
	private int daySign;
	private int resignCount;// 补签次数
	private int rewardSign;// 已经领取的签到次数,二进制表示

	private int recharge1;
	private int recharge2;
	private int recharge3;
	private int recharge4;
	private int recharge5;
	private int ddhszlzw; // 开服活动　登顶华山和　战力之王

	public static final int SLIDE_NUM = 0x1;

	public int getFirstUnsignDay(int dayOfMonth) {
		for (int i = 0; i <= dayOfMonth; i++) {
			if (!isSign(i)) {
				return i;
			}
		}
		return -1;
	}

	public boolean isRewarded(int count) {
		return ((rewardSign >> count) & SLIDE_NUM) == 1;
	}

	public void reward(int count) {
		rewardSign |= (SLIDE_NUM << count);
	}

	public void sign(int day) {
		daySign |= SLIDE_NUM << day;
	}

	public boolean isSign(int day) {
		return ((daySign >> day) & SLIDE_NUM) == 1;
	}

	public int getPlayerId() {
		return playerId;
	}

	public int getDaySign() {
		return daySign;
	}

	public void setDaySign(int daySign) {
		this.daySign = daySign;
	}

	public int getResignCount() {
		return resignCount;
	}

	public void setResignCount(int resignCount) {
		this.resignCount = resignCount;
	}

	public int getRewardSign() {
		return rewardSign;
	}

	public void setRewardSign(int rewardSign) {
		this.rewardSign = rewardSign;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getLvrew() {
		return lvrew;
	}

	public void setLvrew(int lvrew) {
		this.lvrew = lvrew;
	}

	public int getSevrew() {
		return sevrew;
	}

	public void setSevrew(int sevrew) {
		this.sevrew = sevrew;
	}

	public int getRechargerew() {
		return rechargerew;
	}

	public void setRechargerew(int rechargerew) {
		this.rechargerew = rechargerew;
	}

	public int getFirstrecharge() {
		return firstrecharge;
	}

	public void setFirstrecharge(int firstrecharge) {
		this.firstrecharge = firstrecharge;
	}

	public int getSignFreshTime() {
		return signFreshTime;
	}

	public void setSignFreshTime(int signFreshTime) {
		this.signFreshTime = signFreshTime;
	}

	public static int getSliedeNum() {
		return SLIDE_NUM;
	}

	public void setRecharge1(int recharge1) {
		this.recharge1 = recharge1;
	}

	public void setRecharge2(int recharge2) {
		this.recharge2 = recharge2;
	}

	public void setRecharge3(int recharge3) {
		this.recharge3 = recharge3;
	}

	public void setRecharge4(int recharge4) {
		this.recharge4 = recharge4;
	}

	public void setRecharge5(int recharge5) {
		this.recharge5 = recharge5;
	}

	public int getDdhszlzw() {
		return ddhszlzw;
	}

	public void setDdhszlzw(int ddhszlzw) {
		this.ddhszlzw = ddhszlzw;
	}

	public int getRecharge(int index) {
		switch (index) {
		case 0:
			return this.recharge1;
		case 1:
			return this.recharge2;
		case 2:
			return this.recharge3;
		case 3:
			return this.recharge4;
		case 4:
			return this.recharge5;
		}
		return 0;
	}

	public void setRecharge(int index, int value) {
		switch (index) {
		case 0:
			this.recharge1 = value;
			break;
		case 1:
			this.recharge2 = value;
			break;
		case 2:
			this.recharge3 = value;
			break;
		case 3:
			this.recharge4 = value;
			break;
		case 4:
			this.recharge5 = value;
			break;
		}
	}

}
