package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseLevelVO;

public class PlayerLevelRes extends BaseRes<BaseLevelVO> {
	private static final Logger LOGGER = Logger.getLogger(PlayerLevelRes.class);
	public static final String Path = filePath + "csv/cfg_teamlevel.csv";

	private PlayerLevelRes() {
		classz = BaseLevelVO.class;
	}

	private static PlayerLevelRes INSTANCE = new PlayerLevelRes();

	public static PlayerLevelRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("LevelRes.otherInit");
	}

	/**
	 * 查等级数据
	 * 
	 * @param level
	 * @return
	 */
	public BaseLevelVO getBaseLevelVO(int level) {
		for (BaseLevelVO baseLevelVO : PlayerLevelRes.getInstance().getDataList()) {
			if (baseLevelVO.getLevel() == level) {
				return baseLevelVO;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		PlayerLevelRes.getInstance().loadFile(PlayerLevelRes.Path);
		for (BaseLevelVO baseLevelVO : PlayerLevelRes.getInstance().getDataList()) {
			System.out.println(baseLevelVO.getLevel() + "," + baseLevelVO.getMaxExpc());
		}
	}
}