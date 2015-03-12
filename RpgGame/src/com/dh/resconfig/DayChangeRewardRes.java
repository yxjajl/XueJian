package com.dh.resconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.Reward;

public class DayChangeRewardRes extends BaseRes<Reward> {
	private static final Logger LOGGER = Logger.getLogger(DayChangeRewardRes.class);
	public static final String Path = filePath + "csv/cfg_rmbdate.csv";

	public Map<Integer, List<Reward>> map = new HashMap<Integer, List<Reward>>();
	private int DAY_REWARD_INDEX = 1;
	private Object Lock = new Object();

	private DayChangeRewardRes() {
		classz = Reward.class;
	}

	/**
	 * 加
	 */
	public int NextDayChange() {
		synchronized (Lock) {
			if (++DAY_REWARD_INDEX > 7) {
				DAY_REWARD_INDEX = 1;
			}
			return DAY_REWARD_INDEX;
		}
	}

	public int getRewardIndex() {
		synchronized (Lock) {
			return DAY_REWARD_INDEX;
		}
	}

	private static DayChangeRewardRes INSTANCE = new DayChangeRewardRes();

	public static DayChangeRewardRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("dayChange.otherInit");
		for (Reward reward : this.getDataList()) {
			reward.setRaidRewardinfo(reward.gentoRewardinfo());
			List<Reward> list = map.get(reward.getId());
			if (list == null) {
				list = new ArrayList<Reward>();
				map.put(reward.getId(), list);
			}
			list.add(reward);
		}
	}

	public List<Reward> getRewards() {
		return map.get(getRewardIndex());
	}

	@Override
	protected void clear() {
		super.clear();
		map.clear();
	}

	public static void main(String[] args) {
		DayChangeRewardRes.getInstance().loadFile(DayChangeRewardRes.Path);
		ItemRes.getInstance().loadFile(ItemRes.Path);

		// for (Reward reward : DayChangeRewardRes.getInstance().getDataList())
		// {
		// if (reward.getContent() > 0 &&
		// ItemRes.getInstance().getBaseItemVO(reward.getContent()) == null) {
		// System.out.println(reward.getId() + "," + reward.getContent() +
		// "不存　在");
		// }
		// }
		Reward reward = DayChangeRewardRes.getInstance().getRewards().get(0);
		System.out.println(reward.getId() + "," + reward.getContent() + "," + reward.getNumber());
	}

}
