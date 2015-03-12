package com.dh.game.vo.user;

import java.util.Date;

public class PlayerHeroHangVO {
	private int heroHangId;
	private int playerId;
	private int heroId;
	private short isHang;
	private Date beginTime;
	private Date endTime;

	public int getHeroHangId() {
		return heroHangId;
	}

	public void setHeroHangId(int heroHangId) {
		this.heroHangId = heroHangId;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public short getIsHang() {
		return isHang;
	}

	public void setIsHang(short isHang) {
		this.isHang = isHang;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
