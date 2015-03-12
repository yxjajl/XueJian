package com.dh.resconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.Reward;
import com.dh.util.RandomUtil;
import com.dh.util.RewardUtil;

public class TaskRewardRes extends BaseRes<Reward> {
	private static final Logger LOGGER = Logger.getLogger(TaskRewardRes.class);
	public static final String Path = filePath + "csv/cfg_taskreward.csv";

	public Map<Integer, List<Reward>> map = new HashMap<Integer, List<Reward>>();

	private TaskRewardRes() {
		classz = Reward.class;
	}

	private static TaskRewardRes INSTANCE = new TaskRewardRes();

	public static TaskRewardRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("RewardRes.otherInit");

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

	/**
	 * 10 连抽 抽9个
	 * 
	 * @param rewardGroupID
	 * @param rn
	 * @return
	 */
	public List<Reward> randomReward2(int rewardGroupID, int rn) {
		List<Reward> result = new ArrayList<Reward>();
		List<Reward> list = map.get(rewardGroupID);

		if (list != null && list.size() > 0) {
			int maxRandom = 0;

			for (Reward reward : list) {
				maxRandom += reward.getProbability();
			}

			while (result.size() < rn) {
				int n = 0;
				int random = RandomUtil.randomInt(maxRandom);
				for (Reward reward : list) {

					// 通过orderid来控制　奖励的个数
					if (grantCount(result, reward.getOrderId())) { // result.getcount >= reward.getOrderId()
						continue;
					}
					n += reward.getProbability();
					if (random <= n) {
						// maxRandom = maxRandom - reward.getProbability();
						result.add(reward);
						break;
					}
				}

			}
		}

		return result;
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

	public static void main(String[] args) {
		TaskRewardRes.getInstance().loadFile(TaskRewardRes.Path);
		ItemRes.getInstance().loadFile(ItemRes.Path);

		for (Reward reward : TaskRewardRes.getInstance().getDataList()) {
			if (reward.getContent() > 0 && ItemRes.getInstance().getBaseItemVO(reward.getContent()) == null) {
				System.out.println(reward.getId() + "," + reward.getContent() + "不存　在");
			}
		}

		// for (NameRes name : list) {
		// String[] firStrings = name.firstName.split(name.firstName);
		// Random random = new Random();
		// int fstName = random.nextInt(firStrings.length);
		// System.out.println(firStrings[fstName]);
		// }
		// if ((NameRes.getIntance().getKNameRes())!=null) {
		// int n9 = 0;
		// int n1 = 0;
		//
		// for (int x = 0; x < 1000; x++) {
		// List<Reward> list = RewardRes.getInstance().randomReward2(3, 9);
		// for (Reward reward : list) {
		// if (reward.getOrderId() == 9) {
		// n9++;
		// } else if (reward.getOrderId() == 1) {
		// n1++;
		// }
		// System.out.println(reward.getType() + "," + reward.getContent() + "," + reward.getNumber() + "=================" + reward.getOrderId());
		// }
		// }
		//
		// System.out.println(n9 + "," + n1);

		// }
	}

}
