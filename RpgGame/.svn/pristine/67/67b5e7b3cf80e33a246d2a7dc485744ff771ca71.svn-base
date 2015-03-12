package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseMonsterVO;
import com.dh.util.VOUtil;

public class MonsterRes extends BaseRes<BaseMonsterVO> {
	private static final Logger LOGGER = Logger.getLogger(MonsterRes.class);
	public static final String Path = filePath + "csv/cfg_monster.csv";

	private MonsterRes() {
		classz = BaseMonsterVO.class;
	}

	private static MonsterRes INSTANCE = new MonsterRes();

	public static MonsterRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("MonsterRes.otherInit");
		for (BaseMonsterVO baseMonsterVO : MonsterRes.getInstance().getDataList()) {
			baseMonsterVO.setFinalHero(VOUtil.getFinalHero(baseMonsterVO));
		}
	}

	/**
	 * 查等级数据
	 * 
	 * @param level
	 * @return
	 */
	public BaseMonsterVO getBaseMonsterVO(int cfgId) {
		for (BaseMonsterVO baseMonsterVO : MonsterRes.getInstance().getDataList()) {
			if (baseMonsterVO.getCfgId() == cfgId) {
				return baseMonsterVO;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		MonsterRes.getInstance().loadFile(MonsterRes.Path);
		for (BaseMonsterVO baseMonsterVO : MonsterRes.getInstance().getDataList()) {
			System.out.println(baseMonsterVO.getCommonatk() + "," + baseMonsterVO.getCfgId());
		}
		
		System.out.println(MonsterRes.getInstance().getBaseMonsterVO(10000).getFinalHero().getAtk());
	}
}
