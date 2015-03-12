package com.dh.game.vo.base;

public class BaseEquipStarVO {
	private int cfgId;
	private int reqLevel;
	private int reqMoney;
	private int itemid1;
	private int itemnum1;
	private int itemid2;
	private int itemnum2;
	private int itemid3;
	private int itemnum3;
	private int resCfgId;
	
	private int[] itemid = new int[3];
	private int[] itemnum =  new int[3];

	public int getCfgId() {
		return cfgId;
	}

	public void setCfgId(int cfgId) {
		this.cfgId = cfgId;
	}

	public int getReqLevel() {
		return reqLevel;
	}

	public void setReqLevel(int reqLevel) {
		this.reqLevel = reqLevel;
	}

	public int getReqMoney() {
		return reqMoney;
	}

	public void setReqMoney(int reqMoney) {
		this.reqMoney = reqMoney;
	}

	public int getItemid1() {
		return itemid1;
	}

	public void setItemid1(int itemid1) {
		this.itemid1 = itemid1;
	}

	public int getItemnum1() {
		return itemnum1;
	}

	public void setItemnum1(int itemnum1) {
		this.itemnum1 = itemnum1;
	}

	public int getItemid2() {
		return itemid2;
	}

	public void setItemid2(int itemid2) {
		this.itemid2 = itemid2;
	}

	public int getItemnum2() {
		return itemnum2;
	}

	public void setItemnum2(int itemnum2) {
		this.itemnum2 = itemnum2;
	}

	public int getItemid3() {
		return itemid3;
	}

	public void setItemid3(int itemid3) {
		this.itemid3 = itemid3;
	}

	public int getItemnum3() {
		return itemnum3;
	}

	public void setItemnum3(int itemnum3) {
		this.itemnum3 = itemnum3;
	}

	public int getResCfgId() {
		return resCfgId;
	}

	public void setResCfgId(int resCfgId) {
		this.resCfgId = resCfgId;
	}

	public int[] getItemid() {
		return itemid;
	}

	public void setItemid(int[] itemid) {
		this.itemid = itemid;
	}

	public int[] getItemnum() {
		return itemnum;
	}

	public void setItemnum(int[] itemnum) {
		this.itemnum = itemnum;
	}

}
