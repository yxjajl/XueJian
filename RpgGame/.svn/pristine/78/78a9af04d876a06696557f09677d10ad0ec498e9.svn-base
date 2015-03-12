package com.dh.processor;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.constants.CommonConstants;
import com.dh.enums.GMIOEnum;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.BaseProto.BUILDID;
import com.dh.game.vo.BaseProto.BuildInfo;
import com.dh.game.vo.base.BaseBuildLevelVO;
import com.dh.game.vo.hero.HeroTiredProto.HeroRestRequest;
import com.dh.game.vo.hero.HeroTiredProto.HeroRestResponse;
import com.dh.game.vo.hero.HeroTiredProto.HeroTired;
import com.dh.game.vo.hero.HeroTiredProto.HeroTiredDetailResponse;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_UNIT_TYPE;
import com.dh.game.vo.user.PlayerBuildVO;
import com.dh.game.vo.user.PlayerHeroHangVO;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.game.vo.user.PlayerTimerVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.BuildLevelRes;
import com.dh.resconfig.VipLevelRes;
import com.dh.service.HeroHangService;
import com.dh.service.HeroService;
import com.dh.service.PlayerAccountService;
import com.dh.service.PlayerTimerService;
import com.dh.util.CommandUtil;
import com.dh.util.GameUtil;
import com.dh.vo.user.UserCached;

@Component
public class HeroHangProcessor {
	private static final Logger LOGGER = Logger.getLogger(HeroHangProcessor.class);
	private static final int QUICKSLEEPGOLDINGOT = 5; // 快速恢复统一 5元宝
	@Resource
	private HeroHangService heroHangService;
	@Resource
	private PlayerTimerService playerTimerService;
	@Resource
	private PlayerAccountService playerAccountService;
	@Resource
	private HeroService heroService;

