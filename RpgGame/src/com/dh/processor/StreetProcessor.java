package com.dh.processor;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.Cache.RedisMap;
import com.dh.Cache.ServerHandler;
import com.dh.constants.CommonConstants;
import com.dh.constants.StreetConstants;
import com.dh.enums.GMIOEnum;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.constant.RedisKey;
import com.dh.game.vo.base.BaseFazhenVO;
import com.dh.game.vo.base.BaseGridVO;
import com.dh.game.vo.base.BaseMachineVO;
import com.dh.game.vo.base.BaseResourceVO;
import com.dh.game.vo.base.Reward;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.street.StreetProto.BattleLogResp;
import com.dh.game.vo.street.StreetProto.BoxRewardReq;
import com.dh.game.vo.street.StreetProto.BoxRewardResp;
import com.dh.game.vo.street.StreetProto.BriefLogInfo;
import com.dh.game.vo.street.StreetProto.BriefLogResp;
import com.dh.game.vo.street.StreetProto.EnemyCenterReq;
import com.dh.game.vo.street.StreetProto.EnemyLogResp;
import com.dh.game.vo.street.StreetProto.FactoryMakeReq;
import com.dh.game.vo.street.StreetProto.FactoryResp;
import com.dh.game.vo.street.StreetProto.FactoryUpReq;
import com.dh.game.vo.street.StreetProto.FactoryUpResp;
import com.dh.game.vo.street.StreetProto.GRID_FRESH_TYPE;
import com.dh.game.vo.street.StreetProto.GridAtkFreshReq;
import com.dh.game.vo.street.StreetProto.GridFreshReq;
import com.dh.game.vo.street.StreetProto.GridFreshResp;
import com.dh.game.vo.street.StreetProto.HuntReq;
import com.dh.game.vo.street.StreetProto.HuntResp;
import com.dh.game.vo.street.StreetProto.LogRewardReq;
import com.dh.game.vo.street.StreetProto.LogRewardResp;
import com.dh.game.vo.street.StreetProto.MonsterInfo;
import com.dh.game.vo.street.StreetProto.OPEN_STATUS;
import com.dh.game.vo.street.StreetProto.OpenGridReq;
import com.dh.game.vo.street.StreetProto.OpenGridResp;
import com.dh.game.vo.street.StreetProto.RES_STATUS;
import com.dh.game.vo.street.StreetProto.ResBaseInfoResp;
import com.dh.game.vo.street.StreetProto.ResHeroInfo;
import com.dh.game.vo.street.StreetProto.ResInfoReq;
import com.dh.game.vo.street.StreetProto.ResInfoResp;
import com.dh.game.vo.street.StreetProto.ResItemInfo;
import com.dh.game.vo.street.StreetProto.ResRewardAllResp;
import com.dh.game.vo.street.StreetProto.ResRewardReq;
import com.dh.game.vo.street.StreetProto.ResRewardResp;
import com.dh.game.vo.street.StreetProto.StreetDefendReq;
import com.dh.game.vo.street.StreetProto.StreetDefendResp;
import com.dh.game.vo.street.StreetProto.StreetDefendTeamReq;
import com.dh.game.vo.street.StreetProto.StreetGridInfo;
import com.dh.game.vo.street.StreetProto.StreetQuitDefReq;
import com.dh.game.vo.street.StreetProto.StreetResInfo;
import com.dh.game.vo.street.StreetProto.UpGridResp;
import com.dh.game.vo.user.PlayerAccountVO;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.game.vo.user.PlayerVO;
import com.dh.game.vo.user.StreetBoxVO;
import com.dh.game.vo.user.StreetDefendLogVO;
import com.dh.game.vo.user.StreetEnemyVO;
import com.dh.game.vo.user.StreetMonsterVO;
import com.dh.game.vo.user.StreetResPlayerVO;
import com.dh.game.vo.user.StreetResVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.FaZhenRes;
import com.dh.resconfig.GridRes;
import com.dh.resconfig.MachineRes;
import com.dh.resconfig.ResourceRes;
import com.dh.resconfig.RewardRes;
import com.dh.service.HeroService;
import com.dh.service.KnapsackService;
import com.dh.service.MailService;
import com.dh.service.PlayerAccountService;
import com.dh.service.RewardService;
import com.dh.service.StreetService;
import com.dh.sync.LockConstant;
import com.dh.sync.SyncLock;
import com.dh.sync.SyncUtil;
import com.dh.util.CommandUtil;
import com.dh.util.DateUtil;
import com.dh.util.MsgUtil;
import com.dh.util.VOUtil;
import com.dh.vo.user.UserCached;
import com.dh.vo.user.UserHero;
import com.dh.vo.user.UserStreet;

@Component
public class StreetProcessor {
	public final static Logger logger = Logger.getLogger(StreetProcessor.class);
	@Resource
	private KnapsackService knapsackService;
	@Resource
	private StreetService streetService;
	@Resource
	private MailService mailService;
	@Resource
	private HeroService heroService;
	@Resource
	private PlayerAccountService playerAccountService;
	@Resource
	private RewardService rewardService;

