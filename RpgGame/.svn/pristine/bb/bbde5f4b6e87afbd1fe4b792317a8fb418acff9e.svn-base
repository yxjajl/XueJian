package com.dh.dao;

import com.dh.game.vo.user.PlayerBuildVO;
import com.dh.game.vo.user.PlayerTimerVO;

public interface PlayerTimerMapper {
	public PlayerTimerVO getPlayerTimer(int playerid);

	// 修改招蓦的CD
	public void updatePlayerTimer(PlayerTimerVO playerTimerVO);

	// 修改议事厅的 领俸禄 CD
	public void updateYIPlayerTimer(PlayerTimerVO playerTimerVO);

	public void updateSalaryTime(PlayerTimerVO playerTimerVO);

	public void updatePowerTimer(PlayerTimerVO playerTimerVO);

	public void updateknpsackTime(PlayerTimerVO playerTimerVO);

	// 英雄免费挂机次数
	public void updateHeroHungryTimer(PlayerTimerVO playerTimerVO);

	// 功勋商店
	public void updateExploitShop(PlayerTimerVO playerTimerVO);

	// 建筑
	public PlayerBuildVO getBuild(int playerid);

	public void updateBuild(PlayerBuildVO playerBuildVO);

	public void insertPlayerTimeVO(PlayerTimerVO playerTimerVO);

	public void updateVipReward(PlayerTimerVO playerTimerVO);

	// 新手引导
	public void updateGuide(PlayerTimerVO playerTimerVO);

	public void updateOnlineReward(PlayerTimerVO playerTimerVO);

	// 刷新所有在线奖励
	public void freshTimerEveryDay();

	// 更新经验和银两副本
	public void updateMERaid(PlayerTimerVO playerTimerVO);

	// 更新体力购买次数
	public void updateBuyPowerCount(PlayerTimerVO playerTimerVO);

	// 竞技值购买次数(每日)
	public void updatejjcBuyTimes(PlayerTimerVO playerTimerVO);

	public void dayFreshOnLogin(PlayerTimerVO playerTimerVO);

	public void updateDayChange(PlayerTimerVO playerTimerVO);

	// 每日必做奖励
	public void updateDayDoneReward(PlayerTimerVO playerTimerVO);

	// 每日必做
	public void updateDayDone(PlayerTimerVO playerTimerVO);

	// 必做计数
	public void updateDayDoneStr(PlayerTimerVO playerTimerVO);

	// 每日得分
	public void updateDayDoneScore(PlayerTimerVO playerTimerVO);

	public void updateLegionTime(PlayerTimerVO playerTimerVO);
	//cdkey使用情况
	public void updateCDKEY(PlayerTimerVO playerTimerVO);
	

}
