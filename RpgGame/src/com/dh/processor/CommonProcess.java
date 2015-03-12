package com.dh.processor;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.constants.RaidConstant;
import com.dh.game.vo.base.BaseHeroRecruitVO;
import com.dh.game.vo.common.CommonProto.BriefInfoResp;
import com.dh.game.vo.common.CommonProto.CountDownResp;
import com.dh.game.vo.hero.HeroProto.RECRUIT_TYPE;
import com.dh.game.vo.user.PlayerArenaVO;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.game.vo.user.PlayerTimerVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.HeroRecruitRes;
import com.dh.resconfig.VipLevelRes;
import com.dh.service.ArenaService;
import com.dh.util.DateUtil;
import com.dh.util.GameUtil;
import com.dh.vo.user.UserCached;
import com.dh.vo.user.UserStreet;

@Component
public class CommonProcess {
	@Resource
	private ArenaService arenaService;

	public void getBriefInfo(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		BriefInfoResp.Builder resp = BriefInfoResp.newBuilder();
		List<PlayerHeroVO> heros = userCached.getUserHero().getHeroList();
		resp.setHeroNum(heros.size());
		resp.setRaidId(userCached.getUserRaid().getMaxRaid());
		resp.setChapter(userCached.getUserRaid().getCurChapter());
		PlayerArenaVO arenaVO = arenaService.getPlayerArenaVO(userCached);
		resp.setPvpRank(arenaVO.getOrdernum()).setPvpKills(arenaVO.getCcwin()).setMaxPvpKills(arenaVO.getCc3win());
		resp.setPvpCountDown(GameUtil.getRemainTime(System.currentTimeMillis(), ArenaService.nextFreshTime1));// 竞技场倒计时
		// resp.setPvpPower(userCached.getPlayerAccountVO().getPvp());
		UserStreet street = userCached.getUserStreet();
		if (street != null) {
			resp.setResNum(street.getResList().size() - 1);
		}
		// PlayerBuildVO b = userCached.getUserTimer().getPlayerBuildVO();
		// resp.setCityLevel(b.getB1());
		PlayerTimerVO t = userCached.getUserTimer().getPlayerTimerVO();
		resp.setSalaryStep(t.getSalaryStep());
		// resp.setYxdLevel(b.getB2());
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	public void getCountDown(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		CountDownResp.Builder resp = CountDownResp.newBuilder();
		Date ystEndDate = userCached.getUserTimer().getPlayerTimerVO().getBuildD1();
		Date yxdEndDate = userCached.getUserTimer().getPlayerTimerVO().getBuildD2();
		long curTime = System.currentTimeMillis();
		resp.setYst(ystEndDate == null ? 0 : Math.max(0, (int) ((ystEndDate.getTime() - curTime) / 1000)));
		resp.setYxd(yxdEndDate == null ? 0 : Math.max(0, (int) ((yxdEndDate.getTime() - curTime) / 1000)));

		resp.setCommerce(0);

		BaseHeroRecruitVO baseHeroRecruitVO = HeroRecruitRes.getInstance().getBaseHeroRecruitVO(RECRUIT_TYPE.TYPE_MONEY);
		Date tempDate = userCached.getUserTimer().getTimerDate(baseHeroRecruitVO.getType());
		long remainTime = 0;
		if (baseHeroRecruitVO.getCd() > 0 && tempDate != null) {
			remainTime = baseHeroRecruitVO.getCd() - (curTime - tempDate.getTime());
		}
		resp.setFreeHire(Math.max(0, (int) (remainTime / 1000)));

		baseHeroRecruitVO = HeroRecruitRes.getInstance().getBaseHeroRecruitVO(RECRUIT_TYPE.TYPE_RMB);
		tempDate = userCached.getUserTimer().getTimerDate(baseHeroRecruitVO.getType());
		if (baseHeroRecruitVO.getCd() > 0 && tempDate != null) {
			remainTime = baseHeroRecruitVO.getCd() - (curTime - tempDate.getTime());
		}
		resp.setRMBHire(Math.max(0, (int) (remainTime / 1000)));
		int now = DateUtil.getNow();
		PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();
		int countDown = RaidConstant.RAID_E_CD - (now - playerTimerVO.getERaidDate());
		int totalTime = RaidConstant.RAID_E_TIME + playerTimerVO.getERaidBuyCount() - playerTimerVO.getERaidCount();
		resp.setETime(totalTime);
		resp.setECountDown(countDown);

		countDown = RaidConstant.RAID_M_CD - (now - playerTimerVO.getMRaidDate());
		totalTime = RaidConstant.RAID_M_TIME + playerTimerVO.getMRaidBuyCount() - playerTimerVO.getMRaidCount();
		resp.setMCountDown(countDown);
		resp.setMTime(totalTime);

		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}
}
