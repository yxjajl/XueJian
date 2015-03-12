package com.dh.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dh.Cache.RedisMap;
import com.dh.Cache.ServerHandler;
import com.dh.constants.LegionConstant;
import com.dh.dao.LegionMapper;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.RedisKey;
import com.dh.game.vo.base.BaseLegionBossVO;
import com.dh.game.vo.legion.LegionProto.MEM_TYPE;
import com.dh.game.vo.user.LegionLogVO;
import com.dh.game.vo.user.LegionMemVO;
import com.dh.game.vo.user.LegionVO;
import com.dh.game.vo.user.PlayerVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.LegionBossRes;
import com.dh.resconfig.LegionMemRes;
import com.dh.resconfig.LegionRes;
import com.dh.sqlexe.SqlSaveThread;
import com.dh.sync.SyncLock;
import com.dh.sync.SyncUtil;
import com.dh.util.CodeTool;
import com.dh.util.CommandUtil;
import com.dh.util.DateUtil;
import com.dh.util.SqlBuild;
import com.dh.vo.user.UserCached;

@Service
public class LegionService {
	private final static Logger logger = Logger.getLogger(LegionService.class);
	@Resource
	private SqlBuild sqlBuild;
	@Resource
	private SqlSaveThread sqlSaveThread;
	@Resource
	private LegionMapper legionMapper;
	@Resource
	private RestoreService restoreService;

	/**
	 * 所有军团的初始化加载
	 */
	public void initLoalLegion() {
		int startId = restoreService.getStartIdByKey(RestoreService.LEGION_KEY);
		List<LegionVO> legions = legionMapper.getLegions();
		for (LegionVO legionVO : legions) {
			legionVO.setBaseLegionVO(LegionRes.getInstance().getBaseLegionByLevel(legionVO.getLegionLevel()));
			List<LegionMemVO> mems = legionMapper.getMems(legionVO.getId());// 加载成员
			int combat = 0;
			for (LegionMemVO legionMemVO : mems) {
				legionMemVO.setBaseLegionMemVO(LegionMemRes.getInstance().getMemDonateByDonate(legionMemVO.getDonate()));
				BaseLegionBossVO bossVO = null;
				bossVO = LegionBossRes.getInstance().getBaseLegionBossById(legionMemVO.getBossId());
				if (legionMemVO.getBossStatus() == 0) {// 还未攻打过
					legionMemVO.nextBoss(bossVO);
					int killer = legionVO.getBossStatus(bossVO.getId());
					initBossHp(killer, legionMemVO);
				} else {
					legionMemVO.setBaseLegionBossVO(bossVO);
				}
				PlayerVO playerVO = RedisMap.getPlayerVObyId(legionMemVO.getPlayerId());
				// 更新战斗力
				legionMemVO.setCombat(playerVO.getCombat());
				combat += legionMemVO.getCombat();
				legionVO.addMem(legionMemVO);
			}
			legionVO.setCombat(combat);
			List<LegionLogVO> logs = legionMapper.getLogs(legionVO.getId());// 加载日志
			int maxLogId = 0;
			for (LegionLogVO legionLogVO : logs) {
				legionLogVO.setDataList(CodeTool.decodeStrList(legionLogVO.getDataStr()));
				if (legionLogVO.getId() > maxLogId) {
					maxLogId = legionLogVO.getId();
				}
			}
			legionVO.setLogs(logs);
			legionVO.setMaxLogId(maxLogId);
			if (legionVO.getKillerStr() == null || legionVO.getKillerStr().isEmpty()) {
				legionVO.setKillerStr(CodeTool.encodeIntArray(new int[LegionConstant.LEGION_BOSS_NUM]));
			}
			legionVO.setBossKiller(CodeTool.decodeIntArray(legionVO.getKillerStr()));

			Collections.sort(legionVO.getLogs());
		}
		LegionConstant.initLoadLegion(legions, startId);
	}

	public static void initMemEveryDay(LegionVO legionVO, LegionMemVO memVO) {
		memVO.nextBoss(LegionBossRes.getInstance().getBaseLegionBossById(1));
		memVO.setHp(-1);
		memVO.setBossRewardId(0);
		int killer = legionVO.getBossStatus(memVO.getBossId());
		initBossHp(killer, memVO);
	}

	/**
	 * 初始化boss血量
	 * 
	 * @param killer
	 * @param memVO
	 */
	public static void initBossHp(int killer, LegionMemVO memVO) {
		if (memVO.getHp() == -1) {
			int totalHp = memVO.getBaseLegionBossVO().getBaseMonsterVO().getHp();
			if (killer > 0) {// 被攻击过一次增加降血量buf
				memVO.setHp((int) (totalHp * 0.8));
			} else {
				memVO.setHp(totalHp);
			}
		}
	}

