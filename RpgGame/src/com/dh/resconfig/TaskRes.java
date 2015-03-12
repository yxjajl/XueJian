package com.dh.resconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.constant.TaskConstant;
import com.dh.game.vo.base.BaseTaskVO;

public class TaskRes extends BaseRes<BaseTaskVO> {
	private static final Logger LOGGER = Logger.getLogger(TaskRes.class);
	public static final String Path = filePath + "csv/cfg_task.csv";

	Map<Integer, BaseTaskVO> mainMap = new HashMap<Integer, BaseTaskVO>(); // 主线
	Map<Integer, BaseTaskVO> branchMap = new HashMap<Integer, BaseTaskVO>(); // 支线
	Map<Integer, BaseTaskVO> dailyMap = new HashMap<Integer, BaseTaskVO>(); // 日常
	Map<Integer, BaseTaskVO> gressMap = new HashMap<Integer, BaseTaskVO>(); //进度任务
	List<BaseTaskVO> initList = new ArrayList<BaseTaskVO>();

	private TaskRes() {
		classz = BaseTaskVO.class;
	}

	@Override
	protected void clear() {
		super.clear();
		mainMap.clear();
		branchMap.clear();
		dailyMap.clear();
		gressMap.clear();
	}

	private static TaskRes INSTANCE = new TaskRes();

	public static TaskRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("TaskRes.otherInit");

		for (BaseTaskVO baseTaskVO : TaskRes.getInstance().getDataList()) {
			if (baseTaskVO.getFrontmission() == -1) {
				initList.add(baseTaskVO);
			}

			this.getNextTaskById(baseTaskVO.getId(), baseTaskVO.getNextBaseTaskVO());

			if (baseTaskVO.getType() == TaskConstant.TASK_TYPE_MAIN) {
				mainMap.put(baseTaskVO.getId(), baseTaskVO);
			} else if (baseTaskVO.getType() == TaskConstant.TASK_TYPE_BRANCH) {
				branchMap.put(baseTaskVO.getId(), baseTaskVO);
			} else if (baseTaskVO.getType() == TaskConstant.TASK_TYPE_DAILY) {
				dailyMap.put(baseTaskVO.getId(), baseTaskVO);
			} else if (baseTaskVO.getType() == TaskConstant.TASK_TYPE_GRESS) {
				gressMap.put(baseTaskVO.getId(), baseTaskVO);
			}
		}
	}

	public List<BaseTaskVO> getReqAchieveList() {
		return initList;
	}

	/**
	 * 查等级数据
	 * 
	 * @param level
	 * @return
	 */
	public BaseTaskVO getTask(int taskId) {
		for (BaseTaskVO baseTaskVO : TaskRes.getInstance().getDataList()) {
			if (baseTaskVO.getId() == taskId) {
				return baseTaskVO;
			}
		}
		return null;
	}

	/**
	 * 据任务类型和id
	 * 
	 * @param taskId
	 * @param type
	 * @return
	 */
	public BaseTaskVO getTask(int taskId, int type) {
		if (type == TaskConstant.TASK_TYPE_MAIN) {
			return mainMap.get(taskId);
		} else if (type == TaskConstant.TASK_TYPE_BRANCH) {
			return branchMap.get(taskId);
		} else if (type == TaskConstant.TASK_TYPE_DAILY) {
			return dailyMap.get(taskId);
		} else if (type == TaskConstant.TASK_TYPE_GRESS) {
			return gressMap.get(taskId);
		}
		
		
		return null;
	}

	public void getNextTaskById(int taskId, List<BaseTaskVO> list) {
		for (BaseTaskVO baseTaskVO : TaskRes.getInstance().getDataList()) {
			if (baseTaskVO.getFrontmission() == taskId) {
				list.add(baseTaskVO);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		TaskRes.getInstance().loadFile(TaskRes.Path);
		TaskRewardRes.getInstance().loadFile(TaskRewardRes.Path);
		for (BaseTaskVO baseTaskVO : TaskRes.getInstance().getDataList()) {
			System.out.println(baseTaskVO.getId() + ":" + baseTaskVO.getLevel() + "," + baseTaskVO.getTargetid() + baseTaskVO.getNextBaseTaskVO());
			if (TaskRewardRes.getInstance().getRewardRateGroup(baseTaskVO.getTaskreward()) == null) {
				System.out.println("不存在的奖励组" + baseTaskVO.getTaskreward());
			}
		}

		// System.out.println( TaskRes.getInstance().getTask(13,2).getNumber());
	}
}
