package com.dh.sqlexe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;

import com.alibaba.fastjson.JSON;
import com.dh.Cache.JedisTool;
import com.dh.Cache.RedisList;
import com.dh.game.constant.RedisKey;
import com.dh.netty.NettyServerHandler;
import com.dh.s2s.queue.BaseQueue;

@Component
public class SqlExeThread extends Thread {
	private static Logger logger = Logger.getLogger(SqlExeThread.class);
	private BaseQueue<List<String>> baseQueue = new BaseQueue<List<String>>();
	private final static int MAX_SQL = 50;

	@Resource
	private JdbcDAO jdbcDAO;

	private SqlExeThread() {
		super();
	}

	public void putSqlList(List<String> list) {
		baseQueue.put(list);
	}

	public void run() {
		while (true) {
			try {
				register();
				getFromRedis();
				TimeUnit.MILLISECONDS.sleep(50);
				// Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("SqlSaveThread.exesql" + e.getMessage(), e);
			}
		}
	}

	public void register() {
		int n = baseQueue.getQueueSize();
		if (n > 0) {
			long d1 = System.currentTimeMillis();
			for (int i = 0; i < n; i++) {

				List<String> list = baseQueue.take();

				if (list != null && list.size() > 0) {
					try {
						jdbcDAO.batchSql(list.toArray(new String[0]));
					} catch (Exception e) {
						logger.error("SqlSaveThread.batchSql" + e.getMessage(), e);
						logger.error("==============error sql register start===============");
						for (String str : list) {
							logger.error(str);
						}
						logger.error("==============error sql register end===============");
					}
				}
			}

			long d2 = System.currentTimeMillis();
			long d3 = d2 - d1;
			if (d3 > 100) {
				logger.debug("exec list size use = " + (System.currentTimeMillis() - d1));
			}
		}
	}

	long d2 = 0;
	long d1 = 0;

	public void getFromRedis() {

		long count = RedisList.llen(RedisKey.MAP_SQL);
		if (count > 0) {
			if (count > 30) {
				logger.error("==============sql count ===============" + count);
			}
			if (count > MAX_SQL) {
				count = MAX_SQL;
			}
			ShardedJedis sharding = JedisTool.getResource();
			ShardedJedisPipeline pipeline = sharding.pipelined();
			for (long l = 0; l < count; l++) {
				pipeline.lpop(RedisKey.MAP_SQL);

			}

			List<Object> results = pipeline.syncAndReturnAll();
			JedisTool.returnJedis(sharding);

			// if (results != null && results.size() > 0) {
			// for (Object obj : results) {
			// exesql((String) obj);
			// }
			// }

			List<String> list = new ArrayList<String>(results.size());
			for (int i = 0; i < results.size(); i++) {

				if (results.get(i) == null) {
					continue;
				}
				// System.out.println("["+i+"] = "+results.get(i).toString());
				try {
					String sql = JSON.parseObject(results.get(i).toString(), SqlVO.class).getSql();
					if (!StringUtils.isEmpty(sql)) {
						list.add(sql);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			batchSql(list.toArray(new String[0]));
		}

		if (NettyServerHandler.System_status == 1 && baseQueue.getQueueSize() == 0) {
			boolean isPrint = false;
			if (d1 == 0) {
				d1 = System.currentTimeMillis();
				isPrint = true;
			} else if (d2 == 0) {
				d2 = System.currentTimeMillis();
			} else {
				d2 = System.currentTimeMillis();
			}

			if ((d2 - d1) >= 30000) {
				isPrint = true;
				d1 = d2;
			}

			if (isPrint) {
				System.err.println("SQL EXE length ============== count == " + count + " baseQueue.getQueueSize(): " + baseQueue.getQueueSize());
				logger.error("SQL EXE length ============== count == " + count + " baseQueue.getQueueSize(): " + baseQueue.getQueueSize());
			}
		}

	}

	// public void exesql(String sql) {
	// try {
	// // logger.debug("exec" + sql);
	// if (sql != null && sql.trim().length() > 0) {
	// jdbcDAO.execute(sql);
	// }
	// } catch (Exception e) {
	// logger.error("SqlSaveThread.exesql" + e.getMessage(), e);
	// }
	// }

	public void batchSql(String[] sqls) {
		if (sqls == null || sqls.length == 0) {
			return;
		}
		try {
			long d1 = System.currentTimeMillis();

			if (sqls != null && sqls.length > 0) {
				jdbcDAO.batchSql(sqls);
			}
			long d2 = System.currentTimeMillis();
			long d3 = d2 - d1;
			if (d3 > 100) {
				logger.debug("exec sql size = " + sqls.length);
				logger.debug("exec sql size use = " + (System.currentTimeMillis() - d1));
			}
		} catch (Exception e) {
			logger.error("SqlSaveThread.batchSql" + e.getMessage(), e);
			logger.error("==============error sql array start===============");
			for (String str : sqls) {
				logger.error(str);
			}
			logger.error("==============error sql array end===============");
		}
	}

}
