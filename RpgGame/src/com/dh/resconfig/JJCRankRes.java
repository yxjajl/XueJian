package com.dh.resconfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseJJCRankVO;
import com.dh.game.vo.base.Reward;

public class JJCRankRes extends BaseRes<BaseJJCRankVO> {
	private static final Logger LOGGER = Logger.getLogger(JJCRankRes.class);
	public static final String Path = filePath + "csv/cfg_JJCRanking.csv";
	public Map<Integer, List<Reward>> map = new HashMap<Integer, List<Reward>>(); // 名次上限,奖励
	public TreeSet<BaseJJCRankVO> upRankSet = new TreeSet<BaseJJCRankVO>(new JJCRankCompare());

	private JJCRankRes() {
		classz = BaseJJCRankVO.class;
	}

	private static JJCRankRes INSTANCE = new JJCRankRes();

	public static JJCRankRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("JJCRankRes.otherInit");

		for (BaseJJCRankVO baseJJCRankVO : JJCRankRes.getInstance().getDataList()) {
			List<Reward> list = map.get(baseJJCRankVO.getUpper());
			if (list == null) {
				list = new ArrayList<Reward>();
				map.put(baseJJCRankVO.getUpper(), list);
				if (baseJJCRankVO.getLower() == 0) {
					baseJJCRankVO.setLower(100000000);
				}
				upRankSet.add(baseJJCRankVO);
			}
			Reward reward = new Reward();
			reward.setId(baseJJCRankVO.getId());
			reward.setType(baseJJCRankVO.getType());
			reward.setContent(baseJJCRankVO.getContent());
			reward.setNumber(baseJJCRankVO.getNumber());
			list.add(reward);

		}
		this.getDataList().clear();
	}

	/**
	 * 根据连胜场次放送奖励
	 * 
	 * @param succ
	 */
	public List<Reward> getRewardListByRank(int rankUpper) {
		return map.get(rankUpper);
	}

	// public Map<Integer, List<Reward>> getRankMap() {
	// return map;
	// }
	//
	/**
	 * 根据连胜场次放送奖励
	 * 
	 * @param succ
	 *            胜利次数
	 */
	public int getRewardIdByPlayerRank(int rank) {
		int maxReward = 0;
		for (int value : map.keySet()) {
			if (rank >= value && maxReward < value) {
				maxReward = value;
			}
		}

		return maxReward;
	}

	public TreeSet<BaseJJCRankVO> getUpRankSet() {
		return upRankSet;
	}

	public void clear() {
		super.clear();
		map.clear();
		upRankSet.clear();
	}

	public static void main(String[] args) throws Exception {
		JJCRankRes.getInstance().loadFile(JJCRankRes.Path);
		for (int i = 1; i < 1002; i++) {
			System.out.println(""+i+" = " + JJCRankRes.getInstance().getRewardIdByPlayerRank(i));
		}
		// Map<Integer, List<Reward>> map = JJCRankRes.getInstance().getRankMap();
		// for (Map.Entry<Integer, List<Reward>> mapentry : map.entrySet()) {
		// System.out.println(mapentry.getKey());
		// }
		// for (BaseJJCRankVO aseJJCRankVO : JJCRankRes.getInstance().getUpRankSet()) {
		// System.out.println("rankUP:" + aseJJCRankVO.getUpper() + "," + aseJJCRankVO.getLower());
		// }
	}
}

class JJCRankCompare implements Comparator<BaseJJCRankVO> {

	@Override
	public int compare(BaseJJCRankVO o1, BaseJJCRankVO o2) {
		if (o1.getUpper() > o2.getUpper())
			return 1;
		if (o1.getUpper() < o2.getUpper())
			return -1;
		return 0;
	}

}
