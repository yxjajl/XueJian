package com.dh.resconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseWelfareVO;

//BaseWelfareVO 福利
public class WelfareRes extends BaseRes<BaseWelfareVO> {
	private static final Logger LOGGER = Logger.getLogger(WelfareRes.class);
	public static final String Path = filePath + "csv/cfg_welfare.csv";
	public List<BaseWelfareVO> LEVELLIST = new ArrayList<BaseWelfareVO>();
	public List<BaseWelfareVO> LOGINLIST = new ArrayList<BaseWelfareVO>();
	public List<BaseWelfareVO> RECHARGELIST = new ArrayList<BaseWelfareVO>();
	public BaseWelfareVO FistBaseWelfareVO = null;
	public Map<Integer, List<BaseWelfareVO>> sxjzMap = new HashMap<Integer, List<BaseWelfareVO>>();
	// public Map<Integer, BaseWelfareVO> huangguaMap = new HashMap<Integer, BaseWelfareVO>(); // 黄瓜仙子的礼包 (自定义vip礼包)
	public Map<Integer, List<BaseWelfareVO>> vipMap = new HashMap<Integer, List<BaseWelfareVO>>();
	public Map<Integer, List<BaseWelfareVO>> vipTypeMap = new HashMap<Integer, List<BaseWelfareVO>>();

	public final static int LEVEL_REWARD = 1; // 等级礼包
	public final static int LOGIN_REWARD = 2; // 7日登陆奖励
	public final static int RECHARGE_REWARD = 3; // 充值回馈
	public final static int FIRST_RECHARG = 10;// 首冲

	public final static int ZAO_MU10 = 11;// 全服累计十连抽达到500次
	public final static int JJCTZ = 12;// 全服竞技场挑战达到1000次
	public final static int ENHANCE = 13;// 全服装备强化达到1000次
	public final static int HEROUPLEVEL = 14;// 全服英雄升级达到1000次
	public final static int VIP1 = 21;// 全服vip1数量达到200个
	public final static int VIP4 = 24;// 全服vip4数量达到50个
	public final static int VIP7 = 27;// 全服vip7数量达到30个
	public final static int VIP8 = 28;// 全服vip8数量达到20个
	public final static int VIP9 = 29;// 全服vip9数量达到20个
	public final static int VIP10 = 30;// 全服vip10数量达到20个

	public final static int DYMICVIP1 = 91;
	public final static int DYMICVIP4 = 94;
	public final static int DYMICVIP7 = 97;
	public final static int DYMICVIP8 = 98;
	public final static int DYMICVIP9 = 99;
	public final static int DYMICVIP10 = 100;

	private WelfareRes() {
		classz = BaseWelfareVO.class;
	}

	@Override
	protected void clear() {
		super.clear();
		LEVELLIST.clear();
		LOGINLIST.clear();
		RECHARGELIST.clear();
		sxjzMap.clear();
		// huangguaMap.clear();
		vipMap.clear();
	}

	private static WelfareRes INSTANCE = new WelfareRes();

	public static WelfareRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("WelfareRes.otherInit");
		for (BaseWelfareVO baseWelfareVO : WelfareRes.getInstance().getDataList()) {
			if (baseWelfareVO.getType() == LEVEL_REWARD) {
				LEVELLIST.add(baseWelfareVO);
			} else if (baseWelfareVO.getType() == LOGIN_REWARD) {
				LOGINLIST.add(baseWelfareVO);
			} else if (baseWelfareVO.getType() == RECHARGE_REWARD) {
				RECHARGELIST.add(baseWelfareVO);
			} else if (baseWelfareVO.getType() == FIRST_RECHARG) {
				FistBaseWelfareVO = baseWelfareVO;
			} else if (baseWelfareVO.getType() <= 80 && baseWelfareVO.getType() > 10) {
				List<BaseWelfareVO> list = sxjzMap.get(baseWelfareVO.getType());
				if (list == null) {
					list = new ArrayList<BaseWelfareVO>();
					sxjzMap.put(baseWelfareVO.getType(), list);
				}

				list.add(baseWelfareVO);
			} else if (baseWelfareVO.getType() <= 100 && baseWelfareVO.getType() > 90) {
				// huangguaMap.put(baseWelfareVO.getType(), baseWelfareVO);

				List<BaseWelfareVO> list = vipMap.get(baseWelfareVO.getFinished());
				if (list == null) {
					list = new ArrayList<BaseWelfareVO>();
					vipMap.put(baseWelfareVO.getFinished(), list);
				}
				list.add(baseWelfareVO);

				list = vipTypeMap.get(baseWelfareVO.getType());
				if (list == null) {
					list = new ArrayList<BaseWelfareVO>();
					vipTypeMap.put(baseWelfareVO.getType(), list);
				}
				list.add(baseWelfareVO);

				// vipMap.put(baseWelfareVO.getFinished(), baseWelfareVO);
			}
		}
	}

	public List<BaseWelfareVO> getSXJZ(int type) {
		return sxjzMap.get(type);
	}

	public BaseWelfareVO getHuangGua(int type, int state) {
		List<BaseWelfareVO> list = getHuangGuaGifByType(type);
		if (list != null && list.size() > 0) {
			for (BaseWelfareVO baseWelfareVO : list) {
				if (baseWelfareVO.getNum() == state) {
					return baseWelfareVO;
				}
			}
		}
		return null;
	}

	public List<BaseWelfareVO> getHuangGuaGifByVip(int vip) {
		return vipMap.get(vip);
	}

	public List<BaseWelfareVO> getHuangGuaGifByType(int type) {
		return vipTypeMap.get(type);
	}

	public static int getLabelByType(int type) {
		if (type < 10) {
			return 1;
		}

		if (type == 10) {
			return 2;
		}

		if (type < 80) {
			return 3;
		}

		if (type <= 100) {
			return 4;
		}

		return 1;
	}

	public static void main(String[] args) throws Exception {
		WelfareRes.getInstance().loadFile(WelfareRes.Path);
		for (BaseWelfareVO baseWelfareVO : WelfareRes.getInstance().getDataList()) {
			System.out.println(baseWelfareVO.getType() + "," + baseWelfareVO.getRewardid());
		}

		// System.out.println("a" +WelfareRes.getInstance().getHuangGuaGifByVip(7).size());
	}
}