package com.dh.resconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.Reward;
import com.dh.util.RandomUtil;
import com.dh.util.RewardUtil;

public class LegionRewardRes extends BaseRes<Reward> {
	private static final Logger LOGGER = Logger.getLogger(LegionRewardRes.class);
	public static final String Path = filePath + "csv/cfg_gangreward.csv";

	public Map<Integer, List<Reward>> map = new HashMap<Integer, List<Reward>>();

	private LegionRewardRes() {
		classz = Reward.class;
	}

	private static LegionRewardRes INSTANCE = new LegionRewardRes();

	public static LegionRewardRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("LegionRewardRes.otherInit");
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

	@Override
	protected void clear() {
		super.clear();
		map.clear();
	}

	/**
	 * 返回计算概率过后的List
	 * */
	public List<Reward> getRewardRateGroup(int rewardGroupID) {
		return RewardUtil.getReward(rewardGroupID, map);
	}

	/**
	 * 在奖励中抽几个
	 * 
	 * @param rewardGroupID
	 * @param rn
	 * @return
	 */
	public List<Reward> randomReward(int rewardGroupID, int rn) {
		List<Reward> result = new ArrayList<Reward>();
		List<Reward> list = map.get(rewardGroupID);

		if (list != null && list.size() > 0) {
			int maxRandom = 0;
			for (Reward reward : list) {
				maxRandom += reward.getProbability();
			}

			for (int i = 0; i < rn; i++) {
				int n = 0;
				int random = RandomUtil.randomInt(maxRandom);
				for (Reward reward : list) {
					if (result.contains(reward)) {
						continue;
					}

					n += reward.getProbability();
					if (random <= n) {
						maxRandom = maxRandom - reward.getProbability();
						result.add(reward);
						break;
					}
				}

			}
		}

		return result;
	}

	public static void main(String[] args) {
		LegionRewardRes.getInstance().loadFile(LegionRewardRes.Path);
		ItemRes.getInstance().loadFile(ItemRes.Path);

		for (Reward reward : LegionRewardRes.getInstance().getDataList()) {
			System.out.println(reward.getId());

		}
	}

}
