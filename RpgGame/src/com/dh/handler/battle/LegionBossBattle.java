package com.dh.handler.battle;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.base.BaseLegionBossVO;
import com.dh.game.vo.legion.LegionProto.MEM_TYPE;
import com.dh.game.vo.raid.RaidProto.HeroTeam;
import com.dh.game.vo.raid.RaidProto.RaidEndRequest;
import com.dh.game.vo.raid.RaidProto.RaidEndResponse;
import com.dh.game.vo.raid.RaidProto.RaidPrepareRequest;
import com.dh.game.vo.raid.RaidProto.RaidPrepareResponse;
import com.dh.game.vo.raid.RaidProto.RaidStartRequest;
import com.dh.game.vo.user.LegionMemVO;
import com.dh.game.vo.user.LegionVO;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.netty.NettyMessageVO;
import com.dh.service.LegionService;
import com.dh.util.CodeTool;
import com.dh.util.CommandUtil;
import com.dh.util.DateUtil;
import com.dh.util.GameUtil;
import com.dh.vo.WorldBossVO;
import com.dh.vo.user.UserCached;

//帮派boss
@Component
public class LegionBossBattle implements IBattle {
	// private static Logger logger = Logger.getLogger(LegionBossBattle.class);
	@Resource
	private LegionService legionService;

	@Override
	public void battleDetail(RaidPrepareRequest req, UserCached userCached, List<NettyMessageVO> commandList) throws Exception {
		LegionVO legionVO = legionService.getMineLegion(userCached, commandList);
		LegionService.filterDestroy(legionVO);
		LegionMemVO mineMemVO = LegionService.getLegionMemVO(legionVO, userCached.getPlayerId(), commandList);
		if (mineMemVO.getCountDown(DateUtil.getNow()) > 0) {
			throw new GameException(AlertEnum.CD_NOT_FINISH);
		}
		if (mineMemVO.getBossStatus() != 1) {
			throw new GameException(AlertEnum.LEGION_BOSS_UNACCISSABLE);
		}
		RaidPrepareResponse.Builder resp = RaidPrepareResponse.newBuilder();
		GameUtil.addBaseMonster(resp, mineMemVO.getBaseLegionBossVO().getBaseMonsterVO());
		resp.setRaidid(req.getRaidid());
		resp.setBossHp(mineMemVO.getHp());
		resp.setType(req.getType());
		HeroTeam.Builder heroTeam = HeroTeam.newBuilder();
		if (CodeTool.isNotEmpty(userCached.getUserHero().getAtkHeroList())) {
			for (PlayerHeroVO playerHeroVO : userCached.getUserHero().getAtkHeroList()) {
				heroTeam.addHeroIds(playerHeroVO.getId());
			}
		}
		resp.setHeroTeam(heroTeam);
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
		int hunt = req.getScore();
		RaidEndResponse.Builder resp = RaidEndResponse.newBuilder();
		resp.setRaidid(req.getRaidid());
		resp.setIsSucc(req.getIsSucc());
		resp.setType(req.getType());
		CommandUtil.packageNettyMessage(CSCommandConstant.RAID_END, resp.build().toByteArray(), commandList);
		legionService.subBossHp(userCached, hunt, new ArrayList<NettyMessageVO>());
	}

}
