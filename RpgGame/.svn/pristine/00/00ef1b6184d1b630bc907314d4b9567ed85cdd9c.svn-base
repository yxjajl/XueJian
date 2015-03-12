package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.HeroPreNameVO;
import com.dh.util.RandomUtil;

public class HeroPreNameRes extends BaseRes<HeroPreNameVO> {
	private static final Logger LOGGER = Logger.getLogger(HeroPreNameRes.class);
	public static final String Path = filePath + "csv/cfg_prefix.csv";

	private HeroPreNameRes() {
		classz = HeroPreNameVO.class;
	}

	private static HeroPreNameRes INSTANCE = new HeroPreNameRes();

	public static HeroPreNameRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("HeroPreNameRes.otherInit");
	}

	/**
	 * 查等级数据
	 * 
	 * @param level
	 * @return
	 */
	public HeroPreNameVO getHeroPreNameVO() {
		int n = RandomUtil.randomInt(getDataList().size());
		return getDataList().get(n);
	}

	public int getCC() {
		int n = RandomUtil.randomInt(2);
		return n == 0 ? -1 : 1;
	}

	public HeroPreNameVO getHeroPreNameVO(int id) {
		for (HeroPreNameVO heroPreNameVO : this.getDataList()) {
			if (heroPreNameVO.getId() == id) {
				return heroPreNameVO;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		HeroPreNameRes.getInstance().loadFile(HeroPreNameRes.Path);
		for (HeroPreNameVO heroPreNameVO : HeroPreNameRes.getInstance().getDataList()) {
			System.out.println(heroPreNameVO.getId() + "," + heroPreNameVO.getName1() + "," + HeroPreNameRes.getInstance().getCC());
		}
		System.out.println();
	}
}
