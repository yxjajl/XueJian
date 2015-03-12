package com.dh.resconfig;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseBoxVO;
import com.dh.game.vo.base.Reward;
import com.dh.util.RandomUtil;

public class BoxRewardRes extends BaseRes<BaseBoxVO> {
	private static final Logger LOGGER = Logger.getLogger(BoxRewardRes.class);
	public static final String Path = filePath + "csv/cfg_box.csv";

	private BoxRewardRes() {
		classz = BaseBoxVO.class;
	}

	private static BoxRewardRes INSTANCE = new BoxRewardRes();

	public static BoxRewardRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		for (BaseBoxVO baseBoxVO : INSTANCE.getDataList()) {
			List<Reward> rewards = RewardRes.getInstance().getRewardRateGroup(baseBoxVO.getRewardid());
			baseBoxVO.setRewards(rewards);
		}
		LOGGER.info("boxRes init finished!");
	}

	/**
	 * 根据玩家等级刷宝箱 <br/>
	 * 2014年7月17日
	 * 
	 * @param level
	 *            玩家等级<br/>
	 * @author dingqu-pc100
	 */
	public BaseBoxVO getBoxRewardById(int level) {
		int maxLevel = level + 5;
		int minLevel = level - 5;
		List<BaseBoxVO> levelList = new ArrayList<BaseBoxVO>();
		for (BaseBoxVO baseBoxVO : INSTANCE.getDataList()) {
			if (baseBoxVO.getOpenlevel() < maxLevel && baseBoxVO.getOpenlevel() > minLevel) {
				levelList.add(baseBoxVO);
			}
		}
		if (!levelList.isEmpty()) {
			return levelList.get(RandomUtil.randomInt(levelList.size()));
		}
		LOGGER.error("数据表配置问题,找不到对应宝箱,\n,当前等级:" + level + "当前以最后一个宝箱");
		return INSTANCE.getDataList().get(INSTANCE.getDataList().size() - 1);
	}

	public BaseBoxVO getBoxVOByCfgId(int cfgId) {
		for (BaseBoxVO baseBoxVO : INSTANCE.getDataList()) {
			if (baseBoxVO.getId() == cfgId) {
				return baseBoxVO;
			}
		}
		return null;

	}

	public static void main(String[] args) throws Exception {
		BoxRewardRes.getInstance().loadFile(BoxRewardRes.Path);
		for (BaseBoxVO baseBoxVO : BoxRewardRes.getInstance().getDataList()) {
			LOGGER.info("id" + baseBoxVO.getId());
		}
	}
}