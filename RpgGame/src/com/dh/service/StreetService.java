package com.dh.service;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dh.Cache.RedisList;
import com.dh.Cache.RedisMap;
import com.dh.constants.StreetConstants;
import com.dh.dao.PlayerMapper;
import com.dh.dao.StreetMapper;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.constant.RedisKey;
import com.dh.game.vo.base.BaseBoxVO;
import com.dh.game.vo.base.BaseFazhenVO;
import com.dh.game.vo.base.BaseHeroInfoVO;
import com.dh.game.vo.base.BaseMachineVO;
import com.dh.game.vo.base.BaseMonsterGroupVO;
import com.dh.game.vo.base.BaseResourceVO;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_UNIT_TYPE;
import com.dh.game.vo.street.StreetProto.ENEMY_STATUS;
import com.dh.game.vo.street.StreetProto.EnemyUpdateResp;
import com.dh.game.vo.street.StreetProto.GRID_FRESH_TYPE;
import com.dh.game.vo.street.StreetProto.OPEN_STATUS;
import com.dh.game.vo.street.StreetProto.RES_STATUS;
import com.dh.game.vo.street.StreetProto.StreetGridInfo;
import com.dh.game.vo.street.StreetProto.UpGridResp;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.game.vo.user.PlayerVO;
import com.dh.game.vo.user.StreetBoxVO;
import com.dh.game.vo.user.StreetDefendLogVO;
import com.dh.game.vo.user.StreetEnemyVO;
import com.dh.game.vo.user.StreetMonsterVO;
import com.dh.game.vo.user.StreetResPlayerVO;
import com.dh.game.vo.user.StreetResVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.BoxRewardRes;
import com.dh.resconfig.FaZhenRes;
import com.dh.resconfig.HeroRes;
import com.dh.resconfig.MachineRes;
import com.dh.resconfig.MonsterGroupRes;
import com.dh.resconfig.ResourceRes;
import com.dh.sqlexe.SqlSaveThread;
import com.dh.util.CodeTool;
import com.dh.util.CommandUtil;
import com.dh.util.DateUtil;
import com.dh.util.SqlBuild;
import com.dh.util.VOUtil;
import com.dh.vo.user.UserCached;
import com.dh.vo.user.UserStreet;

@Service
public class StreetService {
	public final static Logger LOGGER = Logger.getLogger(StreetService.class);

	@Resource
	private StreetMapper streetMapper;
	@Resource
	private PlayerMapper playerMapper;
	@Resource
	private SqlBuild sqlBuild;
	@Resource
	private SqlSaveThread sqlSaveThread;
	@Resource
	private HeroService heroService;
	@Resource
	private KnapsackService knapsackService;
	@Resource
	private ArenaService arenaService;

