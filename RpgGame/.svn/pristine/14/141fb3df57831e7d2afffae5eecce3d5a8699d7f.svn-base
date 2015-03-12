package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseManyPeopleRaidVO;

public class ManyPeopleRaidRes extends BaseRes<BaseManyPeopleRaidVO> {
	private static final Logger LOGGER = Logger.getLogger(ManyPeopleRaidRes.class);
	public static final String Path = filePath + "csv/cfg_manyraid.csv";

	private ManyPeopleRaidRes() {
		classz = BaseManyPeopleRaidVO.class;
	}

	private static ManyPeopleRaidRes INSTANCE = new ManyPeopleRaidRes();

	public static ManyPeopleRaidRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("ManyPeopleRaidRes.otherInit");
		for (BaseManyPeopleRaidVO baseManyPeopleRaidVO : ManyPeopleRaidRes.getInstance().getDataList()) {
			this.getDataMap().put(baseManyPeopleRaidVO.getRaidid(), baseManyPeopleRaidVO);
		}
	}

	/**
	 * 查等级数据
	 * 
	 * @param level
	 * @return
	 */
	public BaseManyPeopleRaidVO getBaseLevelVO(int raidid) {
		return this.getDataMap().get(raidid);
	}

	public static void main(String[] args) throws Exception {
		ManyPeopleRaidRes.getInstance().loadFile(ManyPeopleRaidRes.Path);
		for (BaseManyPeopleRaidVO baseManyPeopleRaidVO : ManyPeopleRaidRes.getInstance().getDataList()) {
			System.out.println(baseManyPeopleRaidVO.getRaidid() + ", " + baseManyPeopleRaidVO.getName());
		}
	}
}
