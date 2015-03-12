package com.dh.Cache;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;

import com.alibaba.fastjson.JSON;
import com.dh.constants.StreetConstants;
import com.dh.game.constant.RedisKey;
import com.dh.game.vo.user.PlayerAccountVO;
import com.dh.game.vo.user.PlayerArenaVO;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.game.vo.user.PlayerVO;
import com.dh.game.vo.user.StreetResPlayerVO;
import com.dh.game.vo.user.StreetResVO;
import com.dh.main.InitLoad;

public class RedisMap {
	private final static Logger logger = Logger.getLogger(RedisMap.class);

	public static void clearRedis(List<PlayerVO> playerVOList) {
		// ShardedJedis sharding = JedisTool.getResource();
		// for (PlayerVO playerVO : playerVOList) {
		// ShardedJedisPipeline pipeline = sharding.pipelined();
		// Response<Set<String>> fields =
		// pipeline.hkeys(RedisKey.HERO_DEF_MAP+playerVO.getPlayerId());
		// pipeline.hset(RedisKey.ARENA_MAP,
		// String.valueOf(playerVO.getPlayerId()),
		// JSON.toJSONString(playerArenaVO));
		// }
		// @SuppressWarnings("unused")
		// List<Object> results = pipeline.syncAndReturnAll();
		//

		// JedisTool.returnJedis(sharding);
	}

	public static void updatePlayerHero(PlayerHeroVO playerHeroVO) {
		String heroStr = JSON.toJSONString(playerHeroVO);
		RedisMap.hset(JedisTool.createCombinedKey(RedisKey.PLAYER_HERO, playerHeroVO.getPlayerId()), String.valueOf(playerHeroVO.getId()), heroStr);
	}

	public static void updatePlayerAccount(PlayerAccountVO playerAccountVO) {
		RedisMap.hset(RedisKey.PLAYER_ACCOUNT_MAP, playerAccountVO.getPlayerId() + "", JSON.toJSONString(playerAccountVO));
	}

	public static PlayerAccountVO getPlayerAccount(int playerId) {
		String accountStr = RedisMap.hget(RedisKey.PLAYER_ACCOUNT_MAP, playerId + "");
		if (accountStr == null) {
			return null;
		}
		return JSON.parseObject(accountStr, PlayerAccountVO.class);
	}

	public static void addArenaList(List<PlayerArenaVO> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		ShardedJedis sharding = JedisTool.getResource();
		ShardedJedisPipeline pipeline = sharding.pipelined();
		for (PlayerArenaVO playerArenaVO : list) {
			pipeline.hset(RedisKey.ARENA_MAP, String.valueOf(playerArenaVO.getPlayerId()), JSON.toJSONString(playerArenaVO));
		}
		@SuppressWarnings("unused")
		List<Object> results = pipeline.syncAndReturnAll();
		JedisTool.returnJedis(sharding);
	}

	/**
	 * 将数据库数据加载到Redis中
	 * 
	 * @param list
	 */
	public static void addPlayerList(List<PlayerVO> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		ShardedJedis sharding = JedisTool.getResource();
		ShardedJedisPipeline pipeline = sharding.pipelined();
		for (PlayerVO playerVO : list) {
			pipeline.hset(RedisKey.PLAYERVO_MAP, String.valueOf(playerVO.getPlayerId()), JSON.toJSONString(playerVO));
			pipeline.hset(RedisKey.NAME_MAP, playerVO.getUserName(), String.valueOf(playerVO.getPlayerId()));
			pipeline.hset(RedisKey.NICK_MAP, playerVO.getName(), String.valueOf(playerVO.getPlayerId()));
			pipeline.zadd(RedisKey.PLAYERLEVEL_SORTLIST, playerVO.getLevel(), String.valueOf(playerVO.getPlayerId()));

		}
		@SuppressWarnings("unused")
		List<Object> results = pipeline.syncAndReturnAll();
		JedisTool.returnJedis(sharding);
	}

	public static void setResPlayerVO(String key, StreetResPlayerVO streetResPlayerVO) {
		ShardedJedis sharding = JedisTool.getResource();
		sharding.hset(key + streetResPlayerVO.getId(), String.valueOf(streetResPlayerVO.getPlayerId()), JSON.toJSONString(streetResPlayerVO));
		JedisTool.returnJedis(sharding);
	}

