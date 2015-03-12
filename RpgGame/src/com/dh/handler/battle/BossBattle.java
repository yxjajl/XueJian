package com.dh.handler.battle;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.raid.RaidProto.BATTLE_TYPE;
import com.dh.game.vo.raid.RaidProto.HeroTeam;
import com.dh.game.vo.raid.RaidProto.RaidEndRequest;
import com.dh.game.vo.raid.RaidProto.RaidEndResponse;
import com.dh.game.vo.raid.RaidProto.RaidPrepareRequest;
import com.dh.game.vo.raid.RaidProto.RaidPrepareResponse;
import com.dh.game.vo.raid.RaidProto.RaidStartRequest;
import com.dh.game.vo.user.PlayerBossVO;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.netty.NettyMessageVO;
import com.dh.service.ActivityService;
import com.dh.util.CodeTool;
import com.dh.util.CommandUtil;
import com.dh.util.GameUtil;
import com.dh.vo.WorldBossVO;
import com.dh.vo.user.UserCached;

@Component
public class BossBattle implements IBattle {
	private static Logger logger = Logger.getLogger(BossBattle.class);
	public final static int BATTLE_HUNGRY = 5; // 战斗扣除疲劳值
	@Resource
	private ActivityService activityService;

	@Override
	public void battleDetail(RaidPrepareRequest req, UserCached userCached, List<NettyMessageVO> commandList) throws Exception {
		WorldBossVO instance = WorldBossVO.getInstance();
		if (!instance.isBattle()) {
			throw new GameException(AlertEnum.ACTIVITY_BOSS_NOT_OPEN);
		}
		PlayerBossVO bossVO = activityService.getPlayerBossVO(userCached);
		if (bossVO.getCountDown() > 0) {
			throw new GameException(AlertEnum.CD_HAD_FINISHED);
		}

		RaidPrepareResponse.Builder resp = RaidPrepareResponse.newBuilder();
		resp.setRaidid(req.getRaidid());
		// resp.addMonsterCfgIds(instance.getBaseMonsterVO().getCfgId());
		GameUtil.addBaseMonster(resp, instance.getBaseMonsterVO());

		resp.setType(BATTLE_TYPE.BOSS);
		resp.setBossAddtion(bossVO.getBossLogVO().getAddtion());

		HeroTeam.Builder heroTeam = HeroTeam.newBuilder();
		if (CodeTool.isNotEmpty(userCached.getUserHero().getAtkHeroList())) {
			for (PlayerHeroVO playerHeroVO : userCached.getUserHero().getAtkHeroList()) {
				heroTeam.addHeroIds(playerHeroVO.getId());
			}
		}
		resp.setHeroTeam(heroTeam);
		resp.setBossHp(instance.getHp());
		CommandUtil.packageNettyMessage(CSCommandConstant.RAID_DETAIL, resp.build().toByteArray(), commandList);
	}

	@Override
	public void battleStart(UserCached userCached, RaidStartRequest request, List<NettyMessageVO> commandList) throws Exception {

		if (CodeTool.isEmpty(userCached.getUserHero().getAtkHeroList())) {
			throw new Exception("没有配上阵英雄");
		}
	}

	/**
	 * 战斗结束处理
	 */
	@Override
	public void battleEnd(UserCached userCached, RaidEndRequest req, List<NettyMessageVO> commandList) throws Exception {
		// if (req.getIsSucc() == RECORDSUCC.SUCC_VALUE) {
		// }
		int hunt = req.getScore();
		RaidEndResponse.Builder resp = RaidEndResponse.newBuilder();
		resp.setRaidid(req.getRaidid());
		resp.setIsSucc(req.getIsSucc());
		resp.setType(req.getType());
		CommandUtil.packageNettyMessage(CSCommandConstant.RAID_END, resp.build().toByteArray(), commandList);
		for (NettyMessageVO nettyMessageVO : commandList) {
			ServerHandler.sendMessageToPlayer(nettyMessageVO, userCached.getPlayerId());
		}
		commandList.clear();
		activityService.subBossHp(userCached, hunt, commandList);
		activityService.addDayDone(userCached, 3, commandList);
	}

}
