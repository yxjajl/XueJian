package com.dh.resconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.Reward;
import com.dh.util.RandomUtil;
import com.dh.util.RewardUtil;

public class WelfareRewardRes extends BaseRes<Reward> {
	private static final Logger LOGGER = Logger.getLogger(WelfareRewardRes.class);
	public static final String Path = filePath + "csv/cfg_welfareward.csv";

	public Map<Integer, List<Reward>> map = new HashMap<Integer, List<Reward>>();

	private WelfareRewardRes() {
		classz = Reward.class;
	}

	private static WelfareRewardRes INSTANCE = new WelfareRewardRes();

	public static WelfareRewardRes getInstance() {
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

	public static void main(String[] args) {
		WelfareRewardRes.getInstance().loadFile(WelfareRewardRes.Path);
		ItemRes.getInstance().loadFile(ItemRes.Path);

		for (Reward reward : WelfareRewardRes.getInstance().getDataList()) {
			if (reward.getContent() > 0 && ItemRes.getInstance().getBaseItemVO(reward.getContent()) == null) {
				System.out.println(reward.getId() + "," + reward.getContent() + "不存　在");
			}
		}

		for (Reward reward : WelfareRewardRes.getInstance().getRewardRateGroup(10000)) {
			System.out.println(reward.getId() + "," + reward.getContent());
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
