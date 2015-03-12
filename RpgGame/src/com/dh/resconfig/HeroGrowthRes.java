package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseGrowthVO;

/**
 * 英雄成长
 * 
 * @author RickYu
 * 
 */
public class HeroGrowthRes extends BaseRes<BaseGrowthVO> {
	private static final Logger LOGGER = Logger.getLogger(HeroGrowthRes.class);
	public static final String Path = filePath + "csv/cfg_growth.csv";

	private HeroGrowthRes() {
		classz = BaseGrowthVO.class;
	}

	private static HeroGrowthRes INSTANCE = new HeroGrowthRes();

	public static HeroGrowthRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("HeroGrowthRes.otherInit");
	}

	/**
	 * 查英雄成长数据
	 * 
	 * @param level
	 * @return
	 */
	public BaseGrowthVO getBaseGrowthVO(int cfgId) {
		for (BaseGrowthVO baseGrowthVO : HeroGrowthRes.getInstance().getDataList()) {
			if (baseGrowthVO.getCfgId() == cfgId) {
				return baseGrowthVO;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		HeroGrowthRes.getInstance().loadFile(HeroGrowthRes.Path);
		for (BaseGrowthVO baseGrowthVO : HeroGrowthRes.getInstance().getDataList()) {
			System.out.println(baseGrowthVO.getCfgId() + "," + baseGrowthVO.getHit());
		}
	}
}
