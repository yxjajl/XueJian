package com.dh.handler.battle;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.Cache.RedisMap;
import com.dh.Cache.ServerHandler;
import com.dh.constants.CommonConstants;
import com.dh.constants.StreetConstants;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.constant.RedisKey;
import com.dh.game.vo.base.Reward;
import com.dh.game.vo.item.ArenaProto.RECORDSUCC;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_UNIT_TYPE;
import com.dh.game.vo.raid.RaidProto.HeroTeam;
import com.dh.game.vo.raid.RaidProto.RaidEndRequest;
import com.dh.game.vo.raid.RaidProto.RaidEndResponse;
import com.dh.game.vo.raid.RaidProto.RaidPrepareRequest;
import com.dh.game.vo.raid.RaidProto.RaidPrepareResponse;
import com.dh.game.vo.raid.RaidProto.RaidStartRequest;
import com.dh.game.vo.street.StreetProto.GRID_FRESH_TYPE;
import com.dh.game.vo.user.PlayerAccountVO;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.game.vo.user.PlayerVO;
import com.dh.game.vo.user.StreetDefendLogVO;
import com.dh.game.vo.user.StreetEnemyVO;
import com.dh.game.vo.user.StreetResPlayerVO;
import com.dh.game.vo.user.StreetResVO;
import com.dh.main.InitLoad;
import com.dh.netty.NettyMessageVO;
import com.dh.service.ActivityService;
import com.dh.service.HeroService;
import com.dh.service.MailService;
import com.dh.service.PlayerAccountService;
import com.dh.service.RewardService;
import com.dh.service.StreetService;
import com.dh.sync.LockConstant;
import com.dh.sync.SyncLock;
import com.dh.sync.SyncUtil;
import com.dh.util.CodeTool;
import com.dh.util.CommandUtil;
import com.dh.util.DateUtil;
import com.dh.util.MsgUtil;
import com.dh.util.VOUtil;
import com.dh.vo.user.UserCached;
import com.dh.vo.user.UserStreet;

@Component
public class StreetBattle implements IBattle {
	@Resource
	private PlayerAccountService playerAccountService;
	@Resource
	private StreetService streetService;
	@Resource
	private HeroService heroService;
	@Resource
	private ActivityService activityService;

	@Override
	public void battleDetail(RaidPrepareRequest req, UserCached userCached, List<NettyMessageVO> commandList) throws Exception {
		int enemyId = req.getRaidid();
		int resId = req.getResId();
		boolean isCenter = resId == StreetConstants.CENTER_GRID_NUM;

		UserCached enemy = null;
		StreetResVO enemyStreetRes = null;
		List<PlayerHeroVO> heros;

		HeroTeam.Builder heroTeam = HeroTeam.newBuilder().setType(req.getType());
		int now = DateUtil.getNow();
		StreetResPlayerVO enemyResPlayerVO;
		if (isCenter) {// 门派
			SyncLock lockObject = SyncUtil.getInstance().getLock(LockConstant.LOCK_RES_PLAYERVO + "_" + resId + "_" + enemyId);
			synchronized (lockObject) {
				enemyResPlayerVO = RedisMap.getStreetResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, resId, enemyId);
				if (enemyResPlayerVO != null) {
					if (!StreetConstants.hasBattleOverTime(enemyResPlayerVO.getBattleTime())) {// 门派战斗还未结束
						throw new GameException(AlertEnum.STREET_BATTLE_CENTER_BUSYING);
					}
					enemyResPlayerVO.setBattleTime(now);
				}
			}

		}
		// 攻击后直接失去封山效果
		UserStreet userStreet = userCached.getUserStreet();
		StreetResVO centerRes = userStreet.getStreetResById(StreetConstants.CENTER_GRID_NUM);
		if (centerRes.getWhosyourdaddy() != 0) {
			centerRes.setWhosyourdaddy(0);
			streetService.updateStreetRes(centerRes);
			commandList.add(CommandUtil.packageAnyProperties(PLAYER_UNIT_TYPE.UNIT_PLAYER, userCached.getPlayerId(), PLAYER_PROPERTY.PROPERTY_DADDY_LIMIT, centerRes.getWhosyourdaddy()));
		}
		RaidPrepareResponse.Builder resp = RaidPrepareResponse.newBuilder().setRaidid(req.getRaidid()).setType(req.getType()).setResId(resId);
		if (enemyId > 0) {
			enemyStreetRes = RedisMap.getStreetResByPIdAndRId(resId, enemyId);
			if (isCenter && StreetConstants.hasDaddyInTime(enemyStreetRes.getWhosyourdaddy())) {
				throw new GameException(AlertEnum.RES_WHOSYOUDADDY);
			}
			if (isCenter && userCached.getUserStreet().isDefeat(enemyId)) {// 复仇专用,只能复仇一次,对方复仇后又能复仇
				throw new GameException(AlertEnum.STREET_HAD_REVENG);
			}
			heros = RedisMap.getPlayerHeroByResId(enemyId, resId);
		} else {
			enemy = StreetService.getEnemyById(enemyId, resId, userCached.getPlayerId());
			heros = enemy.getUserHero().getHeroList();
			enemyStreetRes = new StreetResVO();
			enemyStreetRes.setMachineLine(enemy.getUserStreet().getRobotMachineLine() == null ? "" : enemy.getUserStreet().getRobotMachineLine());
			enemyStreetRes.setFzId(enemy.getUserStreet().getRobotFzId());
		}

