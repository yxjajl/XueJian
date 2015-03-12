package com.dh.game.vo.user.log;

import java.util.Date;

public class PlayerAccountLog {
	private int playerId;
	private int currency;
	private int amount;
	private int beAmount;
	private int operateType;
	private Date operateTime;
	private String reason;

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getBeAmount() {
		return beAmount;
	}

	public void setBeAmount(int beAmount) {
		this.beAmount = beAmount;
	}

	public int getOperateType() {
		return operateType;
	}

	public void setOperateType(int operateType) {
		this.operateType = operateType;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
