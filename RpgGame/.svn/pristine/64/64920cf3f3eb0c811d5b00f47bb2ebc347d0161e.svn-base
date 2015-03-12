package com.dh.Cache;

import java.util.List;
import java.util.Set;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;

import com.dh.game.constant.RedisKey;
import com.dh.game.vo.user.PlayerArenaVO;

public class RedisSortSet {

	public static void addArenaList(List<PlayerArenaVO> list) {
		if (list == null || list.size() == 0) {
			return;
		}

		ShardedJedis sharding = JedisTool.getResource();
		ShardedJedisPipeline pipeline = sharding.pipelined();
		for (PlayerArenaVO playerArenaVO : list) {
			pipeline.zadd(RedisKey.ARENA_SORTLIST, playerArenaVO.getOrdernum(), String.valueOf(playerArenaVO.getPlayerId()));
		}
		@SuppressWarnings("unused")
		List<Object> results = pipeline.syncAndReturnAll();
		JedisTool.returnJedis(sharding);
	}

	/**
	 * 向集合中增加一条记录,如果这个值已存在，这个值对应的权重将被置为新的权重
	 * 
	 * @param String
	 *            key
	 * @param double score 权重
	 * @param String
	 *            member 要加入的值，
	 * @return 状态码 1成功，0已存在member的值
	 * */
	public static long zadd(String key, double score, String member) {
		ShardedJedis sharding = JedisTool.getResource();
		long s = sharding.zadd(key, score, member);
		JedisTool.returnJedis(sharding);
		return s;
	}

	/**
	 * 获取集合中元素的数量
	 * 
	 * @param String
	 *            key
	 * @return 如果返回0则集合不存在
	 * */
	public static long zcard(String key) {
		ShardedJedis sharding = JedisTool.getResource();
		long len = sharding.zcard(key);
		JedisTool.returnJedis(sharding);
		return len;
	}

	/**
	 * 返回指定位置的集合元素,0为第一个元素，-1为最后一个元素
	 * 
	 * @param String
	 *            key
	 * @param int start 开始位置(包含)
	 * @param int end 结束位置(包含)
	 * @return Set<String>
	 * */
	public static Set<String> zrange(String key, int start, int end) {
		ShardedJedis sharding = JedisTool.getResource();
		Set<String> set = sharding.zrange(key, start, end);
		JedisTool.returnJedis(sharding);
		return set;
	}

	/**
	 * 返回指定位置的集合元素,0为第一个元素，-1为最后一个元素
	 * 
	 * @param String
	 *            key
	 * @param int start 开始位置(包含)
	 * @param int end 结束位置(包含)
	 * @param offset从第几个位置开始
	 * @param count数量
	 * @return Set<String>
	 * */
	public static Set<String> zrangeByScore(String key, int min, int max, int offset, int count) {
		ShardedJedis sharding = JedisTool.getResource();
		Set<String> set = sharding.zrangeByScore(key, min, max, offset, count);
		JedisTool.returnJedis(sharding);
		return set;
	}

	/**
	 * 返回指定权重区间的元素集合
	 * 
	 * @param String
	 *            key
	 * @param double min 上限权重
	 * @param double max 下限权重
	 * @return Set<String>
	 * */
	public static Set<String> zrangeByScore(String key, double min, double max) {
		ShardedJedis sharding = JedisTool.getResource();
		Set<String> set = sharding.zrangeByScore(key, min, max);
		JedisTool.returnJedis(sharding);
		return set;
	}

	/**
	 * 从集合中删除成员
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            member
	 * @return 返回1成功
	 * */
	public static long zrem(String key, String member) {
		ShardedJedis sharding = JedisTool.getResource();
		long s = sharding.zrem(key, member);
		JedisTool.returnJedis(sharding);
		return s;
	}

	/**
	 * 删除
	 * 
	 * @param key
	 * @return
	 */
	public static long zrem(String key) {
		ShardedJedis sharding = JedisTool.getResource();
		long s = sharding.del(key);
		JedisTool.returnJedis(sharding);
		return s;
	}

	/**
	 * 删除给定权重区间的元素
	 * 
	 * @param String
	 *            key
	 * @param double min 下限权重(包含)
	 * @param double max 上限权重(包含)
	 * @return 删除的数量
	 * */
	public static long zremrangeByScore(String key, int min, int max) {
		ShardedJedis sharding = JedisTool.getResource();
		long s = sharding.zremrangeByScore(key, min, max);
		JedisTool.returnJedis(sharding);
		return s;
	}

	/**
	 * 删除给定位置区间的元素
	 * 
	 * @param String
	 *            key
	 * @param int start 开始区间，从0开始(包含)
	 * @param int end 结束区间,-1为最后一个元素(包含)
	 * @return 删除的数量
	 * */
	public long zremrangeByRank(String key, int start, int end) {
		ShardedJedis sharding = JedisTool.getResource();
		long s = sharding.zremrangeByRank(key, start, end);
		JedisTool.returnJedis(sharding);
		return s;
	}

	/**
	 * 获取指定值在集合中的位置，集合排序从低到高
	 * 
	 * @see zrevrank
	 * @param String
	 *            key
	 * @param String
	 *            member
	 * @return long 位置
	 * */
	public static long zrank(String key, String member) {
		ShardedJedis sharding = JedisTool.getResource();
		long index = sharding.zrank(key, member);
		JedisTool.returnJedis(sharding);
		return index;
	}

	/**
	 * 获取指定权重区间内集合的数量
	 * 
	 * @param String
	 *            key
	 * @param double min 最小排序位置
	 * @param double max 最大排序位置
	 * */
	public static long zcount(String key, double min, double max) {
		ShardedJedis sharding = JedisTool.getResource();
		long len = sharding.zcount(key, min, max);
		JedisTool.returnJedis(sharding);
		return len;
	}

	/**
	 * 总数
	 * 
	 * @param key
	 * @return
	 */
	public static long zcount(String key) {
		ShardedJedis sharding = JedisTool.getResource();
		long len = sharding.zcount(key, "-inf", "+inf");
		JedisTool.returnJedis(sharding);
		return len;
	}

}