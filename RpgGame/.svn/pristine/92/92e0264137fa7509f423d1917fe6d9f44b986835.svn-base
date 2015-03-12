package com.dh.resconfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseJJCSuccVO;
import com.dh.game.vo.base.Reward;

public class JJCSuccRes extends BaseRes<BaseJJCSuccVO> {
	private static final Logger LOGGER = Logger.getLogger(JJCSuccRes.class);
	public static final String Path = filePath + "csv/cfg_JJCSuccessiveVictory.csv";
	public Map<Integer, List<Reward>> map = new HashMap<Integer, List<Reward>>(); // 奖励组id和奖励
	public List<Integer> succList = new ArrayList<Integer>();

	private JJCSuccRes() {
		classz = BaseJJCSuccVO.class;
	}

	private static JJCSuccRes INSTANCE = new JJCSuccRes();

	public static JJCSuccRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("JJCSuccRes.otherInit");
		for (BaseJJCSuccVO baseJJCSuccVO : JJCSuccRes.getInstance().getDataList()) {
			List<Reward> list = map.get(baseJJCSuccVO.getStreak());
			if (list == null) {
				list = new ArrayList<Reward>();
				map.put(baseJJCSuccVO.getStreak(), list);
				succList.add(baseJJCSuccVO.getStreak());
			}

			Reward reward = new Reward();
			reward.setId(baseJJCSuccVO.getId());
			reward.setType(baseJJCSuccVO.getType());
			reward.setContent(baseJJCSuccVO.getContent());
			reward.setNumber(baseJJCSuccVO.getNumber());
			list.add(reward);

		}

		Collections.sort(succList);
		this.getDataList().clear();
	}

	/**
	 * 根据连胜场次放送奖励
	 * 
	 * @param succ
	 */
	public List<Reward> getRewardListBySucc(int succ) {
		return map.get(succ);
	}

//	/**
//	 * 根据连胜场次放送奖励
//	 * 
//	 * @param succ
//	 *            胜利次数
//	 */
//	public int getRewardIdByPlayerSucc(int succ) {
//		int maxReward = 0;
//		for (int value : succList) {
//			if (succ >= value) {
//				maxReward = value;
//			} else {
//				break;
//			}
//		}
//
//		return maxReward;
//	}

	public int getNextRewardId(int rewardId, int succ) {
		int maxReward = 0;
		for (int value : succList) {
			if (succ >= value && value > rewardId) {
				maxReward = value;
				break;
			} 
		}

		return maxReward;
	}
	
	public void clear() {
		super.clear();
		map.clear();
		succList.clear();
	}

	public static void main(String[] args) throws Exception {
		JJCSuccRes.getInstance().loadFile(JJCSuccRes.Path);

		System.out.println(JJCSuccRes.getInstance().getNextRewardId(55,300));
		// for (BaseJJCSuccVO BaseJJCSuccVO : JJCSuccRes.getInstance().getDataList()) {
		// System.out.println(BaseJJCSuccVO.getLevel() + "," + BaseJJCSuccVO.getExp());
		// }
	}
}
