package com.dh.resconfig;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseShopVO;

public class ScoreShopRes extends BaseRes<BaseShopVO> {
	private static final Logger LOGGER = Logger.getLogger(ScoreShopRes.class);
	public static final String Path = filePath + "csv/cfg_heroscore.csv";

	private Map<Integer, BaseShopVO> moneyList = new HashMap<Integer, BaseShopVO>();

	private ScoreShopRes() {
		classz = BaseShopVO.class;
	}

	private static ScoreShopRes INSTANCE = new ScoreShopRes();

	public static ScoreShopRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("ScoreShopRes.otherInit");
		for (BaseShopVO baseShopVO : this.getDataList()) {
			moneyList.put(baseShopVO.getId(), baseShopVO);
			// if (baseShopVO.getMoney_type() == 1) { // 游戏币
			// } else if (baseShopVO.getMoney_type() == 2) {// 元宝币
			// rmbList.put(baseShopVO.getId(), baseShopVO);
			// }

		}
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
		ScoreShopRes.getInstance().loadFile(ScoreShopRes.Path);
		for (BaseShopVO baseShopVO : ScoreShopRes.getInstance().getDataList()) {
			System.out.println(baseShopVO.getItem_id() + "," + baseShopVO.getPrice());
		}
		 BaseShopVO baseShopVO = ScoreShopRes.getInstance().getMoneyBaseShopVO(22400);
		 System.out.println(baseShopVO.getItem_id() + "," + baseShopVO.getPrice());
	}
}
