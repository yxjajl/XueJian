package com.dh.constants;

/**
 * 通用常量类 2014年4月4日
 */
public class CommonConstants {
	public static final int NAME_LENGTH_MIN = 4;
	public static final int NAME_LENGTH_MAX = 14;
	public static final int PARTCACHE_LIFETIME = 10 * 60 * 60;
	public static final int PLAYERID_START = 1626000;

	// 1-149; 江湖住守 hanstatus = 10
	public static final int STREE_LINE_START = 1;
	public static final int STREE_LINE_END = 149;
	// 150进攻队列 hangstatus = 0;
	public static final int ATK_LINE_START = 150; // 进攻列
	public static final int HERO_LINE_FREE = 0;// 英雄状态空闲

	public final static int HANG_STATUS_NORMAL = 0;// 通用正常状态
	public final static int HANG_STATUS_SLEEP = 1; // 养心殿
	public final static int HANG_STATUS_BUSSI = 2; // 跑商
	public static final int HANG_STATUS_STREET = 10;// 江湖状态

	// 疲劳值上限
	public static final int HUNGRY_UP_LIMIT = 100;
	public static final int POWER_UP_LIMIT = 100;

	// 开启背包格子的时间
	public static final int KNASPACK_STARTGIRD = 32; // 初数格子数
	public static final int KNASPACK_MAX_GIRD = 200;
	public static final long KNASPACK_STAR = 0; // 开背包所需初始时间
	public static final long KNASPACK_STEP = 60000L; // 时间增量
	public static final int KNASPACK_RMB = 50;// 开背包所需元宝

	// 体力相关
	public static final int POWER_ADD_PER_PERIOD = 300;// 每增加一点所需时间,300s

	public final static int MSG_TYPE_STREET_SOMETING = 1;// 江湖系统消息
	public final static int MSG_TYPE_STREET_NOTHING = 0;// 江湖系统消息

	public final static int GUIDE_NUM = 50;
	public final static byte SLIDE_NUM = 00000001;
	public final static int SLIDE_INT_NUM = 0x1;

	/** 各个模块开启等级 */
	public final static int OPEN_LEVEL = 17;// 世界boss开启等级

	/**
	 * 升级:体力增加/级<不受体力上限影响>
	 */
	public final static int calPowerAddByLevel(int oldLevel, int newLevel) {
		int sum = 0;
		for (int level = oldLevel + 1; level <= newLevel; level++) {
			if (level < 21) {
				sum += 20;
			} else if (level < 31) {
				sum += 30;
			} else if (level < 41) {
				sum += 40;
			} else if (level < 51) {
				sum += 50;
			} else {
				sum += 60;
			}
		}
		return sum;
	}

}
