package com.dh.Cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;

import com.dh.game.constant.RedisKey;

public class JedisTool {

	public static String TSTPSW = "asdf1234";
	// public static String serverip = "127.0.0.1";
	public static int dbIndex = 2;

	public static String serverip = "192.168.0.121"; // 211.149.218.52
	// public static String serverip = "211.149.218.52"; // 211.149.218.52

	public static ShardedJedisPool pool;
	public static boolean IS_DEBUG_RELOAD = false;

	// public static Jedis jedis;
	static {

		// jedis = new Jedis(serverip);
		// jedis.auth(TSTPSW);// 验证密码
		// jedis.select(dbIndex);
		String osName = System.getProperty("os.name");
		if (!osName.startsWith("Windows")) {
			serverip = "127.0.0.1";
		}

		JedisShardInfo jsi = new JedisShardInfo(serverip, 6379);
		jsi.setPassword(TSTPSW);
		List<JedisShardInfo> shards = Arrays.asList(jsi); // 使用相同的ip:port,仅作测试

		// Collection<Jedis> js=jsi.get
		// System.out.println(js.size());
		// Iterator<Jedis> it=js.iterator();
		// while(it.hasNext()){
		// Jedis j=it.next();
		// System.out.println(j.get("db0"));
		// j.select(1);
		// System.out.println(j.get("db1"));
		// }

		// jedis = new Jedis("192.168.1.121");
		// sharding = new ShardedJedis(shards);
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(300);
		config.setMaxIdle(100);
		config.setMaxWait(10000);
		config.setTestOnBorrow(false);
		// config.setWhenExhaustedAction(WHEN_EXHAUSTED_GROW);

		pool = new ShardedJedisPool(new JedisPoolConfig(), shards);
		IS_DEBUG_RELOAD = true;
	}

	public static ShardedJedis getResource() {
		ShardedJedis shardedJedis = pool.getResource();
		Collection<Jedis> js = shardedJedis.getAllShards();
		Iterator<Jedis> it = js.iterator();
		while (it.hasNext()) {
			Jedis j = it.next();
			j.select(dbIndex);
		}

		return shardedJedis;
	}

	public static String get(String key) {
		ShardedJedis sharding = JedisTool.getResource();

		String value = sharding.get(key);
		returnJedis(sharding);
		return value;
	}

	// public static int getInt(String key) {
	// String num = get(key);
	// if (condition) {
	//
	// }
	// return Integer.parseInt(get(key));
	// }

	public static String set(String key, String value) {
		ShardedJedis sharding = JedisTool.getResource();
		String status = sharding.set(key, value);
		returnJedis(sharding);
		return status;
	}

	/**
	 * 增量,已经被强转为int
	 * 
	 * @param key
	 * @return
	 */
	public static int incr(String key) {
		ShardedJedis sharding = JedisTool.getResource();
		long count = sharding.incr(key);
		returnJedis(sharding);
		return (int) count;

	}

	public static int add(String key, int num) {
		ShardedJedis sharding = JedisTool.getResource();
		long count = sharding.incrBy(key, num);
		returnJedis(sharding);
		return (int) count;

	}

	public static long del(String key) {
		ShardedJedis sharding = JedisTool.getResource();
		long count = sharding.del(key);
		returnJedis(sharding);
		return count;
	}

	/**
	 * 回收jedis
	 * 
	 * @param jedis
	 */
	public static void returnJedis(ShardedJedis sharding) {
		pool.returnResource(sharding);
	}

	// /**
	// * 通过玩家id获得redisplayerHeroDef<br/>
	// * 2014年7月24日
	// *
	// * @author dingqu-pc100
	// */
	// public static List<PlayerHeroDefVO> getPlayerHeroDefsFromRedis(int
	// playerId) {
	// String HerosMap = get(createCombinedKey(RedisKey.PH, playerId));
	// return JSON.parseArray(HerosMap, PlayerHeroDefVO.class);
	// }

	/**
	 * key: PH_playerId <br/>
	 * 2014年7月23日
	 * 
	 * @author dingqu-pc100
	 */
	public static String createCombinedKey(String redisKey, int playerId) {
		return redisKey + playerId;
	}

