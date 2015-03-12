package com.dh.Cache;

import java.util.List;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dh.game.constant.RedisKey;
import com.dh.game.vo.user.BattleRecordVO;
import com.dh.game.vo.user.MsgNoticeVO;

public class RedisList {
	// 添加竞技场个人战报
	public static void addArenaBattleRecord(List<BattleRecordVO> list) {
		if (list == null || list.size() == 0) {
			return;
		}

		if (null != JedisTool.get(RedisKey.HAVE_BATTLE_RECORED)) {
			return;
		}
		ShardedJedis sharding = JedisTool.getResource();
		ShardedJedisPipeline pipeline = sharding.pipelined();
		for (BattleRecordVO battleRecordVO : list) {
			sharding.del(RedisKey.BATTLE_RECORED + battleRecordVO.getPlayerId());
		}

		for (BattleRecordVO battleRecordVO : list) {
			pipeline.rpush(RedisKey.BATTLE_RECORED + battleRecordVO.getPlayerId(), JSON.toJSONString(battleRecordVO));
		}

		pipeline.set(RedisKey.HAVE_BATTLE_RECORED, RedisKey.HAVE_BATTLE_RECORED);

		@SuppressWarnings("unused")
		List<Object> results = pipeline.syncAndReturnAll();
		JedisTool.returnJedis(sharding);
	}

	public static void addMsgNotice(int playerId, String key, int type, String content) {
		MsgNoticeVO msgNoticeVO = new MsgNoticeVO();
		msgNoticeVO.setType(type);
		msgNoticeVO.setContent(content);
		rpush(key + playerId, JSONObject.toJSONString(msgNoticeVO));
	}

	public static boolean checkHasNoticeAndRemove(String msgKey, int playerId) {
		String key = msgKey + playerId;
		boolean isExist = llen(key) > 0;
		JedisTool.del(key);
		return isExist;

	}

	/**
	 * 返回范围值
	 * 
	 * @param playerId
	 * @return
	 */
	public static List<String> lrange(int playerId, int start, int end) {
		ShardedJedis sharding = JedisTool.getResource();
		String key = RedisKey.BATTLE_RECORED + playerId;
		List<String> list = sharding.lrange(key, start, end);
		JedisTool.returnJedis(sharding);
		return list;
	}

	/**
	 * 取全部记录
	 * 
	 * @param playerId
	 * @return
	 */
	public static List<String> lrange(int playerId) {
		String key = RedisKey.BATTLE_RECORED + playerId;
		ShardedJedis sharding = JedisTool.getResource();
		List<String> list = sharding.lrange(key, 0, -1);
		JedisTool.returnJedis(sharding);
		return list;
	}

	/**
	 * 返回list的大小
	 * 
	 * @param key
	 * @return
	 */
	public static long llen(String key) {
		ShardedJedis sharding = JedisTool.getResource();
		long count = sharding.llen(key);
		JedisTool.returnJedis(sharding);
		return count;
	}

	/**
	 * 移除首元素并返回
	 * 
	 * @param key
	 * @return
	 */
	public static String lpop(String key) {
		ShardedJedis sharding = JedisTool.getResource();
		String value = sharding.lpop(key);
		JedisTool.returnJedis(sharding);
		return value;
	}

	/**
	 * 从尾部插入数据
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static long rpush(String key, String value) {
		ShardedJedis sharding = JedisTool.getResource();

		long count = sharding.rpush(key, value);
		JedisTool.returnJedis(sharding);
		return count;
	}

}
