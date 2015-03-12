package com.dh.resconfig;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dh.constants.StreetConstants;
import com.dh.game.vo.base.BaseMonsterGroupVO;
import com.dh.game.vo.base.BaseMonsterVO;
import com.dh.util.RandomUtil;

public class MonsterGroupRes extends BaseRes<BaseMonsterGroupVO> {
	private static final Logger LOGGER = Logger.getLogger(MonsterGroupRes.class);
	public static final String Path = filePath + "csv/cfg_group.csv";

	private MonsterGroupRes() {
		classz = BaseMonsterGroupVO.class;
	}

	private static MonsterGroupRes INSTANCE = new MonsterGroupRes();
	private static List<BaseMonsterGroupVO> robotMonsterList = new ArrayList<BaseMonsterGroupVO>(StreetConstants.ROBOT_TOTAL_LEVEL);
	private static List<BaseMonsterGroupVO> gridMonsterList = new ArrayList<BaseMonsterGroupVO>(StreetConstants.ROBOT_TOTAL_LEVEL);

	public static MonsterGroupRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		robotMonsterList.clear();
		gridMonsterList.clear();
		BaseMonsterVO baseMonsterVO = null;
		for (BaseMonsterGroupVO baseMonsterGroupVO : INSTANCE.getDataList()) {
			String[] groups = baseMonsterGroupVO.getGroup().split(";");
			for (String string : groups) {
				baseMonsterVO = MonsterRes.getInstance().getBaseMonsterVO(Integer.parseInt(string));
				if (baseMonsterVO == null) {
					LOGGER.error("数据表异常,怪物不存在");
				}
				baseMonsterGroupVO.getMonsters().add(baseMonsterVO);
			}
			if (!baseMonsterGroupVO.getMachine().isEmpty()) {
				String[] machineStr = baseMonsterGroupVO.getMachine().split(";");
				for (String string : machineStr) {
					baseMonsterGroupVO.getMachines().add(Integer.valueOf(string));
				}
			}
		}
		for (BaseMonsterGroupVO baseMonsterGroupVO : INSTANCE.getDataList()) {
			if (baseMonsterGroupVO.getType() == StreetConstants.MONSTER_TYPE_ROBOT) {
				robotMonsterList.add(baseMonsterGroupVO);
			} else {
				gridMonsterList.add(baseMonsterGroupVO);
				// System.out.println("id" + baseMonsterGroupVO.getId() +
				// ",type: " + baseMonsterGroupVO.getType() + ",url:" +
				// baseMonsterGroupVO.getUrl());
			}
		}

		LOGGER.info("monster init finished");
	}

	/**
	 * 根据玩家等级刷怪物组 <br/>
	 * 2014年7月17日
	 * 
	 * @param level
	 *            玩家等级<br/>
	 * @author dingqu-pc100
	 */
	public BaseMonsterGroupVO getMonsterByLevel(int level, int type) {
		int maxLevel = level + 5;
		int minLevel = level - 5;
		List<BaseMonsterGroupVO> levelList = new ArrayList<BaseMonsterGroupVO>();
		if (type == StreetConstants.MONSTER_TYPE_ROBOT) {
			for (BaseMonsterGroupVO baseMonsterGroupVO : robotMonsterList) {
				if (baseMonsterGroupVO.getType() == type && baseMonsterGroupVO.getLevel() < maxLevel && baseMonsterGroupVO.getLevel() > minLevel) {
					levelList.add(baseMonsterGroupVO);
				}
			}
		} else {
			for (BaseMonsterGroupVO baseMonsterGroupVO : gridMonsterList) {
				if (baseMonsterGroupVO.getType() == type && baseMonsterGroupVO.getLevel() < maxLevel && baseMonsterGroupVO.getLevel() > minLevel) {
					levelList.add(baseMonsterGroupVO);
				}
			}
		}
		if (!levelList.isEmpty()) {
			return levelList.get(RandomUtil.randomInt(levelList.size()));
		}
		return null;
	}

	public BaseMonsterGroupVO getMonsterGroupByCfgId(int cfgId) {
		for (BaseMonsterGroupVO baseMonsterGroupVO : gridMonsterList) {
			if (baseMonsterGroupVO.getId() == cfgId) {
				return baseMonsterGroupVO;
			}
		}
		return null;
	}

	/**
	 * 获取对应等级及星级的机器人
	 * 
	 * @param level
	 * @param star
	 * @return
	 */
	public BaseMonsterGroupVO getRobotByLevelAndStar(int level, int star) {
		int maxLevel = level + 5;
		int minLevel = level - 5;
		List<BaseMonsterGroupVO> levelStarList = new ArrayList<BaseMonsterGroupVO>();
		for (BaseMonsterGroupVO baseMonsterGroupVO : robotMonsterList) {
			if (baseMonsterGroupVO.getLevel() <= maxLevel && baseMonsterGroupVO.getLevel() >= minLevel && baseMonsterGroupVO.getStar() == star) {
				levelStarList.add(baseMonsterGroupVO);
			}
		}
		if (!levelStarList.isEmpty()) {
			return levelStarList.get(RandomUtil.randomInt(levelStarList.size()));
		}
		return null;
	}

	public void clear() {
		super.clear();
		robotMonsterList.clear();
		gridMonsterList.clear();
	}

	public static void main(String[] args) throws Exception {
		MonsterRes.getInstance().loadFile(MonsterRes.Path);
		MonsterGroupRes.getInstance().loadFile(MonsterGroupRes.Path);
		for (BaseMonsterGroupVO baseMonsterGroupVO : MonsterGroupRes.getInstance().getDataList()) {
			System.out.println("id" + baseMonsterGroupVO.getId() + ",type: " + baseMonsterGroupVO.getType() + ",url:" + baseMonsterGroupVO.getUrl());
		}

		MonsterGroupRes.getInstance().getRobotByLevelAndStar(76, 1);
		for (int i = 15; i < 70; i++) {
			for (int j = 1; j < 4; j++) {
				BaseMonsterGroupVO baseMonsterGroupVO = MonsterGroupRes.getInstance().getRobotByLevelAndStar(i, j);
				// System.out.println("id" + baseMonsterGroupVO.getId() +
				// ",level: " + baseMonsterGroupVO.getStar() + ",url:" +
				// baseMonsterGroupVO.getUrl());
				// for (BaseMonsterVO baseMonsterVO :
				// baseMonsterGroupVO.getMonsters()) {
				// System.out.println("\tcfgId:" + baseMonsterVO.getCfgId() +
				// ",name:" + baseMonsterVO.getName() + ",level:" +
				// baseMonsterVO.getLevel());
				// }
			}
		}

	}

	public List<BaseMonsterGroupVO> getRobotMonsterList() {
		return robotMonsterList;
	}

	public static void setRobotMonsterList(List<BaseMonsterGroupVO> robotMonsterList) {
		MonsterGroupRes.robotMonsterList = robotMonsterList;
	}
}