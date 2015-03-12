package com.dh.handler.battle;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.constants.CommonConstants;
import com.dh.constants.GameRecordConstants;
import com.dh.constants.RaidConstant;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.base.BaseActivityVO;
import com.dh.game.vo.base.BaseRaidInfo;
import com.dh.game.vo.base.Reward;
import com.dh.game.vo.common.CommonProto.CountType;
import com.dh.game.vo.item.ArenaProto.RECORDSUCC;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.raid.RaidProto.BATTLE_TYPE;
import com.dh.game.vo.raid.RaidProto.HeroTeam;
import com.dh.game.vo.raid.RaidProto.MERaidDetail;
import com.dh.game.vo.raid.RaidProto.MERaidDetail.Builder;
import com.dh.game.vo.raid.RaidProto.RaidEndRequest;
import com.dh.game.vo.raid.RaidProto.RaidEndResponse;
import com.dh.game.vo.raid.RaidProto.RaidPrepareRequest;
import com.dh.game.vo.raid.RaidProto.RaidPrepareResponse;
import com.dh.game.vo.raid.RaidProto.RaidStartRequest;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.game.vo.user.PlayerTimerVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.RaidRes;
import com.dh.resconfig.RewardRes;
import com.dh.service.ActivityService;
import com.dh.service.CommonService;
import com.dh.service.MailService;
import com.dh.service.PlayerAccountService;
import com.dh.service.PlayerTimerService;
import com.dh.service.RewardService;
import com.dh.util.CodeTool;
import com.dh.util.CommandUtil;
import com.dh.util.DateUtil;
import com.dh.util.GameUtil;
import com.dh.util.VOUtil;
import com.dh.vo.user.UserCached;

@Component
public class MEBattle implements IBattle {
	private static org.apache.log4j.Logger LOGGER = Logger.getLogger(MEBattle.class);
	@Resource
	private PlayerAccountService playerAccountService;
	@Resource
	private RewardService rewardService;
	@Resource
	private PlayerTimerService playerTimerService;
	@Resource
	private ActivityService activityService;
	@Resource
	private CommonService commonService;

	@Override
	public void battleDetail(RaidPrepareRequest request, UserCached userCached, List<NettyMessageVO> commandList) throws Exception {
		BaseRaidInfo baseRaidInfo = RaidRes.getInstance().getBaseRaidInfo(request.getRaidid());
		if (baseRaidInfo == null) {
			LOGGER.error("错误的副本id" + request.getRaidid());
			throw new Exception("错误的副本id" + request.getRaidid());
		}
		boolean isE = baseRaidInfo.getRaidid() == RaidRes.ME_RAID_E;
		if (!isE && baseRaidInfo.getRaidid() != RaidRes.ME_RAID_M) {
			throw new GameException(AlertEnum.ME_RAID_ERROR);
		}
		playerAccountService.hasEnoughPower(userCached, baseRaidInfo.getEnergy());
		PlayerTimerVO timeVO = userCached.getUserTimer().getPlayerTimerVO();
		int count, buyCount, lastAtkDate;
		if (isE) {
			count = timeVO.getERaidCount();
			buyCount = timeVO.getERaidBuyCount();
			lastAtkDate = timeVO.getERaidDate();
		} else {
			count = timeVO.getMRaidCount();
			buyCount = timeVO.getMRaidBuyCount();
			lastAtkDate = timeVO.getMRaidDate();
		}
		if (count >= buyCount + RaidConstant.RAID_E_TIME) {
			throw new GameException(AlertEnum.ME_RAID_NOT_COUNT);
		}
		int now = DateUtil.getNow();
		if (now - lastAtkDate < RaidConstant.RAID_E_CD) {
			throw new GameException(AlertEnum.CD_NOT_FINISH);
		}

		RaidPrepareResponse.Builder raidPrepareResponse = RaidPrepareResponse.newBuilder();
		raidPrepareResponse.setRaidid(request.getRaidid());
		raidPrepareResponse.setCombat(baseRaidInfo.getCombat()).setType(BATTLE_TYPE.ME);

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

		// int teamExp = 0;
		// int money = 0;
		// int heroExp = 0;
		// if (rewardList != null && rewardList.size() > 0) {
		// for (Reward reward : rewardList) {
		// if (reward.getType() == RewardService.REWARD_TYPE_GOODS) {
		// raidPrepareResponse.addRewards(reward.getRaidRewardinfo());
		// } else if (reward.getType() == RewardService.REWARD_TYPE_PLAYER_EXP)
		// {
		// teamExp += reward.getNumber();
		// } else if (reward.getType() == RewardService.REWARD_TYPE_HERO_EXP) {
		// heroExp += reward.getNumber();
		// } else if (reward.getType() == RewardService.REWARD_TYPE_MONEY) {
		// money += reward.getNumber();
		// }
		// }
		// }
		int num = userCached.getPlayerVO().getLevel() * RaidConstant.RAID_ME_REWARD_RATE;// 根据等级计算产出
		// raidEndResponse.setHeroExp(isE ? num : 0);
		// raidEndResponse.setMoney(isE ? 0 : num);

		raidPrepareResponse.setTeamExp(0);
		raidPrepareResponse.setHeroExp(isE ? num : 0);
		raidPrepareResponse.setMoney(isE ? 0 : num);

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
		// PlayerRaidVO playerRaidVO =
		// userCached.getUserRaid().getPlayerRaidVO(request.getRaidid());
		// if (playerRaidVO == null) {
		// throw new GameException(AlertEnum.RAID_NOT_FOUND);
		// }

		if (CodeTool.isEmpty(userCached.getUserHero().getAtkHeroList())) {
			throw new Exception("没有配上阵英雄");
		}

		for (PlayerHeroVO playerHeroVO : userCached.getUserHero().getAtkHeroList()) {
			if (playerHeroVO.getHungre() >= CommonConstants.HUNGRY_UP_LIMIT) {
				throw new GameException(AlertEnum.HERO_HUNGRY_OVER_LIMIT);
			}
		}

	}

