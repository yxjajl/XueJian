package com.dh.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dh.game.vo.base.Reward;

public class RewardUtil {

	public final static int MAX_STATIC_VALUE = 10000;

	public static List<Reward> getReward(int rewardGroupID, Map<Integer, List<Reward>> map) {
		return getReward(rewardGroupID, map, 1);
	}

	public static List<Reward> getReward(int rewardGroupID, Map<Integer, List<Reward>> map, int max) {
		List<Reward> rewards = map.get(rewardGroupID);
		List<Reward> result = new ArrayList<Reward>();
		if (rewards != null && rewards.size() > 0) {
			List<Reward> randomList1 = new ArrayList<Reward>();
			List<Reward> randomList2 = new ArrayList<Reward>();
			for (Reward reward : rewards) {
				if (reward.getMode() == 0) {
					result.add(reward);
					max++;
				} else if (reward.getMode() == 1) {// 1：随机。随机给玩家一个奖励。
					randomList1.add(reward);
				} else if (reward.getMode() == 2) {// 每一个单独随机
					if (RandomUtil.randomInt(MAX_STATIC_VALUE) <= reward.getProbability()) {
						result.add(reward);
					}
				} else if (reward.getMode() == 3) {
					randomList2.add(reward);
				}
			}

			if (randomList1.size() > 0) {
				couqu(result, randomList1, 1);
			}

			if (randomList2.size() > 0 && result.size() < max) {
				couqu(result, randomList2, max - result.size());
			}

		}
		return result;
	}

	public static void couqu(List<Reward> result, List<Reward> randomList, int max) {
		int total = 0;
		int n = 0;
		int size = randomList.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {// 统计总数
				total += randomList.get(i).getProbability();
			}
			while (n < max) {
				int sumPosibility = 0;
				int randomNum = RandomUtil.randomInt(total);
				for (Reward tempReward : randomList) {
					sumPosibility += tempReward.getProbability();

					if (tempReward.getMode() == 3 && grantCount(result, tempReward.getOrderId())) { // result.getcount
						continue;
					}
					// 实现方式:检测随机数落在那个区间
					if (randomNum < sumPosibility) {// 选中该条奖励的概率参数
													// 选中概率=当前概率÷（规则1概率+规则2概率+……）×100%
						result.add(tempReward);
						n++;
						break;
					}
				}
			} // end while
		}

	}

	public static boolean grantCount(List<Reward> list, int orderId) {
		int result = 0;
		for (Reward reward : list) {
			if (reward.getOrderId() == orderId) {
				result++;
				if (result >= orderId) {
					return true;
				}
			}
		}

		return false;
	}
}
