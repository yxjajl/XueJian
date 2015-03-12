package com.dh.sqlexe;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;

import com.alibaba.fastjson.JSON;
import com.dh.Cache.JedisTool;
import com.dh.game.constant.RedisKey;
import com.dh.s2s.queue.BaseQueue;

@Component
public class SqlSaveThread extends Thread {
	private static Logger logger = Logger.getLogger(SqlSaveThread.class);
	private BaseQueue<String> baseQueue = new BaseQueue<String>();

	private SqlSaveThread() {
		super();
	}

	public void run() {
		while (true) {
			try {
				putToRedis();
				TimeUnit.MILLISECONDS.sleep(200);
				// Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("SqlSaveThread.exesql" + e.getMessage(), e);
			}
		}
	}

	public void putSql(int playerId, String sql) {
		SqlVO sqlVO = new SqlVO();
		sqlVO.setPlayerId(playerId);
		sqlVO.setSql(sql);
		String str = JSON.toJSONString(sqlVO);
		if (str == null || str.equals("null")) {
			System.out.println("kjkd");
		}

		baseQueue.put(str);

		// System.out.println(str);
		//
		// System.out.println(JSON.parseObject(str, SqlVO.class).getSql());
	}

	public int putToRedis() {
		int n = baseQueue.getQueueSize();
		if (n > 0) {
			ShardedJedis sharding = JedisTool.getResource();
			ShardedJedisPipeline pipeline = sharding.pipelined();
			for (int i = 0; i < n; i++) {
				pipeline.rpush(RedisKey.MAP_SQL, baseQueue.take());
			}
			@SuppressWarnings("unused")
			List<Object> results = pipeline.syncAndReturnAll();
			JedisTool.returnJedis(sharding);
		}
		return n;
	}

	public static void main(String[] args) throws Exception {
		// SqlSaveThread st = new SqlSaveThread();
		// st.start();
		//
		// for (int i = 0; i < 1000000; i++) {
		// st.putSql("" + i);
		// }
	}
}
