package com.dh.resconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseRaidInfo;
import com.dh.util.Tool;

public class RaidRes extends BaseRes<BaseRaidInfo> {
	private static final Logger LOGGER = Logger.getLogger(RaidRes.class);
	public static final String Path = filePath + "csv/cfg_raid.csv";

	public final static int RAID_MODE_NOR = 1;// 普通副本
	public final static int RAID_MODE_ELI = 2; // 精英副本

	public final static int RAID_DIFF_EASY = 1;
	public final static int ME_RAID_E = 30001;
	public final static int ME_RAID_M = 30002;

	// public final static int RAID_DIFF_NORMAL = 2;
	// public final static int RAID_DIFF_DIFFC = 3;
	public int MAX_CHARPTER = 1; // 最大章节
	public int MAX_CLICHARPTER = 1; //
	Map<Integer, List<BaseRaidInfo>> raidChapMap = new HashMap<Integer, List<BaseRaidInfo>>();
	Map<Integer, List<BaseRaidInfo>> eliRaidChapMap = new HashMap<Integer, List<BaseRaidInfo>>();
	Map<Integer, BaseRaidInfo> raidMap = new HashMap<Integer, BaseRaidInfo>();

	// private final static int POWER_EAXY = 5;

	// private final static int POWER_NORM = 8;
	// private final static int POWER_DIFF = 10;

	private RaidRes() {
		classz = BaseRaidInfo.class;
	}

	public void clear() {
		super.clear();
		raidChapMap.clear();
		eliRaidChapMap.clear();
		raidMap.clear();
	}

	private static RaidRes INSTANCE = new RaidRes();

	public static RaidRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("RaidRes.otherInit");
		for (BaseRaidInfo baseRaidInfo : RaidRes.getInstance().getDataList()) {

			if (baseRaidInfo.getHero() != null) {
				if (baseRaidInfo.getHero().indexOf(';') > -1) {
					String[] ss = baseRaidInfo.getHero().split(";");
					int[] heros = new int[ss.length];
					for (int i = 0; i < ss.length; i++) {
						heros[i] = Integer.valueOf(ss[i]);
					}
					baseRaidInfo.setHeros(heros);

				} else if ("0".equals(baseRaidInfo.getHero())) {
					// 不设值
				} else {
					int[] heros = new int[1];
					heros[0] = Integer.valueOf(baseRaidInfo.getHero());
					baseRaidInfo.setHeros(heros);
				}
			}

			baseRaidInfo.setDiff(RAID_DIFF_EASY);
			List<Integer> setMonsIds1 = new ArrayList<Integer>();
			int[] intArr = Tool.strToIntArr(baseRaidInfo.getFile1(), ";");

			if (intArr != null && intArr.length > 0) {
				for (int value : intArr) {
					setMonsIds1.add(value);
				}
			}

			baseRaidInfo.setSetMonsIds1(setMonsIds1);

			intArr = Tool.strToIntArr(baseRaidInfo.getFile2(), ";");

			List<Integer> setMonsIds2 = new ArrayList<Integer>();
			if (intArr != null && intArr.length > 0) {
				for (int value : intArr) {
					setMonsIds2.add(value);
				}
			}
			baseRaidInfo.setSetMonsIds2(setMonsIds2);

			intArr = Tool.strToIntArr(baseRaidInfo.getFile3(), ";");
			List<Integer> setMonsIds3 = new ArrayList<Integer>();
			if (intArr != null && intArr.length > 0) {
				for (int value : intArr) {
					setMonsIds3.add(value);
				}
			}

			baseRaidInfo.setSetMonsIds3(setMonsIds3);

			List<BaseRaidInfo> list = null;
			if (RAID_MODE_NOR == baseRaidInfo.getType()) {
				list = raidChapMap.get(baseRaidInfo.getChapterid());
				if (list == null) {
					list = new ArrayList<BaseRaidInfo>();
					raidChapMap.put(baseRaidInfo.getChapterid(), list);
				}

				// 副本最大章节
				if (MAX_CHARPTER < baseRaidInfo.getChapterid()) {
					MAX_CHARPTER = baseRaidInfo.getChapterid();
				}
				list.add(baseRaidInfo);
			} else if (RAID_MODE_ELI == baseRaidInfo.getType()) {
				list = eliRaidChapMap.get(baseRaidInfo.getChapterid());
				if (list == null) {
					list = new ArrayList<BaseRaidInfo>();
					eliRaidChapMap.put(baseRaidInfo.getChapterid(), list);
				}
				// 精英副本最大章节
				if (MAX_CLICHARPTER < baseRaidInfo.getChapterid()) {
					MAX_CLICHARPTER = baseRaidInfo.getChapterid();
				}
				list.add(baseRaidInfo);
			}
			raidMap.put(baseRaidInfo.getRaidid(), baseRaidInfo);
		}
	}

	public List<BaseRaidInfo> getRaidListByChapterId(int chapterId, int mode) {
		if (RAID_MODE_NOR == mode) {
			return raidChapMap.get(chapterId);
		} else {
			return eliRaidChapMap.get(chapterId);
		}
	}

	/**
	 * 取下一个关卡
	 * 
	 * @param raidid
	 * @return
	 */
	public BaseRaidInfo getNextBaseRaidInfo(int raidid, int mode) {
		for (BaseRaidInfo baseRaidInfo : RaidRes.getInstance().getDataList()) {
			if (baseRaidInfo.getReq_raid() == raidid && baseRaidInfo.getType() == mode) {
				return baseRaidInfo;
			}
		}
		return null;
	}

	public BaseRaidInfo getBaseRaidInfo(int raidid) {
		return raidMap.get(raidid);
	}

	public static void main(String[] args) throws Exception {
		RaidRes.getInstance().loadFile(RaidRes.Path);
		RewardRes.getInstance().loadFile(RewardRes.Path);

		// System.out.println(RaidRes.getInstance().getNextBaseRaidInfo(10101, RaidRes.RAID_MODE_ELI));
		// for (BaseRaidInfo baseRaidInfo : RaidRes.getInstance().getDataList()) {
		// System.out.println(baseRaidInfo.getRaidid() + "," + baseRaidInfo.getDec() + "," + baseRaidInfo.getHeros());
		// if (baseRaidInfo.getHeros() != null) {
		// for (int value : baseRaidInfo.getHeros()) {
		// System.out.print(" 助阵英雄id" + value);
		// }
		// System.out.println();
		// }
		// }
		for (BaseRaidInfo baseRaidInfo : RaidRes.getInstance().getDataList()) {
			if (null == RewardRes.getInstance().getRewardRateGroup(baseRaidInfo.getReward())) {
				System.out.println("baseRaidInfo = " + baseRaidInfo.getRaidid());
			}
		}
	}
}
