package com.dh.game.vo.base;

import java.util.Date;

public class BaseActivityVO {
	private int id;
	private String name;
	private short dayreset;
	private short type;
	private short time_type;
	private String weekly;
	private Date begin_time;
	private Date end_time;
	private String dec;
	private short position;
	private short state;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getDayreset() {
		return dayreset;
	}

	public void setDayreset(short dayreset) {
		this.dayreset = dayreset;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public short getTime_type() {
		return time_type;
	}

	public void setTime_type(short time_type) {
		this.time_type = time_type;
	}

	public String getWeekly() {
		return weekly;
	}

	public void setWeekly(String weekly) {
		this.weekly = weekly;
	}

	public Date getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(Date begin_time) {
		this.begin_time = begin_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}

	public short getPosition() {
		return position;
	}

	public void setPosition(short position) {
		this.position = position;
	}

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

}
