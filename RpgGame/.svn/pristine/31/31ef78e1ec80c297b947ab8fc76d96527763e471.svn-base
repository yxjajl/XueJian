package com.dh.resconfig;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseShopVO;

public class ShopRes extends BaseRes<BaseShopVO> {
	private static final Logger LOGGER = Logger.getLogger(ShopRes.class);
	public static final String Path = filePath + "csv/cfg_shop.csv";

	private Map<Integer, BaseShopVO> moneyList = new HashMap<Integer, BaseShopVO>();

	public static final int TYPE_RMB_AND_GIFT = 2; // 元宝和礼金
	public static final int TYPE_MONEY = 1; // 银两
	public static final int TYPE_EXPLOIT = 3; // 功勋
	public static final int TYPE_LXS = 23997;// 炼心石;
	public static final int TYPE_GIFT = 4; // 礼金
	public static final int TYPE_RMB = 8; // 纯元宝
	// public static final int type_qdian = 100; // Q点

	private ShopRes() {
		classz = BaseShopVO.class;
	}

	private static ShopRes INSTANCE = new ShopRes();

	public static ShopRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("ShopRes.otherInit");
		for (BaseShopVO baseShopVO : this.getDataList()) {
			moneyList.put(baseShopVO.getId(), baseShopVO);
			// if (baseShopVO.getMoney_type() == 1) { // 游戏币
			// } else if (baseShopVO.getMoney_type() == 2) {// 元宝币
			// rmbList.put(baseShopVO.getId(), baseShopVO);
			// }

		}
		this.getDataList().clear();
	}

	@Override
	protected void clear() {
		super.clear();
		moneyList.clear();
	}

	public BaseShopVO getMoneyBaseShopVO(int serialId) {
		return moneyList.get(serialId);
	}

	public static void main(String[] args) throws Exception {
		ShopRes.getInstance().loadFile(ShopRes.Path);
		for (BaseShopVO baseShopVO : ShopRes.getInstance().getDataList()) {
			System.out.println(baseShopVO.getItem_id() + "," + baseShopVO.getPrice());
		}
		BaseShopVO baseShopVO = ShopRes.getInstance().getMoneyBaseShopVO(350);
		System.out.println(baseShopVO.getItem_id() + "," + baseShopVO.getPrice());
	}
}