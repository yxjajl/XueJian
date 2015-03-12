package com.dh.handler.battle;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.constants.CommonConstants;
import com.dh.constants.SkillConstants;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.constant.TaskConstant;
import com.dh.game.vo.base.BaseRaidInfo;
import com.dh.game.vo.base.PassivesSkillVO;
import com.dh.game.vo.base.Reward;
import com.dh.game.vo.item.ArenaProto.RECORDSUCC;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.raid.RaidProto.HeroTeam;
import com.dh.game.vo.raid.RaidProto.RaidEndRequest;
import com.dh.game.vo.raid.RaidProto.RaidEndResponse;
import com.dh.game.vo.raid.RaidProto.RaidPrepareRequest;
import com.dh.game.vo.raid.RaidProto.RaidPrepareResponse;
import com.dh.game.vo.raid.RaidProto.RaidStartRequest;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.game.vo.user.PlayerRaidVO;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.RaidProcessor;
import com.dh.resconfig.RaidRes;
import com.dh.resconfig.RewardRes;
import com.dh.service.ActivityService;
import com.dh.service.ArenaService;
import com.dh.service.HeroService;
import com.dh.service.PlayerAccountService;
import com.dh.service.RaidService;
import com.dh.service.RewardService;
import com.dh.util.CodeTool;
import com.dh.util.CommandUtil;
import com.dh.util.GameUtil;
import com.dh.util.MyClassLoaderUtil;
import com.dh.vo.user.UserCached;

@Component
public class RaidBattle implements IBattle {
	private static Logger logger = Logger.getLogger(RaidProcessor.class);
	public final static int BATTLE_HUNGRY = 3; // 战斗扣除疲劳值
	// public final static int BATLLE_COMBAT = 5;// 体力值
	@Resource
	private RaidService raidService;
	@Resource
	private ArenaService arenaService;
	@Resource
	private PlayerAccountService playerAccountService;
	@Resource
	private HeroService heroService;
	@Resource
	private RewardService rewardService;
	@Resource
	private ActivityService activityService;

	@Override
	public void battleDetail(RaidPrepareRequest request, UserCached userCached, List<NettyMessageVO> commandList) throws Exception {

		BaseRaidInfo baseRaidInfo = RaidRes.getInstance().getBaseRaidInfo(request.getRaidid());
		if (baseRaidInfo == null) {
			logger.error("错误的副本id" + request.getRaidid());
			throw new Exception("错误的副本id" + request.getRaidid());
		}

		RaidPrepareResponse.Builder raidPrepareResponse = RaidPrepareResponse.newBuilder();
		raidPrepareResponse.setRaidid(request.getRaidid());
		raidPrepareResponse.setCombat(baseRaidInfo.getCombat());

		GameUtil.addMonster(raidPrepareResponse, baseRaidInfo.getSetMonsIds1());
		GameUtil.addMonster(raidPrepareResponse, baseRaidInfo.getSetMonsIds2());
		GameUtil.addMonster(raidPrepareResponse, baseRaidInfo.getSetMonsIds3());

		if (baseRaidInfo.getHeros() != null && baseRaidInfo.getHeros().length > 0) {
			for (int value : baseRaidInfo.getHeros()) {
				if (value > 0) {
					raidPrepareResponse.addHelpHeroCfgId(value);
				}
			}
		}

		List<Reward> rewardList = RewardRes.getInstance().getRewardRateGroup(baseRaidInfo.getReward());

		int teamExp = 0;
		int money = 0;
		int heroExp = 0;
		if (rewardList != null && rewardList.size() > 0) {
			for (Reward reward : rewardList) {
				if (reward.getType() == RewardService.REWARD_TYPE_GOODS) {
					raidPrepareResponse.addRewards(reward.getRaidRewardinfo());
				} else if (reward.getType() == RewardService.REWARD_TYPE_PLAYER_EXP) {
					teamExp += reward.getNumber();
				} else if (reward.getType() == RewardService.REWARD_TYPE_HERO_EXP) {
					heroExp += reward.getNumber();
				} else if (reward.getType() == RewardService.REWARD_TYPE_MONEY) {
					money += reward.getNumber();
				}
			}
		}
		raidPrepareResponse.setTeamExp(teamExp);
		raidPrepareResponse.setHeroExp(heroExp);
		raidPrepareResponse.setMoney(money);

		HeroTeam.Builder heroTeam = HeroTeam.newBuilder();
		if (CodeTool.isNotEmpty(userCached.getUserHero().getAtkHeroList())) {
			for (PlayerHeroVO playerHeroVO : userCached.getUserHero().getAtkHeroList()) {
				heroTeam.addHeroIds(playerHeroVO.getId());
			}
		}
		raidPrepareResponse.setHeroTeam(heroTeam);
		CommandUtil.packageNettyMessage(CSCommandConstant.RAID_DETAIL, raidPrepareResponse.build().toByteArray(), commandList);

	}

