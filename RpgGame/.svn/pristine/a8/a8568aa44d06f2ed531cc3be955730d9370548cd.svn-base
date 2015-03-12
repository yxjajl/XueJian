package com.dh.resconfig;

import java.util.List;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseResourceVO;
import com.dh.game.vo.base.Reward;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

public class ResourceRes extends BaseRes<BaseResourceVO> {
	private static final Logger LOGGER = Logger.getLogger(BaseResourceVO.class);
	public static final String Path = filePath + "csv/cfg_resource.csv";
	public List<BaseResourceVO> list;

	private ResourceRes() {
		classz = BaseResourceVO.class;
	}

	private static ResourceRes INSTANCE = new ResourceRes();

	public static ResourceRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		for (BaseResourceVO baseResourceVO : INSTANCE.getDataList()) {
			List<Reward> rewards = RewardRes.getInstance().getRewardRateGroup(baseResourceVO.getRewardid());
			if (rewards == null) {
				LOGGER.error("找不到奖励,数据表异常");
			}
			baseResourceVO.setRewards(rewards);
		}
		list = java.util.Collections.unmodifiableList(this.getDataList());
	}

	/**
	 * 格子id<br/>
	 * dingqu-pc100<br/>
	 * 2014年7月17日
	 */
	public BaseResourceVO getResourceByGridId(int id) {
		for (BaseResourceVO baseResourceVO : INSTANCE.getDataList()) {
			if (baseResourceVO.getSeat() == id) {
				return baseResourceVO;
			}
		}
		return null;
	}

	public BaseResourceVO getResourceVOByCfgId(int cfgId) {
		for (BaseResourceVO baseResourceVO : INSTANCE.getDataList()) {
			if (baseResourceVO.getId() == cfgId) {
				return baseResourceVO;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		ResourceRes.getInstance().loadFile(ResourceRes.Path);
		BaseResourceVO temp = ResourceRes.getInstance().getResourceByGridId(31);
		System.out.println(temp.getId() + ": url" + temp.getName());
	}

}
