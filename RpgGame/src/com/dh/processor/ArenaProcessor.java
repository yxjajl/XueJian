package com.dh.processor;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.dh.Cache.RedisList;
import com.dh.Cache.RedisMap;
import com.dh.Cache.RedisSortSet;
import com.dh.Cache.ServerHandler;
import com.dh.constants.ItemConstants;
import com.dh.constants.LegionConstant;
import com.dh.enums.GMIOEnum;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.constant.RedisKey;
import com.dh.game.vo.base.Reward;
import com.dh.game.vo.item.ArenaProto.ArenaRankRequest;
import com.dh.game.vo.item.ArenaProto.ArenaRankResponse;
import com.dh.game.vo.item.ArenaProto.BattleRecord;
import com.dh.game.vo.item.ArenaProto.BattleRecordResponse;
import com.dh.game.vo.item.ArenaProto.ChangeEmenResponse;
import com.dh.game.vo.item.ArenaProto.LookDefanceTeamRequest;
import com.dh.game.vo.item.ArenaProto.LookDefanceTeamResponse;
import com.dh.game.vo.item.ArenaProto.RECORDSUCC;
import com.dh.game.vo.item.ArenaProto.RECORDTYPE;
import com.dh.game.vo.item.ArenaProto.RankInfo;
import com.dh.game.vo.item.ArenaProto.RewardJCCRequest;
import com.dh.game.vo.item.ArenaProto.RewardJCCResponse;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.user.BattleRecordVO;
import com.dh.game.vo.user.LegionVO;
import com.dh.game.vo.user.PlayerArenaVO;
import com.dh.game.vo.user.PlayerHeroDefVO;
import com.dh.game.vo.user.PlayerTimerVO;
import com.dh.game.vo.user.PlayerVO;
import com.dh.handler.arena.ArenaRedisTool;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.JJCRankRes;
import com.dh.resconfig.JJCSuccRes;
import com.dh.resconfig.VipLevelRes;
import com.dh.service.ArenaService;
import com.dh.service.KnapsackService;
import com.dh.service.PlayerAccountService;
import com.dh.service.PlayerTimerService;
import com.dh.service.RewardService;
import com.dh.util.CodeTool;
import com.dh.util.CommandUtil;
import com.dh.util.DateUtil;
import com.dh.util.GameUtil;
import com.dh.util.Tool;
import com.dh.util.VOUtil;
import com.dh.vo.user.UserCached;

@Component
public class ArenaProcessor {
	@Resource
	private ArenaService arenaService;
	@Resource
	private PlayerAccountService playerAccountService;
	@Resource
	private KnapsackService knapsackService;
	@Resource
	private RewardService rewardService;
	@Resource
	private PlayerTimerService playerTimerService;

