package com.dh.resconfig;

import com.dh.game.vo.base.BaseHeroRecruitVO;
import com.dh.game.vo.hero.HeroProto.RECRUIT_TYPE;

public class HeroRecruitRes extends BaseRes<BaseHeroRecruitVO> {
	private final static RECRUIT_TYPE[] TYPE = { RECRUIT_TYPE.TYPE_MONEY, RECRUIT_TYPE.TYPE_RMB, RECRUIT_TYPE.TYPE_RMB10, RECRUIT_TYPE.TYPE_MONEY10 };
	private final static int[] RECRUIT_PRICE = { 100000, 100, 930, 1000000 };
	private final static long[] RECRUIT_CD = { 600000L, 72 * 3600000L, 0, 0 };// 600000L
	public final static int[] RECRUIT_REWARD = { 1, 2, 5, 4 };

	public final static int RMBZMSCORE = 10;// 元宝招蓦送积分
	public final static int RMBZMSCORE10 = 100;// 元宝招蓦送积分

	private HeroRecruitRes() {
		// classz = BaseLevelVO.class;
		BaseHeroRecruitVO baseHeroRecruitVO = null;
		for (int i = 0; i < TYPE.length; i++) {
			baseHeroRecruitVO = new BaseHeroRecruitVO();
			baseHeroRecruitVO.setType(TYPE[i]);
			baseHeroRecruitVO.setPrice(RECRUIT_PRICE[i]);
			baseHeroRecruitVO.setCd(RECRUIT_CD[i]);
			baseHeroRecruitVO.setRewardId(RECRUIT_REWARD[i]);
			this.getDataList().add(baseHeroRecruitVO);
		}

	}

	/**
	 * 取招募信息
	 * 
	 * @param type
	 * @return
	 */
	public BaseHeroRecruitVO getBaseHeroRecruitVO(RECRUIT_TYPE type) {
		for (BaseHeroRecruitVO baseHeroRecruitVO : this.getDataList()) {
			if (baseHeroRecruitVO.getType() == type) {
				return baseHeroRecruitVO;
			}
		}
		return null;
	}

	private static HeroRecruitRes INSTANCE = new HeroRecruitRes();

	public static HeroRecruitRes getInstance() {
		return INSTANCE;
	}

}
