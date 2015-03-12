package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.EnhanceCostVO;

public class SkillLevelCostRes extends BaseRes<EnhanceCostVO> {
	private static final Logger LOGGER = Logger.getLogger(SkillLevelCostRes.class);
	public static final String Path = filePath + "csv/cfg_skillcost.csv";

	private SkillLevelCostRes() {
		classz = EnhanceCostVO.class;
	}

	private static SkillLevelCostRes INSTANCE = new SkillLevelCostRes();

	public static SkillLevelCostRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("SkillLevelCostRes.otherInit");
	}

	/**
	 * 查等级数据
	 * 
	 * @param level
	 * @return
	 */
	public EnhanceCostVO getEnhanceCostVO(int level) {
		if (level <= getDataList().size()) {
			return getDataList().get(level - 1);
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		SkillLevelCostRes.getInstance().loadFile(SkillLevelCostRes.Path);
		EnhanceCostVO enhanceCostVO = SkillLevelCostRes.getInstance().getEnhanceCostVO(10);
//		for (EnhanceCostVO enhanceCostVO : SkillLevelCostRes.getInstance().getDataList()) {
			System.out.println(enhanceCostVO.getLevel() + "," + enhanceCostVO.getCostMoney());
//		}
	}
}