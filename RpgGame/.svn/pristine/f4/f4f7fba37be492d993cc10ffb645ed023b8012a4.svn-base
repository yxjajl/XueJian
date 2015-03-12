package com.dh.resconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.Reward;
import com.dh.util.RewardUtil;

public class ExpeditionRewardRes extends BaseRes<Reward> {
	private static final Logger LOGGER = Logger.getLogger(ExpeditionRewardRes.class);
	public static final String Path = filePath + "csv/cfg_expeditionreward.csv";

	private Map<Integer, List<Reward>> map = new HashMap<Integer, List<Reward>>();

	public Map<Integer, List<Reward>> getMap() {
		return map;
	}


	private ExpeditionRewardRes() {
		classz = Reward.class;
	}

	private static ExpeditionRewardRes INSTANCE = new ExpeditionRewardRes();

	public static ExpeditionRewardRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("ExpeditionRewardRes.otherInit");

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

	public static void main(String[] args) {
		ExpeditionRewardRes.getInstance().loadFile(ExpeditionRewardRes.Path);
		ItemRes.getInstance().loadFile(ItemRes.Path);
		List<Reward> rewards = ExpeditionRewardRes.getInstance().getRewardRateGroup(230);
		for (Reward reward : rewards) {
			System.out.println(reward.getId() + "," + reward.getContent() + "," + reward.getMode() + "," + reward.getProbability());

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
		// List<Reward> list =
		// ExpeditionRewardRes.getInstance().randomReward2(3, 9);
		// for (Reward reward : list) {
		// if (reward.getOrderId() == 9) {
		// n9++;
		// } else if (reward.getOrderId() == 1) {
		// n1++;
		// }
		// System.out.println(reward.getType() + "," + reward.getContent() + ","
		// + reward.getNumber() + "=================" + reward.getOrderId());
		// }
		// }
		//
		// System.out.println(n9 + "," + n1);

		// }
	}

}
