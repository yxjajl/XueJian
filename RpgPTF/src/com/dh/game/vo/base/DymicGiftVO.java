package com.dh.game.vo.base;

public class DymicGiftVO {
	private int playerId;
	private String nick;
	private int vip;
	private int state;
	private int cgiftGold;
	private int lingiftGold;
	private int num;

	public synchronized int getGiftGold() {
		int value = cgiftGold - lingiftGold;
		lingiftGold = cgiftGold;
		if (value < 0) {
			value = 0;
		}
		num = 0;
		return value;
	}

	public synchronized void addGiftGold(int value) {
		num++;
		cgiftGold += value;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getVip() {
		return vip;
	}

	public void setVip(int vip) {
		this.vip = vip;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getCgiftGold() {
		return cgiftGold;
	}

	public void setCgiftGold(int cgiftGold) {
		this.cgiftGold = cgiftGold;
	}

	public int getLingiftGold() {
		return lingiftGold;
	}

	public void setLingiftGold(int lingiftGold) {
		this.lingiftGold = lingiftGold;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
