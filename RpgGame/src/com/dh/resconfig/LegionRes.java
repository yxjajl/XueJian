package com.dh.resconfig;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseLegionVO;

//
public class LegionRes extends BaseRes<BaseLegionVO> {
	private static final Logger LOGGER = Logger.getLogger(LegionRes.class);
	public static final String Path = filePath + "csv/cfg_ganglevel.csv";
	private Map<Integer, BaseLegionVO> BASE_LEGIONS_MAP = new HashMap<Integer, BaseLegionVO>();

	private LegionRes() {
		classz = BaseLegionVO.class;
	}

	private static LegionRes INSTANCE = new LegionRes();

	public static LegionRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("baseLegionVO.otherInit");
		for (BaseLegionVO baseLegionVO : this.getDataList()) {
			BASE_LEGIONS_MAP.put(baseLegionVO.getId(), baseLegionVO);
		}
	}

	public BaseLegionVO getBaseLegionByLevel(int level) {
		return BASE_LEGIONS_MAP.get(level);
	}

	@Override
	protected void clear() {
		BASE_LEGIONS_MAP.clear();
		super.clear();
	}

	public static void main(String[] args) throws Exception {
		LegionRewardRes.getInstance().loadFile(LegionRewardRes.Path);
		ItemRes.getInstance().loadFile(ItemRes.Path);
		LegionRes.getInstance().loadFile(LegionRes.Path);
		BaseLegionVO legionVO = LegionRes.getInstance().getBaseLegionByLevel(1);
		System.out.println(legionVO.getId());
	}
}