package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseLevelVO;

public class LevelRes extends BaseRes<BaseLevelVO> {
	private static final Logger LOGGER = Logger.getLogger(LevelRes.class);
	public static final String Path = filePath + "csv/cfg_level.csv";

	private LevelRes() {
		classz = BaseLevelVO.class;
	}

	private static LevelRes INSTANCE = new LevelRes();

	public static LevelRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("LevelRes.otherInit");
		int countExp = 0;
		for (BaseLevelVO baseLevelVO : LevelRes.getInstance().getDataList()) {
			baseLevelVO.setMaxExpc(countExp);
			countExp += baseLevelVO.getExp();
		}
	}

	/**
	 * 查等级数据
	 * 
	 * @param level
	 * @return
	 */
	public BaseLevelVO getBaseLevelVO(int level) {
		for (BaseLevelVO baseLevelVO : LevelRes.getInstance().getDataList()) {
			if (baseLevelVO.getLevel() == level) {
				return baseLevelVO;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		LevelRes.getInstance().loadFile(LevelRes.Path);
		for (BaseLevelVO baseLevelVO : LevelRes.getInstance().getDataList()) {
			System.out.println(baseLevelVO.getLevel() + "," + baseLevelVO.getExp() + ", " + baseLevelVO.getMaxExpc());
		}
	}
}