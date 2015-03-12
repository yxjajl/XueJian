package com.dh.handler.battle;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.constants.CommonConstants;
import com.dh.constants.SkillConstants;
import com.dh.constants.StreetConstants;
import com.dh.enums.GMIOEnum;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.base.BaseMonsterGroupVO;
import com.dh.game.vo.base.BaseMonsterVO;
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
import com.dh.game.vo.user.StreetMonsterVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.RewardRes;
import com.dh.service.ArenaService;
import com.dh.service.HeroService;
import com.dh.service.PlayerAccountService;
import com.dh.service.RewardService;
import com.dh.service.StreetService;
import com.dh.util.CodeTool;
import com.dh.util.CommandUtil;
import com.dh.util.GameUtil;
import com.dh.vo.user.UserCached;
import com.dh.vo.user.UserStreet;

@Component
public class MonsterBattle implements IBattle {
	@Resource
	private ArenaService arenaService;
	@Resource
	private PlayerAccountService playerAccountService;
	@Resource
	private RewardService rewardService;
	@Resource
	private HeroService heroService;
	@Resource
	private StreetService streetService;

	@Override
	public void battleDetail(RaidPrepareRequest request, UserCached userCached, List<NettyMessageVO> commandList) throws Exception {
		playerAccountService.hasEnoughPvP(userCached, StreetConstants.STREET_BATTLE_MONSTER_PVP_COST);
		int resId = request.getResId();

		RaidPrepareResponse.Builder raidPrepareResponse = RaidPrepareResponse.newBuilder().setResId(resId).setRaidid(request.getRaidid()).setType(request.getType());

		HeroTeam.Builder heroTeam = HeroTeam.newBuilder();
		for (PlayerHeroVO playerHeroVO : userCached.getUserHero().getAtkHeroList()) {
			heroTeam.addHeroIds(playerHeroVO.getId());
		}
		raidPrepareResponse.setHeroTeam(heroTeam);
		UserStreet userStreet = userCached.getUserStreet();
		StreetMonsterVO monsterVO = userStreet.getMonsterByResId(resId + 0);
		if (monsterVO == null) {
			throw new GameException(AlertEnum.STREET_BATTLE_BOX_NULL);
		}

		BaseMonsterGroupVO baseMonsterGroupVO = monsterVO.getBaseMonsterGroupVO();
		List<BaseMonsterVO> monsters = baseMonsterGroupVO.getMonsters();
		// for (BaseMonsterVO baseMonsterVO : monsters) {
		// raidPrepareResponse.addMonsterCfgIds(baseMonsterVO.getCfgId());
		// if (InitLoad.DEBUG_BETA) {
		// break;
		// }
		// }

		GameUtil.addBaseMonster(raidPrepareResponse, monsters);

		List<Reward> rewardList = RewardRes.getInstance().getRewardRateGroup(monsterVO.getBaseMonsterGroupVO().getRewardid());
		raidPrepareResponse.addAllMachineIds(baseMonsterGroupVO.getMachines());

		int teamExp = 0, money = 0, heroExp = 0;
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
		raidPrepareResponse.setTeamExp(teamExp);
		raidPrepareResponse.setHeroExp(heroExp);
		raidPrepareResponse.setMoney(money);

		CommandUtil.packageNettyMessage(CSCommandConstant.RAID_DETAIL, raidPrepareResponse.build().toByteArray(), commandList);

	}

	@Override
	public void battleStart(UserCached userCached, RaidStartRequest request, List<NettyMessageVO> commandList) throws Exception {
		UserStreet userStreet = userCached.getUserStreet();

		if (userStreet.getMonsterByResId(request.getResId()) == null) {
			throw new GameException(AlertEnum.STREET_BATTLE_MONSTER_NULL);
		}

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
		int resId = request.getResId();
		UserStreet userStreet = userCached.getUserStreet();
		if (userStreet.getMonsterByResId(resId) == null) {
			throw new GameException(AlertEnum.STREET_BATTLE_MONSTER_NULL);
		}
		// 扣除英雄的疲劳值
		int addmoney = 0, addteamExp = 0, addheroexp = 0;

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

				int pilaozi = RaidBattle.BATTLE_HUNGRY;
				// if ((passivesSkillVO =
				// playerHeroVO.hasPassSkill(SkillConstants.PS_ID_HUNGRY14)) !=
				// null) {
				// pilaozi = pilaozi * passivesSkillVO.getValue() / 100;
				// }

				int mm = Math.min(playerHeroVO.getHungre() + pilaozi, CommonConstants.HUNGRY_UP_LIMIT);
				playerHeroVO.setHungre(mm);
				heroService.updateHero(playerHeroVO);
				commandList.add(CommandUtil.packageHeroInfo(playerHeroVO));
			}
		}
		playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_PVP, StreetConstants.STREET_BATTLE_MONSTER_PVP_COST, userCached.getPlayerAccountVO(), commandList, "江湖战斗扣pvp值");
		RaidEndResponse.Builder raidEndResponse = RaidEndResponse.newBuilder().setRaidid(request.getRaidid()).setType(request.getType()).setIsSucc(request.getIsSucc()).setTeamExp(0).setMoney(0);
		if (request.getIsSucc() == RECORDSUCC.SUCC_VALUE) { // 挑战副本成功
			List<StreetMonsterVO> monsterList = userStreet.getMonsterList();
			for (StreetMonsterVO streetMonsterVO : monsterList) {
				if (streetMonsterVO.getId() == resId) {
					streetService.deleteGridMonster(streetMonsterVO);
					userStreet.getFreeGrids()[resId] = 1;
					StreetMonsterVO monsterVO = userStreet.getMonsterByResId(resId);
					BaseMonsterGroupVO baseMonsterGroupVO = monsterVO.getBaseMonsterGroupVO();
					List<Reward> rewardList = RewardRes.getInstance().getRewardRateGroup(baseMonsterGroupVO.getRewardid());
					rewardService.rewardRaid(userCached, rewardList, commandList, raidEndResponse, addmoney, addteamExp, addheroexp, GMIOEnum.IN_STREET_MOSTER.usage());
					monsterList.remove(streetMonsterVO);
					StreetService.packStreetGridUpdate(2, null, 0, resId, commandList);
					break;
				}
			}
		}
		CommandUtil.packageNettyMessage(CSCommandConstant.RAID_END, raidEndResponse.build().toByteArray(), commandList);
	}
}
