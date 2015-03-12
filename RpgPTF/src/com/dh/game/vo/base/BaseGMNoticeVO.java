package com.dh.game.vo.base;

public class BaseGMNoticeVO {
	private int id;
	private int count;// 次数为0则会移除该消息
	
	private int period;// 频率,分钟
	private int sumMin;// 累计分钟
	private String msg;// 消息内容

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getSumMin() {
		return sumMin;
	}

	public void setSumMin(int sumMin) {
		this.sumMin = sumMin;
	}
}
