package com.dh.handler.battle;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.constants.GameRecordConstants;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.constant.TaskConstant;
import com.dh.game.vo.base.GameRecordVO;
import com.dh.game.vo.item.ArenaProto.RECORDSUCC;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.raid.RaidProto.HeroTeam;
import com.dh.game.vo.raid.RaidProto.RaidEndRequest;
import com.dh.game.vo.raid.RaidProto.RaidEndResponse;
import com.dh.game.vo.raid.RaidProto.RaidPrepareRequest;
import com.dh.game.vo.raid.RaidProto.RaidPrepareResponse;
import com.dh.game.vo.raid.RaidProto.RaidStartRequest;
import com.dh.game.vo.user.PlayerArenaVO;
import com.dh.game.vo.user.PlayerHeroDefVO;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.handler.arena.ArenaRedisTool;
import com.dh.netty.NettyMessageVO;
import com.dh.service.ActivityService;
import com.dh.service.ArenaService;
import com.dh.service.BaseInfoService;
import com.dh.service.PlayerAccountService;
import com.dh.util.CodeTool;
import com.dh.util.CommandUtil;
import com.dh.util.MyClassLoaderUtil;
import com.dh.util.VOUtil;
import com.dh.vo.user.UserCached;

@Component
public class JingjiBattle implements IBattle {

	private final static int JJCB_SUCC_EXPLOIT = 70;
	private final static int JJCB_FAIL_EXPLOIT = 30;
	@Resource
	private ArenaService arenaService;
	@Resource
	private PlayerAccountService playerAccountService;
	@Resource
	private BaseInfoService baseInfoService;
	@Resource
	private ActivityService activityService;

	@Override
	public void battleDetail(RaidPrepareRequest request, UserCached userCached, List<NettyMessageVO> commandList) throws Exception {
		playerAccountService.hasEnoughPvP(userCached, ArenaService.COSTEXPVP);
		PlayerArenaVO playerArenaVO = arenaService.getPlayerArenaVO(userCached);
		int otherPlayerId = request.getRaidid();
		if (playerArenaVO.getEnemline().indexOf(String.valueOf(otherPlayerId)) < 0) {
			throw new Exception("对手阵容里没有此人" + otherPlayerId);
		}
		// PlayerArenaVO otherPlayerArenaVO =
		// ArenaService.getPlayerArenaVOFromRedis(String.valueOf(otherPlayerId));

		RaidPrepareResponse.Builder raidPrepareResponse = RaidPrepareResponse.newBuilder();
		raidPrepareResponse.setRaidid(request.getRaidid());
		raidPrepareResponse.setType(request.getType());

		HeroTeam.Builder heroTeam = HeroTeam.newBuilder();
		// int i = 0;
		for (PlayerHeroVO playerHeroVO : userCached.getUserHero().getAtkHeroList()) {
			heroTeam.addHeroIds(playerHeroVO.getId());
		}
		raidPrepareResponse.setHeroTeam(heroTeam);

		List<PlayerHeroDefVO> list = ArenaRedisTool.getPlayerHeroDefList(otherPlayerId);
		if (CodeTool.isNotEmpty(list)) {
			for (PlayerHeroDefVO playerHeroDefVO : list) {
				raidPrepareResponse.addFinalHero(VOUtil.getFinalHero(playerHeroDefVO));
				// i++;
			}
		}

		CommandUtil.packageNettyMessage(CSCommandConstant.RAID_DETAIL, raidPrepareResponse.build().toByteArray(), commandList);
	}

	@Override
	public void battleStart(UserCached userCached, RaidStartRequest request, List<NettyMessageVO> commandList) throws Exception {
		if (userCached.getPlayerId() == request.getRaidid()) {
			throw new Exception("居然挑战自己....." + request.getRaidid());
		}
		playerAccountService.hasEnoughPvP(userCached, ArenaService.COSTEXPVP);
		playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_PVP, ArenaService.COSTEXPVP, userCached.getPlayerAccountVO(), commandList, "竞技场挑战扣pvp值");

		// 竞技场挑战累计计数
		GameRecordVO gameRecordVO = GameRecordConstants.getGameRecordVO(GameRecordConstants.JJCTZ);
		gameRecordVO.setValue1(GameRecordConstants.JJCTZ_VALUE.incrementAndGet());
		baseInfoService.updateGameRecordVO(gameRecordVO);

	}

	@Override
	public void battleEnd(UserCached userCached, RaidEndRequest request, List<NettyMessageVO> commandList) throws Exception {
		// PlayerArenaVO playerArenaVO = null;

		if (request.getIsSucc() == RECORDSUCC.SUCC_VALUE) { // 挑战副本成功

			// playerArenaVO

			arenaService.mywinner(userCached.getPlayerId(), request.getRaidid());
			RaidEndResponse.Builder raidEndResponse = RaidEndResponse.newBuilder();
			raidEndResponse.setRaidid(request.getRaidid());
			raidEndResponse.setIsSucc(request.getIsSucc());
			raidEndResponse.setMoney(JJCB_SUCC_EXPLOIT);
			raidEndResponse.setType(request.getType());

			CommandUtil.packageNettyMessage(CSCommandConstant.RAID_END, raidEndResponse.build().toByteArray(), commandList);

			playerAccountService.addCurrency(PLAYER_PROPERTY.PROPERTY_EXPLOIT, JJCB_SUCC_EXPLOIT, userCached.getPlayerAccountVO(), commandList, "竞技场胜利加功勋");
			activityService.addDayDone(userCached, 2, commandList);

		} else {
			arenaService.myloster(userCached.getPlayerId(), request.getRaidid());
			RaidEndResponse.Builder raidEndResponse = RaidEndResponse.newBuilder();
			raidEndResponse.setRaidid(request.getRaidid());
			raidEndResponse.setIsSucc(request.getIsSucc());
			raidEndResponse.setMoney(JJCB_FAIL_EXPLOIT);
			raidEndResponse.setType(request.getType());
			CommandUtil.packageNettyMessage(CSCommandConstant.RAID_END, raidEndResponse.build().toByteArray(), commandList);

			playerAccountService.addCurrency(PLAYER_PROPERTY.PROPERTY_EXPLOIT, JJCB_FAIL_EXPLOIT, userCached.getPlayerAccountVO(), commandList, "竞技场失败加功勋");
		}

		arenaService.getArenaDetail(userCached, commandList);

		// 参加Ｎ场竞技
		MyClassLoaderUtil.getInstance().getTaskCheck().changTaskByReQTypeAcc(userCached, TaskConstant.TASK_JJC_BATTLE, request.getIsSucc(), 1 ,commandList);
	}
}