	@Override
	public void battleEnd(UserCached userCached, RaidEndRequest request, List<NettyMessageVO> commandList) throws Exception {
		int addmoney = 0, addteamExp = 0, addheroexp = 0;
		BaseRaidInfo baseRaidInfo = RaidRes.getInstance().getBaseRaidInfo(request.getRaidid());
		// 扣除英雄的疲劳值
		if (baseRaidInfo == null) {
			LOGGER.error("错误的副本id" + request.getRaidid());
			throw new Exception("错误的副本id" + request.getRaidid());
		}
		playerAccountService.hasEnoughPower(userCached, baseRaidInfo.getEnergy());
		playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_POWER, baseRaidInfo.getEnergy(), userCached.getPlayerAccountVO(), commandList, "攻打银两和经验副本扣行动力");
		boolean isE = baseRaidInfo.getRaidid() == RaidRes.ME_RAID_E;
		if (!isE && baseRaidInfo.getRaidid() != RaidRes.ME_RAID_M) {
			throw new GameException(AlertEnum.ME_RAID_ERROR);
		}
		PlayerTimerVO timeVO = userCached.getUserTimer().getPlayerTimerVO();
		int now = DateUtil.getNow();
		if (isE) {
			timeVO.setERaidCount(timeVO.getERaidCount() + 1);
			timeVO.setERaidDate(now);
			CommandUtil.updateCountDown(CountType.E_RAID, RaidConstant.RAID_E_CD, RaidConstant.RAID_E_TIME + timeVO.getERaidBuyCount() - timeVO.getERaidCount(), commandList);
		} else {
			timeVO.setMRaidCount(timeVO.getMRaidCount() + 1);
			timeVO.setMRaidDate(now);
			CommandUtil.updateCountDown(CountType.M_RAID, RaidConstant.RAID_M_CD, RaidConstant.RAID_M_TIME + timeVO.getMRaidBuyCount() - timeVO.getMRaidCount(), commandList);
		}
		playerTimerService.updateMERaid(timeVO);

		if (request.getIsSucc() == RECORDSUCC.SUCC_VALUE) { // 挑战副本成功
			RaidEndResponse.Builder raidEndResponse = RaidEndResponse.newBuilder();
			raidEndResponse.setRaidid(request.getRaidid());
			raidEndResponse.setIsSucc(request.getIsSucc());
			int num = userCached.getPlayerVO().getLevel() * RaidConstant.RAID_ME_REWARD_RATE;// 根据等级计算产出
			// raidEndResponse.setHeroExp(isE ? num : 0);
			// raidEndResponse.setMoney(isE ? 0 : num);

			List<Reward> rewardList = new ArrayList<Reward>();
			rewardList.add(MailService.createReward(isE ? RewardService.REWARD_TYPE_HERO_EXP : RewardService.REWARD_TYPE_MONEY, 0, num));

			// List<Reward> rewardList =
			// RewardRes.getInstance().getRewardRateGroup(baseRaidInfo.getReward());

			rewardService.rewardRaid(userCached, rewardList, commandList, raidEndResponse, addmoney, addteamExp, addheroexp);
			raidEndResponse.setType(request.getType());

			CommandUtil.packageNettyMessage(CSCommandConstant.RAID_END, raidEndResponse.build().toByteArray(), commandList);
			if (isE) {
				activityService.addDayDone(userCached, 6, commandList);
			} else {
				activityService.addDayDone(userCached, 7, commandList);
			}
		} else {
			RaidEndResponse.Builder raidEndResponse = RaidEndResponse.newBuilder();
			raidEndResponse.setRaidid(request.getRaidid());
			raidEndResponse.setIsSucc(request.getIsSucc());
			raidEndResponse.setTeamExp(0);
			raidEndResponse.setMoney(0);
			raidEndResponse.setHeroExp(0);
			raidEndResponse.setType(request.getType());

			CommandUtil.packageNettyMessage(CSCommandConstant.RAID_END, raidEndResponse.build().toByteArray(), commandList);
		}
		BaseActivityVO MEActiviy = GameRecordConstants.getMERaid(isE ? GameRecordConstants.MERAID_EXPC : GameRecordConstants.MERAID_MONEY);
		Builder MEResp = MERaidDetail.newBuilder();
		MEResp.addMERaidInfo(VOUtil.packMERaid(userCached, MEActiviy, now));
		NettyMessageVO nettyMessageVO = new NettyMessageVO();
		nettyMessageVO.setData(MEResp.build().toByteArray());
		nettyMessageVO.setCommandCode(CSCommandConstant.RAID_ME_DETAIL);
		commandList.add(nettyMessageVO);

	}
}
