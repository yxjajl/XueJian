package com.dh.resconfig;

import java.util.List;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseCDKeyConfigVO;
import com.dh.game.vo.base.Reward;
import com.dh.service.RewardService;

public class CdKeyRes extends BaseRes<BaseCDKeyConfigVO> {
	private static final Logger LOGGER = Logger.getLogger(CdKeyRes.class);
	public static final String Path = filePath + "csv/cfg_cdkey.csv";

	private CdKeyRes() {
		classz = BaseCDKeyConfigVO.class;
	}

	private static CdKeyRes INSTANCE = new CdKeyRes();

	public static CdKeyRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("CdKeyRes.otherInit");
		Reward reward = null;
		for (BaseCDKeyConfigVO baseCDKeyConfigVO : CdKeyRes.getInstance().getDataList()) {
			reward = new Reward();
			reward.setMode(0);
			reward.setType(RewardService.REWARD_TYPE_GOODS);
			reward.setContent(baseCDKeyConfigVO.getItem());
			reward.setNumber(1);
			baseCDKeyConfigVO.getRewardList().add(reward);
		}
	}

	public List<Reward> getItemCfgIdByType(int type) {
		BaseCDKeyConfigVO baseCDKeyConfigVO = getBaseCDKeyConfigVO(type);
		if (baseCDKeyConfigVO != null) {
			return baseCDKeyConfigVO.getRewardList();
		}
		return null;
	}

	public BaseCDKeyConfigVO getBaseCDKeyConfigVO(int type) {
		for (BaseCDKeyConfigVO BaseCDKeyConfigVO : CdKeyRes.getInstance().getDataList()) {
			if (BaseCDKeyConfigVO.getType() == type) {
				return BaseCDKeyConfigVO;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		CdKeyRes.getInstance().loadFile(CdKeyRes.Path);
		for (BaseCDKeyConfigVO BaseCDKeyConfigVO : CdKeyRes.getInstance().getDataList()) {
			System.out.println(BaseCDKeyConfigVO.getKeybegin() + "," + BaseCDKeyConfigVO.getType() + ", " + BaseCDKeyConfigVO.getDescribe() + ","
					+ BaseCDKeyConfigVO.getRewardList().get(0).getContent());
		}
	}
}
