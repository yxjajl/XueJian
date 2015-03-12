package com.dh.resconfig;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseLegionMemVO;

//
public class LegionMemRes extends BaseRes<BaseLegionMemVO> {
	private static final Logger LOGGER = Logger.getLogger(LegionMemRes.class);
	public static final String Path = filePath + "csv/cfg_gangmembers.csv";
	private Map<Integer, BaseLegionMemVO> BASE_MEMS_MAP = new HashMap<Integer, BaseLegionMemVO>();

	private LegionMemRes() {
		classz = BaseLegionMemVO.class;
	}

	private static LegionMemRes INSTANCE = new LegionMemRes();

	public static LegionMemRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("baseLegionMemVO.otherInit");
		for (BaseLegionMemVO baseLegionVO : this.getDataList()) {
			BASE_MEMS_MAP.put(baseLegionVO.getId(), baseLegionVO);
		}
	}

	public BaseLegionMemVO getBaseMemByLevel(int level) {
		return BASE_MEMS_MAP.get(level);
	}

	public BaseLegionMemVO getMemDonateByDonate(int donate) {
		int length = this.getDataList().size();
		BaseLegionMemVO baseLegionMemVO = null;
		BaseLegionMemVO nextLegionMemVO = null;
		for (int i = 0; i < length; i++) {
			baseLegionMemVO = this.getDataList().get(i);
			nextLegionMemVO = null;
			if (i < length - 1) {
				nextLegionMemVO = this.getDataList().get(i + 1);
			}
			if (donate >= baseLegionMemVO.getExp() && (nextLegionMemVO == null || donate < nextLegionMemVO.getExp())) {
				return baseLegionMemVO;
			}

		}

		return null;
	}

	@Override
	protected void clear() {
		BASE_MEMS_MAP.clear();
		super.clear();
	}

	public static void main(String[] args) throws Exception {
		LegionRewardRes.getInstance().loadFile(LegionRewardRes.Path);
		ItemRes.getInstance().loadFile(ItemRes.Path);
		LegionMemRes.getInstance().loadFile(LegionMemRes.Path);
		BaseLegionMemVO legionVO = LegionMemRes.getInstance().getBaseMemByLevel(1);
		System.out.println(legionVO.getId());
		for (int i = 0; i < 110000; i += 1) {
			getInstance().getMemDonateByDonate(i).getId();
		}
		System.out.println("finished");
	}
}