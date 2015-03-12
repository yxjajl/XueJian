package com.dh.game.vo.user;

import com.dh.game.vo.base.BaseTaskVO;

/**
 * 任务实体类
 * 
 * @author zqgame
 * @date 2013-10-21
 * 
 */
public class PlayerTaskVO {

	/** 任务编号 */
	private int taskId;

	/** 玩家编号 */
	private int playerId;

	/** 任务类型 1：主线任务 2：支线任务 */
	private int taskType;

	/** 任务状态 0可接取 1进行中 2已完成未领奖 3已领奖 */
	private int status;
	private int finishNum; // 需要完成数

	private transient BaseTaskVO baseTaskVO;

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getFinishNum() {
		return finishNum;
	}

	public void setFinishNum(int finishNum) {
		this.finishNum = finishNum;
	}

	public BaseTaskVO getBaseTaskVO() {
		return baseTaskVO;
	}

	public void setBaseTaskVO(BaseTaskVO baseTaskVO) {
		this.baseTaskVO = baseTaskVO;
	}

}