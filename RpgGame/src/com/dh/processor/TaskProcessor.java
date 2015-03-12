package com.dh.processor;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.constant.TaskConstant;
import com.dh.game.vo.base.BaseTaskVO;
import com.dh.game.vo.base.Reward;
import com.dh.game.vo.task.PlayerTask.ReceiveTaskRequest;
import com.dh.game.vo.task.PlayerTask.TaskInfoResponse;
import com.dh.game.vo.user.PlayerTaskVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.TaskRewardRes;
import com.dh.service.HeroService;
import com.dh.service.RewardService;
import com.dh.service.TasksService;
import com.dh.util.CommandUtil;
import com.dh.util.MyClassLoaderUtil;
import com.dh.util.VOUtil;
import com.dh.vo.user.UserCached;

@Component
public class TaskProcessor {
	private static Logger logger = Logger.getLogger(TaskProcessor.class);
	@Resource
	private TasksService tasksService;
	@Resource
	private RewardService rewardService;
	@Resource
	private HeroService heroService;

	// @Resource
	// private AreaGroupService areaGroupService;

	/**
	 * 接任务
	 * 
	 * @param request
	 * @param nettyMessageVO
	 * @param commandList
	 */
	public void receiveTask(ReceiveTaskRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerid = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerid);
		int taskId = request.getTaskId();
		logger.debug("taskId = " + taskId + ", playerid = " + playerid);
		PlayerTaskVO playerTask = tasksService.getReceiveblePlayerTaskById(userCached, taskId);// 查找玩家是否存在可接取的任务
		if (playerTask == null) {
			throw new GameException(AlertEnum.TASK_NO_HAVE_EXITS);
		}

		if (playerTask.getBaseTaskVO().getLevel() > userCached.getPlayerVO().getLevel()) {
			throw new GameException(AlertEnum.ROLE_LEVEL_NO);
		}

		playerTask.setStatus(TaskConstant.TASK_UNDERWAY);

		MyClassLoaderUtil.getInstance().getTaskCheck().receiveReady(userCached, playerTask);

		tasksService.changeTaskStatus(playerTask, commandList);

