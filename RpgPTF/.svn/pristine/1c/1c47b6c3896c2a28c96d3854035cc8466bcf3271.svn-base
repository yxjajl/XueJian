package com.dh.game.vo.user;

import java.util.Date;

/**
 * 竞技场
 * 
 * @author RickYu
 * 
 */
public class PlayerArenaVO implements Comparable<PlayerArenaVO> {
	private int playerId;
	private int ordernum; // 排名
	private int win;// 总胜利场次
	private int lost; // 失败场次
	private int cc3win; // 周期内　最高连胜场次
	private int ccwin; // 周期内　当前连胜场次
	private int ccwinhis; // 历史最高连胜场次
	private Date failEndTime;
	private int reward0; // 每日奖励
	private int reward3; // 周期连胜奖励
	// private String defline;//
	private int toporder;// 历史最高排名
	private Date enemyremainTime;
	private String duobaoline;// 夺宝对手 playerid
	private String enemline; // 竞技场对手
	private int combat;
	private int guwu; // 鼓舞剩余场次
	private int lastdayrank1; // 上期rank最后的rank排名
	private int lastdayrank3; // 上期最后的高最 连胜场次

	public String getDuobaoline() {
		return duobaoline;
	}

	public void setDuobaoline(String duobaoline) {
		this.duobaoline = duobaoline;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public Date getEnemyremainTime() {
		return enemyremainTime;
	}

	public void setEnemyremainTime(Date enemyremainTime) {
		this.enemyremainTime = enemyremainTime;
	}

	public String getEnemline() {
		return enemline;
	}

	public void setEnemline(String enemline) {
		this.enemline = enemline;
	}

	public int getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public int getLost() {
		return lost;
	}

	public void setLost(int lost) {
		this.lost = lost;
	}

	public int getCc3win() {
		return cc3win;
	}

	public void setCc3win(int cc3win) {
		this.cc3win = cc3win;
	}

	public int getCcwin() {
		return ccwin;
	}

	public void setCcwin(int ccwin) {
		this.ccwin = ccwin;
	}

	public int getCcwinhis() {
		return ccwinhis;
	}

	public void setCcwinhis(int ccwinhis) {
		this.ccwinhis = ccwinhis;
	}

	public Date getFailEndTime() {
		return failEndTime;
	}

	public void setFailEndTime(Date failEndTime) {
		this.failEndTime = failEndTime;
	}

	public int getReward0() {
		return reward0;
	}

	public void setReward0(int reward0) {
		this.reward0 = reward0;
	}

	public int getReward3() {
		return reward3;
	}

	public void setReward3(int reward3) {
		this.reward3 = reward3;
	}

	// public String getDefline() {
	// return defline;
	// }
	//
	// public void setDefline(String defline) {
	// this.defline = defline;
	// }

	public int getToporder() {
		return toporder;
	}

	public void setToporder(int toporder) {
		this.toporder = toporder;
	}

	public int getCombat() {
		return combat;
	}

	public void setCombat(int combat) {
		this.combat = combat;
	}

	public int getGuwu() {
		return guwu;
	}

	public void setGuwu(int guwu) {
		this.guwu = guwu;
	}

	public int getLastdayrank1() {
		return lastdayrank1;
	}

	public void setLastdayrank1(int lastdayrank1) {
		this.lastdayrank1 = lastdayrank1;
	}

	public int getLastdayrank3() {
		return lastdayrank3;
	}

	public void setLastdayrank3(int lastdayrank3) {
		this.lastdayrank3 = lastdayrank3;
	}

	@Override
	public int compareTo(PlayerArenaVO playerArenaVO) {
		if (ordernum > playerArenaVO.getOrdernum())
			return 1;
		if (ordernum == playerArenaVO.getOrdernum())
			return 0;
		return -1;

	}

}