	public void loadAllPlayerStreet(List<PlayerVO> players) throws Exception {
		int playerId;
		for (PlayerVO playerVO : players) {
			playerId = playerVO.getPlayerId();
			if (playerVO.getLevel() < StreetConstants.STREET_OPEN_LEVEL) {
				continue;
			}
			UserStreet userStreet = streetMapper.getUserStreet(playerVO.getPlayerId());
			if (userStreet == null) {
				continue;
			}
			List<StreetResVO> resList = streetMapper.getUserStreetRes(playerId);
			for (StreetResVO streetResVO : resList) {
				int resId = streetResVO.getId();
				BaseResourceVO baseResourceVO = ResourceRes.getInstance().getResourceByGridId(resId);
				streetResVO.setBaseResourceVO(baseResourceVO);
				streetResVO.setItems(MailService.decodeRewardString(streetResVO.getRewards()));
				if (!RedisMap.hexists(RedisKey.STREET_RES_FREE_PLAYER + resId, playerId + "") && !RedisMap.hexists(RedisKey.STREET_RES_BATTLE_PLAYER + resId, playerId + "")
						&& !RedisMap.hexists(RedisKey.STREET_RES_PRODUCE_PLAYER + resId, playerId + "")) {// 如果是清理Redis,则将数据库重新加载进redis,否则只要任何一个已经存在则表示已经加载完成,直接跳过
					StreetResPlayerVO streetResPlayerVO = new StreetResPlayerVO();
					streetResPlayerVO.setPlayerId(playerId);
					streetResPlayerVO.setLevel(playerVO.getLevel());
					streetResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_FREE);
					streetResPlayerVO.setId(baseResourceVO.getSeat());
					streetResPlayerVO.setStartTime(streetResVO.getBeginTime());

					if (streetResVO.getId() == StreetConstants.CENTER_GRID_NUM) {
						RedisMap.setResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, streetResPlayerVO);
					} else {
						if (streetResVO.getCalTime() == 0) {// 兼容以前数据
							streetResVO.setCalTime(streetResVO.getBeginTime());
						}
						if (!StreetConstants.hasOvertime(streetResVO.getCalTime())) {// 如果在生产中
							streetResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_PRODUCING);
							RedisMap.setResPlayerVO(RedisKey.STREET_RES_PRODUCE_PLAYER, streetResPlayerVO);
						} else {
							RedisMap.setResPlayerVO(RedisKey.STREET_RES_FREE_PLAYER, streetResPlayerVO);
							// 需要回收redis中英雄
							List<PlayerHeroVO> heros = RedisMap.getPlayerHeroByResId(playerId, streetResVO.getId());
							for (PlayerHeroVO playerHeroVO2 : heros) {
								heroService.freeHeroFromStreet(playerHeroVO2, null);
							}
						}
					}
					RedisMap.hset(RedisKey.PLAYER_STREET_RES + streetResVO.getId(), String.valueOf(streetResVO.getPlayerId()), JSON.toJSONString(streetResVO));
				} else {
					return;//
				}
			}

		}
	}

	/**
	 * 加载玩家江湖数据
	 * 
	 * @param userCached
	 * @throws Exception
	 */
	public void loadPlayerStreet(UserCached userCached) throws Exception {
		if (userCached.getPlayerVO().getLevel() < StreetConstants.STREET_OPEN_LEVEL) {
			return;
		}
		int playerId = userCached.getPlayerId();
		UserStreet userStreet = streetMapper.getUserStreet(playerId);
		if (userStreet == null) {
			return;
		}
		userStreet.setGrids(CodeTool.decodeStrToBytes(userStreet.getGridStr()));

		int openNum = 0;
		for (byte b : userStreet.getGrids()) {
			if (b == 1)
				openNum++;
		}
		userStreet.setOpenedNum(openNum);
		List<StreetResVO> resList = streetMapper.getUserStreetRes(playerId);
		for (StreetResVO streetResVO : resList) {
			int resId = streetResVO.getId();
			BaseResourceVO baseResourceVO = ResourceRes.getInstance().getResourceByGridId(resId);
			streetResVO.setBaseResourceVO(baseResourceVO);
			streetResVO.setItems(MailService.decodeRewardString(streetResVO.getRewards()));
			if (!RedisMap.hexists(RedisKey.STREET_RES_FREE_PLAYER + resId, streetResVO.getPlayerId() + "")
					&& !RedisMap.hexists(RedisKey.STREET_RES_BATTLE_PLAYER + resId, streetResVO.getPlayerId() + "")
					&& !RedisMap.hexists(RedisKey.STREET_RES_PRODUCE_PLAYER + resId, streetResVO.getPlayerId() + "")) {// 恢复redis
				StreetResPlayerVO streetResPlayerVO = new StreetResPlayerVO();
				streetResPlayerVO.setPlayerId(userCached.getPlayerId());
				streetResPlayerVO.setLevel(userCached.getPlayerVO().getLevel());
				streetResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_FREE);
				streetResPlayerVO.setId(baseResourceVO.getSeat());
				streetResPlayerVO.setStartTime(streetResVO.getBeginTime());
				if (streetResVO.getId() == StreetConstants.CENTER_GRID_NUM) {// 门派
					RedisMap.setResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, streetResPlayerVO);
				} else {// 资源点
					if (streetResVO.getCalTime() == 0) {// 兼容以前数据
						streetResVO.setCalTime(streetResVO.getBeginTime());
					}
					if (!StreetConstants.hasOvertime(streetResVO.getCalTime())) {// 如果在生产中
						streetResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_PRODUCING);
						RedisMap.setResPlayerVO(RedisKey.STREET_RES_PRODUCE_PLAYER, streetResPlayerVO);
					} else {
						RedisMap.setResPlayerVO(RedisKey.STREET_RES_FREE_PLAYER, streetResPlayerVO);
						// 需要回收redis中英雄
						List<PlayerHeroVO> heros = RedisMap.getPlayerHeroByResId(playerId, streetResVO.getId());
						for (PlayerHeroVO playerHeroVO2 : heros) {
							heroService.freeHeroFromStreet(playerHeroVO2, null);
						}
					}
				}
				RedisMap.hset(RedisKey.PLAYER_STREET_RES + streetResVO.getId(), String.valueOf(streetResVO.getPlayerId()), JSON.toJSONString(streetResVO));
			}
		}

		userStreet.setResList(resList);

		List<StreetBoxVO> boxList = streetMapper.getStreetBox(playerId);
		for (StreetBoxVO streetBoxVO : boxList) {
			streetBoxVO.setBaseBoxVO(BoxRewardRes.getInstance().getBoxVOByCfgId(streetBoxVO.getCfgId()));
		}
		userStreet.setBoxList(boxList);
		List<StreetMonsterVO> monsterList = streetMapper.getStreetMonster(playerId);
		for (StreetMonsterVO streetMonsterVO : monsterList) {
			streetMonsterVO.setBaseMonsterGroupVO(MonsterGroupRes.getInstance().getMonsterGroupByCfgId(streetMonsterVO.getCfgId()));
		}
		userStreet.setMonsterList(monsterList);

		userStreet.setEnemies(streetMapper.getEnemies(playerId));
		int maxLogId = 0;
		List<StreetDefendLogVO> logs = streetMapper.getDefendLogs(playerId);
		Collections.sort(logs);
		for (StreetDefendLogVO streetDefendLogVO : logs) {
			maxLogId = Math.max(streetDefendLogVO.getId(), maxLogId);
		}
		userStreet.setDefendLogs(logs);
		userStreet.setMaxLogId(maxLogId);
		if (userStreet.checkProduced()) {
			userStreet.setStatus(userStreet.getStatus());
		}
		if (RedisList.checkHasNoticeAndRemove(RedisKey.MSG_NOTICE_STREET, playerId)) {
			userStreet.setStatus(1);
		}
		userCached.setUserStreet(userStreet);
	}

	public void insertUserStreet(UserCached userCached, List<NettyMessageVO> commandList) throws Exception {
		UserStreet userStreet = new UserStreet();
		userStreet.setPlayerId(userCached.getPlayerId());
		userStreet.getGrids()[StreetConstants.CENTER_GRID_NUM] = 1;// 门派点
		userStreet.setGridStr(CodeTool.encodeBytesToStr(userStreet.getGrids()));
		userStreet.setOpenedNum(1);
		List<BaseMachineVO> mList = MachineRes.getInstance().getAllMachineByLevel(1);
		StringBuffer mString = new StringBuffer();
		for (BaseMachineVO baseMachineVO : mList) {
			mString.append(baseMachineVO.getCfgId()).append(StreetConstants.HERO_SPLIT_CHAR);
		}
		userStreet.setFactoryM(mString.toString());
		List<BaseFazhenVO> fzList = FaZhenRes.getInstance().getAllFzByLevel(1);
		StringBuffer fzString = new StringBuffer();
		for (BaseFazhenVO baseFazhenVO : fzList) {
			fzString.append(baseFazhenVO.getCfgId()).append(StreetConstants.HERO_SPLIT_CHAR);
		}
		userStreet.setFactoryFz(fzString.toString());

		userStreet.setFzIds("");
		userStreet.setMachines("");

		BaseResourceVO baseResourceVO = ResourceRes.getInstance().getResourceByGridId(StreetConstants.CENTER_GRID_NUM);
		userCached.setUserStreet(userStreet);
		addResource(userCached, baseResourceVO);
		BaseHeroInfoVO baseHeroInfoVO1 = HeroRes.getInstance().getBaseHeroInfoVO(30040);
		BaseHeroInfoVO baseHeroInfoVO2 = HeroRes.getInstance().getBaseHeroInfoVO(30104);
		heroService.addPlayerHero(baseHeroInfoVO1, 1, userCached, false, commandList);
		heroService.addPlayerHero(baseHeroInfoVO2, 1, userCached, false, commandList);
		sqlSaveThread.putSql(userStreet.getPlayerId(),sqlBuild.getSql("com.dh.dao.StreetMapper.insertPlayerStreet", userStreet));

	}

	public void useDaddy(UserCached userCached, List<NettyMessageVO> commandList) throws Exception {
		UserStreet userStreet = userCached.getUserStreet();
		if (userStreet == null) {
			throw new GameException(AlertEnum.RES_DADDY_USER_FAIL);
		}
		StreetResPlayerVO streetResPlayerVO = RedisMap.getStreetResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, StreetConstants.CENTER_GRID_NUM, userStreet.getPlayerId());
		if (streetResPlayerVO.getBattleTime() != 0 && !StreetConstants.hasBattleOverTime(streetResPlayerVO.getBattleTime())) {
			throw new GameException(AlertEnum.STREET_BATTLE_CENTER_CANT_DADDY);
		}

		StreetResVO centerRes = userStreet.getStreetResById(StreetConstants.CENTER_GRID_NUM);
		if (centerRes != null) {
			centerRes.setWhosyourdaddy(DateUtil.getNow());
			updateStreetRes(centerRes);
			commandList.add(CommandUtil.packageAnyProperties(PLAYER_UNIT_TYPE.UNIT_PLAYER, userCached.getPlayerId(), PLAYER_PROPERTY.PROPERTY_DADDY_LIMIT, StreetConstants.RES_WHOSYOUDADDY));
			// knapsackService.removeItem(userCached, 1000, 1, commandList);
		}

	}

	public StreetResVO addResource(UserCached userCached, BaseResourceVO baseResourceVO) throws Exception {
		int playerId = userCached.getPlayerId();
		StreetResVO streetResVO = new StreetResVO();
		streetResVO.setPlayerId(playerId);
		streetResVO.setId(baseResourceVO.getSeat());
		streetResVO.setCfgId(baseResourceVO.getId());
		streetResVO.setStatus(RES_STATUS.FREE_VALUE);
		streetResVO.setBaseResourceVO(baseResourceVO);
		streetResVO.setMachineLine("");
		userCached.getUserStreet().getResList().add(streetResVO);
		sqlSaveThread.putSql(playerId,sqlBuild.getSql("com.dh.dao.StreetMapper.insertStreetRes", streetResVO));

		// 加入搜索队列
		StreetResPlayerVO streetResPlayerVO = new StreetResPlayerVO();
		streetResPlayerVO.setPlayerId(userCached.getPlayerId());
		streetResPlayerVO.setLevel(userCached.getPlayerVO().getLevel());
		streetResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_FREE);
		streetResPlayerVO.setId(baseResourceVO.getSeat());
		if (streetResPlayerVO.getId() == StreetConstants.CENTER_GRID_NUM) {// 门派一直放置在战斗队列中
			RedisMap.setResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, streetResPlayerVO);
		} else {
			RedisMap.setResPlayerVO(RedisKey.STREET_RES_FREE_PLAYER, streetResPlayerVO);
		}
		updateStreetRes(streetResVO);
		return streetResVO;
	}

	public void insertStreetDefendLog(StreetDefendLogVO streetDefendLogVO) {
		sqlSaveThread.putSql(streetDefendLogVO.getPlayerId(), sqlBuild.getSql("com.dh.dao.StreetMapper.insertStreetDefendLog", streetDefendLogVO));
	}

	public void addStreetDefendLog(UserStreet userStreet, StreetDefendLogVO streetDefendLogVO, List<NettyMessageVO> commandList, StreetEnemyVO streetEnemyVO2) {
		streetDefendLogVO.setId(userStreet.getMaxLogId());
		userStreet.getDefendLogs().add(0, streetDefendLogVO);
		insertStreetDefendLog(streetDefendLogVO);
		for (int i = userStreet.getDefendLogs().size() - 1; userStreet.getDefendLogs().size() > 10; i--) {
			userStreet.getDefendLogs().remove(i);
			deleteBattleLog(streetDefendLogVO);
		}
	}

	/**
	 * @param type
	 *            1:资源点更新,2:更新怪物,3:更新宝箱
	 * @param streetResVO
	 * @param boxId
	 * @param mosterId
	 *            格子id
	 * @param commandList
	 */
	public static void packStreetGridUpdate(int type, StreetResVO streetResVO, int boxId, int mosterId, List<NettyMessageVO> commandList) {
		UpGridResp.Builder resp = UpGridResp.newBuilder();
		if (type == 1) {
			resp.setStreetGridInfo(StreetGridInfo.newBuilder().setId(streetResVO.getId()).setStreetResInfo(VOUtil.packStreetRes(streetResVO)).setOpenStaus(OPEN_STATUS.OPEN_CONTENT));
		} else if (type == 2) {
			resp.setStreetGridInfo(StreetGridInfo.newBuilder().setId(mosterId).setOpenStaus(OPEN_STATUS.OPEN_NOCENTENT));
		} else if (type == 3) {
		}
		NettyMessageVO nettyMessageVO = new NettyMessageVO();
		nettyMessageVO.setCommandCode(CSCommandConstant.STREET_GRID_UPDATE);
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/**
	 * 是否已经刷满 <br/>
	 * 2014年7月18日
	 * 
	 * @author dingqu-pc100
	 */
	public static boolean checkFreshNotFull(UserStreet userStreet) {
		List<StreetBoxVO> boxList = userStreet.getBoxList();
		List<StreetMonsterVO> monsterList = userStreet.getMonsterList();
		return boxList.size() + monsterList.size() < userStreet.getOpenedNum() / StreetConstants.FRESH_PERCENT; // 不超过总数*百分比
	}

	public void updateStreetRes(StreetResVO streetResVO) throws Exception {
		streetResVO.setRewards(MailService.encoderReward(streetResVO.getItems()));
		sqlSaveThread.putSql(streetResVO.getPlayerId(), sqlBuild.getSql("com.dh.dao.StreetMapper.updateStreetRes", streetResVO));
		RedisMap.hset(RedisKey.PLAYER_STREET_RES + streetResVO.getId(), String.valueOf(streetResVO.getPlayerId()), JSON.toJSONString(streetResVO));
	}

	/**
	 * 释放资源点:敌人过期,进攻结束{成功{放弃配置},失败},战斗关闭状态{准备界面关闭,开始战斗关闭,战斗中关闭, 结算没有发送confend或者quitconf}
	 * 
	 * @param streetResVO
	 * @param streetResPlayerVO
	 */
	public static void freeStreetResAndPlayer(StreetResVO streetResVO, StreetResPlayerVO streetResPlayerVO, List<NettyMessageVO> commandList) {
		streetResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_FREE);
		streetResPlayerVO.setEnemyId(0);
		streetResVO.setStatus(RES_STATUS.FREE_VALUE);
		packStreetGridUpdate(1, streetResVO, 0, 0, commandList);
		RedisMap.setResPlayerVO(RedisKey.STREET_RES_FREE_PLAYER, streetResPlayerVO);
	}

	public static void removeRobotFromBattle(StreetResPlayerVO robotResPlayerVO) {
		RedisMap.delResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, robotResPlayerVO);
		StreetConstants.removeRobotCache(robotResPlayerVO.getId(), robotResPlayerVO.getEnemyId());
	}

	/**
	 * 刷宝箱<br/>
	 * 2014年7月17日
	 * 
	 * @param id
	 *            格子id
	 * @author dingqu-pc100
	 * @return
	 */
	public StreetBoxVO addBox(UserCached userCached, int id) {
		UserStreet userStreet = userCached.getUserStreet();
		BaseBoxVO box = BoxRewardRes.getInstance().getBoxRewardById(userCached.getPlayerVO().getLevel());
		List<StreetBoxVO> boxList = userStreet.getBoxList();
		if (box != null) {
			userStreet.getGrids()[id] = 1;
			StreetBoxVO streetBoxVO = new StreetBoxVO();
			streetBoxVO.setBaseBoxVO(box);
			streetBoxVO.setPlayerId(userCached.getPlayerVO().getPlayerId() + 0);
			streetBoxVO.setId(id);
			streetBoxVO.setCfgId(box.getId());
			streetBoxVO.setType(0);
			boxList.add(streetBoxVO);
			sqlSaveThread.putSql(userCached.getPlayerVO().getPlayerId(), sqlBuild.getSql("com.dh.dao.StreetMapper.insertStreetBox", streetBoxVO));
			return streetBoxVO;
		}
		return null;
	}

	public StreetMonsterVO addMonster(UserCached userCached, int id) {
		UserStreet userStreet = userCached.getUserStreet();
		List<StreetMonsterVO> monsterList = userStreet.getMonsterList();
		BaseMonsterGroupVO monster = MonsterGroupRes.getInstance().getMonsterByLevel(userCached.getPlayerVO().getLevel(), StreetConstants.MONSTER_TYPE_GRID);
		if (monster != null) {
			userStreet.getGrids()[id] = 1;
			StreetMonsterVO streetMonsterVO = new StreetMonsterVO();
			streetMonsterVO.setPlayerId(userCached.getPlayerId());
			streetMonsterVO.setBaseMonsterGroupVO(monster);
			streetMonsterVO.setId(id);
			streetMonsterVO.setType(1);
			streetMonsterVO.setCfgId(monster.getId());
			monsterList.add(streetMonsterVO);
			sqlSaveThread.putSql(userCached.getPlayerId(), sqlBuild.getSql("com.dh.dao.StreetMapper.insertStreetMonster", streetMonsterVO));
			return streetMonsterVO;
		}
		return null;
	}

	public void updateResource(StreetResVO streetResVO) {
		sqlSaveThread.putSql(streetResVO.getPlayerId(), sqlBuild.getSql("com.dh.dao.StreetMapper.updateStreetRes", streetResVO));
	}

	public void updatePlayerStreet(UserStreet userStreet) {
		userStreet.setGridStr(CodeTool.encodeBytesToStr(userStreet.getGrids()));
		sqlSaveThread.putSql(userStreet.getPlayerId(), sqlBuild.getSql("com.dh.dao.StreetMapper.updatePlayerStreet", userStreet));
	}

	public void deleteGridBox(StreetBoxVO streetBoxVO) {
		sqlSaveThread.putSql(streetBoxVO.getPlayerId(), sqlBuild.getSql("com.dh.dao.StreetMapper.delStreetBox", streetBoxVO));
	}

	public void deleteGridMonster(StreetMonsterVO streetMonsterVO) {
		sqlSaveThread.putSql(streetMonsterVO.getPlayerId(), sqlBuild.getSql("com.dh.dao.StreetMapper.delStreetMonster", streetMonsterVO));
	}

	public void deleteBattleLog(StreetDefendLogVO streetDefendLogVO) {
		sqlSaveThread.putSql(streetDefendLogVO.getPlayerId(), sqlBuild.getSql("com.dh.dao.StreetMapper.deleteStreetDefendLog", streetDefendLogVO));
	}

	public void updateStreetDefendLog(StreetDefendLogVO streetDefendLogVO) {
		sqlSaveThread.putSql(streetDefendLogVO.getPlayerId(), sqlBuild.getSql("com.dh.dao.StreetMapper.updateStreetDefLog", streetDefendLogVO));

	}

	public static int getMatchLevelRobot(int level) {
		int targetLevel;
		int div = StreetConstants.ROBOT_TOTAL_LEVEL + StreetConstants.STREET_OPEN_LEVEL - level;
		if (div <= 0 || Math.random() < Math.log10(Math.sqrt(div))) {
			targetLevel = (int) (level + 5 - 10 * Math.random());
		} else {
			targetLevel = (int) (level + 15 - 30 * Math.random());
		}
		return Math.min(Math.max(StreetConstants.STREET_OPEN_LEVEL, targetLevel), StreetConstants.ROBOT_TOTAL_LEVEL);
	}

	public static UserCached createMatchEnemy(StreetResPlayerVO myResVO, int resId, int level) throws Exception {
		BaseResourceVO baseResourceVO = ResourceRes.getInstance().getResourceByGridId(resId);
		if (baseResourceVO == null) {
			throw new GameException(AlertEnum.STREET_RES_NOT_EXIST);
		}

		StreetResPlayerVO enemyVO = RedisMap.filterResPlayerVO(resId, level, myResVO.getPlayerId());
		UserCached enemy = null;
		if (enemyVO == null) {// 未找到满足条件敌人
			enemy = StreetConstants.getRobotEnemyByStar(baseResourceVO.getStar());
			StreetConstants.saveRobotCached(enemy, resId, myResVO.getPlayerId());
			enemyVO = new StreetResPlayerVO();
			enemyVO.setStartTime(DateUtil.getNow());
			enemyVO.setPlayerId(enemy.getPlayerId());
			enemyVO.setId(resId);
		} else {
			enemy = getNotRobotEnemy(enemyVO.getPlayerId());
		}
		enemyVO.setEnemyId(myResVO.getPlayerId());

		myResVO.setStatus(StreetConstants.PLAYER_RES_STATUS_ATK_PREPARE);
		myResVO.setEnemyId(enemyVO.getPlayerId());
		RedisMap.preStreetBattle(myResVO, enemyVO);
		return enemy;
	}

	/**
	 * 获得敌方信息
	 * 
	 * @param enemyId
	 * @param resId
	 *            资源点id
	 * @param playerId
	 *            己方id
	 * @return
	 */
	public static PlayerVO getEnemyVO(int enemyId, int resId, int playerId) {
		if (enemyId < 0) {// 机器人
			return StreetConstants.getRobotById(resId, playerId).getPlayerVO();
		} else {
			return RedisMap.getPlayerVObyId(enemyId);
		}
	}

	public static UserCached getEnemyById(int enemyId, int resId, int playerId) {
		if (enemyId > 0) {
			return getNotRobotEnemy(enemyId);
		} else {
			return StreetConstants.getRobotById(resId, playerId);
		}
	}

	/**
	 * 查找非机器人数据,如果当前不在线,则从redis取 <br/>
	 * 2014年7月23日
	 * 
	 * @author dingqu-pc100
	 */
	public static UserCached getNotRobotEnemy(int playerId) {
		UserCached userCached = new UserCached();
		userCached.setPlayerId(playerId);
		String playerVOjson = RedisMap.hget(RedisKey.PLAYERVO_MAP, String.valueOf(playerId));
		userCached.setPlayerVO(JSON.parseObject(playerVOjson, PlayerVO.class));
		// defHeros
		List<PlayerHeroVO> heros = RedisMap.hvalPlayerHeroVOs(playerId);

		userCached.getUserHero().setHeroList(heros);
		return userCached;
	}

	/**
	 * 检查是否复仇成功
	 * 
	 * @param userStreet
	 * @param enemyVO
	 * @param commandList
	 */
	public void revengeEnemy(UserStreet userStreet, PlayerVO enemyVO, List<NettyMessageVO> commandList) {
		List<StreetEnemyVO> enemies = userStreet.getEnemies();
		for (StreetEnemyVO streetEnemyVO : enemies) {
			if (streetEnemyVO.getEnemyId() == enemyVO.getPlayerId()) {// 成功复仇
				streetEnemyVO.setLevel(enemyVO.getLevel());
				streetEnemyVO.setIsRevenge(1);
				sqlSaveThread.putSql(streetEnemyVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.StreetMapper.updateEnemy", streetEnemyVO));
				NettyMessageVO nettyMessageVO = new NettyMessageVO();
				nettyMessageVO.setCommandCode(CSCommandConstant.STREET_LOG_ENEMY_UPDATE);
				nettyMessageVO.setData(EnemyUpdateResp.newBuilder().setStatus(ENEMY_STATUS.ENEMY_REVENG).setEnemyId(streetEnemyVO.getEnemyId()).build().toByteArray());
				commandList.add(nettyMessageVO);
				commandList.add(CommandUtil.packStreetGridFresh(GRID_FRESH_TYPE.FRESH_TYPE_REVENGE_SUCC, StreetConstants.CENTER_GRID_NUM));
				return;
			}
		}
	}

	// /**
	// * 加入报仇记录
	// */
	// public void clearRevengRecord(UserStreet userStreet, int anemyId) {
	// List<StreetEnemyVO> enemies = userStreet.getEnemies();
	// for (StreetEnemyVO streetEnemyVO : enemies) {
	// if (streetEnemyVO.getEnemyId() == anemyId) {// 成功复仇
	// streetEnemyVO.setIsRevenge(0);
	//
	//
	//
	//
	// sqlSaveThread.putSql(sqlBuild.getSql("com.dh.dao.StreetMapper.updateEnemy",
	// streetEnemyVO));
	// return;
	// }
	// }
	// }

	// public void addEnemy(UserStreet userStreet, PlayerVO enemyVO, boolean
	// isSucc) {
	// List<StreetEnemyVO> enemies = userStreet.getEnemies();
	// StreetEnemyVO streetEnemy = null;
	// for (StreetEnemyVO streetEnemyVO : enemies) {
	// if (streetEnemyVO.getEnemyId() == enemyVO.getPlayerId() && isSucc) {//
	// 成功复仇
	// streetEnemyVO.setIsRevenge(1);
	// streetEnemyVO.setLevel(enemyVO.getLevel());
	// streetEnemy = streetEnemyVO;
	// sqlSaveThread.putSql(sqlBuild.getSql("com.dh.dao.StreetMapper.updateEnemy",
	// streetEnemy));
	// return;
	// }
	// }
	// }

	public void addEnemy(UserStreet userStreet, StreetDefendLogVO streetDefendLogVO, PlayerVO enemyVO, List<NettyMessageVO> commandList) {
		List<StreetEnemyVO> enemies = userStreet.getEnemies();
		StreetEnemyVO streetEnemy = null;
		int now = DateUtil.getNow();
		for (StreetEnemyVO streetEnemyVO : enemies) {
			if (streetEnemyVO.getEnemyId() == streetDefendLogVO.getEnemyId()) {// 只能复仇一次
				streetEnemy = streetEnemyVO;
				// return;
				if (!DateUtil.isToday(streetEnemyVO.getAtkTime())) {
					streetEnemyVO.setIsRevenge(0);
					streetEnemyVO.setLevel(enemyVO.getLevel());
					streetEnemyVO.setAtkTime(now);
					sqlSaveThread.putSql(streetEnemyVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.StreetMapper.updateEnemy", streetEnemyVO));
					break;
				}
			}
		}
		if (streetEnemy == null) {
			streetEnemy = new StreetEnemyVO();
			streetEnemy.setPlayerId(userStreet.getPlayerId());
			streetEnemy.setAtkTime(now);
			streetEnemy.setEnemyId(streetDefendLogVO.getEnemyId());
			streetEnemy.setHeadIcon(enemyVO.getHeadIcon());
			streetEnemy.setLevel(enemyVO.getLevel());
			streetEnemy.setName(enemyVO.getName());
			streetEnemy.setIsRevenge(0);
			enemies.add(streetEnemy);

			sqlSaveThread.putSql(streetEnemy.getPlayerId(),sqlBuild.getSql("com.dh.dao.StreetMapper.insertEnemy", streetEnemy));
		}
		// NettyMessageVO nettyMessageVO = new NettyMessageVO();
		// nettyMessageVO.setCommandCode(CSCommandConstant.STREET_LOG_ENEMY_UPDATE);
		// nettyMessageVO.setData(EnemyLogResp.newBuilder().addEnemyLogInfo(VOUtil.packEnemyInfo(streetEnemy)).build().toByteArray());
		// commandList.add(nettyMessageVO);
	}

	/**
	 * 将最大的战斗力更新上去
	 * 
	 * @param userCached
	 * @param combat
	 */
	public void updateStreetResPlayerVOCombat(UserCached userCached, int combat) {
		// UserStreet userStreet = userCached.getUserStreet();
		// if (userStreet == null) {
		// return;
		// }
		// List<StreetResVO> resList = userStreet.getResList();
		// StreetResPlayerVO streetResPlayerVO;
		// String key = RedisKey.STREET_RES_FREE_PLAYER;
		// int resId;
		// int playerId = userCached.getPlayerId();
		// for (StreetResVO streetResVO : resList) {
		// resId = streetResVO.getId();
		// SyncLock lockObject =
		// SyncUtil.getInstance().getLock(LockConstant.LOCK_RES_PLAYERVO + "_" +
		// resId + "_" + playerId);
		// synchronized (lockObject) {
		// streetResPlayerVO = RedisMap.getStreetResPlayerVO(key, resId,
		// playerId);
		// if (streetResPlayerVO == null) {
		// key = RedisKey.STREET_RES_BATTLE_PLAYER;
		// streetResPlayerVO = RedisMap.getStreetResPlayerVO(key, resId,
		// playerId);
		// if (streetResPlayerVO == null) {
		// key = RedisKey.STREET_RES_PRODUCE_PLAYER;
		// streetResPlayerVO = RedisMap.getStreetResPlayerVO(key, resId,
		// playerId);
		// }
		// }
		// streetResPlayerVO =
		// RedisMap.getStreetResPlayerVO(userCached.getPlayerId(),
		// streetResVO.getId());
		// streetResPlayerVO.setMaxCombat(combat);
		// RedisMap.setResPlayerVO(key, streetResPlayerVO);
		// }
		// }

	}

}
