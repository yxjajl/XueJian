package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.EnhanceCostVO;

public class EquipmentEnhanceCostRes extends BaseRes<EnhanceCostVO> {
	private static final Logger LOGGER = Logger.getLogger(EquipmentEnhanceCostRes.class);
	public static final String Path = filePath + "csv/cfg_EquipmentCost.csv";

	private EquipmentEnhanceCostRes() {
		classz = EnhanceCostVO.class;
	}

	private static EquipmentEnhanceCostRes INSTANCE = new EquipmentEnhanceCostRes();

	public static EquipmentEnhanceCostRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("EquipmentEnhanceCostRes.otherInit");
	}

	/**
	 * 查等级数据
	 * 
	 * @param level
	 * @return
	 */
	public EnhanceCostVO getEnhanceCostVO(int level) {
		for (EnhanceCostVO enhanceCostVO : EquipmentEnhanceCostRes.getInstance().getDataList()) {
			if (enhanceCostVO.getLevel() == level) {
				return enhanceCostVO;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		EquipmentEnhanceCostRes.getInstance().loadFile(EquipmentEnhanceCostRes.Path);
		for (EnhanceCostVO enhanceCostVO : EquipmentEnhanceCostRes.getInstance().getDataList()) {
			System.out.println(enhanceCostVO.getLevel() + "," + enhanceCostVO.getCostMoney());
		}
	}
}