	/**
	 * 竞技场明细
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void getArenaDetail(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		if (userCached.getPlayerVO().getLevel() < ArenaService.ARENA_REQ_LEVEL) {
			throw new GameException(AlertEnum.PLAYERLEVELNOENOUGH_CANTUSEJJC);
		}
		arenaService.getArenaDetail(userCached, commandList);
	}

	// 竞技场排行榜
	public void getArenaRank(ArenaRankRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		// int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		// UserCached userCached = ServerHandler.getUserCached(playerId);

		int page = request.getPage();
		if (page < 0 || page > 9) {
			page = 0;
		}

		ArenaRankResponse.Builder arenaRankResponse = ArenaRankResponse.newBuilder();
		arenaRankResponse.setPage(page);

		Set<String> setString = RedisSortSet.zrange(RedisKey.ARENA_SORTLIST, 0, 99);
		if (setString != null && setString.size() > 0) {
			List<PlayerArenaVO> arenList = RedisMap.hgetPlayerArenaVOList(setString);
			for (PlayerArenaVO playerArenaVO : arenList) {
				RankInfo.Builder rankInfo = RankInfo.newBuilder();
				rankInfo.setPlayerId(playerArenaVO.getPlayerId());
				PlayerVO playerVO = RedisMap.getPlayerVOById(playerArenaVO.getPlayerId());
				rankInfo.setNick(playerVO.getName());
				rankInfo.setLevel(playerVO.getLevel());
				String legionName = "";
				if (playerVO.getLegionId() != 0) {
					LegionVO legionVO = LegionConstant.getLegion(playerVO.getLegionId());
					if (legionVO != null) {
						legionName = legionVO.getLegionName();
					}
				}
				rankInfo.setLegion(legionName);
				rankInfo.setViplevel(playerVO.getVip());
				rankInfo.setCombat(playerArenaVO.getCombat());
				rankInfo.setOrder(playerArenaVO.getOrdernum());
				arenaRankResponse.addRankInfo(rankInfo);
			}
		}

		CommandUtil.packageNettyMessage(CSCommandConstant.ARENA_RANK_LIST, arenaRankResponse.build().toByteArray(), commandList);
	}

	/*
	 * 领取竞技场奖励
	 */
	public void rewardJJC(RewardJCCRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		if (request.getRewardid1() == 0 && request.getRewardid3() == 0) {
			return;
		}

		// System.out.println("ArenaProcessor.rewardJJC = " +
		// request.getRewardid1() + "," + request.getRewardid3());
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		PlayerArenaVO playerArenaVO = arenaService.getPlayerArenaVO(userCached);

		RewardJCCResponse.Builder response = RewardJCCResponse.newBuilder();
		List<Reward> rewardList = null;
		long curTime = System.currentTimeMillis();
		if (request.getRewardid1() > 0) {
			if (request.getRewardid1() != playerArenaVO.getReward0()) {
				throw new GameException(AlertEnum.REWARD_NOT_FOUND);
			}

			rewardList = JJCRankRes.getInstance().getRewardListByRank(request.getRewardid1());
			rewardService.checkAndReward(userCached, rewardList, commandList);
			playerArenaVO.setReward0(0);

			// int reward1 =
			// JJCRankRes.getInstance().getRewardIdByPlayerRank(playerArenaVO.getOrdernum());
			// response.setRewardid1(reward1);
			// response.setReward1RemainTime(GameUtil.getRemainTime(System.currentTimeMillis(),
			// ArenaService.nextFreshTime1));

		} else {
			// 我能领取的连胜奖励最小
			int reward3 = JJCSuccRes.getInstance().getNextRewardId(playerArenaVO.getReward3(), playerArenaVO.getCc3win());
			if (request.getRewardid3() != reward3) {
				throw new GameException(AlertEnum.REWARD_NOT_FOUND);
			}

			rewardList = JJCSuccRes.getInstance().getRewardListBySucc(request.getRewardid3());
			rewardService.checkAndReward(userCached, rewardList, commandList);
			playerArenaVO.setReward3(reward3);

			// int reward3 =
			// JJCSuccRes.getInstance().getRewardIdByPlayerSucc(playerArenaVO.getCc3win());
			// response.setRewardid3(reward3);
			// response.setReward3RemainTime(GameUtil.getRemainTime(System.currentTimeMillis(),
			// ArenaService.nextFreshTime3));

		}

		if (request.getRewardid1() > 0) {
			response.setRewardid1(playerArenaVO.getReward0());
			response.setReward1RemainTime(0);// 单日奖励剩余时间
			response.setLastRank(playerArenaVO.getLastdayrank1());

		} else {
			int reward1 = JJCRankRes.getInstance().getRewardIdByPlayerRank(playerArenaVO.getOrdernum());
			response.setRewardid1(reward1);
			response.setReward1RemainTime(GameUtil.getRemainTime(curTime, ArenaService.nextFreshTime1));// 单日奖励剩余时间
			response.setLastRank(playerArenaVO.getOrdernum());
		}

		int reward3 = JJCSuccRes.getInstance().getNextRewardId(playerArenaVO.getReward3(), playerArenaVO.getCc3win());
		response.setRewardid3(reward3);
		if (reward3 > 0) {
			response.setReward3RemainTime(0);
		} else {
			response.setReward3RemainTime(GameUtil.getRemainTime(curTime, ArenaService.nextFreshTime1));
		}
		response.setLastSuccWin(playerArenaVO.getCc3win());

		arenaService.updatePlayerArenaVOCachedAndDB(playerArenaVO, false);

		CommandUtil.packageNettyMessage(CSCommandConstant.ARENA_REWARD, response.build().toByteArray(), commandList);
	}

