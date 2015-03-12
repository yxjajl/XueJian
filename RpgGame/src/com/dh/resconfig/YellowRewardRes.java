package com.dh.resconfig;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.Reward;
import com.dh.game.vo.base.YellowRewardVO;

/**
 * 黄钻礼礼包
 * 
 * @author RickYu
 * 
 */
public class YellowRewardRes extends BaseRes<YellowRewardVO> {
	private static final Logger LOGGER = Logger.getLogger(YellowRewardRes.class);
	public static final String Path = filePath + "csv/cfg_hzreward.csv";

	private HashMap<Integer, YellowRewardVO> dayMap = new HashMap<Integer, YellowRewardVO>();
	private YellowRewardVO ydayYellowRewardVO;
	private YellowRewardVO newYellowRewardVO;
	private HashMap<Integer, YellowRewardVO> levelMap = new HashMap<Integer, YellowRewardVO>();

	private YellowRewardRes() {
		classz = YellowRewardVO.class;
	}

	public final static int YELLOW_TYPE_DAY = 1;// 黄钻每日礼包
	public final static int YELLOW_TYPE_YDAY = 2;// 年费黄钻每日礼包
	public final static int YELLOW_TYPE_NEW = 3; // 新手礼包 1
	public final static int YELLOW_TYPE_LEVEL = 4; // 成长礼包 1

	private static YellowRewardRes INSTANCE = new YellowRewardRes();

	public static YellowRewardRes getInstance() {
		return INSTANCE;
	}

	@Override
	protected void clear() {
		// TODO Auto-generated method stub
		super.clear();
		dayMap.clear();
		levelMap.clear();
	}

	/**
	 * 每日年费黄钻 礼包
	 * 
	 * @return
	 */
	public YellowRewardVO getYdayYellowRewardVO() {
		return ydayYellowRewardVO;
	}

	/**
	 * 黄钻新手礼包
	 * 
	 * @return
	 */
	public YellowRewardVO getNewYellowRewardVO() {
		return newYellowRewardVO;
	}

	/**
	 * 每日黄钻礼包
	 * 
	 * @param id
	 * @return
	 */
	public YellowRewardVO getDayYellowRewardVO(int vipLevel) {
		return dayMap.get(vipLevel);
	}

	/**
	 * 黄钻成长（升级）　礼包
	 * 
	 * @return
	 */
	public YellowRewardVO getLevelYellowRewardVO(int roleLevel) {
		return levelMap.get(roleLevel);
	}

	public HashMap<Integer, YellowRewardVO> getLevelMap() {
		return levelMap;
	}

	public void otherInit() {
		LOGGER.info("YellowReward.otherInit");
		for (YellowRewardVO yellowRewardVO : YellowRewardRes.getInstance().getDataList()) {
			if (yellowRewardVO.getReward_type1() > 0) {
				Reward reward = new Reward();
				reward.setContent(yellowRewardVO.getContent1());
				reward.setType(yellowRewardVO.getReward_type1());
				reward.setNumber(yellowRewardVO.getNumber1());
				yellowRewardVO.getRewards().add(reward);
			}

			if (yellowRewardVO.getReward_type2() > 0) {
				Reward reward = new Reward();
				reward.setContent(yellowRewardVO.getContent2());
				reward.setType(yellowRewardVO.getReward_type2());
				reward.setNumber(yellowRewardVO.getNumber2());
				yellowRewardVO.getRewards().add(reward);
			}

			if (yellowRewardVO.getReward_type3() > 0) {
				Reward reward = new Reward();
				reward.setContent(yellowRewardVO.getContent3());
				reward.setType(yellowRewardVO.getReward_type3());
				reward.setNumber(yellowRewardVO.getNumber3());
				yellowRewardVO.getRewards().add(reward);
			}

			if (YELLOW_TYPE_DAY == yellowRewardVO.getType()) {
				dayMap.put(yellowRewardVO.getId(), yellowRewardVO);
			} else if (YELLOW_TYPE_YDAY == yellowRewardVO.getType()) {
				ydayYellowRewardVO = yellowRewardVO;
			} else if (YELLOW_TYPE_NEW == yellowRewardVO.getType()) {
				newYellowRewardVO = yellowRewardVO;
			} else if (YELLOW_TYPE_LEVEL == yellowRewardVO.getType()) {
				yellowRewardVO.setId(yellowRewardVO.getId() - 400);
				levelMap.put(yellowRewardVO.getId(), yellowRewardVO);
			}

		}
	}

	public static void main(String[] args) throws Exception {
		YellowRewardRes.getInstance().loadFile(YellowRewardRes.Path);
		ItemRes.getInstance().loadFile(ItemRes.Path);
		// for (YellowRewardVO yellowRewardVO : YellowRewardRes.getInstance().getDataList()) {
		// System.out.println(yellowRewardVO.getId() + "," + yellowRewardVO.getRewards().get(0).getContent());
		// for (Reward reward : yellowRewardVO.getRewards()) {
		// if (reward.getType() == 1 && ItemRes.getInstance().getBaseItemVO(reward.getContent()) == null) {
		// System.err.println("找不到物口" + reward.getContent());
		// }
		// }
		// }

		for (Reward reward : YellowRewardRes.getInstance().getYdayYellowRewardVO().getRewards()) {
			System.out.println(reward.getType() + "," + reward.getContent() + "," + reward.getNumber());
		}

	}
}