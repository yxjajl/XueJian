package com.dh.resconfig;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseShopVO;

public class ExpeditionShopRes extends BaseRes<BaseShopVO> {
	private static final Logger LOGGER = Logger.getLogger(ExpeditionShopRes.class);
	public static final String Path = filePath + "csv/cfg_expeditionshop.csv";

	private Map<Integer, BaseShopVO> moneyList = new HashMap<Integer, BaseShopVO>();

	private ExpeditionShopRes() {
		classz = BaseShopVO.class;
	}

	private static ExpeditionShopRes INSTANCE = new ExpeditionShopRes();

	public static ExpeditionShopRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("ExpeditionShopRes.otherInit");
		for (BaseShopVO baseShopVO : this.getDataList()) {
			moneyList.put(baseShopVO.getId(), baseShopVO);
			// if (baseShopVO.getMoney_type() == 1) { // 游戏币s
			// } else if (baseShopVO.getMoney_type() == 2) {// 元宝币
			// rmbList.put(baseShopVO.getId(), baseShopVO);
			// }

		}
		this.getDataList().clear();
	}

	public Map<Integer, BaseShopVO> getMoneyList() {
		return moneyList;
	}

	@Override
	protected void clear() {
		super.clear();
		moneyList.clear();
	}

	public BaseShopVO getMoneyBaseShopVO(int serialId) {
		this.getDataList();
		return moneyList.get(serialId);
	}

	public static void main(String[] args) throws Exception {
		ExpeditionShopRes.getInstance().loadFile(ExpeditionShopRes.Path);
		// for (BaseShopVO baseShopVO : ExpeditionShopRes.getInstance().getDataList()) {
		// System.out.println(baseShopVO.getItem_id() + "," + baseShopVO.getPrice());
		// }
		BaseShopVO baseShopVO = ExpeditionShopRes.getInstance().getMoneyBaseShopVO(35);
		System.out.println(baseShopVO.getItem_id() + "," + baseShopVO.getPrice());
	}
}