	/**
	 * 查看玩家阵容
	 * 
	 * @param request
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void lookEnemTeam(LookDefanceTeamRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		List<PlayerHeroDefVO> list = ArenaRedisTool.getPlayerHeroDefList(request.getPlayerid());
		LookDefanceTeamResponse.Builder response = LookDefanceTeamResponse.newBuilder();
		if (list != null && list.size() > 0) {
			for (PlayerHeroDefVO playerHeroDefVO : list) {
				response.addFinalHero(VOUtil.getFinalHero(playerHeroDefVO));
			}
		}

		CommandUtil.packageNettyMessage(CSCommandConstant.ARENA_LOOK_TEAM, response.build().toByteArray(), commandList);
	}

	/**
	 * 主动刷新CD清0
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void enemCDZero(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		PlayerArenaVO playerArenaVO = arenaService.getPlayerArenaVO(userCached);

		if (knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), ItemConstants.ITEM_CDESC_LENQUE, 1)) {
			knapsackService.removeItem(userCached, ItemConstants.ITEM_CDESC_LENQUE, 1, commandList);
		} else {
			playerAccountService.hasEnoughRMBAndGift(userCached, ArenaService.JJCRMB);
			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_RMB, ArenaService.JJCRMB, userCached.getPlayerAccountVO(), commandList, "刷新竞技场对手", GMIOEnum.OUT_CD_PVP_MATCH.usage());
		}

		playerArenaVO.setEnemyremainTime(new Date());

		arenaService.updatePlayerArenaVOCachedAndDB(playerArenaVO, false);

		CommandUtil.packageNettyMessage(CSCommandConstant.ARENA_CD_ENEM, null, commandList);

	}

	/**
	 * 战斗CD清0
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void failCDZero(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		PlayerArenaVO playerArenaVO = arenaService.getPlayerArenaVO(userCached);

		if (knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), ItemConstants.ITEM_CDESC_LENQUE, 1)) {
			knapsackService.removeItem(userCached, ItemConstants.ITEM_CDESC_LENQUE, 1, commandList);
		} else {
			playerAccountService.hasEnoughRMBAndGift(userCached, ArenaService.JJCRMB);
			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_RMB, ArenaService.JJCRMB, userCached.getPlayerAccountVO(), commandList, "刷新竞技场对手", GMIOEnum.OUT_CD_PVP_BATTLE.usage());
		}

		playerArenaVO.setFailEndTime(new Date());

		arenaService.updatePlayerArenaVOCachedAndDB(playerArenaVO, false);

		CommandUtil.packageNettyMessage(CSCommandConstant.ARENA_CD_FAIL, null, commandList);

	}

	/**
	 * 主动刷新动手
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void refreshEnem(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		PlayerArenaVO playerArenaVO = arenaService.getPlayerArenaVO(userCached);
		boolean result = arenaService.refreshEmenAnernaInfo(playerArenaVO, false);
		// if (!result) {
		//
		// if
		// (knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(),
		// ItemConstants.ITEM_CDESC_LENQUE, 1)) {
		// knapsackService.removeItem(userCached,
		// ItemConstants.ITEM_HEROZAOMU_R10, 1, commandList);
		// } else {
		// playerAccountService.hasEnoughRMB(userCached, ArenaService.JJCRMB);
		// playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_RMB,
		// ArenaService.JJCRMB, userCached.getPlayerAccountVO(), commandList,
		// "刷新竞技场对手");
		// }
		// arenaService.refreshEmenAnernaInfo(playerArenaVO, true);
		// }

		if (!result) {
			throw new GameException(AlertEnum.CD_NOT_FINISH);
		}

		ChangeEmenResponse.Builder builder = ChangeEmenResponse.newBuilder();
		long curTime = System.currentTimeMillis();
		builder.setEnemRemainTime(GameUtil.getRemainTime(curTime, playerArenaVO.getEnemyremainTime())); // 剩余刷新时间
		String str = playerArenaVO.getEnemline(); // 防守阵容
		if (CodeTool.isNotEmpty(str)) {
			int[] arr = Tool.strToIntArr(str);
			for (int otherPlayerId : arr) {
				builder.addEmenAnernaInfo(arenaService.getEmenAnernaInfo(otherPlayerId));
			}
		}

		arenaService.updatePlayerArenaVOCachedAndDB(playerArenaVO, false);

		CommandUtil.packageNettyMessage(CSCommandConstant.ARENA_REFRESH_ENEM, builder.build().toByteArray(), commandList);

	}

	// /**
	// * 更新防守阵容
	// *
	// * @param nettyMessageVO
	// * @param commandList
	// * @throws Exception
	// */
	// public void updateDefanceTeam(UpdateDefanceTeamRequest request,
	// NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws
	// Exception {
	// int playerId = ServerHandler.get(nettyMessageVO.getChannel());
	// UserCached userCached = ServerHandler.getUserCached(playerId);
	// if (request.getDefHeroIdsCount() == 0) {
	// throw new GameException(AlertEnum.JJCDEFANCETEAM_NOT_NULL);
	// }
	//
	// // StringBuffer strbuff = new StringBuffer();
	// // int i = 0;
	// for (int id : request.getDefHeroIdsList()) {
	// PlayerHeroVO hero = null;
	// if ((hero = userCached.getUserHero().getPlayerHeroVOById(id)) == null) {
	// throw new GameException(AlertEnum.HERO_NOT_FOUND);
	// }
	// if (hero.getLineStatus() != 0) {
	// throw new GameException(AlertEnum.HERO_BUSYING);
	// }
	// hero.setLineStatus(CommonConstants.DEFEND_PVPLINE_START);
	// // heroService.updateHero(hero);
	// arenaService.addPlayerDefHero(hero);
	// // if (i > 0) {
	// // strbuff.append(",");
	// // }
	// // strbuff.append(id);
	// // i++;
	// }
	//
	// PlayerArenaVO playerArenaVO = arenaService.getPlayerArenaVO(userCached);
	// // playerArenaVO.setDefline(strbuff.toString());
	// arenaService.updateDefanceTeam(playerArenaVO, userCached);
	//
	// CommandUtil.packageNettyMessage(CSCommandConstant.ARENA_UPDATETEAM, null,
	// commandList);
	//
	// }

