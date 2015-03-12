package com.dh.dao;

import java.util.List;

import com.dh.game.vo.user.PlayerTaskVO;

public interface PlayerTaskMapper {
	/**
	 * 得到玩家未领奖的任务
	 * @param playerId
	 * @return
	 */
	public List<PlayerTaskVO>  getPlayerTaskList(int playerId);
	
	public int getPlayerTaskCount(int playerId);
	
	
	/**
	 * 创建任务
	 * @param playerTask
	 */
	public void createTask(PlayerTaskVO playerTask);
	
	/**
	 * 更改任务的状态
	 * @param playerTask
	 */
	public void changeTaskStatus(PlayerTaskVO playerTask);
	
	/**
	 * 更改任务的完成度
	 * @param playerTask
	 */
	public void changeTaskNumById(PlayerTaskVO playerTask);
	
}
