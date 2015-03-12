package com.dh.vo.user;

import java.util.ArrayList;
import java.util.List;

import com.dh.constants.StreetConstants;
import com.dh.game.vo.user.StreetBoxVO;
import com.dh.game.vo.user.StreetDefendLogVO;
import com.dh.game.vo.user.StreetEnemyVO;
import com.dh.game.vo.user.StreetMonsterVO;
import com.dh.game.vo.user.StreetResVO;

public class UserStreet implements IClear {
	private int playerId;
	private int status;// {0:无状态,1:资源点被占领,2:门派被攻击}
	private int resStatus;
	private int centerStatus;

	private String gridStr;
	private transient byte[] grids = new byte[StreetConstants.MAX_GRIDS_NUM];
	private transient byte[] freeGrids = new byte[StreetConstants.MAX_GRIDS_NUM];//
	private transient byte[] reachbleGrids = new byte[StreetConstants.MAX_GRIDS_NUM];
	// 当前空置资源点
	private int openedNum;// 当前开启格子数
	private int boxFreshTime;// 上次宝箱刷新时间,unit:s
	private int monsterFreshTime;// 上次怪物刷新时间unit:s

	private transient List<StreetResVO> resList = new ArrayList<StreetResVO>();// 资源点
	private transient List<StreetBoxVO> boxList = new ArrayList<StreetBoxVO>();// 宝箱
	private transient List<StreetMonsterVO> monsterList = new ArrayList<StreetMonsterVO>();// 怪物

	private transient List<StreetEnemyVO> enemies = new ArrayList<StreetEnemyVO>(5);
	private transient List<StreetDefendLogVO> defendLogs = new ArrayList<StreetDefendLogVO>();

	private String fzIds;// 玩家拥有的法阵
	private String machines;// 玩家拥有的器械;

	private transient String factoryFz;// 军械坊开通法阵
	private transient String factoryM;// 器械房开通器械

	private transient int robotFzId;// 机器人时使用
	private transient String robotMachineLine;// 机器人时使用

	private transient int maxLogId;

	public void clearStatus() {
		centerStatus = 0;
		resStatus = 0;
	}

	@Override
	public void clear() {
		resList.clear();
		resList = null;
		boxList.clear();
		boxList = null;
		monsterList.clear();
		monsterList = null;
		enemies.clear();
		enemies = null;
		defendLogs.clear();
		defendLogs = null;
	}

	public StreetDefendLogVO getLogById(int id) {
		for (StreetDefendLogVO streetDefendLogVO : defendLogs) {
			if (streetDefendLogVO.getId() == id) {
				return streetDefendLogVO;
			}
		}
		return null;
	}

	public boolean checkProduced() {
		for (StreetResVO streetResVO : resList) {
			if (!streetResVO.getItems().isEmpty()) {
				status = 2;
				return true;
			}
		}
		return false;
	}

	public StreetMonsterVO getMonsterByResId(int resId) {
		for (StreetMonsterVO streetMonsterVO : monsterList) {
			if (streetMonsterVO.getId() == resId) {
				return streetMonsterVO;
			}
		}
		return null;
	}

	public StreetBoxVO getBoxByResId(int resId) {
		for (StreetBoxVO streetBoxVO : boxList) {
			if (streetBoxVO.getId() == resId) {
				return streetBoxVO;
			}
		}
		return null;
	}

	public StreetMonsterVO getMonsterById(int id) {
		for (StreetMonsterVO streetMonsterVO : monsterList) {
			if (streetMonsterVO.getId() == id) {
				return streetMonsterVO;
			}
		}
		return null;
	}

	public StreetResVO getStreetResById(int id) {
		for (StreetResVO streetResVO : resList) {
			if (streetResVO.getId() == id) {
				return streetResVO;
			}
		}
		return null;
	}

	public boolean isDefeat(int enemyId) {
		for (StreetEnemyVO streetEnemyVO : enemies) {
			if (streetEnemyVO.getEnemyId() == enemyId) {
				return streetEnemyVO.getIsRevenge() == 1;
			}
		}
		return false;
	}

	public void appendFz(int id) {

	}

	public void appendMachine(String resMachine) {

	}

	/**
	 * 报废资源点中器械 一次;只删除一个
	 * 
	 * @param id
	 */
	public void removeFzById(int id) {
		boolean notDel = true;
		StringBuffer idStr = new StringBuffer();
		if (fzIds != null && !fzIds.isEmpty()) {
			String[] ids = StreetConstants.HERO_SPLIT_CHAR.split(fzIds);
			for (int i = 0; i < ids.length; i++) {
				if (Integer.valueOf(ids[i]) == id && notDel) {
					notDel = false;
				} else {
					idStr.append(ids[i]).append(StreetConstants.HERO_SPLIT_CHAR);
				}
			}
			this.fzIds = idStr.toString();
		}
	}

