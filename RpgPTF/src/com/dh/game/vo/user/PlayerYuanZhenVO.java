package com.dh.game.vo.user;

//t_player_yuanzhen
public class PlayerYuanZhenVO {
	private int playerId;
	private int index; // 序号
	private int otherId; // 对手id
	private byte status; // 是否过关

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getOtherId() {
		return otherId;
	}

	public void setOtherId(int otherId) {
		this.otherId = otherId;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

}
