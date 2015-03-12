package com.dh.resconfig;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseLegionBossVO;
import com.dh.game.vo.base.BaseLegionBossVO;

//
public class LegionBossRes extends BaseRes<BaseLegionBossVO> {
	private static final Logger LOGGER = Logger.getLogger(LegionBossRes.class);
	public static final String Path = filePath + "csv/cfg_gangboss.csv";
	private Map<Integer, BaseLegionBossVO> LEGION_BOSS_MAP = new HashMap<Integer, BaseLegionBossVO>();

	private LegionBossRes() {
		classz = BaseLegionBossVO.class;
	}

	private static LegionBossRes INSTANCE = new LegionBossRes();

	public static LegionBossRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("baseLegionBossVO.otherInit");
		for (BaseLegionBossVO baseLegionVO : this.getDataList()) {
			baseLegionVO.setBaseMonsterVO(MonsterRes.getInstance().getBaseMonsterVO(baseLegionVO.getMonster()));
			LEGION_BOSS_MAP.put(baseLegionVO.getId(), baseLegionVO);
		}
	}

	public BaseLegionBossVO getBaseLegionBossById(int id) {
		return LEGION_BOSS_MAP.get(id);
	}

	@Override
	protected void clear() {
		LEGION_BOSS_MAP.clear();
		super.clear();
	}

	public static void main(String[] args) throws Exception {
		LegionRewardRes.getInstance().loadFile(LegionRewardRes.Path);
		ItemRes.getInstance().loadFile(ItemRes.Path);
		LegionBossRes.getInstance().loadFile(LegionBossRes.Path);
		BaseLegionBossVO legionVO = LegionBossRes.getInstance().getBaseLegionBossById(1);
		System.out.println(legionVO.getId());
	}
}