	// private static boolean lockkey(String key, int playerId) throws Exception
	// {
	// String PRE_LOCKKEY = "REDIS_LOCK_";
	// String lockKey = PRE_LOCKKEY + key;
	// // checkIsInMulti();
	// long timeout = 2000; // 2秒超时
	// long start = System.currentTimeMillis();
	// for (int i = 0; i < 1000; i++) {
	// jedis.watch(lockKey);
	// if ("0".equals(jedis.get(lockKey))) {
	// Transaction trans = jedis.multi();
	// trans.set(lockKey, "" + playerId);
	// List<Object> response = trans.exec();
	// if (response != null && response.get(0).equals("OK")) {
	// return true;
	// }
	// }
	// if (timeout != 0 && (System.currentTimeMillis() - start) > timeout) {
	// return false;
	// }
	// Thread.sleep(20);
	// }
	// return false;
	// }
	//
	// public static boolean lock(String key, int playerId) throws Exception {
	// String PRE_LOCKKEY = "REDIS_LOCK_";
	// String lockKey = PRE_LOCKKEY + key;
	// for (int i = 0; i < 100; i++) {
	// String strValue = jedis.get(lockKey);
	// if ("0".equals(strValue) && lockkey(key, playerId)) {
	// return true;
	// }
	// Thread.sleep(20);
	// }
	// return false;
	// }
	//
	// public static boolean unlock(String key, int playerId) {
	// String PRE_LOCKKEY = "REDIS_LOCK_";
	// String lockKey = PRE_LOCKKEY + key;
	// String value = jedis.get(lockKey);
	// if (value.equals("0")) {
	// return true;
	// }
	// if (value.equals("" + playerId)) {
	// jedis.set(lockKey, "0");
	// return true;
	// }
	//
	// return false;
	// }
	//
	// public static void clearSomeCache() {
	// Set<String> keyss = jedis.keys("*");
	// for (String string : keyss) {
	// System.out.println(string);
	// if (string.contains(RedisKey.PLAYER_HERO) ||
	// string.contains(RedisKey.PLAYERVO_MAP) ||
	// string.contains(RedisKey.PLAYER_ACCOUNT_MAP)) {
	// del(string);
	// }
	// }
	// }

	public static void main(String[] args) throws Exception {
		JedisShardInfo jsi = new JedisShardInfo(serverip, 6379);
		jsi.setPassword(TSTPSW);
		System.out.println(jsi.createResource().flushAll());
		// Long i = jedis.incr("vincepeng112");
		// Jedis jedis = new Jedis("211.149.218.52");
		// // Jedis jedis = new Jedis(serverip);
		// jedis.auth(TSTPSW);// 验证密码
		// jedis.select(dbIndex);
		// System.out.println(jedis.hlen(RedisKey.PLAYERVO_MAP));
		// System.out.println(jedis.hget(RedisKey.PLAYER_STREET_RES + 40,
		// String.valueOf(1626265)));
		// RedisMap.getStreetResByPIdAndRId(StreetConstants.CENTER_GRID_NUM,
		// streetEnemyVO.getEnemyId())
		// System.out.println(jedis.flushAll());

		// RedisList.rpush("cc", "1");
		// RedisList.rpush("cc", "2");
		// RedisList.rpush("cc", "3");

		// StreetResVO s = new StreetResVO();
		// s.setId(1);
		// // System.out.println(JSON.toJSON(s));
		// StringBuilder sb = new StringBuilder("");
		// s.setMachineLine(sb.toString());
		// System.out.println(s.getMachineLine());
		// Set<String> keyss = jedis.keys("*");
		// int i = 0;
		// for (String string : keyss) {
		// System.out.println(string);
		// if (string.contains(RedisKey.PLAYER_HERO) ||
		// string.contains(RedisKey.PLAYERVO_MAP) ||
		// string.contains(RedisKey.PLAYER_ACCOUNT_MAP)) {
		// jedis.del(string);
		// System.out.println("删除数据:->" + i++);
		// }
		// }
	}

	public static void addMailIds(HashMap<Integer, Integer> map) {
		ShardedJedis sharding = JedisTool.getResource();
		ShardedJedisPipeline pipeline = sharding.pipelined();
		Set<Integer> keys = map.keySet();
		for (Integer integer : keys) {
			pipeline.set(RedisKey.PLAYER_MAIL_ID + integer, map.get(integer) + "");
		}
		pipeline.syncAndReturnAll();
		JedisTool.returnJedis(sharding);
	}
}
