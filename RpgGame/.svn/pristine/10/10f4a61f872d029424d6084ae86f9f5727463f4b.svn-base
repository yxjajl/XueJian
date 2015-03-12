package com.dh.resconfig;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.Reward;
import com.dh.util.RandomUtil;

/**
 * 夺宝翻牌
 * 
 * @author RickYu
 * 
 */
public class RobRes extends BaseRes<Reward> {
	private static final Logger LOGGER = Logger.getLogger(RobRes.class);
	public static final String Path = filePath + "csv/cfg_rob.csv";

	private RobRes() {
		classz = Reward.class;
	}

	private static RobRes INSTANCE = new RobRes();

	public static RobRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("RobRes.otherInit");
		for (Reward reward : this.getDataList()) {
			reward.setRaidRewardinfo(reward.gentoRewardinfo());
		}
	}

	/**
	 * 在奖励中抽几个
	 * 
	 * @param rewardGroupID
	 * @param rn
	 * @return
	 */
	public List<Reward> randomReward(int level) {
		return randomReward(level, 5);
	}

	public List<Reward> randomReward(int level, int rn) {
		List<Reward> result = new ArrayList<Reward>();
		List<Reward> list = this.getDataList();
		if (list != null && list.size() > 0) {
			int maxRandom = 0;
			for (Reward reward : list) {
				if (reward.getLevel() > level) {
					continue;
				}
				maxRandom += reward.getProbability();
			}

			for (int i = 0; i < rn; i++) {
				int n = 0;
				int random = RandomUtil.randomInt(maxRandom);
				for (Reward reward : list) {
					if (reward.getLevel() > level) {
						continue;
					}
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

	public static void main(String[] args) throws Exception {
		RobRes.getInstance().loadFile(RobRes.Path);

		for (Reward reward : RobRes.getInstance().randomReward(4, 3)) {
			System.out.println("reward " + reward.getLevel() + "," + reward.getContent());
		}
	}
}