		commandList.add(CommandUtil.getTaskResponse(playerTask));
	}

	/**
	 * 提交任务,领奖
	 * 
	 * @param request
	 * @param nettyMessageVO
	 * @param commandList
	 */
	public void submitRewardProcessor(ReceiveTaskRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		int taskId = request.getTaskId();
		PlayerTaskVO playerTask = tasksService.getTaskById(userCached, taskId);

		if (playerTask == null) {
			throw new GameException(AlertEnum.TASK_NO_HAVE_EXITS);
		}
		finishTask(playerTask, userCached, commandList);
		if (playerTask.getTaskType() == TaskConstant.TASK_TYPE_MAIN) {
			int mainTaskId = playerTask.getTaskId();
			PlayerTaskVO gressPlayerTask = getUnderwayPlayerTaskById(userCached, TaskConstant.TASK_GRESS_REWARD);
			if (gressPlayerTask != null) {
				int num = this.getNum(gressPlayerTask, mainTaskId + 0);
				MyClassLoaderUtil.getInstance().getTaskCheck().changTaskByReQType(userCached, TaskConstant.TASK_GRESS_REWARD, gressPlayerTask.getBaseTaskVO().getTargetid(), num, commandList);
				if (gressPlayerTask != null && gressPlayerTask.getBaseTaskVO().getCompletetype() == TaskConstant.TASK_GRESS_REWARD && gressPlayerTask.getStatus() == TaskConstant.TASK_COMPLIETE) {
					finishTask(gressPlayerTask, userCached, commandList);
				}
			}
			
		}
	}

	public void finishTask(PlayerTaskVO playerTask, UserCached userCached, List<NettyMessageVO> commandList) throws Exception {
		if (playerTask.getStatus() == TaskConstant.TASK_COMPLIETE) {
			int rewardGroupID = playerTask.getBaseTaskVO().getTaskreward();
			List<Reward> list = TaskRewardRes.getInstance().getRewardRateGroup(rewardGroupID);
			MyClassLoaderUtil.getInstance().getTaskCheck().specProcess(userCached, playerTask, commandList);
			rewardService.checkAndReward(userCached, list, commandList);// 领奖

			playerTask.setStatus(TaskConstant.TASK_REWARD);
			tasksService.changeTaskStatus(playerTask, commandList);// 改变任务的状态为已提交领奖
			tasksService.removeTask(userCached, playerTask.getTaskId());// 移除缓存中已领奖的任务
			TaskInfoResponse.Builder taskList = TaskInfoResponse.newBuilder();
			taskList.addMyTask(VOUtil.getTask(playerTask));
			List<BaseTaskVO> nextList = playerTask.getBaseTaskVO().getNextBaseTaskVO();// 领奖完了之后继续发送后续任务
			if (nextList != null && nextList.size() > 0) {
				// System.out.println("next.getTargetid() = " + next.getTargetid());
				// MyTask.Builder nextTask = MyTask.newBuilder();
				// if (next.getType() == 1) {// 主线
				// nextTask.setTaskId(next.getId());
				// nextTask.setTaskName(next.getTaskName());
				// nextTask.setTaskType(next.getTaskType());
				// nextTask.setStatus(TaskConstant.TASK_CAN_GET);
				// } else if (next.getTaskType() == 2) {// 支线
				// if (userCached.getPlayerVO().getLevel() >= next.getReqLevel()) {
				// nextTask.setTaskId(next.getTaskId());
				// nextTask.setTaskName(next.getTaskName());
				// nextTask.setTaskType(next.getTaskType());
				// nextTask.setStatus(TaskConstant.TASK_CAN_GET);
				// }
				//
				// }
				int roleLevel = userCached.getPlayerVO().getLevel();
				for (BaseTaskVO next : nextList) {
					PlayerTaskVO nextPlayerTaskVO = BaseTaskToPlayerTaskVO(next, userCached.getPlayerId());

					if (tasksService.addTask(userCached, nextPlayerTaskVO)) {// 将下一个可接取的任务加入缓存
						tasksService.createTask(nextPlayerTaskVO);
					}
					if (next.getType() == TaskConstant.TASK_TYPE_MAIN || roleLevel >= next.getLevel()) {
						taskList.addMyTask(VOUtil.getTask(nextPlayerTaskVO));
					}
				}
			}
			NettyMessageVO nettyMessageVO = new NettyMessageVO();
			nettyMessageVO.setData(taskList.build().toByteArray());
			nettyMessageVO.setCommandCode(CSCommandConstant.TASK_UPDATE);
			commandList.add(nettyMessageVO);

		} else {
			throw new GameException(AlertEnum.TASK_NO_COMPLET_NOREWARD);
		}
	}

	public PlayerTaskVO getUnderwayPlayerTaskById(UserCached userCached, int reqType) {
		for (PlayerTaskVO playerTask : userCached.getTaskList()) {
			// LOGGER.debug("playerTask = " + playerTask);
			// LOGGER.debug("getBaseTaskVO = " + playerTask.getBaseTaskVO());
			if (playerTask.getBaseTaskVO().getCompletetype() == reqType && playerTask.getStatus() == TaskConstant.TASK_UNDERWAY) {
				return playerTask;
			}
		}
		return null;
	}

	public int getNum(PlayerTaskVO playerTask, int taskId) {
		int num = 0;
		if (playerTask.getBaseTaskVO().getCompletetype() == TaskConstant.TASK_GRESS_REWARD) {
			if (taskId >= playerTask.getBaseTaskVO().getTargetid() && taskId < (playerTask.getBaseTaskVO().getTargetid() + playerTask.getBaseTaskVO().getNumber())) {
				num = taskId - playerTask.getBaseTaskVO().getTargetid() + 1;
				// System.out.println("num 1 = " + num);
			} else if (taskId >= (playerTask.getBaseTaskVO().getTargetid() + playerTask.getBaseTaskVO().getNumber())) {
				num = playerTask.getBaseTaskVO().getNumber();
				// System.out.println("num 2 = " + num);
			}
		}
		return num;
	}

	public static PlayerTaskVO BaseTaskToPlayerTaskVO(BaseTaskVO baseTaskVO, int playerId) {
		PlayerTaskVO playerTask = new PlayerTaskVO();

		playerTask.setPlayerId(playerId);
		playerTask.setTaskId(baseTaskVO.getId());
		playerTask.setTaskType(baseTaskVO.getType());
		playerTask.setFinishNum(0);
		playerTask.setBaseTaskVO(baseTaskVO);

		// 进度任务主动接
		if (baseTaskVO.getType() == TaskConstant.TASK_TYPE_GRESS) {
			playerTask.setStatus(TaskConstant.TASK_UNDERWAY);
		} else {
			playerTask.setStatus(TaskConstant.TASK_CAN_GET);
		}
		return playerTask;
	}
}
