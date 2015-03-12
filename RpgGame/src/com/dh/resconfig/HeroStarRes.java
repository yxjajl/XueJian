package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseHeroStarVO;

public class HeroStarRes extends BaseRes<BaseHeroStarVO> {
	private static final Logger LOGGER = Logger.getLogger(HeroStarRes.class);
	public static final String Path = filePath + "csv/cfg_herostar.csv";

	private HeroStarRes() {
		classz = BaseHeroStarVO.class;
	}

	private static HeroStarRes INSTANCE = new HeroStarRes();

	public static HeroStarRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("HeroStarRes.otherInit");
	}

	/**
	 * 查英雄升星数据
	 * 
	 * @param level
	 * @return
	 */
	public BaseHeroStarVO getBaseHeroStarVO(int cfgId) {
		for (BaseHeroStarVO baseHeroStarVO : HeroStarRes.getInstance().getDataList()) {
			if (baseHeroStarVO.getCfgId() == cfgId) {
				return baseHeroStarVO;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		HeroStarRes.getInstance().loadFile(HeroStarRes.Path);
		for (BaseHeroStarVO baseHeroStarVO : HeroStarRes.getInstance().getDataList()) {
			System.out.println(baseHeroStarVO.getCfgId() + "," + baseHeroStarVO.getResCfgId());
		}
	}

}
