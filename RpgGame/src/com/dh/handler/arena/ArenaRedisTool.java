package com.dh.handler.arena;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;

import com.alibaba.fastjson.JSON;
import com.dh.Cache.JedisTool;
import com.dh.game.constant.RedisKey;
import com.dh.game.vo.user.PlayerHeroDefVO;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.resconfig.HeroRes;

public class ArenaRedisTool {
	// public static List<PlayerHeroDefVO> getPlayerHeroDefsFromRedis(int playerId) {
	// Set<String> = RedisMap.hget(key, fieid);
	// return JSON.parseArray(HerosMap, PlayerHeroDefVO.class);
	// }

	// 从缓存中取数据
	public static List<PlayerHeroDefVO> getPlayerHeroDefList(int playerId) {
		String key = RedisKey.HERO_DEF_MAP + playerId;
		ShardedJedis sharding = JedisTool.getResource();
		List<String> result = sharding.hvals(key);
		JedisTool.returnJedis(sharding);
		int len = 10;

		if (result != null && result.size() > 10) {
			len = result.size();
		}

		List<PlayerHeroDefVO> phList = new ArrayList<PlayerHeroDefVO>(len);
		for (String str : result) {
			phList.add(JSON.parseObject(str, PlayerHeroDefVO.class));
		}
		return phList;
	}

	// 存英雄镜像数据
	public static void savePlayerHeroVO(int playerId, List<PlayerHeroVO> list) {
		String key = RedisKey.HERO_DEF_MAP + playerId;
		JedisTool.del(key);
		ShardedJedis sharding = JedisTool.getResource();
		ShardedJedisPipeline pipeline = sharding.pipelined();
		int n = 0;
		for (PlayerHeroVO playerHeroVO : list) {
			if (n >= 5) {
				break;
			}
			String strPlayerHeroDefVO = JSON.toJSONString(playerToHeroDefVO(playerHeroVO));
			pipeline.hset(key, String.valueOf(playerHeroVO.getId()), strPlayerHeroDefVO);
		}
		@SuppressWarnings("unused")
		List<Object> results = pipeline.syncAndReturnAll();
		JedisTool.returnJedis(sharding);
	}

	public static void savePlayerHeroDefVO(int playerId, List<PlayerHeroDefVO> list) {
		String key = RedisKey.HERO_DEF_MAP + playerId;
		JedisTool.del(key);
		ShardedJedis sharding = JedisTool.getResource();
		ShardedJedisPipeline pipeline = sharding.pipelined();
		for (PlayerHeroDefVO playerHeroDefVO : list) {
			String strPlayerHeroDefVO = JSON.toJSONString(playerHeroDefVO);
			pipeline.hset(key, String.valueOf(playerHeroDefVO.getId()), strPlayerHeroDefVO);
		}
		@SuppressWarnings("unused")
		List<Object> results = pipeline.syncAndReturnAll();
		JedisTool.returnJedis(sharding);
	}

	public static PlayerHeroDefVO playerToHeroDefVO(PlayerHeroVO playerHeroVO) {
		PlayerHeroDefVO playerHeroDefVO = new PlayerHeroDefVO();
		playerHeroDefVO.setId(playerHeroVO.getId());
		playerHeroDefVO.setName(playerHeroVO.getName());
		playerHeroDefVO.setPlayerId(playerHeroVO.getPlayerId());
		playerHeroDefVO.setCfgId(playerHeroVO.getCfgId());
		playerHeroDefVO.setLevel(playerHeroVO.getLevel());
		if (playerHeroVO.getBaseHeroInfoVO() == null) {
			playerHeroVO.setBaseHeroInfoVO(HeroRes.getInstance().getBaseHeroInfoVO(playerHeroVO.getCfgId()));
		}
		playerHeroDefVO.setStar(playerHeroVO.getBaseHeroInfoVO().getStar());
		playerHeroDefVO.setHp(playerHeroVO.getFinal_hp());
		playerHeroDefVO.setDef(playerHeroVO.getFinal_def());
		playerHeroDefVO.setMdef(playerHeroVO.getFinal_mdef());
		playerHeroDefVO.setAtk(playerHeroVO.getFinal_atk());
		playerHeroDefVO.setMatk(playerHeroVO.getFinal_matk());
		playerHeroDefVO.setHit(playerHeroVO.getFinal_hit());
		playerHeroDefVO.setDodge(playerHeroVO.getFinal_dodge());
		playerHeroDefVO.setCir_rate(playerHeroVO.getFinal_cir_rate());
		playerHeroDefVO.setCombat(playerHeroVO.getCombat());
		playerHeroDefVO.setPassivesSkill(playerHeroVO.getPassivesSkill());
		playerHeroDefVO.setSkillLevel(playerHeroVO.getSkillLevel());
		playerHeroDefVO.setYzhp(playerHeroVO.getYzhp());
		playerHeroDefVO.setYzanger(playerHeroVO.getYzanger());
		return playerHeroDefVO;
	}
}
