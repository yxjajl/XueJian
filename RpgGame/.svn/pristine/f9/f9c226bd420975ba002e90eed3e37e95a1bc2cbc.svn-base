package com.dh.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dh.game.vo.base.BaseMonsterVO;
import com.dh.game.vo.raid.RaidProto.FinalMonster;
import com.dh.game.vo.raid.RaidProto.RaidPrepareResponse;
import com.dh.resconfig.MonsterRes;

public class GameUtil {

	public static int calHeroToExp(int star, int maxExcp) {
		int heroexpc = 0;
		heroexpc = (int) (10 * Math.pow(star, 4.5) + maxExcp / 5);
		return heroexpc;
	}

	public static int calPowerBuyCost(int time) {
		return 20 * time;
	}

	public static String getIpString(String ip) {
		return ip.substring(ip.indexOf("/") + 1, ip.indexOf(":"));
	}

	// 根据CD和开始时间计算剩余时间
	// 返回结果 单位(秒)
	public static int getRemainTime(long curTime, Date oldDate, long cd) {
		if (oldDate == null || cd == 0) {
			return 0;
		}
		long remainTime = cd - (curTime - oldDate.getTime());
		if (remainTime < 0) {
			remainTime = 0;
		}
		return ((int) (remainTime / 1000));
	}

	/**
	 * 根据结束时间计算声音剩余时间
	 * 
	 * @param curTime
	 * @param endDate
	 * @return//返回结果 单位(秒)
	 */
	public static int getRemainTime(long curTime, Date endDate) {
		long endTime = endDate.getTime();
		if (curTime >= endTime) {
			return 0;
		}

		return ((int) ((endTime - curTime) / 1000));
	}

	public static int getRemainTime(long curTime, long endTime) {
		if (curTime >= endTime) {
			return 0;
		}

		return ((int) ((endTime - curTime) / 1000));
	}

	/**
	 * 计算每日定时刷新的下一刷新时间
	 * 
	 * @param hour
	 * @param min
	 * @return
	 */
	public static long computerNextReFreshTime(int hour, int min) {
		long curTime = System.currentTimeMillis();
		Calendar ca = Calendar.getInstance();
		ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH), hour, min, 0);
		if (ca.getTimeInMillis() <= curTime) {
			ca.add(Calendar.DAY_OF_MONTH, 1);
		}
		return ca.getTimeInMillis();
	}

	public static long computerNextReFreshTime2(long startTime, long interval) {
		long curTime = System.currentTimeMillis();
		while ((startTime + interval) < curTime) {
			startTime += interval;
		}

		return startTime + interval;
	}

	/**
	 * 根据指定的刷新小时队列，计算下次的刷新时间
	 * 
	 * @param hour
	 * @return
	 */
	public static Date computerNextReFreshTime3(int[] hour) {
		Calendar ca = Calendar.getInstance();
		boolean isCross = true;
		int thisDayHour = ca.get(Calendar.HOUR_OF_DAY);
		int nextHour = hour[0];
		for (int value : hour) {
			if (thisDayHour < value) {
				nextHour = value;
				isCross = false;
			}
		}

		ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH), nextHour, 0, 0);
		if (isCross) {
			ca.add(Calendar.DAY_OF_MONTH, 1);
		}
		return ca.getTime();
	}

	/**
	 * 夺宝概率
	 * 
	 * @param myLevel
	 * @param otherLevel
	 * @return
	 */
	public static int getGrabRate(int myLevel, int otherLevel) {
		int rate = (50 + otherLevel - myLevel);

		rate = Math.max(20, rate);
		rate = Math.min(80, rate);
		return rate;

	}

	public static List<Integer> getRandomString(int min, int max, int n, int myorder) {
		int randomMax = max - min + 1;
		List<Integer> result = new ArrayList<Integer>();
		if (max - min <= 5) {
			for (int i = min; i <= max; i++) {
				if (i != myorder) {
					result.add(i);
				}
			}
			return result;
		}

		int m = 0;
		int random = 0;
		while (m < n) {
			random = RandomUtil.randomInt(randomMax) + min;
			if (random == myorder) {
				continue;
			}
			if (!result.contains(random)) {
				result.add(random);
				m++;
			}
		}
		return result;
	}

	public static void addMonster(RaidPrepareResponse.Builder raidPrepareResponse, final List<Integer> list) {
		if (list != null && list.size() > 0) {
			FinalMonster.Builder finalMonster = FinalMonster.newBuilder();
			for (int value : list) {
				finalMonster.addFinalHero(MonsterRes.getInstance().getBaseMonsterVO(value).getFinalHero());
			}
			raidPrepareResponse.addFinalMonster(finalMonster);
		}
	}

	public static void addBaseMonster(RaidPrepareResponse.Builder raidPrepareResponse, final List<BaseMonsterVO> list) {
		if (list != null) {
			FinalMonster.Builder finalMonster = FinalMonster.newBuilder();
			for (BaseMonsterVO baseMonsterVO : list) {
				finalMonster.addFinalHero(baseMonsterVO.getFinalHero());
			}
			raidPrepareResponse.addFinalMonster(finalMonster);
		}
	}

	public static void addBaseMonster(RaidPrepareResponse.Builder raidPrepareResponse, BaseMonsterVO baseMonsterVO) {
		if (baseMonsterVO != null) {
			FinalMonster.Builder finalMonster = FinalMonster.newBuilder();
			finalMonster.addFinalHero(baseMonsterVO.getFinalHero());
			raidPrepareResponse.addFinalMonster(finalMonster);
		}
	}

	public static void main(String[] args) {
		// System.out.println(new Date(computerNextReFreshTime2(1403107200866L,
		// 3 * 24 * 3600 * 1000L)));
		System.out.println(getGrabRate(20, 10));
	}
}
