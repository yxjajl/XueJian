package com.dh.game.vo.user;

import java.util.ArrayList;
import java.util.List;

import com.dh.game.vo.base.BaseResourceVO;
import com.dh.game.vo.base.Reward;

/**
 * 江湖资源点
 * 
 * @author dingqu-pc100
 * 
 */
public class StreetResVO {
	private transient BaseResourceVO baseResourceVO;
	private int id;// 格子id
	private int cfgId;// 配置表id
	private int status;// [状态{0:产出状态,1:正在战斗,2:战斗失败,3:战斗成功,4:产出时间结束}]

	private int playerId;
	private int beginTime;
	private int calTime;// 上次结算时间
	private int cfgTime;// 上次配置时间

	private int fzId;
	private String machineLine;

	private transient String rewards;// 已经生成但未领取
	private transient List<Reward> items = new ArrayList<Reward>();// str转换后奖励
	private transient List<Reward> tempItems = new ArrayList<Reward>();// 临时奖励
	private int whosyourdaddy;// 是否开启无敌,放置开启时间

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(int beginTime) {
		this.beginTime = beginTime;
	}

	public int getCalTime() {
		return calTime;
	}

	public void setCalTime(int calTime) {
		this.calTime = calTime;
	}

	public BaseResourceVO getBaseResourceVO() {
		return baseResourceVO;
	}

	public void setBaseResourceVO(BaseResourceVO baseResourceVO) {
		this.baseResourceVO = baseResourceVO;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMachineLine() {
		return machineLine;
	}

	public void setMachineLine(String machineLine) {
		this.machineLine = machineLine;
	}

	public int getFzId() {
		return fzId;
	}

	public void setFzId(int fzId) {
		this.fzId = fzId;
	}

	public String getRewards() {
		return rewards;
	}

	public void setRewards(String rewards) {
		this.rewards = rewards;
	}

	public int getCfgId() {
		return cfgId;
	}

	public void setCfgId(int cfgId) {
		this.cfgId = cfgId;
	}

	public List<Reward> getItems() {
		return items;
	}

	public void setItems(List<Reward> items) {
		this.items = items;
	}

	public int getCfgTime() {
		return cfgTime;
	}

	public void setCfgTime(int cfgTime) {
		this.cfgTime = cfgTime;
	}

	public List<Reward> getTempItems() {
		return tempItems;
	}

	public void setTempItems(List<Reward> tempItems) {
		this.tempItems = tempItems;
	}

	public int getWhosyourdaddy() {
		return whosyourdaddy;
	}

	public void setWhosyourdaddy(int whosyourdaddy) {
		this.whosyourdaddy = whosyourdaddy;
	}

}
