package com.dh.game.vo.user;

import java.util.Date;

public class PlayerBossVO {
	private int playerId;
	private int startTime;
	private int process;// 进度
	private Date today;
	private BossLogVO bossLogVO;
	private int hunt;
	private int addtion;
	private String name;

	public int getCountDown() {
		if (startTime == 0) {
			return startTime;
		}
		int now = (int) (System.currentTimeMillis() / 1000);
		int dif = 60 - now + startTime;
		if (dif < 0) {
			startTime = 0;
			return startTime;
		} else {
			return dif;
		}
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getProcess() {
		return process;
	}

	public void setProcess(int process) {
		this.process = process;
	}

	public Date getToday() {
		return today;
	}

	public void setToday(Date today) {
		this.today = today;
	}

	public BossLogVO getBossLogVO() {
		return bossLogVO;
	}

	public void setBossLogVO(BossLogVO bossLogVO) {
		this.bossLogVO = bossLogVO;
	}

	public int getHunt() {
		return hunt;
	}

	public void setHunt(int hunt) {
		this.hunt = hunt;
	}

	public int getAddtion() {
		return addtion;
	}

	public void setAddtion(int addtion) {
		this.addtion = addtion;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
