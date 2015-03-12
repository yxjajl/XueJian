package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseEquipStarVO;

public class EquipmentStarRes extends BaseRes<BaseEquipStarVO> {
	private static final Logger LOGGER = Logger.getLogger(EquipmentStarRes.class);
	public static final String Path = filePath + "csv/cfg_EquipmentStar.csv";

	private EquipmentStarRes() {
		classz = BaseEquipStarVO.class;
	}

	private static EquipmentStarRes INSTANCE = new EquipmentStarRes();

	public static EquipmentStarRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("EquipmentStarRes.otherInit");
		for (BaseEquipStarVO baseEquipStarVO : EquipmentStarRes.getInstance().getDataList()) {
			int[] itemid = baseEquipStarVO.getItemid();
			int[] itemnum = baseEquipStarVO.getItemnum();
			itemid[0] = baseEquipStarVO.getItemid1();
			itemid[1] = baseEquipStarVO.getItemid2();
			itemid[2] = baseEquipStarVO.getItemid3();

			itemnum[0] = baseEquipStarVO.getItemnum1();
			itemnum[1] = baseEquipStarVO.getItemnum2();
			itemnum[2] = baseEquipStarVO.getItemnum3();
		}
	}

	/**
	 * 查等级数据
	 * 
	 * @param level
	 * @return
	 */
	public BaseEquipStarVO getBaseEquipStarVO(int cfgId) {
		for (BaseEquipStarVO baseEquipStarVO : EquipmentStarRes.getInstance().getDataList()) {
			if (baseEquipStarVO.getCfgId() == cfgId) {
				return baseEquipStarVO;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		EquipmentStarRes.getInstance().loadFile(EquipmentStarRes.Path);
		for (BaseEquipStarVO baseEquipStarVO : EquipmentStarRes.getInstance().getDataList()) {
			System.out.println(baseEquipStarVO.getCfgId() +","+baseEquipStarVO.getItemid()[0]+ "," + baseEquipStarVO.getResCfgId());
		}
	}
}