	/*
	 * 查看战报
	 */
	public void getBattleRecord(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		// UserCached userCached = ServerHandler.getUserCached(playerId);
		int len = (int) RedisList.llen(RedisKey.BATTLE_RECORED + playerId);

		BattleRecordResponse.Builder response = BattleRecordResponse.newBuilder();
		if (len > 0) {
			BattleRecord.Builder battleRecord = null;
			List<String> list = RedisList.lrange(playerId);// 取全部记录
			if (CodeTool.isNotEmpty(list)) {
				for (String strJson : list) {
					battleRecord = BattleRecord.newBuilder();
					BattleRecordVO temp = JSON.parseObject(strJson, BattleRecordVO.class);
					battleRecord.setType(temp.getAtktype() == RECORDTYPE.ATK_VALUE ? RECORDTYPE.ATK : RECORDTYPE.DEF);
					battleRecord.setOrderNum(temp.getRank());
					PlayerVO otherPlayerVO = RedisMap.getPlayerVOById(temp.getOtherPlayerId());
					if (otherPlayerVO == null) {
						throw new Exception("未加载数据PlayerVO  playerId = " + otherPlayerVO);
					}
					battleRecord.setName(otherPlayerVO.getName());
					battleRecord.setResult(temp.getSucc() == RECORDSUCC.FAIL_VALUE ? RECORDSUCC.FAIL : RECORDSUCC.SUCC);
					battleRecord.setBattleTime(DateUtil.formatDate(temp.getBattleDate()));

					response.addBattleRecord(battleRecord);
				}
			}
		}

		CommandUtil.packageNettyMessage(CSCommandConstant.ARENA_BATTLE_RECORD, response.build().toByteArray(), commandList);

	}

	/**
	 * 增加竞技值
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void addPvp(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		// if
		// (knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(),
		// ItemConstants.ITEM_CDESC_JINJIFU, 1)) {
		// knapsackService.removeItem(userCached,
		// ItemConstants.ITEM_CDESC_JINJIFU, 1, commandList);
		// } else {
		PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();
		if (playerTimerVO.getJjcBuyTimes() >= VipLevelRes.getInstance().getPvpBuyTimes(userCached.getPlayerVO().getVip())) {
			throw new GameException(AlertEnum.PVP_BUYTIMES_LIMIT);
		}
		int cost = ArenaService.RMBVPVP * (playerTimerVO.getJjcBuyTimes() + 1);

		playerAccountService.hasEnoughRMBAndGift(userCached, cost);
		playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_RMB, cost, userCached.getPlayerAccountVO(), commandList, "元宝增加竞技值", GMIOEnum.OUT_BUY_PVP.usage());

		playerTimerVO.setJjcBuyTimes(playerTimerVO.getJjcBuyTimes() + 1);
		playerTimerService.updatejjcBuyTimes(playerTimerVO);
		// }

		playerAccountService.addCurrency(PLAYER_PROPERTY.PROPERTY_PVP, ArenaService.SHOPADDPVP, userCached.getPlayerAccountVO(), commandList, "元宝增加竞技值");

		CommandUtil.packageNettyMessage(CSCommandConstant.SHOP_BUY_POWER, VOUtil.packBuyPower(userCached).build().toByteArray(), commandList);
	}
}
