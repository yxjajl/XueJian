package com.dh.resconfig;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseLegionShopVO;

//
public class LegionShopRes extends BaseRes<BaseLegionShopVO> {
	private static final Logger LOGGER = Logger.getLogger(LegionShopRes.class);
	public static final String Path = filePath + "csv/cfg_gangshop.csv";
	private Map<Integer, BaseLegionShopVO> BASE_SHOP_ITEMS_MAP = new HashMap<Integer, BaseLegionShopVO>();

	private LegionShopRes() {
		classz = BaseLegionShopVO.class;
	}

	private static LegionShopRes INSTANCE = new LegionShopRes();

	public static LegionShopRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("baseLegionShopVO.otherInit");
		for (BaseLegionShopVO baseLegionVO : this.getDataList()) {
			BASE_SHOP_ITEMS_MAP.put(baseLegionVO.getId(), baseLegionVO);
		}
	}

	public BaseLegionShopVO getLegionShopVOByCfgId(int cfgId) {
		return BASE_SHOP_ITEMS_MAP.get(cfgId);
	}

	@Override
	protected void clear() {
		BASE_SHOP_ITEMS_MAP.clear();
		super.clear();
	}

	public static void main(String[] args) throws Exception {
		LegionRewardRes.getInstance().loadFile(LegionRewardRes.Path);
		ItemRes.getInstance().loadFile(ItemRes.Path);
		LegionShopRes.getInstance().loadFile(LegionShopRes.Path);
		BaseLegionShopVO legionVO = LegionShopRes.getInstance().getLegionShopVOByCfgId(20010);
		System.out.println(legionVO.getId());

	}
}