	public static void delResPlayerVO(String key, StreetResPlayerVO streetResPlayerVO) {
		hdel(key + streetResPlayerVO.getId(), String.valueOf(streetResPlayerVO.getPlayerId()));
	}

	public static Object[] getStreetResPlayerVO(int playerId, int resId) {
		String key = RedisKey.STREET_RES_FREE_PLAYER;
		StreetResPlayerVO streetResPlayerVO = RedisMap.getStreetResPlayerVO(key, resId, playerId);
		if (streetResPlayerVO == null) {
			key = RedisKey.STREET_RES_BATTLE_PLAYER;
			streetResPlayerVO = RedisMap.getStreetResPlayerVO(key, resId, playerId);
			if (streetResPlayerVO == null) {
				key = RedisKey.STREET_RES_PRODUCE_PLAYER;
				streetResPlayerVO = RedisMap.getStreetResPlayerVO(key, resId, playerId);
			}
		}
		return new Object[] { streetResPlayerVO, key };

	}

	/**
	 * 过滤出符合条件的玩家
	 * 
	 * @param resId
	 * @param level
	 * @return
	 */
	public synchronized static StreetResPlayerVO filterResPlayerVO(int resId, int level, int playerId) {
		boolean isOver = getRandomLevel(level);
		int minLevel = StreetConstants.STREET_OPEN_LEVEL;
		int maxLevel = StreetConstants.ROBOT_TOTAL_LEVEL;
		if (!isOver) {
			minLevel = Math.max(StreetConstants.STREET_OPEN_LEVEL, level - 10);
			maxLevel = Math.min(StreetConstants.ROBOT_TOTAL_LEVEL, level + 10);
		}
		String key = RedisKey.STREET_RES_PRODUCE_PLAYER + resId;
		if (hlen(key) == 0) {
			return null;
		}
		List<String> listStr = hvals(key);

		StreetResPlayerVO targetPlayer = null;
		ListIterator<String> it = listStr.listIterator();

		int index = 0;
		int rate = InitLoad.DEBUG_BETA ? 1 : 3;
		while (it.hasNext()) {
			String string = it.next();
			// if (index++ % 1 == 0) {// 为了避免争夺太厉害,这里使用3作为稀释因子
			if (index++ % (rate) == 0) {// 为了避免争夺太厉害,这里使用3作为稀释因子
				StreetResPlayerVO streetResPlayerVO = JSON.parseObject(string, StreetResPlayerVO.class);
				if (streetResPlayerVO.getPlayerId() == playerId) {
					logger.error("江湖匹配数据异常,遇到相同playerId" + playerId);
					continue;
				}

				if (!isOver && (streetResPlayerVO.getLevel() < minLevel || streetResPlayerVO.getLevel() > maxLevel)) {// 如果进入随机模式则不需要看等级
					continue;
				}

				if (streetResPlayerVO.getStatus() != StreetConstants.PLAYER_RES_STATUS_PRODUCING) {
					continue;
				}

				if (StreetConstants.hasOvertime(streetResPlayerVO.getStartTime())) {
					continue;
				}

				if (System.currentTimeMillis() - streetResPlayerVO.getStartTime() * 1000L >= StreetConstants.RES_HUNT_DEADLINE) {
					continue;
				}
				targetPlayer = streetResPlayerVO;
				break;
			}

		}

		return targetPlayer;

	}

