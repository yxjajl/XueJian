package com.dh.resconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.Reward;
import com.dh.vo.WorldBossVO;

public class BossRewardRes extends BaseRes<Reward> {
	private static final Logger LOGGER = Logger.getLogger(BossRewardRes.class);
	public static final String Path = filePath + "csv/cfg_worldboss.csv";

	public Map<Integer, List<Reward>> boss1 = new HashMap<Integer, List<Reward>>();
	public Map<Integer, List<Reward>> boss2 = new HashMap<Integer, List<Reward>>();

	private BossRewardRes() {
		classz = Reward.class;
	}

	private static BossRewardRes INSTANCE = new BossRewardRes();

	public static BossRewardRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("worldBoss.otherInit");

		for (Reward reward : this.getDataList()) {
			if (reward.getBossid() == WorldBossVO.BOSS_ONE_CFGID) {
				List<Reward> list = boss1.get(reward.getId());
				if (list == null) {
					list = new ArrayList<Reward>();
					boss1.put(reward.getId(), list);
				}
				list.add(reward);
			} else if (reward.getBossid() == WorldBossVO.BOSS_TWO_CFGID) {
				List<Reward> list = boss2.get(reward.getId());
				if (list == null) {
					list = new ArrayList<Reward>();
					boss2.put(reward.getId(), list);
				}
				list.add(reward);
			}
		}
	}

	@Override
	protected void clear() {
		boss1.clear();
		boss2.clear();
		super.clear();
	}

	/**
	 * 返回计算概率过后的List
	 * */
	public List<Reward> getBossReward(int rewardGroupID, boolean isFirst) {
		if (isFirst) {
			return boss1.get(rewardGroupID);
		} else {
			return boss2.get(rewardGroupID);
		}
	}

	public static void main(String[] args) {
		BossRewardRes.getInstance().reLoadFile(BossRewardRes.Path,"BossRewardRes");
		BossRewardRes.getInstance().reLoadFile(BossRewardRes.Path,"name");
		BossRewardRes.getInstance().reLoadFile(BossRewardRes.Path,"name");
		BossRewardRes.getInstance().reLoadFile(BossRewardRes.Path, "name");
		ItemRes.getInstance().loadFile(ItemRes.Path);
		for (int i = 1; i < 13; i++) {
			List<Reward> b = BossRewardRes.getInstance().getBossReward(11, false);
			System.out.println(b);
			// for (Reward baseBossRewardVO : b) {
			// System.out.println("id: " + baseBossRewardVO.getId() + ",num: " +
			// baseBossRewardVO.getNumber()+"");
			// }
		}

	}

}
