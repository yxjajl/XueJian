package com.dh.resconfig;

import gnu.trove.map.hash.TIntObjectHashMap;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseDayDoneVO;

public class DayDoneRes extends BaseRes<BaseDayDoneVO> {
	private static final Logger LOGGER = Logger.getLogger(FaZhenRes.class);
	public static final String Path = filePath + "csv/cfg_activetype.csv";
	private static DayDoneRes INSTANCE = new DayDoneRes();;
	private final static TIntObjectHashMap<BaseDayDoneVO> DAYS_MAP = new TIntObjectHashMap<BaseDayDoneVO>();

	private DayDoneRes() {
		classz = BaseDayDoneVO.class;
	}

	public void otherInit() {
		for (BaseDayDoneVO baseDayDoneVO : this.getDataList()) {
			DAYS_MAP.put(baseDayDoneVO.getId(), baseDayDoneVO);
		}
		LOGGER.info("dayDone.otherInit");
	}

	public static DayDoneRes getInstance() {
		return INSTANCE;
	}

	public BaseDayDoneVO getDayDoneById(int id) {
		return DAYS_MAP.get(id);
	}

	@Override
	protected void clear() {
		super.clear();
		DAYS_MAP.clear();
	}

	public static void main(String[] args) {
		DayDoneRes.getInstance().loadFile(DayDoneRes.Path);
		System.out.println(DayDoneRes.getInstance().getDayDoneById(1).getIntegral());
	}

}