	@Override
	public void battleStart(UserCached userCached, RaidStartRequest request, List<NettyMessageVO> commandList) throws Exception {
		PlayerRaidVO playerRaidVO = userCached.getUserRaid().getPlayerRaidVO(request.getRaidid());
		if (playerRaidVO == null) {
			throw new GameException(AlertEnum.RAID_NOT_FOUND);
		}

		if (CodeTool.isEmpty(userCached.getUserHero().getAtkHeroList())) {
			throw new Exception("没有配上阵英雄");
		}

		for (PlayerHeroVO playerHeroVO : userCached.getUserHero().getAtkHeroList()) {
			if (playerHeroVO.getHungre() >= CommonConstants.HUNGRY_UP_LIMIT) {
				throw new GameException(AlertEnum.HERO_HUNGRY_OVER_LIMIT);
			}
		}
		BaseRaidInfo baseRaidInfo = RaidRes.getInstance().getBaseRaidInfo(request.getRaidid());
		playerAccountService.hasEnoughPower(userCached, baseRaidInfo.getEnergy());
		// playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_POWER, baseRaidInfo.getEnergy(), userCached.getPlayerAccountVO(), commandList, "打副本扣行动力");

	}

	/**
	 * 战斗结束处理
	 */
	@Override
	public void battleEnd(UserCached userCached, RaidEndRequest request, List<NettyMessageVO> commandList) throws Exception {

		PlayerRaidVO playerRaidVO = userCached.getUserRaid().getPlayerRaidVO(request.getRaidid());
		if (playerRaidVO == null || userCached.getUserRaid().getRaidid() != request.getRaidid()) {
			throw new GameException(AlertEnum.RAID_NOT_FOUND);
		}

		// 扣除英雄的疲劳值
		int addmoney = 0;
		int addteamExp = 0;
		int addheroexp = 0;
		if (CodeTool.isNotEmpty(userCached.getUserHero().getAtkHeroList())) {
			PassivesSkillVO passivesSkillVO = null;
			for (PlayerHeroVO playerHeroVO : userCached.getUserHero().getAtkHeroList()) {
				if ((passivesSkillVO = playerHeroVO.hasPassSkill(SkillConstants.PS_ID_MONEY)) != null) {
					addmoney = passivesSkillVO.getValue();
				}

				if ((passivesSkillVO = playerHeroVO.hasPassSkill(SkillConstants.PS_ID_TEAMEXP)) != null) {
					addteamExp = passivesSkillVO.getValue();
				}

				if ((passivesSkillVO = playerHeroVO.hasPassSkill(SkillConstants.PS_ID_HEROEXP)) != null) {
					addheroexp = passivesSkillVO.getValue();
				}

				int pilaozi = BATTLE_HUNGRY;
				// if ((passivesSkillVO = playerHeroVO.hasPassSkill(SkillConstants.PS_ID_HUNGRY14)) != null) {
				// pilaozi = pilaozi * passivesSkillVO.getValue() / 100;
				// }

				int mm = Math.min(playerHeroVO.getHungre() + pilaozi, CommonConstants.HUNGRY_UP_LIMIT);
				playerHeroVO.setHungre(mm);
				heroService.updateHero(playerHeroVO);
				commandList.add(CommandUtil.packageHeroInfo(playerHeroVO));
			}
		}

		if (request.getIsSucc() == RECORDSUCC.SUCC_VALUE) { // 挑战副本成功

			boolean isFive = false;
			if (playerRaidVO.getScore() < request.getScore()) {
				playerRaidVO.setScore((short) request.getScore());
				if (playerRaidVO.getScore() == 5) {
					isFive = true;
				}
			}
			raidService.updateRaid(playerRaidVO);
			RaidEndResponse.Builder raidEndResponse = RaidEndResponse.newBuilder();
			raidEndResponse.setRaidid(request.getRaidid());
			raidEndResponse.setIsSucc(request.getIsSucc());
			raidEndResponse.setTeamExp(0);
			raidEndResponse.setMoney(0);
			// // 扣除体力
			// playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_POWER,
			// costPower, userCached.getPlayerAccountVO(), commandList,
			// "打副本扣体力");
			//
			BaseRaidInfo baseRaidInfo = RaidRes.getInstance().getBaseRaidInfo(request.getRaidid());

			// 扣体力
			playerAccountService.hasEnoughPower(userCached, baseRaidInfo.getEnergy());
			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_POWER, baseRaidInfo.getEnergy(), userCached.getPlayerAccountVO(), commandList, "打副本扣行动力");
			//
			List<Reward> rewardList = RewardRes.getInstance().getRewardRateGroup(baseRaidInfo.getReward());

			//
			rewardService.rewardRaid(userCached, rewardList, commandList, raidEndResponse, addmoney, addteamExp, addheroexp);
			raidEndResponse.setType(request.getType());

			CommandUtil.packageNettyMessage(CSCommandConstant.RAID_END, raidEndResponse.build().toByteArray(), commandList);

			addNewRaid(userCached, request.getRaidid());
			updateScoreAndOpenNewRaid(userCached, isFive, request.getRaidid(), baseRaidInfo.getChapterid(), commandList);

			// 副本过关任务
			MyClassLoaderUtil.getInstance().getTaskCheck().changTaskByReQType(userCached, TaskConstant.TASK_RAID, baseRaidInfo.getRaidid(), 1, commandList);
			activityService.addDayDone(userCached, 1, commandList);

		} else {
			RaidEndResponse.Builder raidEndResponse = RaidEndResponse.newBuilder();
			raidEndResponse.setRaidid(request.getRaidid());
			raidEndResponse.setIsSucc(request.getIsSucc());
			raidEndResponse.setTeamExp(0);
			// raidEndResponse.setHeroExp(0);
			raidEndResponse.setMoney(0);
			raidEndResponse.setType(request.getType());

			CommandUtil.packageNettyMessage(CSCommandConstant.RAID_END, raidEndResponse.build().toByteArray(), commandList);
		}

	}

	/**
	 * 过关后加新副本
	 * 
	 * @param userCached
	 * @param raidid
	 */
	public void addNewRaid(UserCached userCached, int raidid) {
		// 副本结束处理
		BaseRaidInfo nextRaid = RaidRes.getInstance().getNextBaseRaidInfo(raidid, RaidRes.RAID_MODE_NOR);

		if (nextRaid != null && userCached.getUserRaid().getPlayerRaidVO(nextRaid.getRaidid()) == null) {
			// 加入新副本
			raidService.addPlayerRaidVO(userCached, nextRaid);
		}

		raidService.bucongEliRaid(userCached);
	}

	/**
	 * 打赢后更新积分和开启新关卡
	 * 
	 * @param userCached
	 * @param reqChapter
	 * @param commandList
	 */
	public void updateScoreAndOpenNewRaid(UserCached userCached, boolean isFive, int raidid, int reqChapter, List<NettyMessageVO> commandList) {
		// int index = userCached.getUserRaid().getRaidList().size() - 1;
		// PlayerRaidVO playerRaidVO =
		// userCached.getUserRaid().getRaidList().get(index);

		BaseRaidInfo nextBaseRaidInfo = RaidRes.getInstance().getNextBaseRaidInfo(raidid, RaidRes.RAID_MODE_NOR);
		if (nextBaseRaidInfo != null) {
			PlayerRaidVO playerRaidVO = userCached.getUserRaid().getPlayerRaidVO(nextBaseRaidInfo.getRaidid());
			if (playerRaidVO != null && playerRaidVO.getScore() == 0) {
				reqChapter = nextBaseRaidInfo.getChapterid();
			}
		}
		if (isFive) {
			CommandUtil.packageNettyMessage(CSCommandConstant.RAID_SCORE_CATOON, null, commandList);
		}
		RaidProcessor.raidProgress(userCached, reqChapter, commandList);
	}

}