	/**
	 * 报废资源点中器械
	 * 
	 * @param resMachineLine
	 */
	public void removeMachine(String resMachineLine) {
		String[] resMachineStr = StreetConstants.HERO_SPLIT_CHAR.split(resMachineLine);
		if (resMachineStr != null) {
			for (String string : resMachineStr) {
				removeMachineById(Integer.valueOf(string));
			}
		}
	}

	public void removeMachineById(int cfgId) {
		boolean notDel = true;
		StringBuffer idStr = new StringBuffer();
		String[] userMachine = StreetConstants.HERO_SPLIT_CHAR.split(machines);
		if (userMachine != null) {
			for (String string : userMachine) {
				if (Integer.valueOf(string) == cfgId && notDel) {
					notDel = false;
				} else {
					idStr.append(string).append(StreetConstants.HERO_SPLIT_CHAR);
				}
			}
		}
		this.machines = idStr.toString();
	}

	public void addFz(int cfgId) {
		if (cfgId == 0) {
			return;
		}
		this.fzIds = fzIds + cfgId + StreetConstants.HERO_SPLIT_CHAR;
	}

	public void addMachine(int cfgId) {
		this.machines = machines + cfgId + StreetConstants.HERO_SPLIT_CHAR;
	}

	public void addMachines(String resMachine) {
		if (resMachine == null) {
			return;
		}
		this.machines = this.machines + resMachine;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<StreetResVO> getResList() {
		return resList;
	}

	public void setResList(List<StreetResVO> resList) {
		this.resList = resList;
	}

	public List<StreetEnemyVO> getEnemies() {
		return enemies;
	}

	public void setEnemies(List<StreetEnemyVO> enemies) {
		this.enemies = enemies;
	}

	public List<StreetDefendLogVO> getDefendLogs() {
		return defendLogs;
	}

	public void setDefendLogs(List<StreetDefendLogVO> defendLogs) {
		this.defendLogs = defendLogs;
	}

	public List<StreetBoxVO> getBoxList() {
		return boxList;
	}

	public void setBoxList(List<StreetBoxVO> boxList) {
		this.boxList = boxList;
	}

	public List<StreetMonsterVO> getMonsterList() {
		return monsterList;
	}

	public void setMonsterList(List<StreetMonsterVO> monsterList) {
		this.monsterList = monsterList;
	}

	public int getOpenedNum() {
		return openedNum;
	}

	public void setOpenedNum(int openedNum) {
		this.openedNum = openedNum;
	}

	public int getBoxFreshTime() {
		return boxFreshTime;
	}

	public void setBoxFreshTime(int boxFreshTime) {
		this.boxFreshTime = boxFreshTime;
	}

	public int getMonsterFreshTime() {
		return monsterFreshTime;
	}

	public void setMonsterFreshTime(int monsterFreshTime) {
		this.monsterFreshTime = monsterFreshTime;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getFzIds() {
		return fzIds;
	}

	public void setFzIds(String fzIds) {
		this.fzIds = fzIds;
	}

	public String getMachines() {
		return machines;
	}

	public void setMachines(String machines) {
		this.machines = machines;
	}

	/**
	 * 内部实现加一操作
	 * */
	public int getMaxLogId() {
		maxLogId++;
		return maxLogId;
	}

	public void setMaxLogId(int maxLogId) {
		this.maxLogId = maxLogId;
	}

	public String getGridStr() {
		return gridStr;
	}

	public void setGridStr(String gridStr) {
		this.gridStr = gridStr;
	}

	public byte[] getFreeGrids() {
		return freeGrids;
	}

	public void setFreeGrids(byte[] freeGrids) {
		this.freeGrids = freeGrids;
	}

	public void setGrids(byte[] grids) {
		this.grids = grids;
	}

	public byte[] getGrids() {
		return grids;
	}

	public String getFactoryFz() {
		return factoryFz;
	}

	public void setFactoryFz(String factoryFz) {
		this.factoryFz = factoryFz;
	}

	public String getFactoryM() {
		return factoryM;
	}

	public void setFactoryM(String factoryM) {
		this.factoryM = factoryM;
	}

	public StreetEnemyVO getEnemyById(int enemyId) {
		for (StreetEnemyVO enemyVO : enemies) {
			if (enemyVO.getEnemyId() == enemyId) {
				return enemyVO;
			}
		}
		return null;
	}

	public int getRobotFzId() {
		return robotFzId;
	}

	public void setRobotFzId(int robotFzId) {
		this.robotFzId = robotFzId;
	}

	public String getRobotMachineLine() {
		return robotMachineLine;
	}

	public void setRobotMachineLine(String robotMachineLine) {
		this.robotMachineLine = robotMachineLine;
	}

	public byte[] getReachbleGrids() {
		return reachbleGrids;
	}

	public void setReachbleGrids(byte[] reachbleGrids) {
		this.reachbleGrids = reachbleGrids;
	}

	public int getResStatus() {
		return resStatus;
	}

	public void setResStatus(int resStatus) {
		this.resStatus = resStatus;
	}

	public int getCenterStatus() {
		return centerStatus;
	}

	public void setCenterStatus(int centerStatus) {
		this.centerStatus = centerStatus;
	}

}
