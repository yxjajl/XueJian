package com.dh.resconfig;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseShopDisCountVO;
import com.dh.util.RandomUtil;

public class JJCShopRes extends BaseRes<BaseShopDisCountVO> {
	private static final Logger LOGGER = Logger.getLogger(JJCShopRes.class);
	public static final String Path = filePath + "csv/cfg_JJCShop.csv";

	private JJCShopRes() {
		classz = BaseShopDisCountVO.class;
	}

	private static JJCShopRes INSTANCE = new JJCShopRes();

	public static JJCShopRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("JJCShopRes.otherInit");

	}

	public BaseShopDisCountVO getBaseShop(int serialId) {
		for (BaseShopDisCountVO baseShopDisCountVO : getDataList()) {
			if (baseShopDisCountVO.getId() == serialId) {
				return baseShopDisCountVO;
			}
		}
		return null;
	}

	/**
	 * 在奖励中抽几个
	 * 
	 * @param levelLimit
	 *            等级限制
	 * @param 数量
	 * @return
	 */

	public String randomJJCShop2(int levelLimit, int rn) {
		List<Integer> list = randomJJCShop(levelLimit, rn);

		StringBuffer result = new StringBuffer();
		boolean isFirst = false;
		for (int serialId : list) {
			if (!isFirst) {
				isFirst = true;
			} else {
				result.append(",");
			}
			result.append(serialId);
		}

		return result.toString();

	}

	public List<Integer> randomJJCShop(int levelLimit, int rn) {
		List<Integer> result = new ArrayList<Integer>();

		int maxRandom = this.getDataList().size();
		for (int i = 0; i < rn; i++) {
			int n = 0;
			int random = RandomUtil.randomInt(maxRandom);
			for (BaseShopDisCountVO baseShopDisCountVO : getDataList()) {
				n += baseShopDisCountVO.getWeight();
				if (result.contains(baseShopDisCountVO.getId())) {
					continue;
				}

				if (baseShopDisCountVO.getLevel() > levelLimit) {
					continue;
				}

				
				if (random <= n) {
					maxRandom = maxRandom - baseShopDisCountVO.getWeight();
					result.add(baseShopDisCountVO.getId());
					break;
				}
			}

		}

		return result;
	}

	public static void main(String[] args) throws Exception {
		JJCShopRes.getInstance().loadFile(JJCShopRes.Path);
		System.out.println(JJCShopRes.getInstance().randomJJCShop2(30, 9));
	}
}