	/**
	 * 主界面
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void enterHome(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {

		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();
		if (userStreet == null) {// 江湖尚未开启
			if (userCached.getPlayerVO().getLevel() < StreetConstants.STREET_OPEN_LEVEL) {
				throw new GameException(AlertEnum.ROLE_LEVEL_NO);
			}
			streetService.insertUserStreet(userCached, commandList);
			userStreet = userCached.getUserStreet();
		}
		userStreet.clearStatus();
		freshGrids(userCached, true, true, commandList);

		streetService.updatePlayerStreet(userStreet);
		nettyMessageVO.setData(VOUtil.packUserStreet(userStreet).toByteArray());
		commandList.add(nettyMessageVO);
	}

	/**
	 * 开启云雾<br/>
	 * dingqu-pc100<br/>
	 * 2014年7月17日
	 */
	public void openGrid(OpenGridReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int id = req.getId();
		checkGridValid(id);
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();
		if (userStreet.getGrids()[id] == 1) {
			throw new GameException(AlertEnum.STREET_GRID_OPENED);
		}
		if (!StreetConstants.canBeReachable(id, userStreet.getGrids())) {
			throw new GameException(AlertEnum.STREET_GRID_UNREACHABLE);
		}
		BaseGridVO baseGridVO = GridRes.getInstance().getGridById(id);
		if (userCached.getPlayerVO().getLevel() < baseGridVO.getOpenlevel()) {
			throw new GameException(AlertEnum.STREET_GRID_LEVEL_LOW);
		}
		int cost = baseGridVO.getItem1num();
		playerAccountService.hasEnoughMoney(userCached, cost);

		if (baseGridVO.getItem2id() != 0 && !knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), baseGridVO.getItem2id(), baseGridVO.getItem2num())) {
			throw new GameException(AlertEnum.ITEM_NUM_NOT);
		}
		userStreet.getGrids()[id] = 1;
		OpenGridResp.Builder resp = OpenGridResp.newBuilder();
		StreetGridInfo.Builder gridBuild = StreetGridInfo.newBuilder().setId(id).setOpenStaus(OPEN_STATUS.OPEN_CONTENT);
		BaseResourceVO baseResourceVO = ResourceRes.getInstance().getResourceByGridId(id);
		if (baseResourceVO != null) {// 需要生成资源点
			StreetResVO streetResVO = streetService.addResource(userCached, baseResourceVO);
			gridBuild.setStreetResInfo(VOUtil.packStreetRes(streetResVO));
		} else if (Math.random() < 0.4) {// 生成宝箱(40%概率)或者怪物(60%概率)
			StreetBoxVO box = streetService.addBox(userCached, id);
			gridBuild.setBoxId(box.getCfgId());
		} else {
			StreetMonsterVO monster = streetService.addMonster(userCached, id);
			gridBuild.setMonsterInfo(MonsterInfo.newBuilder().setMonsterId(monster.getCfgId()).setUrl(monster.getBaseMonsterGroupVO().getUrl()).setName(monster.getBaseMonsterGroupVO().getName())
					.setLevel(monster.getBaseMonsterGroupVO().getLevel()));
		}
		userStreet.setOpenedNum(userStreet.getOpenedNum() + 1);
		playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, cost, userCached.getPlayerAccountVO(), commandList, "开启云彩");
		if (baseGridVO.getItem2id() != 0) {
			knapsackService.removeItem(userCached, baseGridVO.getItem2id(), baseGridVO.getItem2num(), commandList);

		}
		streetService.updatePlayerStreet(userStreet);
		resp.addStreetGridInfo(gridBuild);
		byte[] grids = userStreet.getGrids();
		for (int i = 0; i < StreetConstants.MAX_GRIDS_NUM; i++) {
			if (grids[i] == 0 && userStreet.getReachbleGrids()[i] == 0 && StreetConstants.canBeReachable(i, userStreet.getGrids())) {
				resp.addStreetGridInfo(StreetGridInfo.newBuilder().setId(i).setOpenStaus(OPEN_STATUS.REACHABLE));
				userStreet.getReachbleGrids()[i] = 1;
			}
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/**
	 * 搜索 <br/>
	 * 2014年7月21日
	 * 
	 * @author dingqu-pc100
	 * @throws Exception
	 */
	public void hunt(HuntReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		int type = req.getType();
		int resId = req.getResId();
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();
		if (resId == StreetConstants.CENTER_GRID_NUM) {
			logger.error("门派无法搜索");
			return;
		}

		StreetResVO streetResVO = userStreet.getStreetResById(resId);
		if (streetResVO == null) {
			throw new GameException(AlertEnum.STREET_RES_NOT_EXIST);
		}

		StreetResPlayerVO myResVO = RedisMap.getStreetResPlayerVO(RedisKey.STREET_RES_FREE_PLAYER, resId, playerId);
		UserCached enemy;
		PlayerVO enemyVO;
		StreetResVO streetRes;
		if (myResVO != null && (type == 0 || myResVO.getEnemyId() == 0)) {
			enemy = StreetService.createMatchEnemy(myResVO, resId, userCached.getPlayerVO().getLevel());
			enemyVO = enemy.getPlayerVO();
		} else {// 在战斗队列中
			myResVO = RedisMap.getStreetResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, resId, playerId);
			enemy = StreetService.getEnemyById(myResVO.getEnemyId(), resId, myResVO.getPlayerId());
			enemyVO = enemy.getPlayerVO();
		}

		if (enemyVO.getPlayerId() > 0) {
			streetRes = RedisMap.getStreetResByPIdAndRId(resId, enemyVO.getPlayerId());
		} else {
			streetRes = new StreetResVO();
			UserStreet enemyStreet = enemy.getUserStreet();
			streetRes.setPlayerId(enemy.getPlayerId());
			streetRes.setId(resId);
			streetRes.setMachineLine(enemyStreet.getRobotMachineLine() == null ? "" : enemyStreet.getRobotMachineLine());
			streetRes.setFzId(enemyStreet.getRobotFzId());
		}
		streetResVO.setStatus(RES_STATUS.OCCUPY_VALUE);

		// ##未完成:战斗力还未计算##
		HuntResp.Builder resp = HuntResp.newBuilder().setResId(resId).setHeadIcon(enemyVO.getHeadIcon()).setNick(enemyVO.getName()).setLevel(enemyVO.getLevel()).setPlayerId(enemy.getPlayerId());
		resp.setVip(enemyVO.getVip());
		List<PlayerHeroVO> defHeros = enemy.getUserHero().getDefendHerosByResId(resId);

		nettyMessageVO.setData(VOUtil.packHuntResp(resp, streetRes, defHeros).build().toByteArray());
		StreetService.packStreetGridUpdate(1, streetResVO, 0, 0, commandList);
		commandList.add(nettyMessageVO);
	}

	public void getResInfo(ResInfoReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		int resId = req.getResId();

		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();
		List<PlayerHeroVO> defHeros = userCached.getUserHero().getDefendHerosByResId(resId);
		ResInfoResp.Builder resp = ResInfoResp.newBuilder();
		for (PlayerHeroVO playerHeroDefVO : defHeros) {
			resp.addFinalHero(VOUtil.getFinalHero(playerHeroDefVO));
		}
		String machineLine = null;
		StreetResVO streetResVO = userStreet.getStreetResById(resId);
		if ((machineLine = streetResVO.getMachineLine()) != null && !machineLine.isEmpty()) {
			String[] machines = StreetConstants.HERO_SPLIT_CHAR.split(machineLine);
			for (String string : machines) {
				resp.addMachineId(Integer.valueOf(string));
			}
		}
		if (streetResVO.getFzId() != 0)
			resp.setFzId(streetResVO.getFzId());
		int timeLeft = (int) (streetResVO.getBeginTime() + StreetConstants.RES_OWN_PERIOD / 1000 - DateUtil.getNow());
		resp.setTimeLeft(Math.max(0, timeLeft));
		resp.setResId(resId);
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	public void enemyCenter(EnemyCenterReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();
		int enemyId = req.getEnemyId();
		StreetEnemyVO streetEnemyVO = userStreet.getEnemyById(enemyId);
		StreetResPlayerVO enemyResPlayerVO = RedisMap.getStreetResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, StreetConstants.CENTER_GRID_NUM, enemyId);
		if (streetEnemyVO == null || enemyResPlayerVO == null) {// 不存在敌人
			throw new GameException(AlertEnum.STREET_ENEMY_NOT_EXIST);
		}
		StreetResVO streetRes = RedisMap.getStreetResByPIdAndRId(StreetConstants.CENTER_GRID_NUM, enemyId);

		if (StreetConstants.hasDaddyInTime(streetRes.getWhosyourdaddy())) {// 封山令
			throw new GameException(AlertEnum.RES_WHOSYOUDADDY);
		}

		if (!StreetConstants.hasBattleOverTime(enemyResPlayerVO.getStartTime())) {// 门派战斗还未结束
			throw new GameException(AlertEnum.STREET_BATTLE_CENTER_BUSYING);
		}

		UserCached enemyCache = StreetService.getNotRobotEnemy(enemyId);
		PlayerVO enemyVO = enemyCache.getPlayerVO();

		List<PlayerHeroVO> defHeros = enemyCache.getUserHero().getDefendHerosByResId(StreetConstants.CENTER_GRID_NUM);

		nettyMessageVO.setData(VOUtil.packEnemyCenter(enemyVO, streetRes, defHeros).build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/**
	 * 获得自身资源点防守队伍
	 * 
	 * @param req
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void getDefendTeam(StreetDefendTeamReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int resId = req.getResId();
		UserCached userCached = ServerHandler.getUserCached(ServerHandler.get(nettyMessageVO.getChannel()));
		UserStreet userStreet = userCached.getUserStreet();
		StreetResVO streetResVO = userStreet.getStreetResById(resId);
		if (streetResVO == null) {
			throw new GameException(AlertEnum.STREET_RES_NOT_EXIST);
		}

		List<PlayerHeroVO> defHeros = userCached.getUserHero().getDefendHerosByResId(resId);
		nettyMessageVO.setData(VOUtil.packResDefend(defHeros, streetResVO).build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/**
	 * 部署资源点防御 <br/>
	 * 2014年7月23日
	 * 
	 * @author dingqu-pc100
	 * @throws Exception
	 */
	public void configDefend(StreetDefendReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		UserCached userCached = ServerHandler.getUserCached(ServerHandler.get(nettyMessageVO.getChannel()));
		UserStreet userStreet = userCached.getUserStreet();

		int playerId = userCached.getPlayerId();
		int resId = req.getResId();
		int fzId = req.getFzId();
		boolean isCenter = resId == StreetConstants.CENTER_GRID_NUM;
		List<Integer> configMIds = req.getMachineIdList();
		List<PlayerHeroVO> heros = userCached.getUserHero().getHeroList();
		List<Integer> heroIds = req.getHeroIdList();

		NettyMessageVO ownResMsg = null;
		if (heroIds.size() + configMIds.size() == 0) {
			throw new GameException(AlertEnum.STREET_CONFIG_NOT_NULL);
		}
		if (heroIds.size() + configMIds.size() > 5) {
			throw new GameException(AlertEnum.STREET_RES_DEFEND_ERR);
		}
		if (isHerosHasUser(heros, resId, heroIds, commandList)) {
			throw new GameException(AlertEnum.HERO_BUSYING);
		}

		StreetResVO streetResVO = userStreet.getStreetResById(resId);
		if (streetResVO == null) {
			throw new GameException(AlertEnum.STREET_RES_NOT_EXIST);
		}
		StreetResPlayerVO resPlayerVO;
		String key;
		SyncLock lockObject = SyncUtil.getInstance().getLock(LockConstant.LOCK_RES_PLAYERVO + "_" + resId + "_" + playerId);
		synchronized (lockObject) {
			key = RedisKey.STREET_RES_BATTLE_PLAYER;
			resPlayerVO = RedisMap.getStreetResPlayerVO(key, resId, playerId);
			if (resPlayerVO == null) {// 生产状态
				key = RedisKey.STREET_RES_PRODUCE_PLAYER;
				resPlayerVO = RedisMap.getStreetResPlayerVO(key, resId, playerId);
			} else {
				if (resPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_PRODUCING) {// 被搜索到但是还未攻击

				} else if (resPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_BATTLE_DEF) {// 被攻击的准备状态
					if (StreetConstants.hasBattleOverTime(resPlayerVO.getBattleTime())) {// 攻击超时,直接回退到
						resPlayerVO.setBattleTime(0);
						resPlayerVO.setEnemyId(0);
					} else {// 被攻击状态无法进行配置队伍或者撤回操作
						throw new GameException(AlertEnum.RES_BATTLING);
					}

				} else if (resPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_ATK_SUCC) {// 攻击成功,开始生产
					int now = DateUtil.getNow();
					resPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_PRODUCING);
					resPlayerVO.setStartTime(now);
					resPlayerVO.setEnemyId(0);

					streetResVO.setStatus(RES_STATUS.OWN_VALUE);
					streetResVO.setBeginTime(now);
					streetResVO.setCalTime(now);
					RedisMap.delResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, resPlayerVO);
					// 成功防守给前台推送驻守成功包
					ownResMsg = CommandUtil.packStreetGridFresh(GRID_FRESH_TYPE.FRESH_TYPE_OWN_SUCC, resId);
				}
				if (isCenter) {
					key = RedisKey.STREET_RES_BATTLE_PLAYER;
				} else {
					key = RedisKey.STREET_RES_PRODUCE_PLAYER;
				}
				StreetService.packStreetGridUpdate(1, streetResVO, 0, 0, commandList);
			}
			resPlayerVO.setLevel(userCached.getPlayerVO().getLevel());
			updateResDeHeros(userCached, heros, resId, heroIds, commandList);
			RedisMap.setResPlayerVO(key, resPlayerVO);
		}

		StringBuffer machineLine = new StringBuffer(streetResVO.getMachineLine() == null ? "" : streetResVO.getMachineLine());
		for (Integer integer : configMIds) {
			userStreet.removeMachineById(integer);
			machineLine.append(StreetConstants.HERO_SPLIT_CHAR).append(integer);
		}
		streetResVO.setMachineLine(machineLine.toString());

		if (fzId != 0) {
			userStreet.removeFzById(fzId);
			streetResVO.setFzId(req.getFzId());
		}
		streetService.updateStreetRes(streetResVO);

		nettyMessageVO.setData(StreetDefendResp.newBuilder().setResId(resId).setFzId(fzId).addAllHeroId(req.getHeroIdList()).addAllMachineId(req.getMachineIdList()).build().toByteArray());
		commandList.add(nettyMessageVO);
		if (ownResMsg != null) {
			commandList.add(ownResMsg);
		}
	}

	/**
	 * 战斗成功放弃驻守
	 * 
	 * @param req
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void quitDef(StreetQuitDefReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();
		int resId = req.getResId();
		boolean isCenter = resId == StreetConstants.CENTER_GRID_NUM;
		if (isCenter) {
			throw new GameException(AlertEnum.STREET_CENTER_CANT_GIVEUP);
		}
		List<PlayerHeroVO> defHeros = userCached.getUserHero().getDefendHerosByResId(resId);
		StreetResVO streetResVO = userStreet.getStreetResById(resId);
		SyncLock lockObject = SyncUtil.getInstance().getLock(LockConstant.LOCK_RES_PLAYERVO + "_" + resId + "_" + playerId);
		String key;
		synchronized (lockObject) {
			StreetResPlayerVO streetResPlayerVO = null;
			key = RedisKey.STREET_RES_BATTLE_PLAYER;
			streetResPlayerVO = RedisMap.getStreetResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, resId, playerId);// 战斗胜利的放弃驻守
			if (streetResPlayerVO == null) {
				key = RedisKey.STREET_RES_PRODUCE_PLAYER;
				streetResPlayerVO = RedisMap.getStreetResPlayerVO(RedisKey.STREET_RES_PRODUCE_PLAYER, resId, playerId);// 生产状态的放弃驻守
				if (streetResPlayerVO == null) {
					throw new GameException(AlertEnum.STREET_RES_NOT_PRODUCING);
				}
			} else {
				if (StreetConstants.hasBattleOverTime(streetResPlayerVO.getBattleTime())) {// 攻击超时,直接回退到
					streetResPlayerVO.setBattleTime(0);
					streetResPlayerVO.setEnemyId(0);
				} else {
					throw new GameException(AlertEnum.RES_BATTLING);
				}
			}
			RedisMap.delResPlayerVO(key, streetResPlayerVO);
			// RedisMap.hdel(key + streetResPlayerVO.getId(),
			// String.valueOf(streetResPlayerVO.getPlayerId()));
			StreetService.freeStreetResAndPlayer(streetResVO, streetResPlayerVO, commandList);
			for (PlayerHeroVO playerHeroVO : defHeros) {
				heroService.freeHeroFromStreet(playerHeroVO, commandList);
			}
		}
	}

	public void updateResDeHeros(UserCached userCached, List<PlayerHeroVO> heros, int resId, List<Integer> heroIds, List<NettyMessageVO> commandList) throws Exception {
		List<Integer> newHeroIds = new ArrayList<Integer>(5);
		List<Integer> againHeroIds = new ArrayList<Integer>(5);

		for (PlayerHeroVO playerHeroVO : heros) {// 已经存在的队列
			if (playerHeroVO.getLineStatus() == resId) {
				boolean againExist = false;
				for (Integer integer : heroIds) {
					if (playerHeroVO.getId() == integer) {
						againExist = true;
						againHeroIds.add(integer);
						break;
					}
				}
				// 不在使用的hero清理
				if (!againExist) {
					heroService.freeHeroFromStreet(playerHeroVO, commandList);
				}
			}
		}
		for (Integer allId : heroIds) {
			boolean isExist = false;
			for (Integer againId : againHeroIds) {
				if (allId == againId) {
					isExist = true;
					break;
				}
			}
			if (!isExist) {
				newHeroIds.add(allId);
			}
		}

		for (Integer integer : newHeroIds) {
			for (PlayerHeroVO playerHeroVO : heros) {
				if (integer == playerHeroVO.getId()) {
					if (playerHeroVO.getHang_status() != 0) {
						throw new GameException(AlertEnum.HERO_BUSYING);
					}
					playerHeroVO.setLineStatus(resId);
					playerHeroVO.setHang_status(CommonConstants.HANG_STATUS_STREET);
					heroService.updateHero(playerHeroVO);
					userCached.getUserHero().getAtkHeroList().remove(playerHeroVO);
					CommandUtil.updateHeroStatus(playerHeroVO, commandList);
				}
			}
		}

	}

	public boolean isHerosHasUser(List<PlayerHeroVO> heros, int resId, List<Integer> heroIds, List<NettyMessageVO> commandList) {

		List<Integer> newHeroIds = new ArrayList<Integer>(5);
		List<Integer> againHeroIds = new ArrayList<Integer>(5);

		for (PlayerHeroVO playerHeroVO : heros) {// 已经存在的队列
			if (playerHeroVO.getLineStatus() == resId) {
				for (Integer integer : heroIds) {
					if (playerHeroVO.getId() == integer) {
						againHeroIds.add(integer);
						break;
					}
				}
			}
		}
		for (Integer allId : heroIds) {
			boolean isExist = false;
			for (Integer againId : againHeroIds) {
				if (allId == againId) {
					isExist = true;
					break;
				}
			}
			if (!isExist) {
				newHeroIds.add(allId);
			}
		}

		for (Integer integer : newHeroIds) {
			for (PlayerHeroVO playerHeroVO : heros) {
				if (integer == playerHeroVO.getId()) {
					if (playerHeroVO.getHang_status() != 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 获得军器坊
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void getFactory(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();
		String fzStr = userStreet.getFactoryFz();
		String mStr = userStreet.getFactoryM();
		FactoryResp.Builder resp = FactoryResp.newBuilder();
		if (!fzStr.isEmpty()) {
			String[] FzIds = StreetConstants.HERO_SPLIT_CHAR.split(fzStr);
			for (int i = FzIds.length - 1; i >= 0; i--) {
				resp.addFIds(Integer.valueOf(FzIds[i]));
			}
		}
		if (!mStr.isEmpty()) {
			String[] mIds = StreetConstants.HERO_SPLIT_CHAR.split(mStr);
			for (int i = mIds.length - 1; i >= 0; i--) {
				resp.addMIds(Integer.valueOf(mIds[i]));
			}
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/**
	 * 器械法阵升级
	 * 
	 * @param req
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void factoryUp(FactoryUpReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();

		int type = req.getType();
		int cfgId = req.getCfgId();
		boolean notExist = true;
		if (type == 1) {// machine
			String mStr = userStreet.getFactoryM();
			StringBuffer tempMStr = new StringBuffer();
			BaseMachineVO baseMachineVO = MachineRes.getInstance().getMachineById(cfgId);
			if (baseMachineVO == null) {// 如果不是一级必须从以前基础上升级
				throw new GameException(AlertEnum.STREET_FACTORY_UPDATE_M);
			}
			if (mStr != null) {
				String[] mIds = StreetConstants.HERO_SPLIT_CHAR.split(mStr);
				for (String string : mIds) {
					if (Integer.valueOf(string) != baseMachineVO.getCfgId()) {
						tempMStr.append(string).append(StreetConstants.HERO_SPLIT_CHAR);
					} else {
						notExist = false;
					}
				}
			}
			if (notExist) {
				throw new GameException(AlertEnum.STREET_FACTORY_UPDATE_M);
			}
			if (baseMachineVO.getReq_level() > userCached.getPlayerVO().getLevel()) {
				throw new GameException(AlertEnum.ROLE_LEVEL_NO);
			}
			if (baseMachineVO.getLevel() == userCached.getPlayerVO().getLevel()) {
				throw new GameException(AlertEnum.LEVEL_NOT_MORE_THAN_ROLE);
			}
			int cost = baseMachineVO.getUpdate_number1();
			playerAccountService.hasEnoughMoney(userCached, cost);
			if (!knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), baseMachineVO.getUpdate_cost2(), baseMachineVO.getUpdate_number2())) {
				throw new GameException(AlertEnum.ITEM_NUM_NOT);
			}

			BaseMachineVO upBseMachine = MachineRes.getInstance().getMachineByTypeAndLevel(baseMachineVO.getType(), baseMachineVO.getLevel() + 1);
			if (upBseMachine == null) {
				throw new GameException(AlertEnum.STREET_FACTORY_MAX_LEVEL);
			}

			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, cost, userCached.getPlayerAccountVO(), commandList, "升级器械");
			knapsackService.removeItem(userCached, baseMachineVO.getUpdate_cost2(), baseMachineVO.getUpdate_number2(), commandList);
			cfgId = upBseMachine.getCfgId();
			tempMStr.append(cfgId).append(StreetConstants.HERO_SPLIT_CHAR);
			userStreet.setFactoryM(tempMStr.toString());
		} else {// fz
			String fzStr = userStreet.getFactoryFz();
			StringBuffer tempFzStr = new StringBuffer();
			BaseFazhenVO baseFazhen = FaZhenRes.getInstance().getFazhenById(cfgId);
			if (baseFazhen == null) {
				throw new GameException(AlertEnum.STREET_FACTORY_UPDATE_FZ);
			}
			if (fzStr != null) {
				String[] fzIds = StreetConstants.HERO_SPLIT_CHAR.split(fzStr);
				for (String string : fzIds) {
					if (Integer.valueOf(string) != baseFazhen.getCfgId()) {
						tempFzStr.append(string).append(StreetConstants.HERO_SPLIT_CHAR);
					} else {
						notExist = false;
					}
				}
			}
			if (notExist) {
				throw new GameException(AlertEnum.STREET_FACTORY_UPDATE_FZ);
			}

			if (baseFazhen.getReq_level() > userCached.getPlayerVO().getLevel()) {
				throw new GameException(AlertEnum.ROLE_LEVEL_NO);
			}
			if (baseFazhen.getLevel() == userCached.getPlayerVO().getLevel()) {
				throw new GameException(AlertEnum.LEVEL_NOT_MORE_THAN_ROLE);
			}
			int cost = baseFazhen.getUpdate_number1();
			playerAccountService.hasEnoughMoney(userCached, cost);
			if (!knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), baseFazhen.getUpdate_cost2(), baseFazhen.getUpdate_number2())) {
				throw new GameException(AlertEnum.ITEM_NUM_NOT);
			}
			BaseFazhenVO upFazhenVO = FaZhenRes.getInstance().getFaZhenByTypeAndLevel(baseFazhen.getType(), baseFazhen.getLevel() + 1);
			if (upFazhenVO == null) {
				throw new GameException(AlertEnum.STREET_FACTORY_MAX_LEVEL);
			}
			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, cost, userCached.getPlayerAccountVO(), commandList, "升级器械");
			cfgId = upFazhenVO.getCfgId();
			tempFzStr.append(cfgId).append(StreetConstants.HERO_SPLIT_CHAR);
			userStreet.setFactoryFz(tempFzStr.toString());
			System.out.println("升级fz结果: " + tempFzStr);
			knapsackService.removeItem(userCached, baseFazhen.getUpdate_cost2(), baseFazhen.getUpdate_number2(), commandList);
		}
		streetService.updatePlayerStreet(userStreet);
		nettyMessageVO.setData(FactoryUpResp.newBuilder().setCfgId(cfgId).setType(type).build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/**
	 * 制造
	 * 
	 * @param req
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void factoryMake(FactoryMakeReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();

		int type = req.getType();
		int cfgId = req.getCfgId();
		boolean notExist = true;

		if (type == 1) {// machine
			String mStr = userStreet.getFactoryM();
			if (mStr != null) {
				String[] mIds = StreetConstants.HERO_SPLIT_CHAR.split(mStr);
				for (String string : mIds) {
					if (Integer.valueOf(string) == cfgId) {
						notExist = false;
						break;
					}
				}
			}
			BaseMachineVO baseMachineVO = MachineRes.getInstance().getMachineById(cfgId);

			if (baseMachineVO == null || notExist) {// 如果不是一级必须从以前基础上升级
				throw new GameException(AlertEnum.STREET_FACTORY_MAKE_M);
			}
			if (baseMachineVO.getReq_level() > userCached.getPlayerVO().getLevel()) {
				throw new GameException(AlertEnum.ROLE_LEVEL_NO);
			}
			int cost = baseMachineVO.getProduce_number1();
			playerAccountService.hasEnoughMoney(userCached, cost);
			if (!knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), baseMachineVO.getProduce_cost2(), baseMachineVO.getProduce_number2())) {
				throw new GameException(AlertEnum.ITEM_NUM_NOT);
			}

			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, cost, userCached.getPlayerAccountVO(), commandList, "升级器械");
			knapsackService.removeItem(userCached, baseMachineVO.getProduce_cost2(), baseMachineVO.getProduce_number2(), commandList);
			userStreet.addMachine(baseMachineVO.getCfgId());
		} else {// fz
			String fzStr = userStreet.getFactoryFz();
			if (fzStr != null) {
				String[] fzIds = StreetConstants.HERO_SPLIT_CHAR.split(fzStr);
				for (String string : fzIds) {
					if (Integer.valueOf(string) == cfgId) {
						notExist = false;
					}
				}
			}
			BaseFazhenVO baseFazhen = FaZhenRes.getInstance().getFazhenById(cfgId);
			if (baseFazhen == null || notExist) {// 如果不是一级必须从以前基础上升级
				throw new GameException(AlertEnum.STREET_FACTORY_MAKE_FZ);
			}
			if (baseFazhen.getReq_level() > userCached.getPlayerVO().getLevel()) {
				throw new GameException(AlertEnum.ROLE_LEVEL_NO);
			}
			int cost = baseFazhen.getProduce_cost1();
			playerAccountService.hasEnoughMoney(userCached, cost);
			if (!knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), baseFazhen.getProduce_cost2(), baseFazhen.getProduce_number2())) {
				throw new GameException(AlertEnum.ITEM_NUM_NOT);
			}
			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, cost, userCached.getPlayerAccountVO(), commandList, "升级器械");
			knapsackService.removeItem(userCached, baseFazhen.getProduce_cost2(), baseFazhen.getProduce_number2(), commandList);
			userStreet.addFz(baseFazhen.getProduce_cost2());
		}
		streetService.updatePlayerStreet(userStreet);
		nettyMessageVO.setData(FactoryUpResp.newBuilder().setCfgId(cfgId).setType(type).build().toByteArray());
		commandList.add(nettyMessageVO);

	}

	/**
	 * 获取江湖简报
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void briefLog(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();
		if (userStreet == null) {
			return;
		}

		BriefLogResp.Builder resp = BriefLogResp.newBuilder();
		List<StreetResVO> resList = userStreet.getResList();
		TIntObjectHashMap<List<StreetResPlayerVO>> resMap = new TIntObjectHashMap<List<StreetResPlayerVO>>(6);
		for (StreetResVO streetResVO : resList) {
			int type = streetResVO.getBaseResourceVO().getType();
			if (type == 100) {
				continue;
			}
			List<StreetResPlayerVO> resTypeList = resMap.get(type);
			if (resTypeList == null) {
				resTypeList = new ArrayList<StreetResPlayerVO>();
				resMap.put(type, resTypeList);
			}

			StreetResPlayerVO streetResPlayerVO = (StreetResPlayerVO) RedisMap.getStreetResPlayerVO(streetResVO.getPlayerId(), streetResVO.getId())[0];
			resTypeList.add(streetResPlayerVO);
		}

		for (int type : resMap.keys()) {
			int producing = 0;
			List<StreetResPlayerVO> resTypeList = resMap.get(type);
			for (StreetResPlayerVO streetResPlayerVO : resTypeList) {
				if (streetResPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_PRODUCING) {
					producing++;
				}
			}
			resp.addBriefLogInfo(BriefLogInfo.newBuilder().setProducing(producing).setTotal(resTypeList.size()).setType(type));
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/**
	 * 战报
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void battltLog(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();

		List<StreetDefendLogVO> logs = userStreet.getDefendLogs();
		Collections.sort(logs);
		BattleLogResp.Builder resp = BattleLogResp.newBuilder();
		for (StreetDefendLogVO streetDefendLogVO : logs) {
			StreetEnemyVO streetEnemyVO = userStreet.getEnemyById(streetDefendLogVO.getEnemyId());
			resp.addBattleLogInfo(VOUtil.packStreetDefLogs(streetDefendLogVO, streetEnemyVO));
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/**
	 * 仇家信息
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void enemyLog(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();

		List<StreetEnemyVO> enemies = userStreet.getEnemies();
		EnemyLogResp.Builder resp = EnemyLogResp.newBuilder();
		for (StreetEnemyVO streetEnemyVO : enemies) {
			StreetResVO enemyStreetRes = RedisMap.getStreetResByPIdAndRId(StreetConstants.CENTER_GRID_NUM, streetEnemyVO.getEnemyId());
			if (enemyStreetRes == null) {
				logger.error("江湖数据异常,playerId:" + streetEnemyVO.getPlayerId() + "\tenemyId:" + streetEnemyVO.getEnemyId());
			} else {
				streetEnemyVO.setHidden(enemyStreetRes.getWhosyourdaddy() > 0 && StreetConstants.hasDaddyInTime(enemyStreetRes.getWhosyourdaddy() + 0));
				resp.addEnemyLogInfo(VOUtil.packEnemyInfo(streetEnemyVO));
			}
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/**
	 * 领取战报奖励
	 * 
	 * @param req
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void logReward(LogRewardReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int id = req.getId();
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();
		List<StreetDefendLogVO> logs = userStreet.getDefendLogs();
		boolean notExist = true;
		for (int i = 0; i < logs.size(); i++) {
			if (logs.get(i).getId() == id) {
				notExist = false;
				if (logs.get(i).getIsReward() == 1) {
					throw new GameException(AlertEnum.COMMON_HAD_REWARD);
				}

				List<Reward> rewards = MailService.decodeRewardString(logs.get(i).getRewards());
				boolean isSucc = rewardService.reward(userCached, rewards, commandList);
				if (isSucc) {
					logs.get(i).setIsReward(1);
					streetService.updateStreetDefendLog(logs.get(i));
					nettyMessageVO.setData(LogRewardResp.newBuilder().setId(id).build().toByteArray());
					commandList.add(nettyMessageVO);
				}
				break;
			}

		}
		if (notExist) {// 可能是战报
			throw new GameException(AlertEnum.STREET_LOG_REWARD_FAIL);
		}

	}

	/**
	 * 领取资源点产出
	 * 
	 * @param req
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void resReward(ResRewardReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();
		int type = req.getType();
		int resId = req.getResId();
		StreetResVO streetResVO = userStreet.getStreetResById(resId);
		if (streetResVO == null) {
			throw new GameException(AlertEnum.STREET_RES_NOT_EXIST);
		}
		List<Reward> rewards = calStreetResReward(streetResVO);
		boolean isSucc;
		if (type != 0) {
			List<Reward> typeReward = new ArrayList<Reward>();
			List<Reward> notTypeReward = new ArrayList<Reward>();
			for (Reward reward : rewards) {
				if (reward.getType() == type) {
					typeReward.add(reward);
				} else {
					notTypeReward.add(reward);
				}
			}
			isSucc = rewardService.reward(userCached, typeReward, commandList);
			if (isSucc) {
				streetResVO.setItems(notTypeReward);
			}
		} else {
			rewardService.rewardAndMail(userCached, rewards, commandList, "资源点产出");
			// streetResVO.getItems().clear();
		}

		streetService.updateStreetRes(streetResVO);
		nettyMessageVO.setData(ResRewardResp.newBuilder().setStreetResInfo(VOUtil.packStreetRes(streetResVO)).build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	public void ResRewardAll(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();
		List<StreetResVO> resList = userStreet.getResList();
		List<Reward> rewards = new ArrayList<Reward>();
		ResRewardAllResp.Builder resp = ResRewardAllResp.newBuilder();
		for (StreetResVO streetResVO : resList) {
			if (streetResVO.getId() != StreetConstants.CENTER_GRID_NUM) {
				List<Reward> rewardList = calStreetResReward(streetResVO);
				if (!rewardList.isEmpty()) {
					rewards.addAll(rewardList);
					streetService.updateStreetRes(streetResVO);
					resp.addStreetResInfo(VOUtil.packStreetRes(streetResVO));
				}
			}
		}
		rewardService.rewardAndMail(userCached, rewards, commandList, "资源点产出");
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/**
	 * 开宝箱
	 * 
	 * @param req
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void boxReward(BoxRewardReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int gridId = checkGridValid(req.getGridId());
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();

		List<StreetBoxVO> boxList = userStreet.getBoxList();
		BoxRewardResp.Builder resp = BoxRewardResp.newBuilder();

		for (StreetBoxVO streetBoxVO : boxList) {
			if (streetBoxVO.getId() == gridId) {
				rewardService.rewardAndMail(userCached, streetBoxVO.getBaseBoxVO().getRewards(), commandList, "江湖开宝箱", GMIOEnum.IN_STREET_BOX.usage());
				boxList.remove(streetBoxVO);
				userStreet.getFreeGrids()[gridId] = 1;
				streetService.deleteGridBox(streetBoxVO);
				resp.setStreetGridInfo(StreetGridInfo.newBuilder().setId(gridId).setOpenStaus(OPEN_STATUS.OPEN_NOCENTENT));
				break;
			}
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/** 处理格子刷新 */
	public void monsterAndBoxFresh(GridFreshReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int type = req.getType();
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();
		byte[] freshGrids = freshGrids(userCached, type == 1, type == 2, commandList);
		GridFreshResp.Builder resp = GridFreshResp.newBuilder();
		for (int i = 0; i < freshGrids.length; i++) {
			if (freshGrids[1] == 1) {// 宝箱
				resp.addStreetGridInfo(StreetGridInfo.newBuilder().setId(i).setBoxId(userStreet.getBoxByResId(i).getCfgId()).setOpenStaus(OPEN_STATUS.OPEN_CONTENT));
			} else if (freshGrids[i] == 2) {
				StreetMonsterVO monster = userStreet.getMonsterByResId(i);
				resp.addStreetGridInfo(StreetGridInfo
						.newBuilder()
						.setId(i)
						.setMonsterInfo(
								MonsterInfo.newBuilder().setMonsterId(monster.getCfgId()).setUrl(monster.getBaseMonsterGroupVO().getUrl()).setLevel(monster.getBaseMonsterGroupVO().getLevel())
										.setName(monster.getBaseMonsterGroupVO().getName())).setOpenStaus(OPEN_STATUS.OPEN_CONTENT));
			}
		}
		int now = DateUtil.getNow();
		resp.setBoxLeft((StreetConstants.BOX_FRESH_PERIOD - (now - userStreet.getBoxFreshTime()))).setMonsterLeft(StreetConstants.MONSTER_FRESH_PERIOD - (now - userStreet.getMonsterFreshTime()));
		nettyMessageVO.setData(resp.build().toByteArray());
		freshGrids = null;
		commandList.add(nettyMessageVO);
	}

	/**
	 * @param userCached
	 * @param freshBox
	 * @param freshMonster
	 * @param commandList
	 * @return
	 * @throws Exception
	 */
	private byte[] freshGrids(UserCached userCached, boolean freshBox, boolean freshMonster, List<NettyMessageVO> commandList) throws Exception {
		UserStreet userStreet = userCached.getUserStreet();
		List<StreetResVO> resList = userStreet.getResList();
		byte[] temp = Arrays.copyOf(userStreet.getGrids(), StreetConstants.MAX_GRIDS_NUM);
		byte[] freshGrids = new byte[StreetConstants.MAX_GRIDS_NUM];

		for (StreetResVO streetResVO : resList) {// 增加资源点
			if (freshBox && freshMonster) {// 对怪物和宝箱的定时刷新,不刷新资源点,特别地由于怪物和宝箱刷新是错开的.
				freshRes(userCached, streetResVO, commandList);
			}
			temp[streetResVO.getId()] = 0;
		}
		for (StreetBoxVO streetBoxVO : userStreet.getBoxList()) {// 增加宝箱
			temp[streetBoxVO.getId()] = 0;
		}
		for (StreetMonsterVO streetMonsterVO : userStreet.getMonsterList()) {// 增加怪物
			temp[streetMonsterVO.getId()] = 0;
		}

		int now = DateUtil.getNow();
		int boxTime = Math.min((now - userStreet.getBoxFreshTime()) / StreetConstants.BOX_FRESH_PERIOD, 5);
		int monsterTime = Math.min((now - userStreet.getBoxFreshTime()) / StreetConstants.MONSTER_FRESH_PERIOD, 5);
		boolean isFreshBox = boxTime > 0;
		boolean isFreshMonster = monsterTime > 0 + 0;

		while (StreetService.checkFreshNotFull(userStreet) && (boxTime > 0 || monsterTime > 0)) {
			for (int i = 0; i < StreetConstants.MAX_GRIDS_NUM && temp[i] == 1; i++) {// 为1则为开启的空格,且当前没有宝箱或者怪物,需要刷新
				if (temp[i] == 0) {
					continue;
				}

				boolean canFreshBox = boxTime > 0;
				boolean canFreshMonster = monsterTime > 0;
				double random = Math.random();
				if (freshBox && canFreshBox && random < 0.3) {
					streetService.addBox(userCached, i);
					freshGrids[i] = 1;
					temp[i] = 0;
					continue;
				} else if (freshMonster && canFreshMonster && random >= 0.3 && random < 0.6) {
					streetService.addMonster(userCached, i);
					freshGrids[i] = 2;
					temp[i] = 0;
					continue;
				}
			}
			boxTime--;
			monsterTime--;
		}
		userStreet.setFreeGrids(temp);
		if (isFreshBox)
			userStreet.setBoxFreshTime(now);
		if (isFreshMonster)
			userStreet.setMonsterFreshTime(now);
		return freshGrids;
	}

	private int calHour(StreetResVO streetResVO, StreetResPlayerVO streetResPlayerVO) {
		if (streetResVO.getBeginTime() == 0) {
			streetResPlayerVO.setEndTime(0);
			streetResVO.setCalTime(0);
			return 0;
		}

		int now = DateUtil.getNow();
		if (streetResVO.getCalTime() == 0 || streetResVO.getCalTime() < streetResPlayerVO.getStartTime()) {// 如果没有计算过,则以最初的占领时间为准
			streetResVO.setCalTime(streetResPlayerVO.getStartTime());
		}
		int hour = 0;
		if (streetResPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_FAIL) {// 战败
			if (streetResPlayerVO.getEndTime() != 0) {
				hour = (streetResPlayerVO.getEndTime() - streetResVO.getCalTime()) / 3600;
				// streetResPlayerVO.setEndTime(0);
			} else {
				logger.error("战败计算奖励数据异常" + streetResPlayerVO.getEndTime());
			}
		} else if (streetResPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_PRODUCING || streetResPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_BATTLE_DEF) {// 生产状态
			hour = (now - streetResVO.getCalTime()) / 3600;
			streetResVO.setCalTime(streetResVO.getCalTime() + hour * 3600);// 时间补偿
		}
		hour = Math.min(hour, StreetConstants.RES_OWN_PERIOD_HOUR);
		hour = Math.max(0, hour);
		return hour;
	}

	/**
	 * 刷新产出和战斗状态
	 * 
	 * @param streetResVO
	 * @return
	 * @throws Exception
	 */
	private void freshRes(UserCached userCached, StreetResVO streetResVO, List<NettyMessageVO> commandList) throws Exception {

		List<Reward> rewardList = new ArrayList<Reward>();

		List<Reward> tempList = new ArrayList<Reward>();
		String key = null;
		boolean isCenter = streetResVO.getId() == StreetConstants.CENTER_GRID_NUM;
		SyncLock lockObject = SyncUtil.getInstance().getLock(LockConstant.LOCK_RES_PLAYERVO + "_" + streetResVO.getId() + "_" + streetResVO.getPlayerId());
		StreetResPlayerVO streetResPlayerVO = null;
		synchronized (lockObject) {
			if ((streetResPlayerVO = RedisMap.getStreetResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, streetResVO.getId(), streetResVO.getPlayerId())) != null) {// 战斗状态或者战斗准备状态
				// 处理战斗遗留问题
				if (isCenter) {// 门派一直在战斗队列中
					if ((streetResPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_BATTLE_ATK || streetResPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_BATTLE_DEF)
							&& StreetConstants.hasBattleOverTime(streetResPlayerVO.getBattleTime())) {// 战斗已经结束,但是未清理,战斗异常的一种
						streetResPlayerVO.setBattleTime(0);
						streetResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_FREE);
						RedisMap.setResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, streetResPlayerVO);
					}
				} else {
					if (streetResPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_FAIL) {// 战败
						rewardList.addAll(calFailReward(userCached, streetResVO, streetResPlayerVO, commandList));
						RedisMap.setResPlayerVO(RedisKey.STREET_RES_FREE_PLAYER, streetResPlayerVO);
					} else if (streetResPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_ATK_PREPARE) {// 战斗准备状态
						StreetResPlayerVO enemyResPlayerVO = RedisMap.getStreetResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, streetResPlayerVO.getId(), streetResPlayerVO.getEnemyId());
						if (enemyResPlayerVO != null && enemyResPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_PRODUCING && !StreetConstants.hasOvertime(enemyResPlayerVO.getStartTime())) {// 为空时可能已经结束,
							streetResVO.setStatus(RES_STATUS.OCCUPY_VALUE);
						} else {
							streetResPlayerVO.setEnemyId(0);
							streetResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_FREE);
							RedisMap.setResPlayerVO(RedisKey.STREET_RES_FREE_PLAYER, streetResPlayerVO);
						}
					} else if (streetResPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_BATTLE_DEF && StreetConstants.hasBattleOverTime(streetResPlayerVO.getBattleTime())) {// 处于防守位置,且战斗超时,.移到生产队列计算
						streetResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_PRODUCING);
						RedisMap.setResPlayerVO(RedisKey.STREET_RES_PRODUCE_PLAYER, streetResPlayerVO);
					} else if (streetResPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_BATTLE_ATK || streetResPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_FREE
							|| streetResPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_ATK_SUCC) {// 战斗异常处理,都相当于失败,交给空闲队列处理
						streetResPlayerVO.setEnemyId(0);
						streetResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_FREE);
						RedisMap.delResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, streetResPlayerVO);
						RedisMap.setResPlayerVO(RedisKey.STREET_RES_FREE_PLAYER, streetResPlayerVO);
					} else if (streetResPlayerVO.getStatus() == StreetConstants.PLAYER_RES_STATUS_PRODUCING && StreetConstants.hasOvertime(streetResPlayerVO.getStartTime())) {// 被搜索到,但是生产时间已经结束,交给生产队列处理
						RedisMap.delResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, streetResPlayerVO);
						RedisMap.setResPlayerVO(RedisKey.STREET_RES_PRODUCE_PLAYER, streetResPlayerVO);
					}
				}
				key = RedisKey.STREET_RES_BATTLE_PLAYER;
			}
			StreetResPlayerVO producingResPlayerVO = null;// 只在生产队列
			if ((producingResPlayerVO = RedisMap.getStreetResPlayerVO(RedisKey.STREET_RES_PRODUCE_PLAYER, streetResVO.getId(), streetResVO.getPlayerId())) != null) {// 生产状态
				streetResPlayerVO = producingResPlayerVO;
				key = RedisKey.STREET_RES_PRODUCE_PLAYER;
				if (StreetConstants.hasOvertime(streetResPlayerVO.getStartTime())) {// 不是门派的超时清算
					rewardList.addAll(calOverTimeReward(userCached, streetResVO, streetResPlayerVO, commandList));
					key = RedisKey.STREET_RES_FREE_PLAYER;
				} else {
					if (streetResVO.getCalTime() == 0 || streetResVO.getCalTime() < streetResPlayerVO.getStartTime()) {//
						streetResVO.setCalTime(streetResPlayerVO.getStartTime());
					}
					if (streetResPlayerVO.getEndTime() != 0) {
						logger.error("数据错误,endtime设置错误");
						streetResPlayerVO.setEndTime(0);
					}

					int hour = (DateUtil.getNow() - streetResVO.getCalTime()) / 3600;
					hour = Math.min(hour, StreetConstants.RES_OWN_PERIOD_HOUR);// 最大8小时
					hour = Math.max(0, hour);// 最小为0
					for (int i = 0; i < hour; i++) {
						tempList.addAll(streetResVO.getBaseResourceVO().getRewards());
					}
					streetResVO.setStatus(RES_STATUS.OWN_VALUE);
				}
			}
			StreetResPlayerVO freeResPlayerVO = null;// 可能只在战斗或者生产队列,避免将前面值覆盖掉
			if ((freeResPlayerVO = RedisMap.getStreetResPlayerVO(RedisKey.STREET_RES_FREE_PLAYER, streetResVO.getId(), streetResVO.getPlayerId())) != null) {// 空闲状态,被打败状态
				streetResPlayerVO = freeResPlayerVO;
				streetResVO.setStatus(RES_STATUS.FREE_VALUE);
				streetResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_FREE);
				key = RedisKey.STREET_RES_FREE_PLAYER;
			}
			// 统计被攻击的奖励
			List<StreetDefendLogVO> logs = streetResPlayerVO.getLogs();
			if (logs.size() > 0) {
				PlayerAccountVO playerAccountVO = userCached.getPlayerAccountVO();
				UserStreet userStreet = userCached.getUserStreet();

				for (StreetDefendLogVO streetDefendLogVO : logs) {
					if (isCenter && streetDefendLogVO.getIsSucc() == 0) {// 自己失败,对方成功,需要扣钱
						playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, Math.min(streetDefendLogVO.getMoney(), playerAccountVO.getGmMoney()), playerAccountVO, commandList,
								"门派被人攻破扣钱");
						playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_EXPC, Math.min(streetDefendLogVO.getExpc(), playerAccountVO.getExpc()), playerAccountVO, commandList,
								"门派被人攻破扣英雄经验");
						userStreet.setCenterStatus(1);
					}
					if (!isCenter && streetDefendLogVO.getIsSucc() == 0) {
						userStreet.setResStatus(streetDefendLogVO.getResId());
					}
					PlayerVO enemyVO = RedisMap.getPlayerVOById(streetDefendLogVO.getEnemyId());
					streetService.addEnemy(userStreet, streetDefendLogVO, enemyVO, commandList);
					StreetEnemyVO streetEnemyVO = userStreet.getEnemyById(streetDefendLogVO.getEnemyId());
					streetService.addStreetDefendLog(userStreet, streetDefendLogVO, commandList, streetEnemyVO);
					rewardService.reward(userCached, MailService.decodeRewardString(streetDefendLogVO.getRewards()), commandList);// 胜利的奖励
				}
				streetResPlayerVO.getLogs().clear();
			}
			RedisMap.setResPlayerVO(key, streetResPlayerVO);
		}
		streetResVO.getItems().addAll(MailService.mergeReward(rewardList));
		streetResVO.getTempItems().addAll(MailService.mergeReward(tempList));
		streetService.updateStreetRes(streetResVO);
	}

	/**
	 * 资源点生产过期清理
	 */
	private List<Reward> calOverTimeReward(UserCached userCached, StreetResVO streetResVO, StreetResPlayerVO streetResPlayerVO, List<NettyMessageVO> commandList) throws Exception {

		// 退回法阵
		UserStreet userStreet = userCached.getUserStreet();
		userStreet.addFz(streetResVO.getFzId());
		userStreet.addMachines(streetResVO.getMachineLine());

		List<Reward> rewardList = new ArrayList<Reward>();

		// 退回英雄
		UserHero userHero = userCached.getUserHero();
		List<PlayerHeroVO> defHeros = userHero.getDefendHerosByResId(streetResVO.getId());
		for (PlayerHeroVO playerHeroVO : defHeros) {
			heroService.freeHeroFromStreet(playerHeroVO, commandList);
		}
		int hour = calHour(streetResVO, streetResPlayerVO);
		calResRewardByHour(hour, rewardList, streetResVO);

		streetResVO.setFzId(0);
		streetResVO.setMachineLine("");
		streetResVO.setBeginTime(0);
		streetResVO.setCalTime(0);

		streetResVO.setRewards(MailService.encoderReward(rewardList));// 添加到数据库
		// streetService.updateResource(streetResVO);
		streetService.updatePlayerStreet(userStreet);

		streetResPlayerVO.setBattleTime(0);
		streetResPlayerVO.setEnemyId(0);
		streetResPlayerVO.setEndTime(0);
		streetResPlayerVO.setLevel(userCached.getPlayerVO().getLevel());
		streetResPlayerVO.setStartTime(0);

		streetResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_FREE);
		RedisMap.delResPlayerVO(RedisKey.STREET_RES_PRODUCE_PLAYER, streetResPlayerVO);// 从生产队列中删除,为了避免线程安全问题,在筛选出设置了deadline,租后十分钟的资源点不进入候选
		RedisMap.setResPlayerVO(RedisKey.STREET_RES_FREE_PLAYER, streetResPlayerVO);
		return rewardList;
	}

	/**
	 * 失败或者生产时间结束 对该资源点的进攻直到整个资源点结算时才清理
	 */
	private List<Reward> calFailReward(UserCached userCached, StreetResVO streetResVO, StreetResPlayerVO streetResPlayerVO, List<NettyMessageVO> commandList) throws Exception {
		int hour = calHour(streetResVO, streetResPlayerVO);
		List<Reward> rewardList = new ArrayList<Reward>();
		UserStreet userStreet = userCached.getUserStreet();
		// 退回英雄
		UserHero userHero = userCached.getUserHero();
		List<PlayerHeroVO> defHeros = userHero.getDefendHerosByResId(streetResVO.getId());
		for (PlayerHeroVO playerHeroVO : defHeros) {
			playerHeroVO.setHungre(CommonConstants.HUNGRY_UP_LIMIT);
			heroService.freeHeroFromStreet(playerHeroVO, commandList);
		}
		calResRewardByHour(hour, rewardList, streetResVO);
		streetResVO.setFzId(0);
		streetResVO.setMachineLine("");
		streetResVO.setBeginTime(0);
		streetResVO.setCalTime(0);

		streetResVO.setRewards(MailService.encoderReward(rewardList));// 添加到数据库
		streetService.updateResource(streetResVO);
		streetService.updatePlayerStreet(userStreet);

		streetResPlayerVO.setBattleTime(0);
		streetResPlayerVO.setEnemyId(0);
		streetResPlayerVO.setEndTime(0);
		streetResPlayerVO.setLevel(userCached.getPlayerVO().getLevel());
		streetResPlayerVO.setStartTime(0);
		streetResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_FREE);
		RedisMap.delResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, streetResPlayerVO);
		// 从战斗队列中删除,为了避免线程安全问题,在筛选出设置了deadline,租后十分钟的资源点不进入候选
		RedisMap.setResPlayerVO(RedisKey.STREET_RES_FREE_PLAYER, streetResPlayerVO);
		return rewardList;
	}

	private List<Reward> calResRewardByHour(int hour, List<Reward> rewardList, StreetResVO streetResVO) {
		for (int i = 0; i < hour; i++) {
			rewardList.addAll(RewardRes.getInstance().getRewardRateGroup(streetResVO.getBaseResourceVO().getRewardid()));
		}
		streetResVO.getTempItems().clear();

		return rewardList;
	}

	/**
	 * 计算资源点奖励产出
	 */
	private List<Reward> calStreetResReward(StreetResVO streetResVO) {
		Object[] objects = RedisMap.getStreetResPlayerVO(streetResVO.getPlayerId(), streetResVO.getId());
		StreetResPlayerVO streetResPlayerVO = (StreetResPlayerVO) objects[0];
		String key = (String) objects[1];
		List<Reward> rewards = streetResVO.getItems();
		int hour = calHour(streetResVO, streetResPlayerVO);
		int rewardId = streetResVO.getBaseResourceVO().getRewardid();
		for (int i = 0; i < hour; i++) {
			rewards.addAll(RewardRes.getInstance().getRewardRateGroup(rewardId));
		}
		rewards = MailService.mergeReward(rewards);
		streetResVO.getTempItems().clear();
		streetResVO.getItems().clear();
		RedisMap.setResPlayerVO(key, streetResPlayerVO);
		return rewards;
	}

	private static int checkGridValid(int gridId) {
		if (gridId >= 0 && gridId < StreetConstants.MAX_GRIDS_NUM) {
			return gridId;
		}
		throw new GameException(AlertEnum.STREET_CENTER_CONFIG_FAIL);
	}

	/**
	 * 格子被攻打刷新
	 * 
	 * @param req
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void getGridAtkFresh(GridAtkFreshReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int resId = req.getResId();
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		UserStreet userStreet = userCached.getUserStreet();
		StreetResVO streetResVO = userStreet.getStreetResById(resId);
		if (streetResVO == null) {
			logger.error("资源点被攻击时刷新,找不到该资源点:" + resId + "\n\t" + userStreet.getResList());
			return;
		}
		freshRes(userCached, streetResVO, commandList);
		StreetResInfo.Builder resBuild = StreetResInfo.newBuilder().setId(streetResVO.getId());
		resBuild.setStatus(streetResVO.getStatus());
		List<Reward> rewards = new ArrayList<Reward>();
		rewards.addAll(streetResVO.getTempItems());
		rewards.addAll(streetResVO.getItems());
		for (Reward reward : rewards) {
			resBuild.addResItemInfo(ResItemInfo.newBuilder().setCfgId(reward.getId()).setType(reward.getType()));
		}
		rewards.clear();
		rewards = null;
		UpGridResp.Builder resp = UpGridResp.newBuilder();
		resp.setStreetGridInfo(StreetGridInfo.newBuilder().setId(streetResVO.getId()).setStreetResInfo(resBuild).setOpenStaus(OPEN_STATUS.OPEN_CONTENT));
		nettyMessageVO.setData(resp.build().toByteArray());
		nettyMessageVO.setCommandCode(CSCommandConstant.STREET_GRID_UPDATE);
		commandList.add(nettyMessageVO);
		MsgUtil.addMsgNotice(playerId, CommonConstants.MSG_TYPE_STREET_NOTHING);
	}

	/**
	 * 通过资源点id获得名字
	 * 
	 * @param req
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void getResBaseInfo(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		ResBaseInfoResp.Builder resp = ResBaseInfoResp.newBuilder();
		List<PlayerHeroVO> heros = RedisMap.hvalPlayerHeroVOs(playerId);
		for (PlayerHeroVO playerHeroVO : heros) {
			if (playerHeroVO.getLineStatus() != 0) {
				BaseResourceVO res = ResourceRes.getInstance().getResourceByGridId(playerHeroVO.getLineStatus());
				if (res != null) {
					resp.addResHeroInfo(ResHeroInfo.newBuilder().setHeroId(playerHeroVO.getId()).setResName(res.getName()));
				}
			}
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}
}
