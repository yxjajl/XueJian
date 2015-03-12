package com.dh.constants;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dh.game.vo.hero.HeroProto.HeroAdvance;
import com.dh.game.vo.hero.HeroProto.HeroRecruitPageResponse;
import com.dh.resconfig.HeroRes;
import com.dh.resconfig.NameRes;
import com.dh.util.RandomUtil;

public class HeroConstants {
	public final static int HERO_MAX_NUM = 200; // 英雄总数上限
	public final static int HERORECRUITMAXTIMES = 5; // 每日银两招蓦次数
	// public final static int HERO_LINESTATUS_RAID = 101;//副本
	// public final static int HERO_LINESTATUS_JJC = 102;//竞技场
	public final static String HERO_PASSIVE_SKILL_INIT = "0-0-0;0-0-0;0-0-0";

	// 招蓦广告
	public final static ArrayList<HeroAdvance> heroAdvanceList = new ArrayList<HeroAdvance>(10);
	public final static ReadWriteLock lock = new ReentrantReadWriteLock(false);

	static {
		for (int i = 0; i < 9; i++) {
			HeroAdvance.Builder heroAdvance = HeroAdvance.newBuilder();
			heroAdvance.setNick(NameRes.getInstance().createRandomNick());
			heroAdvance.setStar(7);
			int n = RandomUtil.randomInt(HeroRes.getInstance().getDataList().size());
			heroAdvance.setHeroName(HeroRes.getInstance().getDataList().get(n).getName());
			heroAdvanceList.add(heroAdvance.build());
		}
	}

	public static void readHeroAdvance(HeroRecruitPageResponse.Builder heroRecruitPageResponse) {
		try {
			lock.readLock().lock();
			for (HeroAdvance heroAdvance : heroAdvanceList) {
				heroRecruitPageResponse.addHeroAdvance(heroAdvance);
			}
		} catch (Exception e) {
		} finally {
			lock.readLock().unlock();
		}
	}

	public static void writeHeroAdvance(String nick, int star, String heroName) {
		try {
			lock.writeLock().lock();
			if (heroAdvanceList.size() >= 10) {
				heroAdvanceList.remove(0);
			}
			HeroAdvance.Builder heroAdvance = HeroAdvance.newBuilder();
			heroAdvance.setNick(nick);
			heroAdvance.setStar(star);
			heroAdvance.setHeroName(heroName);
			heroAdvanceList.add(heroAdvance.build());
		} catch (Exception e) {
		} finally {
			lock.writeLock().unlock();
		}
	}
}
