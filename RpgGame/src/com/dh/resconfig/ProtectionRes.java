package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseProteVO;

public class ProtectionRes extends BaseRes<BaseProteVO> {
	private static final Logger LOGGER = Logger.getLogger(ProtectionRes.class);
	public static final String Path = filePath + "csv/cfg_EquipmentProtection.csv";

	private ProtectionRes() {
		classz = BaseProteVO.class;
	}

	private static ProtectionRes INSTANCE = new ProtectionRes();

	public static ProtectionRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("ProtectionRes.otherInit");
	}

	/**
	 * 查等级数据
	 * 
	 * @param level
	 * @return
	 */
	public BaseProteVO getBaseProteVO(int id) {
		for (BaseProteVO baseProteVO : ProtectionRes.getInstance().getDataList()) {
			if (baseProteVO.getId() == id) {
				return baseProteVO;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		ProtectionRes.getInstance().loadFile(ProtectionRes.Path);
		BaseProteVO baseProteVO = ProtectionRes.getInstance().getBaseProteVO(0);
		System.out.println(baseProteVO.getId() + "," + baseProteVO.getStats());
	}
}