		for (PlayerHeroVO playerHeroVO : heros) {
			resp.addFinalHero(VOUtil.getFinalHero(playerHeroVO));
			if (InitLoad.DEBUG_BETA) {
				break;
			}
		}
		if (CodeTool.isNotEmpty(userCached.getUserHero().getAtkHeroList())) {
			for (PlayerHeroVO playerHeroVO : userCached.getUserHero().getAtkHeroList()) {
				heroTeam.addHeroIds(playerHeroVO.getId());
			}
		}
		resp.setHeroTeam(heroTeam);
		String machineLine = null;
		if ((machineLine = enemyStreetRes.getMachineLine()) != null && !machineLine.isEmpty()) {
			String[] machines = StreetConstants.HERO_SPLIT_CHAR.split(machineLine);
			for (String string : machines) {
				resp.addMachineIds(Integer.valueOf(string));
			}
		}
		if (enemyStreetRes.getFzId() != 0)
			resp.setFzId(enemyStreetRes.getFzId());
		CommandUtil.packageNettyMessage(CSCommandConstant.RAID_DETAIL, resp.build().toByteArray(), commandList);
	}

	@Override
	public void battleStart(UserCached userCached, RaidStartRequest request, List<NettyMessageVO> commandList) throws Exception {
		int playerId = userCached.getPlayerId();
		int resId = request.getResId();
		int enemyId = request.getRaidid();
		if (userCached.getPlayerId() == enemyId) {
			throw new Exception("居然挑战自己....." + request.getRaidid());
		}

		for (PlayerHeroVO playerHeroVO : userCached.getUserHero().getAtkHeroList()) {
			if (playerHeroVO.getHungre() >= CommonConstants.HUNGRY_UP_LIMIT) {
				throw new GameException(AlertEnum.HERO_HUNGRY_OVER_LIMIT);
			}
		}

		playerAccountService.hasEnoughPvP(userCached, StreetConstants.STREET_BATTLE_PVP_COST);
		int now = DateUtil.getNow();
		SyncLock lockObject = SyncUtil.getInstance().getLock(LockConstant.LOCK_RES_PLAYERVO + "_" + resId + "_" + playerId);
		synchronized (lockObject) {
			StreetResPlayerVO playerResPlayerVO = RedisMap.getStreetResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, resId, playerId);
			playerResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_BATTLE_ATK);
			playerResPlayerVO.setBattleTime(now);
			RedisMap.setResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, playerResPlayerVO);
		}
		lockObject = SyncUtil.getInstance().getLock(LockConstant.LOCK_RES_PLAYERVO + "_" + resId + "_" + enemyId);
		synchronized (lockObject) {
			StreetResPlayerVO anemyResPlayerVO = RedisMap.getStreetResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, resId, enemyId);
			anemyResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_BATTLE_DEF);
			anemyResPlayerVO.setBattleTime(now);
			RedisMap.setResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, anemyResPlayerVO);
		}
	}

	@Override
	public void battleEnd(UserCached userCached, RaidEndRequest request, List<NettyMessageVO> commandList) throws Exception {
		int enemyId = request.getRaidid();
		int playerId = userCached.getPlayerId();
		int resId = request.getResId();
		StreetResPlayerVO playerResPlayerVO = RedisMap.getStreetResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, resId, playerId);
		SyncLock lockObject = SyncUtil.getInstance().getLock(LockConstant.LOCK_RES_PLAYERVO + "_" + resId + "_" + enemyId);
		synchronized (lockObject) {
			StreetResPlayerVO enemyResPlayerVO = RedisMap.getStreetResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, resId, enemyId);
			if (playerResPlayerVO == null || enemyResPlayerVO == null) {
				throw new GameException(AlertEnum.STREET_RES_MISSING);
			}

			boolean notRobot = enemyId > 0;
			boolean isCenter = resId == StreetConstants.CENTER_GRID_NUM;

			boolean isSucc = request.getIsSucc() == RECORDSUCC.SUCC_VALUE;
			PlayerVO enemyVO = StreetService.getEnemyVO(enemyId, resId, playerId);
			// 英雄疲劳值
			// PassivesSkillVO passivesSkillVO = null;
			int pilaozi = isSucc ? StreetConstants.STREET_BATTLE_HUNGREY_WIN_COST : StreetConstants.STREET_BATTLE_HUNGREY_LOST_COST;
			for (PlayerHeroVO playerHeroVO : userCached.getUserHero().getAtkHeroList()) {

				int mm = Math.min(playerHeroVO.getHungre() + pilaozi, CommonConstants.HUNGRY_UP_LIMIT);
				playerHeroVO.setHungre(mm);
				heroService.updateHero(playerHeroVO);
				commandList.add(CommandUtil.packageHeroInfo(playerHeroVO));
			}

			RaidEndResponse.Builder raidEndResponse = RaidEndResponse.newBuilder().setIsSucc(request.getIsSucc()).setRaidid(enemyId).setType(request.getType());
			UserStreet userStreet = userCached.getUserStreet();
			int now = DateUtil.getNow();

			if (notRobot) {
				enemyResPlayerVO.setBattleTime(0);
				enemyResPlayerVO.setStatus(isSucc ? StreetConstants.PLAYER_RES_STATUS_FAIL : StreetConstants.PLAYER_RES_STATUS_PRODUCING);

				StreetDefendLogVO logVO = new StreetDefendLogVO();
				logVO.setAtkTime(now);
				logVO.setPlayerId(enemyId);
				logVO.setEnemyId(playerId);
				logVO.setEnemyName(userCached.getPlayerVO().getName());
				logVO.setResId(resId);
				logVO.setIsSucc(isSucc ? 0 : 1);

				if (!isSucc) {// 对方成功防守,增加防守奖励
					List<Reward> rewards = new ArrayList<Reward>();
					rewards.add(MailService.createReward(RewardService.REWARD_TYPE_POWER, 0, StreetConstants.STREET_BATTLE_REWARD_POWER));
					logVO.setRewards(MailService.encoderReward(rewards));
				}

				if (isCenter && isSucc) {// 门派
					PlayerAccountVO enemyAccount = RedisMap.getPlayerAccount(enemyId);
					if (enemyAccount == null) {
						throw new GameException(AlertEnum.ACCOUNTVO_NOT_EXIST);
					}

					int money = calLostCurrency(true, enemyResPlayerVO.getLogs(), enemyAccount);
					int expc = calLostCurrency(false, enemyResPlayerVO.getLogs(), enemyAccount);

					// 成功攻打自己也需要增加日志
					StreetEnemyVO streetEnemyVO = userStreet.getEnemyById(enemyId);
					StreetDefendLogVO centerAtkLogVO = new StreetDefendLogVO();
					List<Reward> rewards = new ArrayList<Reward>();
					rewards.add(MailService.createReward(RewardService.REWARD_TYPE_MONEY, 0, money));
					rewards.add(MailService.createReward(RewardService.REWARD_TYPE_HERO_EXP, 0, expc));
					centerAtkLogVO.setRewards(MailService.encoderReward(rewards));
					centerAtkLogVO.setAtkTime(now);
					centerAtkLogVO.setPlayerId(playerId);
					centerAtkLogVO.setEnemyId(enemyId);
					centerAtkLogVO.setEnemyName(enemyVO.getName());
					centerAtkLogVO.setResId(resId);
					centerAtkLogVO.setIsSucc(isSucc ? 1 : 0+0);
					centerAtkLogVO.setExpc(expc);
					centerAtkLogVO.setMoney(money);
					centerAtkLogVO.setIsAtk(1);
					streetService.addStreetDefendLog(userStreet, centerAtkLogVO, commandList, streetEnemyVO);
					streetService.revengeEnemy(userStreet, enemyVO, commandList);

					logVO.setMoney(money);
					logVO.setExpc(expc);

				}
				enemyResPlayerVO.getLogs().add(0, logVO);
				MsgUtil.addMsgNotice(enemyId, CommonConstants.MSG_TYPE_STREET_SOMETING);
				if (isSucc) {// 向对方push包
					UserCached enemyCache = ServerHandler.getUserCached2(enemyId);
					if (enemyCache != null) {// 向前台push资源点被攻打包
						ServerHandler.sendMessageToPlayer(CommandUtil.packStreetGridFresh(GRID_FRESH_TYPE.FRESH_TYPE_ATKED, resId), enemyId);
					}
				}
			}
			playerResPlayerVO.setEndTime(0);
			playerResPlayerVO.setBattleTime(0);

			if (isCenter) {
				enemyResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_FREE);
				playerResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_FREE);
				RedisMap.setResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, playerResPlayerVO);
				RedisMap.setResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, enemyResPlayerVO);
			} else if (isSucc) {
				enemyResPlayerVO.setEndTime(now);
				if (notRobot) {
					enemyResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_FAIL);
					RedisMap.setResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, enemyResPlayerVO);
				} else {// 移除机器人
					StreetService.removeRobotFromBattle(enemyResPlayerVO);// 将敌方移除战斗队列
				}
				playerResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_ATK_SUCC);
				RedisMap.setResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, playerResPlayerVO);
			} else {
				if (notRobot) {
					enemyResPlayerVO.setStatus(StreetConstants.PLAYER_RES_STATUS_PRODUCING);
					RedisMap.setResPlayerVO(RedisKey.STREET_RES_PRODUCE_PLAYER, enemyResPlayerVO);
					RedisMap.delResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, enemyResPlayerVO);// 将敌方移除战斗队列
				} else {
					StreetService.removeRobotFromBattle(enemyResPlayerVO);
				}
				RedisMap.delResPlayerVO(RedisKey.STREET_RES_BATTLE_PLAYER, playerResPlayerVO);// 从己方移除战斗队列

				StreetResVO streetRes = userStreet.getStreetResById(resId);
				StreetService.freeStreetResAndPlayer(streetRes, playerResPlayerVO, commandList);
			}

			CommandUtil.packageNettyMessage(CSCommandConstant.RAID_END, raidEndResponse.build().toByteArray(), commandList);
			activityService.addDayDone(userCached, 10, commandList);
		}
		playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_PVP, StreetConstants.STREET_BATTLE_PVP_COST, userCached.getPlayerAccountVO(), commandList, "江湖战斗扣pvp值");
	}

	/**
	 * 计算门派被攻打扣款
	 * 
	 * @param isMoneny
	 * @param logs
	 * @param playerAccountVO
	 * @return
	 */
	private int calLostCurrency(boolean isMoneny, List<StreetDefendLogVO> logs, PlayerAccountVO playerAccountVO) {
		int lostNum = 0;
		int getNum, ownNum;
		for (StreetDefendLogVO streetDefendLogVO : logs) {
			if (streetDefendLogVO.getId() == StreetConstants.CENTER_GRID_NUM) {
				if (isMoneny) {
					lostNum += streetDefendLogVO.getMoney();
				} else {
					lostNum += streetDefendLogVO.getExpc();
				}
			}
		}
		if (isMoneny) {
			ownNum = playerAccountVO.getGmMoney();
		} else {
			ownNum = playerAccountVO.getExpc();
		}
		if (ownNum > lostNum) {// 当前拥有的大于瞬时的
			getNum = (int) ((ownNum - lostNum) * (isMoneny ? StreetConstants.BATTLE_GAIN_MONEY_PERCENT : StreetConstants.BATTLE_GAIN_EXP_PERCENT));
			return getNum;
		} else {
			return 0;
		}

	}
}
