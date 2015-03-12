package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseHeroInfoVO;
import com.dh.game.vo.base.BaseHeroStarVO;

/**
 * 英雄基础数据
 * 
 * @author RickYu
 * 
 */
public class HeroRes extends BaseRes<BaseHeroInfoVO> {
	private static final Logger LOGGER = Logger.getLogger(HeroRes.class);
	public static final String Path = filePath + "csv/cfg_hero.csv";

	private HeroRes() {
		classz = BaseHeroInfoVO.class;
	}

	private static HeroRes INSTANCE = new HeroRes();

	public static HeroRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("HeroRes.otherInit");
	}

	/**
	 * 查英雄数据
	 * 
	 * @param level
	 * @return
	 */
	public BaseHeroInfoVO getBaseHeroInfoVO(int cfgId) {
		for (BaseHeroInfoVO baseHeroInfoVO : HeroRes.getInstance().getDataList()) {
			if (baseHeroInfoVO.getCfgId() == cfgId) {
				return baseHeroInfoVO;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		HeroRes.getInstance().loadFile(HeroRes.Path);
		HeroStarRes.getInstance().loadFile(HeroStarRes.Path);
		for (BaseHeroInfoVO baseHeroInfoVO : HeroRes.getInstance().getDataList()) {
			// String str = "update t_player_hero_defance set `name` = '" + baseHeroInfoVO.getName() + "' where cfgid = " + baseHeroInfoVO.getCfgId() + " and `name` is NULL ;";
			// System.out.println(str);
//			System.out.println(baseHeroInfoVO.getLevel() + "," + baseHeroInfoVO.getAtk_range() + "," + baseHeroInfoVO.getCommon_atk());
			if(baseHeroInfoVO.getStar() == 10) {
				continue;
			}
			BaseHeroStarVO baseHeroStarVO = HeroStarRes.getInstance().getBaseHeroStarVO(baseHeroInfoVO.getCfgId());
			if (baseHeroStarVO == null || baseHeroStarVO.getReqLevel() > baseHeroInfoVO.getMaxLevel()) {
				if (baseHeroStarVO == null) {
					System.out.println("null========" + baseHeroInfoVO.getCfgId());
				} else
					System.out.println("error ========" + baseHeroInfoVO.getCfgId() + "," + baseHeroStarVO.getReqLevel() + "," + baseHeroInfoVO.getMaxLevel());
			}
		}
	}

}
