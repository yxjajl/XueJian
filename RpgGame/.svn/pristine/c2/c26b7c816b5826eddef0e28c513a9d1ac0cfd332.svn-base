package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseExpeditionVO;

//BaseExpeditionVO
public class ExpeditionRes extends BaseRes<BaseExpeditionVO> {
	private static final Logger LOGGER = Logger.getLogger(ExpeditionRes.class);
	public static final String Path = filePath + "csv/cfg_expedition.csv";

	private ExpeditionRes() {
		classz = BaseExpeditionVO.class;
	}

	private static ExpeditionRes INSTANCE = new ExpeditionRes();

	public static ExpeditionRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("ExpeditionRes.otherInit");
	}

	/**
	 * 查等级数据
	 * 
	 * @param level
	 * @return
	 */
	public BaseExpeditionVO getBaseExpeditionVO(int id) {
		for (BaseExpeditionVO baseExpeditionVO : ExpeditionRes.getInstance().getDataList()) {
			if (baseExpeditionVO.getId() == id) {
				return baseExpeditionVO;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		ExpeditionRes.getInstance().loadFile(ExpeditionRes.Path);
		for (BaseExpeditionVO baseExpeditionVO : ExpeditionRes.getInstance().getDataList()) {
			System.out.println(baseExpeditionVO.getId() + "," + baseExpeditionVO.getType() + ", " + baseExpeditionVO.getReward());
		}
	}
}