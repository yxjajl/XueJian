package com.dh.game.vo.user;

import java.util.Date;

public class PlayerEliteRaidVO {
	private int playerId;
	private int raidid;
	private int score;
	private int times;
	private Date passdate;
	private int fivereward;

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getRaidid() {
		return raidid;
	}

	public void setRaidid(int raidid) {
		this.raidid = raidid;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public Date getPassdate() {
		return passdate;
	}

	public void setPassdate(Date passdate) {
		this.passdate = passdate;
	}

	public int getFivereward() {
		return fivereward;
	}

	public void setFivereward(int fivereward) {
		this.fivereward = fivereward;
	}

}