	/**
	 * 初始化boss血量
	 * 
	 * @param killer
	 * @param memVO
	 */
	public static void firstInitBossHp(int killer, LegionMemVO memVO) {
		if (memVO.getHp() == -1) {
			int totalHp = memVO.getBaseLegionBossVO().getBaseMonsterVO().getHp();
			if (killer > 0) {// 被攻击过一次增加降血量buf
				memVO.setHp((int) (totalHp * 0.8));
			} else {
				memVO.setHp(totalHp);
			}
		}
	}

	/**
	 * 权限检查
	 */
	public static void filterPermission(int memType, int minNeed) throws Exception {
		if (memType < minNeed) {
			throw new GameException(AlertEnum.LEGION_NOT_PERMISSION);
		}
	}

	/**
	 * 血盟销毁状态无法进行部分操作
	 * 
	 * @param legionVO
	 * @throws Exception
	 */
	public static void filterDestroy(LegionVO legionVO) throws Exception {
		if (legionVO.getDestroyTime() != 0) {
			throw new GameException(AlertEnum.LEGION_DESTROYING);
		}
	}

	/**
	 * 更新军团基本信息,如果具有管理权限同时会push军团最新信息
	 * 
	 * @param userCached
	 * @param commandList
	 */
	public void loginPushBaseLegionInfo(UserCached userCached, List<NettyMessageVO> commandList) {
		NettyMessageVO msg = null;
		// freshPlayerLegionStatus(userCached);

		if (userCached.getPlayerVO().getLegionId() == 0) {
			msg = CommandUtil.packBaseLegionInfo(MEM_TYPE.MEM_TYPE_NONE, null);
		} else {
			LegionVO legionVO = getLegionAndCheckDestroy(userCached.getPlayerVO().getLegionId());
			if (legionVO == null) {
				userCached.getPlayerVO().setLegionId(0);
				updatePlayerVOForLegion(userCached.getPlayerVO());
				msg = CommandUtil.packBaseLegionInfo(MEM_TYPE.MEM_TYPE_NONE, null);
			} else {
				LegionMemVO memVO = legionVO.getMem(userCached.getPlayerId());
				if (memVO == null) {// 被开除
					userCached.getPlayerVO().setLegionId(0);
					updatePlayerVOForLegion(userCached.getPlayerVO());
					msg = CommandUtil.packBaseLegionInfo(MEM_TYPE.MEM_TYPE_NONE, null);
				} else {
					msg = CommandUtil.packBaseLegionInfo(MEM_TYPE.valueOf(memVO.getType()), legionVO);
				}
			}
		}
		commandList.add(msg);
	}

	public void loadLegion(UserCached userCached) {
		// 加载玩家申请列表
		if (userCached.getPlayerVO().getLegionId() == 0) {// 当前没有帮会
			List<LegionMemVO> joins = legionMapper.getJoinList(userCached.getPlayerId());
			for (LegionMemVO legionMemVO : joins) {
				LegionVO legionVO = getLegionAndCheckDestroy(legionMemVO.getId());// 刚刚登陆时不在此push
				if (legionVO != null) {
					// userCached.getUserLegion().getJoinList().add(legionVO);
				}
			}
		}
		// 被拒绝的删除

	}

	/** 增加军团日志 */
	public void addLegionLog(LegionVO legionVO, LegionLogVO legionLogVO) {
		for (int i = legionVO.getLogs().size() - 1; i >= 100; i--) {
			sqlSaveThread.putSql(0,sqlBuild.getSql("com.dh.dao.LegionMapper.delLegionLog", legionVO.getLogs().get(i)));
			legionVO.getLogs().remove(i);
		}
		legionLogVO.setDataStr(CodeTool.encodeStrList(legionLogVO.getDataList()));
		legionVO.getLogs().add(0, legionLogVO);
		sqlSaveThread.putSql(0,sqlBuild.getSql("com.dh.dao.LegionMapper.insertLegionLog", legionLogVO));
	}

	public static LegionLogVO createLegionLog(int type, LegionVO legionVO, String... datas) {
		LegionLogVO logVO = new LegionLogVO();
		logVO.setId(legionVO.getAndIncrLogId());
		logVO.setType(type);
		logVO.setDataList(Arrays.asList(datas));
		logVO.setDate(new Date());
		logVO.setLegionId(legionVO.getId());
		// logVO.setDataStr(CodeTool.encodeStrArray(datas));
		return logVO;
	}

