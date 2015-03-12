package com.dh.vo.gm;

import java.util.Date;

public class GMIOVO {
	private int playerid;
	private int io;
	private int usage;// 用途
	private int value;// 当前值
	private int type;// 影响类型,具体参见PLAYER_PROPERTY
	private int content;
	private int num;//
	private String name;// 名字
	private int price;// 道具单价
	private Date optdate;

	public GMIOVO(int playerid, int io, int usage, int value, int type, int content, int num, String name, int price) {
		this.playerid = playerid;
		this.io = io;
		this.usage = usage;
		this.value = value;
		this.type = type;
		this.content = content;
		this.num = num;
		this.name = name;
		this.price = price;
		this.optdate = new Date();
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public GMIOVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getPlayerid() {
		return playerid;
	}

	public void setPlayerid(int playerid) {
		this.playerid = playerid;
	}

	public int getIo() {
		return io;
	}

	public void setIo(int io) {
		this.io = io;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getContent() {
		return content;
	}

	public void setContent(int content) {
		this.content = content;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getUsage() {
		return usage;
	}

	public void setUsage(int usage) {
		this.usage = usage;
	}

	public Date getOptdate() {
		return optdate;
	}

	public void setOptdate(Date optdate) {
		this.optdate = optdate;
	}

}
