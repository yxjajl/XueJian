package com.dh.resconfig;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseDaySignVO;

public class DaySignRes extends BaseRes<BaseDaySignVO> {
	private static final Logger LOGGER = Logger.getLogger(FaZhenRes.class);
	public static final String Path = filePath + "csv/cfg_datereward.csv";
	private static DaySignRes INSTANCE = new DaySignRes();;
	private final static TIntObjectHashMap<List<BaseDaySignVO>> DAYS_MAP = new TIntObjectHashMap<List<BaseDaySignVO>>();

	private DaySignRes() {
		classz = BaseDaySignVO.class;
	}

	public void otherInit() {
		for (BaseDaySignVO baseDaySignVO : this.getDataList()) {
			List<BaseDaySignVO> daysGroup = DAYS_MAP.get(baseDaySignVO.getDays());
			if (daysGroup == null) {
				daysGroup = new ArrayList<BaseDaySignVO>();
				DAYS_MAP.put(baseDaySignVO.getDays(), daysGroup);
			}
			daysGroup.add(baseDaySignVO);
		}
		LOGGER.info("daySign.otherInit");
	}

	public static DaySignRes getInstance() {
		return INSTANCE;
	}

	public List<BaseDaySignVO> getSignRewardByDays(int days) {
		return DAYS_MAP.get(days);
	}

	@Override
	protected void clear() {
		super.clear();
		DAYS_MAP.clear();
	}

	public static void main(String[] args) {
		DaySignRes.getInstance().loadFile(DaySignRes.Path);
		System.out.println(DaySignRes.getInstance().getSignRewardByDays(1).get(0).getType());
	}

}