	public void addLegion(LegionVO legionVO) {
		LegionConstant.addLegion(legionVO);
		sqlSaveThread.putSql(legionVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.LegionMapper.insertLegion", legionVO));
	}

	// public void removeLegion(int legionId) {
	// LegionConstant.removeLegion(legionId);
	// sqlSaveThread.putSql(sqlBuild.getSql("com.dh.dao.LegionMapper.delLegion",
	// legionId));
	//
	// }

	public void updateLegion(LegionVO legionVO) {
		legionVO.setKillerStr(CodeTool.encodeIntArray(legionVO.getBossKiller()));
		sqlSaveThread.putSql(legionVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.LegionMapper.updateLegion", legionVO));
	}

	public void addMem(LegionVO legionVO, LegionMemVO memVO) {
		legionVO.addMem(memVO);
		sqlSaveThread.putSql(memVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.LegionMapper.insertLegionMem", memVO));
	}

	public void updateMem(LegionMemVO memVO) {
		sqlSaveThread.putSql(memVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.LegionMapper.updateLegionMem", memVO));
	}

	public void delMem(LegionMemVO memVO) {
		sqlSaveThread.putSql(memVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.LegionMapper.delLegionMem", memVO));
	}

	/**
	 * 检查军团解散倒计时是否结束
	 * 
	 * @return true 则代表销毁时间已经到
	 */
	public boolean checkLegionDestroy(LegionVO legionVO) {
		if (legionVO.getDestroyTime() != 0) {
			int div = legionVO.getDestroyTime() + LegionConstant.LEGION_DESTROY_TIME - DateUtil.getNow();
			if (div <= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 申请军团时调用
	 * 
	 * @param legionId
	 * @param commandList
	 * @return
	 */
	public LegionVO getReqLegionById(int legionId, List<NettyMessageVO> commandList) {
		if (legionId == 0) {
			throw new GameException(AlertEnum.LEGION_NOT_EXIST);
		}
		LegionVO legionVO = LegionConstant.getLegion(legionId);
		if (legionVO == null) {
			throw new GameException(AlertEnum.LEGION_NOT_EXIST);
		}
		return legionVO;
	}

	/**
	 * 军团被删除后,玩家主动触发自身军团的相关信息清理
	 */
	public LegionVO getLegionAndCheckDestroy(int legionId) {
		LegionVO legionVO = LegionConstant.getLegion(legionId);
		if (legionVO != null && checkLegionDestroy(legionVO)) {
			Destroy(legionVO);
			legionVO = null;
		}
		return legionVO;
	}

	/**
	 * 军团被删除后,玩家主动触发自身军团的相关信息清理
	 */
	public LegionVO getMineLegion(UserCached userCached, List<NettyMessageVO> commandList) throws Exception {
		// freshPlayerLegionStatus(userCached);
		if (userCached.getPlayerVO().getLegionId() == 0) {
			commandList.add(CommandUtil.packBaseLegionInfo(MEM_TYPE.MEM_TYPE_NONE, null));
			throw new GameException(AlertEnum.LEGION_PLAYER_NOT_HAD);
		}
		LegionVO legionVO = LegionConstant.getLegion(userCached.getPlayerVO().getLegionId());
		if (legionVO == null) {
			commandList.add(CommandUtil.packBaseLegionInfo(MEM_TYPE.MEM_TYPE_NONE, null));
			throw new GameException(AlertEnum.LEGION_NOT_EXIST);
		}
		if (legionVO.getDestroyTime() != 0) {
			if (checkLegionDestroy(legionVO)) {// 对该军团进行销毁
				Destroy(legionVO);
				userCached.getPlayerVO().setLegionId(0);
				// legionVO = null;
				throw new GameException(AlertEnum.LEGION_HAD_DESTROY);
			} else {
				throw new GameException(AlertEnum.LEGION_DESTROYING);
			}
		}
		return legionVO;
	}

	public static LegionMemVO getLegionMemVO(LegionVO legionVO, int playerId, List<NettyMessageVO> commandList) {
		LegionMemVO mineMemVO = legionVO.getMem(playerId);
		if (mineMemVO == null) {
			commandList.add(CommandUtil.packBaseLegionInfo(MEM_TYPE.MEM_TYPE_NONE, null));
			throw new GameException(AlertEnum.LEGION_HAD_REMOVE);
		}
		return mineMemVO;
	}

	// /**
	// * 刷新玩家军团状态
	// *
	// * @param userCached
	// */
	// public static void freshPlayerLegionStatus(UserCached userCached) {
	// SyncLock lockObject =
	// SyncUtil.getInstance().getLock(RedisKey.PLAYERVO_MAP +
	// userCached.getPlayerId());
	// synchronized (lockObject) {
	// PlayerVO playerVO = RedisMap.getPlayerVObyId(userCached.getPlayerId());
	// int mineLegionId = playerVO.getLegionId();
	// userCached.getPlayerVO().setLegionId(mineLegionId);
	// }
	// }

	/**
	 * 玩家军团信息销毁,以及向前台的push
	 * 
	 * @param legionVO
	 */
	private void Destroy(LegionVO legionVO) {
		List<LegionMemVO> reallyMems = legionVO.getRealyMems();
		int memId = 0;
		for (LegionMemVO legionMemVO : reallyMems) {
			memId = legionMemVO.getPlayerId();
			SyncLock lockObject = SyncUtil.getInstance().getLock(RedisKey.PLAYERVO_MAP + memId);
			synchronized (lockObject) {
				PlayerVO playerVO = RedisMap.getPlayerVObyId(memId);
				playerVO.setLegionId(0);
				updatePlayerVOForLegion(playerVO);
			}
		}
		NettyMessageVO msg = CommandUtil.packBaseLegionInfo(MEM_TYPE.MEM_TYPE_NONE, null);
		ServerHandler.broadcastLegion(legionVO.getId(), msg);
		LegionConstant.removeLegion(legionVO.getId());// remove方法一定放置到最后
		sqlSaveThread.putSql(0,sqlBuild.getSql("com.dh.dao.LegionMapper.destroyLegion", legionVO.getId()));
	}

	public void updatePlayerVOForLegion(PlayerVO playerVO) {
		RedisMap.hset(RedisKey.PLAYERVO_MAP, String.valueOf(playerVO.getPlayerId()), JSON.toJSONString(playerVO));
		sqlSaveThread.putSql(playerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerMapper.updatePlayerVOForLegion", playerVO));
	}

	/**
	 * 军团boss扣血
	 */
	public void subBossHp(UserCached userCached, int hunt, List<NettyMessageVO> commandList) {
		if (hunt < 0) {
			hunt = 0;
		}
		try {
			LegionVO legionVO = getMineLegion(userCached, commandList);
			LegionMemVO legionMemVO = getLegionMemVO(legionVO, userCached.getPlayerId(), commandList);
			legionMemVO.setAtkBossDate(DateUtil.getNow());
			if (legionMemVO.getHp() <= hunt) {// 已经打死
				BaseLegionBossVO oldBoss = legionMemVO.getBaseLegionBossVO();
				boolean isFirstKiller = legionVO.getBossStatus(oldBoss.getId()) == 0;
				BaseLegionBossVO nextBoss = LegionBossRes.getInstance().getBaseLegionBossById(legionMemVO.getBaseLegionBossVO().getId() + 1);
				if (nextBoss == null) {// 最后一个boss了
					nextBoss = oldBoss;
					legionMemVO.setBossStatus(3);
				} else {
					if (legionVO.getLegionLevel() < nextBoss.getLevel()) {// 打完所有boss
						legionMemVO.setBossStatus(2);
					} else {
						legionMemVO.setBossStatus(1);
					}
				}
				legionMemVO.nextBoss(nextBoss);
				int killer = legionVO.getBossStatus(nextBoss.getId());
				initBossHp(killer, legionMemVO);
				if (isFirstKiller) {// 第一个击杀这更新击杀情况
					legionVO.killBoss(oldBoss.getId(), userCached.getPlayerId());
					updateLegion(legionVO);
				}
				legionMemVO.setAtkFailDate(0);// 打赢不进入冷却
			} else {
				legionMemVO.setHp(legionMemVO.getHp() - hunt);
				legionMemVO.setAtkFailDate(DateUtil.getNow());
			}
			updateMem(legionMemVO);
		} catch (Exception e) {
			logger.error("江湖boss异常:" + e.getMessage(), e);
		}

	}

	public void freshLegionDay() {
		LegionConstant.legionDayFresh();
		int[] killer = new int[LegionConstant.LEGION_BOSS_NUM];
		sqlSaveThread.putSql(0,sqlBuild.getSql("com.dh.dao.LegionMapper.updateAllLegionKiller", CodeTool.encodeIntArray(killer)));
		// 战斗力每日刷新

	}
}