	/**
	 * 取英雄挂机列表
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void getHeroHangDetail(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		long curTime = System.currentTimeMillis();
		PlayerBuildVO playerBuildVO = userCached.getUserTimer().getPlayerBuildVO();
		PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();

		HeroTiredDetailResponse.Builder heroTiredDetailResponse = HeroTiredDetailResponse.newBuilder();

		int buildTime = 0;
		if (playerTimerVO.getBuildD2() != null) {
			buildTime = GameUtil.getRemainTime(curTime, playerTimerVO.getBuildD2());
		}

		BaseBuildLevelVO baseBuildLevelVO = BuildLevelRes.getInstance().getBaseBuildLevelVO(playerBuildVO.getB2(), BUILDID.YANGXINGDIAN.getNumber());

		BuildInfo.Builder buildInfo = BuildInfo.newBuilder();
		buildInfo.setBuildId(BUILDID.YANGXINGDIAN);
		buildInfo.setBuildLevel(playerBuildVO.getB2());
		buildInfo.setBuildUpRemainTime(buildTime);
		buildInfo.setReqMoney(baseBuildLevelVO.getReqMoney());
		buildInfo.setItemCfgId(baseBuildLevelVO.getItem());
		buildInfo.setNum(baseBuildLevelVO.getNum());
		buildInfo.setNextLevelCD(baseBuildLevelVO.getCd());

		heroTiredDetailResponse.setBuildInfo(buildInfo);

		HeroTired.Builder heroTired = null;
		for (int i = 1; i <= BuildLevelRes.getInstance().getMaxPosition(); i++) {
			PlayerHeroHangVO playerHeroHangVO = userCached.getUserHero().getHeroHangById(i);
			heroTired = HeroTired.newBuilder();

			if (playerHeroHangVO == null) {
				heroTired.setHeroHangId(i);
				heroTired.setHeroId(0);
				heroTired.setIsHang(HeroHangService.HERO_STATUS_OFF);
				heroTired.setRemaintime(0);
				heroTired.setOpenLevel(BuildLevelRes.getInstance().getPositionOpenLevel(i));
			} else {
				heroTired.setHeroHangId(playerHeroHangVO.getHeroHangId());
				heroTired.setHeroId(playerHeroHangVO.getHeroId());
				int remaintime = 0;
				if (playerHeroHangVO.getIsHang() == HeroHangService.HERO_STATUS_ON) {
					remaintime = GameUtil.getRemainTime(curTime, playerHeroHangVO.getEndTime());
					if (remaintime == 0) {
						playerHeroHangVO.setIsHang(HeroHangService.HERO_STATUS_OVER);
					}
				}

				heroTired.setIsHang(playerHeroHangVO.getIsHang());
				heroTired.setRemaintime(remaintime);
				heroTired.setOpenLevel(0);
			}
			heroTiredDetailResponse.addHeroTired(heroTired);

		}

		int waterRemainTime = playerTimerService.refreshWater(userCached);

		heroTiredDetailResponse.setWater(playerTimerVO.getHerorestTime());
		int max = baseBuildLevelVO.getTimes();
		max += VipLevelRes.getInstance().getMaxWater(userCached.getPlayerVO().getVip());
		heroTiredDetailResponse.setMaxwater(max);
		heroTiredDetailResponse.setWaterRemainTime(waterRemainTime);

		CommandUtil.packageNettyMessage(CSCommandConstant.HERO_HUNGRY_DETAIL, heroTiredDetailResponse.build().toByteArray(), commandList);
	}

	/**
	 * 英雄挂机休息
	 * 
	 * @param request
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void heroHangSleep(HeroRestRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		PlayerHeroVO playerHeroVO = userCached.getUserHero().getPlayerHeroVOById(request.getHeroId());
		if (playerHeroVO == null) {
			throw new GameException(AlertEnum.HERO_NOT_FOUND);
		}

		// if (playerHeroVO.getHungre() < HeroHangService.HERO_HANG_MIN_HUNGRY)
		// {
		// throw new GameException(AlertEnum.HERO_NOT_REQUEST_RESET);
		// }

		if (playerHeroVO.getHang_status() == CommonConstants.HANG_STATUS_SLEEP) {
			throw new GameException(AlertEnum.HERO_ALERDAY_RESET);
		}

		PlayerHeroHangVO playerHeroHangVO = userCached.getUserHero().getFreeHeroHang();
		if (playerHeroHangVO == null) {
			throw new GameException(AlertEnum.HERO_HANG_NOTEN);
		}

		// PlayerBuildVO playerBuildVO =
		// userCached.getUserTimer().getPlayerBuildVO();
		// PlayerTimerVO playerTimerVO =
		// userCached.getUserTimer().getPlayerTimerVO();

		long curTime = System.currentTimeMillis();
		playerHeroHangVO.setIsHang(HeroHangService.HERO_STATUS_ON);
		playerHeroHangVO.setBeginTime(new Date(curTime));
		playerHeroHangVO.setEndTime(new Date(curTime + HeroHangService.HANG_CD * playerHeroVO.getHungre()));
		playerHeroHangVO.setHeroId(playerHeroVO.getId());
		heroHangService.updatePlayerHang(playerHeroHangVO);

		playerHeroVO.setHang_status(CommonConstants.HANG_STATUS_SLEEP);
		// 修 改英雄挂机状态
		commandList.add(CommandUtil.packageAnyProperties(PLAYER_UNIT_TYPE.UNIT_HERO, playerHeroVO.getId(), PLAYER_PROPERTY.PROPERTY_HUNGRYSTATUS, playerHeroVO.getHang_status()));

		HeroRestResponse.Builder builder = HeroRestResponse.newBuilder();

		HeroTired.Builder heroTired = HeroTired.newBuilder();
		heroTired.setHeroHangId(playerHeroHangVO.getHeroHangId());
		heroTired.setIsHang(playerHeroHangVO.getIsHang());
		heroTired.setRemaintime(GameUtil.getRemainTime(curTime, playerHeroHangVO.getEndTime()));
		heroTired.setHeroId(playerHeroHangVO.getHeroId());
		heroTired.setOpenLevel(0);

		builder.setHeroTired(heroTired);
		// BaseBuildLevelVO baseBuildLevelVO =
		// BuildLevelRes.getInstance().getBaseBuildLevelVO(playerBuildVO.getB2(),
		// BUILDID.YANGXINGDIAN.getNumber());

		// int waterRemainTime = playerTimerService.refreshWater(userCached);
		// builder.setWater(playerTimerVO.getHerorestTime());
		// int max = baseBuildLevelVO.getTimes();
		// builder.setMaxwater(max);
		// builder.setWaterRemainTime(waterRemainTime);

		CommandUtil.packageNettyMessage(CSCommandConstant.HERO_HUNGRY_SLEEP, builder.build().toByteArray(), commandList);
	}

	/**
	 * 英雄休息加速 (TODO: 注意此处与自动刷新可能有冲突)
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void heroQuickSleep(HeroRestRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		// playerTimerService
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		PlayerBuildVO playerBuildVO = userCached.getUserTimer().getPlayerBuildVO();
		PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();
		BaseBuildLevelVO baseBuildLevelVO = BuildLevelRes.getInstance().getBaseBuildLevelVO(playerBuildVO.getB2(), BUILDID.YANGXINGDIAN.getNumber());
		int maxwater = baseBuildLevelVO.getTimes();
		maxwater += VipLevelRes.getInstance().getMaxWater(userCached.getPlayerVO().getVip());

		PlayerHeroHangVO playerHeroHangVO = userCached.getUserHero().getHeroHangByHeroId(request.getHeroId());
		if (playerHeroHangVO.getIsHang() == HeroHangService.HERO_STATUS_OFF) {
			throw new Exception("此处没有英雄挂机");
		}

		PlayerHeroVO playerHeroVO = userCached.getUserHero().getPlayerHeroVOById(playerHeroHangVO.getHeroId());

		if (playerHeroVO == null) {
			throw new GameException(AlertEnum.HERO_NOT_FOUND);
		}

		// if (playerHeroHangVO.getIsHang() == HeroHangService.HERO_STATUS_ON) {
		// int remaintime = GameUtil.getRemainTime(System.currentTimeMillis(),
		// playerHeroHangVO.getEndTime());
		// if (remaintime == 0) {
		// playerHeroHangVO.setIsHang(HeroHangService.HERO_STATUS_OVER);
		// }
		// }

		// if (playerHeroHangVO.getIsHang() == HeroHangService.HERO_STATUS_ON) {

		// int cost = playerHeroVO.getHungre();
		int waterRemainTime = playerTimerService.refreshWater(userCached);
		int pilao = playerHeroVO.getHungre();

		long dd = System.currentTimeMillis() - playerHeroHangVO.getBeginTime().getTime();

		int pp = (int) (dd / HeroHangService.HANG_CD);

		pilao -= pp;
		if (pilao <= 0) {
			return;
		}

		if (request.getType() == 1) {
			// int cost = pilao * 2;
			int cost = QUICKSLEEPGOLDINGOT;
			playerAccountService.hasEnoughRMBAndGift(userCached, cost);
			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_RMB, cost, userCached.getPlayerAccountVO(), commandList, "英雄休息加速", GMIOEnum.OUT_CD_SLEEP.usage());
		} else {

			if (playerTimerVO.getHerorestTime() >= pilao) {

				if (maxwater == playerTimerVO.getHerorestTime()) {
					playerTimerVO.setHerorestD(new Date());
				}
				playerTimerVO.setHerorestTime(playerTimerVO.getHerorestTime() - pilao);
				playerTimerService.updateHeroHungryTime(playerTimerVO); // 更新
				waterRemainTime = playerTimerService.refreshWater(userCached);
			} else {
				playerTimerService.packageWater(playerTimerVO.getHerorestTime(), maxwater, waterRemainTime, commandList);
			}

		}

		// playerHeroHangVO.setIsHang(HeroHangService.HERO_STATUS_OVER);
		// heroHangService.updatePlayerHang(playerHeroHangVO);

		//
		// playerHeroVO.setHang_status(HeroHangService.HANG_STATUS_NORMAL);
		// }

		playerHeroHangVO.setIsHang(HeroHangService.HERO_STATUS_OFF);
		playerHeroHangVO.setHeroId(0);

		playerHeroVO.setHang_status(CommonConstants.HANG_STATUS_NORMAL);
		playerHeroVO.setHungre(0);
		heroService.updateHero(playerHeroVO);
		heroHangService.updatePlayerHang(playerHeroHangVO);

		// 修 改英雄挂机状态
		commandList.add(CommandUtil.packageSomeProperties(PLAYER_UNIT_TYPE.UNIT_HERO, playerHeroVO.getId(), new PLAYER_PROPERTY[] { PLAYER_PROPERTY.PROPERTY_HUNGRYSTATUS,
				PLAYER_PROPERTY.PROPERTY_HERO_HUNGRY }, new int[] { playerHeroVO.getHang_status(), playerHeroVO.getHungre() }));

		HeroRestResponse.Builder builder = HeroRestResponse.newBuilder();

		HeroTired.Builder heroTired = HeroTired.newBuilder();
		heroTired.setHeroHangId(playerHeroHangVO.getHeroHangId());
		heroTired.setIsHang(playerHeroHangVO.getIsHang());
		heroTired.setRemaintime(0);
		heroTired.setHeroId(playerHeroHangVO.getHeroId());
		heroTired.setOpenLevel(0);

		builder.setHeroTired(heroTired);

		CommandUtil.packageNettyMessage(CSCommandConstant.HERO_HUNGRY_SLEEP, builder.build().toByteArray(), commandList);

		playerTimerService.packageWater(playerTimerVO.getHerorestTime(), maxwater, waterRemainTime, commandList);

		// HeroRestResponse.Builder builder = HeroRestResponse.newBuilder();
		//
		// HeroTired.Builder heroTired = HeroTired.newBuilder();
		// heroTired.setHeroHangId(playerHeroHangVO.getHeroHangId());
		// heroTired.setIsHang(playerHeroHangVO.getIsHang());
		// heroTired.setRemaintime(0);
		// heroTired.setHeroId(playerHeroHangVO.getHeroId());
		// heroTired.setOpenLevel(0);
		//
		// builder.setHeroTired(heroTired);
		// builder.setFreeTime(Math.max(baseBuildLevelVO.getTimes() -
		// playerTimerVO.getHerorestTime(), 0));
		//
		// CommandUtil.packageNettyMessage(CSCommandConstant.HERO_HUNGRY_SLEEP,
		// builder.build().toByteArray(), commandList);

	}

	public void hungryBack(HeroRestRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		// playerTimerService
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		PlayerHeroHangVO playerHeroHangVO = userCached.getUserHero().getHeroHangByHeroId(request.getHeroId());
		if (playerHeroHangVO.getIsHang() == HeroHangService.HERO_STATUS_OFF) {
			throw new Exception("此处没有英雄挂机");
		}

		PlayerHeroVO playerHeroVO = userCached.getUserHero().getPlayerHeroVOById(playerHeroHangVO.getHeroId());
		if (playerHeroVO == null) {
			throw new GameException(AlertEnum.HERO_NOT_FOUND);
		}

		if (playerHeroHangVO.getIsHang() == HeroHangService.HERO_STATUS_ON) {
			int remaintime = GameUtil.getRemainTime(System.currentTimeMillis(), playerHeroHangVO.getEndTime());
			if (remaintime == 0) {
				playerHeroHangVO.setIsHang(HeroHangService.HERO_STATUS_OVER);
			}
		}

		if (HeroHangService.HERO_STATUS_OVER != playerHeroHangVO.getIsHang()) {
			LOGGER.error("挂机时间没到" + playerHeroHangVO.getHeroHangId() + "," + playerHeroHangVO.getHeroId() + "," + playerHeroHangVO.getEndTime() + "," + playerHeroHangVO.getPlayerId());
			throw new Exception("挂机时间还没有到");
		}

		playerHeroHangVO.setIsHang(HeroHangService.HERO_STATUS_OFF);
		playerHeroHangVO.setHeroId(0);

		playerHeroVO.setHang_status(CommonConstants.HANG_STATUS_NORMAL);
		playerHeroVO.setHungre(0);
		heroService.updateHero(playerHeroVO);
		heroHangService.updatePlayerHang(playerHeroHangVO);

		// 修 改英雄挂机状态
		commandList.add(CommandUtil.packageSomeProperties(PLAYER_UNIT_TYPE.UNIT_HERO, playerHeroVO.getId(), new PLAYER_PROPERTY[] { PLAYER_PROPERTY.PROPERTY_HUNGRYSTATUS,
				PLAYER_PROPERTY.PROPERTY_HERO_HUNGRY }, new int[] { playerHeroVO.getHang_status(), playerHeroVO.getHungre() }));

		// PlayerBuildVO playerBuildVO =
		// userCached.getUserTimer().getPlayerBuildVO();

		HeroRestResponse.Builder builder = HeroRestResponse.newBuilder();

		HeroTired.Builder heroTired = HeroTired.newBuilder();
		heroTired.setHeroHangId(playerHeroHangVO.getHeroHangId());
		heroTired.setIsHang(playerHeroHangVO.getIsHang());
		heroTired.setRemaintime(0);
		heroTired.setHeroId(playerHeroHangVO.getHeroId());
		heroTired.setOpenLevel(0);

		builder.setHeroTired(heroTired);

		// int waterRemainTime = playerTimerService.refreshWater(userCached);
		// builder.setWater(playerTimerVO.getHerorestTime());

		// builder.setMaxwater(max);
		// builder.setWaterRemainTime(waterRemainTime);

		CommandUtil.packageNettyMessage(CSCommandConstant.HERO_HUNGRY_SLEEP, builder.build().toByteArray(), commandList);

		// PlayerTimerVO playerTimerVO =
		// userCached.getUserTimer().getPlayerTimerVO();
		// BaseBuildLevelVO baseBuildLevelVO =
		// BuildLevelRes.getInstance().getBaseBuildLevelVO(playerBuildVO.getB2(),
		// BUILDID.YANGXINGDIAN.getNumber());
		// int max = baseBuildLevelVO.getTimes();
		// playerTimerService.packageWater(playerTimerVO.getHerorestTime(),
		// maxwater, waterRemainTime, commandList);
	}

	/**
	 * 定时刷
	 * 
	 * @param userCached
	 * @param commandList
	 * @throws Exception
	 */
	public void hungryBack(UserCached userCached, List<NettyMessageVO> commandList) throws Exception {

		for (PlayerHeroHangVO playerHeroHangVO : userCached.getUserHero().getHeroHangList()) {

			if (playerHeroHangVO.getIsHang() == HeroHangService.HERO_STATUS_ON) {
				int remaintime = GameUtil.getRemainTime(System.currentTimeMillis(), playerHeroHangVO.getEndTime());
				if (remaintime == 0) {
					playerHeroHangVO.setIsHang(HeroHangService.HERO_STATUS_OVER);
				}
			}

			if (HeroHangService.HERO_STATUS_OVER != playerHeroHangVO.getIsHang()) {
				continue;
			}

			PlayerHeroVO playerHeroVO = userCached.getUserHero().getPlayerHeroVOById(playerHeroHangVO.getHeroId());
			if (playerHeroVO == null) {
				throw new GameException(AlertEnum.HERO_NOT_FOUND);
			}

			playerHeroHangVO.setIsHang(HeroHangService.HERO_STATUS_OFF);
			playerHeroHangVO.setHeroId(0);

			playerHeroVO.setHang_status(CommonConstants.HANG_STATUS_NORMAL);
			playerHeroVO.setHungre(0);
			heroService.updateHero(playerHeroVO);
			heroHangService.updatePlayerHang(playerHeroHangVO);

			// 修 改英雄挂机状态
			commandList.add(CommandUtil.packageSomeProperties(PLAYER_UNIT_TYPE.UNIT_HERO, playerHeroVO.getId(), new PLAYER_PROPERTY[] { PLAYER_PROPERTY.PROPERTY_HUNGRYSTATUS,
					PLAYER_PROPERTY.PROPERTY_HERO_HUNGRY }, new int[] { playerHeroVO.getHang_status(), playerHeroVO.getHungre() }));

			// PlayerBuildVO playerBuildVO =
			// userCached.getUserTimer().getPlayerBuildVO();
			// PlayerTimerVO playerTimerVO =
			// userCached.getUserTimer().getPlayerTimerVO();
			// BaseBuildLevelVO baseBuildLevelVO =
			// BuildLevelRes.getInstance().getBaseBuildLevelVO(playerBuildVO.getB2(),
			// BUILDID.YANGXINGDIAN.getNumber());
			HeroRestResponse.Builder builder = HeroRestResponse.newBuilder();

			HeroTired.Builder heroTired = HeroTired.newBuilder();
			heroTired.setHeroHangId(playerHeroHangVO.getHeroHangId());
			heroTired.setIsHang(playerHeroHangVO.getIsHang());
			heroTired.setRemaintime(0);
			heroTired.setHeroId(playerHeroHangVO.getHeroId());
			heroTired.setOpenLevel(0);

			builder.setHeroTired(heroTired);

			// int waterRemainTime =
			// playerTimerService.refreshWater(userCached);
			// builder.setWater(playerTimerVO.getHerorestTime());
			// int max = baseBuildLevelVO.getTimes();
			// builder.setMaxwater(max);
			// builder.setWaterRemainTime(waterRemainTime);

			CommandUtil.packageNettyMessage(CSCommandConstant.HERO_HUNGRY_SLEEP, builder.build().toByteArray(), commandList);

		}

	}

}
