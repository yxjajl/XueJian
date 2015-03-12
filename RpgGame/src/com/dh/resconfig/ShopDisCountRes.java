package com.dh.resconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseShopDisCountVO;
import com.dh.util.RandomUtil;

public class ShopDisCountRes extends BaseRes<BaseShopDisCountVO> {
	private static final Logger LOGGER = Logger.getLogger(ShopDisCountRes.class);
	public static final String Path = filePath + "csv/shop_discount.csv";

	private Map<Integer, BaseShopDisCountVO> rmbMap = new HashMap<Integer, BaseShopDisCountVO>();

	private ShopDisCountRes() {
		classz = BaseShopDisCountVO.class;
	}

	private static ShopDisCountRes INSTANCE = new ShopDisCountRes();

	public static ShopDisCountRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("ShopDisCountRes.otherInit");
		for (BaseShopDisCountVO BaseShopDisCountVO : this.getDataList()) {
			rmbMap.put(BaseShopDisCountVO.getId(), BaseShopDisCountVO);

		}
		this.getDataList().clear();
	}

	@Override
	protected void clear() {
		super.clear();
		rmbMap.clear();
	}

	public BaseShopDisCountVO getRmbBaseShopDisCountVO(int serialId) {
		return rmbMap.get(serialId);
	}

	/**
	 * 在奖励中抽几个
	 * 
	 * @param levelLimit
	 *            等级限制
	 * @param 数量
	 * @return
	 */
	public List<BaseShopDisCountVO> randomShop(int levelLimit, int rn) {
		List<BaseShopDisCountVO> result = new ArrayList<BaseShopDisCountVO>(3);
		List<BaseShopDisCountVO> cloneResult = new ArrayList<BaseShopDisCountVO>(3);

		int maxRandom = rmbMap.size();
		for (int i = 0; i < rn; i++) {
			int n = 0;
			int random = RandomUtil.randomInt(maxRandom);
			for (Integer serialId : rmbMap.keySet()) {
				BaseShopDisCountVO baseShopDisCountVO = rmbMap.get(serialId);
				n += baseShopDisCountVO.getWeight();
				if (result.contains(baseShopDisCountVO)) {
					continue;
				}

				if (baseShopDisCountVO.getLevel() > levelLimit) {
					continue;
				}

				if (random <= n) {
					maxRandom = maxRandom - baseShopDisCountVO.getWeight();
					result.add(baseShopDisCountVO);
					break;
				}
			}

		}

		for (BaseShopDisCountVO baseShopDisCountVO : result) {
			try {
				cloneResult.add((BaseShopDisCountVO) baseShopDisCountVO.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}

		}

		result.clear();
		// result.add((BaseShopDisCountVO) (baseShopDisCountVO.clone()));
		return cloneResult;
	}

	public static void main(String[] args) throws Exception {
		ShopDisCountRes.getInstance().loadFile(ShopDisCountRes.Path);
		// BaseShopDisCountVO baseShopDisCountVO = ShopDisCountRes.getInstance().randomShop(10, 3).get(0);
		// System.out.println(baseShopDisCountVO.getId() + "," + baseShopDisCountVO.getTotal_limit());
		// baseShopDisCountVO.setTotal_limit(0);
		// System.out.println(baseShopDisCountVO.getId() + "," + baseShopDisCountVO.getTotal_limit());
		//
		// baseShopDisCountVO = ShopDisCountRes.getInstance().getRmbBaseShopDisCountVO(baseShopDisCountVO.getId());
		// System.out.println(baseShopDisCountVO.getId() + "," + baseShopDisCountVO.getTotal_limit());

		for (int n = 0; n < 100000; n++) {
			List<BaseShopDisCountVO> list = ShopDisCountRes.getInstance().randomShop(0, 3);
			if (list.get(0).getItem_id() == list.get(1).getItem_id() || list.get(0).getItem_id() == list.get(2).getItem_id() || list.get(2).getItem_id() == list.get(1).getItem_id()) {
				System.out.println("============================================");
				for (BaseShopDisCountVO tt : list) {
					System.out.println(tt.getId() + "," + tt.getItem_id());
				}
			}
		}

	}
}
