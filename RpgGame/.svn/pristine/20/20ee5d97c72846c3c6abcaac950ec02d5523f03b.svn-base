package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.VipLevelVO;

public class VipLevelRes extends BaseRes<VipLevelVO> {
	private static final Logger LOGGER = Logger.getLogger(LevelRes.class);
	public static final String Path = filePath + "csv/cfg_viplevel.csv";

	private VipLevelRes() {
		classz = VipLevelVO.class;
	}

	private static VipLevelRes INSTANCE = new VipLevelRes();

	public static VipLevelRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("VipLevelRes.otherInit");
	}

	public int getVipLevel(int rmb) {
		int level = 0;
		for (VipLevelVO vipLevelVO : VipLevelRes.getInstance().getDataList()) {
			if (rmb >= vipLevelVO.getRMB() && level < vipLevelVO.getLevel()) {
				level = vipLevelVO.getLevel();
			} else {
				break;
			}
		}
		return level;
	}

	/**
	 * 查等级数据
	 * 
	 * @param level
	 * @return
	 */
	public VipLevelVO getVipLevelVO(int level) {
		for (VipLevelVO vipLevelVO : VipLevelRes.getInstance().getDataList()) {
			if (vipLevelVO.getLevel() == level) {
				return vipLevelVO;
			}
		}
		return null;
	}

	/**
	 * 神水最大值
	 * 
	 * @param level
	 * @return
	 */
	public int getMaxWater(int level) {
		if (level == 0) {
			return 0;
		}
		VipLevelVO vipLevelVO = getVipLevelVO(level);
		if (vipLevelVO != null) {
			return vipLevelVO.getPrivilege2();
		}
		return 0;
	}

	/**
	 * 精英副本购买次数
	 * 
	 * @param level
	 * @return
	 */
	public int getEliRaidBuyTimes(int level) {
		VipLevelVO vipLevelVO = getVipLevelVO(level);
		if (vipLevelVO != null) {
			return vipLevelVO.getPrivilege6();
		}
		return 0;
	}

	// 金钱副本购买次数上限
	public int getMoneyiRaidBuyTimes(int level) {
		VipLevelVO vipLevelVO = getVipLevelVO(level);
		if (vipLevelVO != null) {
			return vipLevelVO.getPrivilege4();
		}
		return 0;
	}

	// 经验副本购买次数上限
	public int getExpRaidBuyTimes(int level) {
		VipLevelVO vipLevelVO = getVipLevelVO(level);
		if (vipLevelVO != null) {
			return vipLevelVO.getPrivilege3();
		}
		return 0;
	}

	// 竞技值购买次数上限
	public int getPvpBuyTimes(int level) {
		VipLevelVO vipLevelVO = getVipLevelVO(level);
		if (vipLevelVO != null) {
			return vipLevelVO.getPrivilege5();
		}
		return 0;
	}

	/** 获得补签次数 */
	public int getPvpResignTimes(int level) {
		VipLevelVO vipLevelVO = getVipLevelVO(level);
		if (vipLevelVO != null) {
			return vipLevelVO.getPrivilege1();
		}
		return 0;
	}

	/** 获取远征每日刷新次数 **/
	public int getYuanZhenReset(int level) {
		VipLevelVO vipLevelVO = getVipLevelVO(level);
		if (vipLevelVO != null) {
			return vipLevelVO.getPrivilege9();
		}
		return 1;
	}

	public static void main(String[] args) throws Exception {
		VipLevelRes.getInstance().loadFile(VipLevelRes.Path);
		// for (VipLevelVO VipLevelVO : VipLevelRes.getInstance().getDataList())
		// {
		// System.out.println(VipLevelVO.getLevel() + "," + VipLevelVO.getRMB()
		// + "," + VipLevelVO.getTime());
		// }
		for (int i = 0; i <= 10; i++)
			System.out.println("x:" + i + "," + VipLevelRes.getInstance().getYuanZhenReset(i));
	}
}
