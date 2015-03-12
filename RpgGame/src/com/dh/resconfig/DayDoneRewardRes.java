package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseDayDoneRewardVO;

public class DayDoneRewardRes extends BaseRes<BaseDayDoneRewardVO> {
	private static final Logger LOGGER = Logger.getLogger(DayDoneRewardRes.class);
	public static final String Path = filePath + "csv/cfg_active.csv";

	private DayDoneRewardRes() {
		classz = BaseDayDoneRewardVO.class;
	}

	private static DayDoneRewardRes INSTANCE = new DayDoneRewardRes();

	public static DayDoneRewardRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		for (BaseDayDoneRewardVO baseDayDoneRewardVO : this.getDataList()) {
			baseDayDoneRewardVO.setBaseItemVO(ItemRes.getInstance().getBaseItemVO(baseDayDoneRewardVO.getReward()));
		}
		LOGGER.info("dayDoneReward.otherInit");
	}

	@Override
	protected void clear() {
		super.clear();
	}

	public BaseDayDoneRewardVO getDayDoneReward(int cfgId) {
		for (BaseDayDoneRewardVO baseDayDoneRewardVO : this.getDataList()) {
			if (baseDayDoneRewardVO.getId() == cfgId) {
				return baseDayDoneRewardVO;
			}
		}
		return null;
	}

	public BaseDayDoneRewardVO getDayDoneRewardByScore(int score) {
		for (BaseDayDoneRewardVO baseDayDoneRewardVO : this.getDataList()) {
			if (baseDayDoneRewardVO.getIntegral() == score) {
				return baseDayDoneRewardVO;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		DayDoneRewardRes.getInstance().loadFile(DayDoneRewardRes.Path);
		ItemRes.getInstance().loadFile(ItemRes.Path);
		BaseDayDoneRewardVO b = DayDoneRewardRes.getInstance().getDayDoneReward(1);
		System.out.println(b.getReward());
	}

}
