package com.dh.service;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.dh.Cache.RedisSortSet;
import com.dh.game.constant.RedisKey;
import com.dh.util.RandomUtil;

/**
 * 排行榜
 * 
 * @author RickYu
 * 
 */
@Component
public class RankService {

	public static int getMyRankByLevel(int playerid) {
		return (int) RedisSortSet.zrank(RedisKey.PLAYERLEVEL_SORTLIST, String.valueOf(playerid));
	}

	/**
	 * 随机抽一个等级比自己低value级的人
	 * 
	 * @param level
	 * @param value
	 * @return
	 */
	public static int getLessMyLevel(int minLevel, int maxLevel) {
		Set<String> set = RedisSortSet.zrangeByScore(RedisKey.PLAYERLEVEL_SORTLIST, minLevel, maxLevel);
		if (set == null || set.size() == 0) {
			int cc = (int) RedisSortSet.zcard(RedisKey.PLAYERLEVEL_SORTLIST);
			int n = RandomUtil.randomInt(cc);
			set = RedisSortSet.zrange(RedisKey.PLAYERLEVEL_SORTLIST, n, n);
		}
		int size = set.size();
		int n = 0;
		if (set.size() > 1) {
			n = RandomUtil.randomInt(size);
		}

		int i = 0;
		for (String strJson : set) {
			if (i == n) {
				return Integer.valueOf(strJson);
			}
		}

		return 0;
	}

	public static void main(String[] args) {
		System.out.println(RankService.getLessMyLevel(39, 40));
	}

}