	public static boolean getRandomLevel(int level) {
		int div = StreetConstants.ROBOT_TOTAL_LEVEL + StreetConstants.STREET_OPEN_LEVEL - level;
		if (div <= 0) {
			return false;
		}
		if (Math.random() < Math.log10(Math.sqrt(div))) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 转移到战斗队列
	 * 
	 * @param red
	 * @param blue
	 */
	public static void preStreetBattle(StreetResPlayerVO red, StreetResPlayerVO blue) {
		String key = RedisKey.STREET_RES_PRODUCE_PLAYER;
		String battleKey = RedisKey.STREET_RES_BATTLE_PLAYER;
		if (blue.getPlayerId() > 0) {
			RedisMap.delResPlayerVO(key, blue);
		}
		RedisMap.delResPlayerVO(RedisKey.STREET_RES_FREE_PLAYER, red);
		setResPlayerVO(battleKey, blue);// 加入战斗队列
		setResPlayerVO(battleKey, red);
	}

	public static StreetResPlayerVO getStreetResPlayerVO(String redisKey, int resId, int playerId) {
		String str = hget(redisKey + resId, String.valueOf(playerId));
		return JSON.parseObject(str, StreetResPlayerVO.class);
	}

	public static StreetResVO getStreetResByPIdAndRId(int resId, int playerId) {
		String text = hget(RedisKey.PLAYER_STREET_RES + resId, String.valueOf(playerId));
		StreetResVO streetresVO = JSON.parseObject(text, StreetResVO.class);
		return streetresVO;
	}

	public static List<PlayerHeroVO> getPlayerHeroByResId(int playerId, int resId) {
		List<PlayerHeroVO> heros = hvalPlayerHeroVOs(playerId);
		List<PlayerHeroVO> tempHeros = new ArrayList<PlayerHeroVO>();
		for (PlayerHeroVO playerHeroVO : heros) {
			if (playerHeroVO.getLineStatus() == resId) {
				tempHeros.add(playerHeroVO);
			}
		}
		return tempHeros;
	}

	public static void addPlayerHero(PlayerHeroVO playerHeroVO) {
		String heroStr = JSON.toJSONString(playerHeroVO);
		RedisMap.hset(JedisTool.createCombinedKey(RedisKey.PLAYER_HERO, playerHeroVO.getPlayerId()), String.valueOf(playerHeroVO.getId()), heroStr);
	}

	/**
	 * 添加一个对应关系
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid
	 * @param String
	 *            value
	 * @return 状态码 1成功，0失败，fieid已存在将更新，也返回0
	 * **/
	public static long hset(String key, String fieid, String value) {
		ShardedJedis sharding = JedisTool.getResource();
		long s = sharding.hset(key, fieid, value);
		JedisTool.returnJedis(sharding);
		return s;
	}

	/**
	 * 从hash中删除指定的存储
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid 存储的名字
	 * @return 状态码，1成功，0失败
	 * */
	public static long hdel(String key, String fieid) {
		ShardedJedis sharding = JedisTool.getResource();
		long s = sharding.hdel(key, fieid);
		JedisTool.returnJedis(sharding);
		return s;
	}

	/**
	 * 测试hash中指定的存储是否存在
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid 存储的名字
	 * @return 1存在，0不存在
	 * */
	public static boolean hexists(String key, String fieid) {
		ShardedJedis sharding = JedisTool.getResource();
		boolean s = sharding.hexists(key, fieid);
		JedisTool.returnJedis(sharding);
		return s;
	}

	/**
	 * 返回hash中指定存储位置的值
	 * 
	 * @param String
	 *            key
	 * @param String
	 *            fieid 存储的名字
	 * @return 存储对应的值
	 * */
	public static String hget(String key, String fieid) {
		ShardedJedis sharding = JedisTool.getResource();
		String s = sharding.hget(key, fieid);
		JedisTool.returnJedis(sharding);
		return s;
	}

	public static List<String> hvals(String key) {
		ShardedJedis sharding = JedisTool.getResource();
		List<String> s = sharding.hvals(key);
		JedisTool.returnJedis(sharding);
		return s;
	}

	/**
	 * 获得玩家所有英雄
	 * 
	 * @param key
	 * @return
	 */
	public static List<PlayerHeroVO> hvalPlayerHeroVOs(int playerId) {
		List<String> listStr = hvals(JedisTool.createCombinedKey(RedisKey.PLAYER_HERO, playerId));
		ArrayList<PlayerHeroVO> heros = new ArrayList<PlayerHeroVO>();
		for (String string : listStr) {
			PlayerHeroVO hero = JSON.parseObject(string, PlayerHeroVO.class);
			heros.add(hero);
		}
		return heros;
	}

	public static PlayerVO getPlayerVOById(int playerId) throws Exception {
		return getPlayerVObyId(playerId);
	}

	/**
	 * @param playerId
	 * @return
	 */
	public static PlayerVO getPlayerVObyId(int playerId) {
		String text = hget(RedisKey.PLAYERVO_MAP, String.valueOf(playerId));

		return JSON.parseObject(text, PlayerVO.class);
	}

	public static List<PlayerArenaVO> hgetPlayerArenaVOList(Set<String> set) {
		if (set == null || set.size() == 0) {
			return null;
		}
		ShardedJedis sharding = JedisTool.getResource();
		ShardedJedisPipeline pipeline = sharding.pipelined();
		for (String str : set) {
			pipeline.hget(RedisKey.ARENA_MAP, str);
		}
		List<Object> results = pipeline.syncAndReturnAll();
		JedisTool.returnJedis(sharding);

		if (results == null) {
			return null;
		}

		List<PlayerArenaVO> rr = new ArrayList<PlayerArenaVO>();
		for (Object obj : results) {
			rr.add(JSON.parseObject(obj.toString(), PlayerArenaVO.class));
		}

		return rr;
	}

	public static void hgetPlayerArenaVOList(List<PlayerArenaVO> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		ShardedJedis sharding = JedisTool.getResource();
		ShardedJedisPipeline pipeline = sharding.pipelined();
		for (PlayerArenaVO playerArenaVO : list) {
			String strArena = JSON.toJSONString(playerArenaVO);
			RedisMap.hset(RedisKey.ARENA_MAP, String.valueOf(playerArenaVO.getPlayerId()), strArena);
		}
		@SuppressWarnings("unused")
		List<Object> results = pipeline.syncAndReturnAll();
		JedisTool.returnJedis(sharding);
	}

	// public static List<PlayerHeroVO> getPlayerHeros(int playerId) {
	// String herosStr =
	// JedisTool.get(JedisTool.createCombinedKey(RedisKey.PLAYER_HERO,
	// playerId));
	// List<PlayerHeroVO> heros = JSON.parseArray(herosStr, PlayerHeroVO.class);
	// return heros;
	// }

	/**
	 * 通过玩家id和英雄id获得英雄 <br/>
	 * 2014年7月24日
	 * 
	 * @author dingqu-pc100
	 */
	public static PlayerHeroVO getPlayerHero(int playerId, int heroId) {
		String heroStr = hget(JedisTool.createCombinedKey(RedisKey.PLAYER_HERO, playerId), String.valueOf(heroId));
		return JSON.parseObject(heroStr, PlayerHeroVO.class);
	}

	public static void hAddPlayerHeros(List<PlayerHeroVO> list) {
		ShardedJedis sharding = JedisTool.getResource();
		for (PlayerHeroVO playerHeroVO : list) {
			String strHero = JSON.toJSONString(playerHeroVO);
			sharding.hset(JedisTool.createCombinedKey(RedisKey.PLAYER_HERO, playerHeroVO.getPlayerId()), String.valueOf(playerHeroVO.getId()), strHero);
		}
		JedisTool.returnJedis(sharding);
	}

	/**
	 * 获取hash中存储的个数，类似Map中size方法
	 * 
	 * @param String
	 *            key
	 * @return long 存储的个数
	 * */
	public static long hlen(String key) {
		ShardedJedis sharding = JedisTool.getResource();
		long len = sharding.hlen(key);
		JedisTool.returnJedis(sharding);
		return len;
	}

	public static void main(String[] args) {
		System.out.println(RedisMap.hget(RedisKey.NICK_MAP, "郁闺钡"));

		// List<PlayerHeroVO> heros = RedisMap.hvalPlayerHeroVOs(1626106);
		// for (PlayerHeroVO playerHeroVO : heros) {
		// System.out.println(playerHeroVO.getId());
		// }
		// for (PlayerHeroVO playerHeroVO : heros) {
		// playerHeroVO.setLineStatus(0);
		// RedisMap.updatePlayerHero(playerHeroVO);
		// }
		//
		// RedisMap.hdel(RedisKey.RES_PLAYERVO + 31, String.valueOf(1001355));
		// RedisMap.hdel(RedisKey.RES_PLAYERVO + 40, String.valueOf(1001355));
		// RedisMap.hdel(RedisKey.RES_PLAYERVO + 31, String.valueOf(1621498));
		// RedisMap.hdel(RedisKey.RES_PLAYERVO + 40, String.valueOf(1621498));
		// RedisMap.hdel(RedisKey.RES_PLAYERVO + 40, String.valueOf(1621499));
		// RedisMap.hdel(RedisKey.RES_PLAYERVO + 40, String.valueOf(1621499));
		// RedisMap.hdel(RedisKey.RES_PLAYERVO + 31, String.valueOf(1001356));
		// RedisMap.hdel(RedisKey.RES_PLAYERVO + 40, String.valueOf(1001356));
		// for (int i = 0; i < 80; i++) {
		// List<String> strings = RedisMap.hvals(RedisKey.RES_PLAYERVO + i);
		// if (!strings.isEmpty()) {
		// System.out.println(strings);
		// }
		//
		// // RedisMap.hdel(RedisKey.RES_PLAYERVO+i, fieid)
		// }

	}

}
