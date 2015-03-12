package com.dh.processor;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.enums.GMIOEnum;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.base.BaseRaidInfo;
import com.dh.game.vo.base.Reward;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.raid.CleanOutProto.CleanOutRequest;
import com.dh.game.vo.raid.CleanOutProto.CleanOutResponse;
import com.dh.game.vo.raid.CleanOutProto.RAIDTYPE;
import com.dh.game.vo.raid.CleanOutProto.TimesResult;
import com.dh.game.vo.user.PlayerEliteRaidVO;
import com.dh.game.vo.user.PlayerRaidVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.RaidRes;
import com.dh.resconfig.RewardRes;
import com.dh.service.ActivityService;
import com.dh.service.PlayerAccountService;
import com.dh.service.RewardService;
import com.dh.util.CommandUtil;
import com.dh.vo.user.UserCached;

@Component
public class CleanOutProcessor {
	private static Logger logger = Logger.getLogger(CleanOutProcessor.class);
	private final static long cleanouttime = 120000L - 10000L;// 扫荡时间为２分钟，１秒做为误差

	@Resource
	private PlayerAccountService playerAccountService;
	@Resource
	private RewardService rewardService;
	@Resource
	private ActivityService activityService;

	public void raidCleanOut(CleanOutRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		int raidid = request.getRaidid();
		BaseRaidInfo baseRaidInfo = RaidRes.getInstance().getBaseRaidInfo(raidid);
		if (request.getRaidtype() == RAIDTYPE.NORMAL) {
			PlayerRaidVO playerRaidVO = userCached.getUserRaid().getPlayerRaidVO(raidid);
			if (playerRaidVO == null || playerRaidVO.getScore() < 5) {
				throw new GameException(AlertEnum.RAID_NOT_FOUND);
			}

		} else {
			PlayerEliteRaidVO playerEliteRaidVO = userCached.getUserRaid().getPlayerEliteRaidVO(raidid);
			if (playerEliteRaidVO == null || playerEliteRaidVO.getScore() < 5) {
				throw new GameException(AlertEnum.RAID_NOT_FOUND);
			}
		}

		playerAccountService.hasEnoughPower(userCached, baseRaidInfo.getEnergy());

		long curTime = System.currentTimeMillis();
		long oldTime = userCached.getUserRaid().getLastCleanOutTime();
		if ((curTime - oldTime) < cleanouttime) {
			logger.error("时间间隔没到2分钟就扫荡");
			throw new Exception("时间没到2分钟");
		}

		userCached.getUserRaid().setLastCleanOutTime(curTime);
		playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_POWER, baseRaidInfo.getEnergy(), userCached.getPlayerAccountVO(), commandList, "扫荡副本" + raidid);

		List<Reward> rewardList = RewardRes.getInstance().getRewardRateGroup(baseRaidInfo.getReward());

		rewardService.checkAndReward(userCached, rewardList, commandList);

		CleanOutResponse.Builder cleanOutResponse = CleanOutResponse.newBuilder();
		cleanOutResponse.setRaidid(raidid);
		cleanOutResponse.setRaidtype(request.getRaidtype());

		TimesResult.Builder timesResult = TimesResult.newBuilder();
		timesResult.setTimes(1);
		for (Reward reward : rewardList) {
			timesResult.addRaidRewardinfo(reward.getRaidRewardinfo());
		}

		cleanOutResponse.addTimesResult(timesResult);

		CommandUtil.packageNettyMessage(CSCommandConstant.RAID_CLEAN_OUT, cleanOutResponse.build().toByteArray(), commandList);
		activityService.addDayDone(userCached, 1, commandList);
	}

	/**
	 * 一次扫荡 N 次
	 * 
	 * @param request
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void raidCleanOutAll(CleanOutRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		int raidid = request.getRaidid();
		int times = request.getTimes();

		BaseRaidInfo baseRaidInfo = RaidRes.getInstance().getBaseRaidInfo(raidid);

		int maxTimes = userCached.getPlayerAccountVO().getPower() / baseRaidInfo.getEnergy();

		times = Math.min(times, maxTimes);

		if (times == 0) {
			throw new GameException(AlertEnum.POWER_NOT_ENG);
		}

		int RMB = times * 50;
		boolean isVIp = true;
		if (request.getRaidtype() == RAIDTYPE.NORMAL) {

			if (userCached.getPlayerVO().getVip() < 1) {
				throw new GameException(AlertEnum.NOT_VIP);
			}

			if (userCached.getPlayerVO().getVip() < 6) {
				isVIp = false;
				playerAccountService.hasEnoughRMBAndGift(userCached, RMB);
				// throw new Exception("vip等级不够" +
				// userCached.getPlayerVO().getVip());
			}

			PlayerRaidVO playerRaidVO = userCached.getUserRaid().getPlayerRaidVO(raidid);
			if (playerRaidVO == null || playerRaidVO.getScore() < 5) {
				throw new GameException(AlertEnum.RAID_NOT_FOUND);
			}

		} else {
			if (userCached.getPlayerVO().getVip() < 4) {
				isVIp = false;
				// throw new Exception("vip等级不够" +
				// userCached.getPlayerVO().getVip());
				playerAccountService.hasEnoughRMBAndGift(userCached, RMB);
			}

			PlayerEliteRaidVO playerEliteRaidVO = userCached.getUserRaid().getPlayerEliteRaidVO(raidid);
			if (playerEliteRaidVO == null || playerEliteRaidVO.getScore() < 5) {
				throw new GameException(AlertEnum.RAID_NOT_FOUND);
			}
		}

		int countPower = baseRaidInfo.getEnergy() * times;

		playerAccountService.hasEnoughPower(userCached, countPower);

		playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_POWER, countPower, userCached.getPlayerAccountVO(), commandList, "扫荡副本" + raidid);

		if (!isVIp) {
			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_RMB, RMB, userCached.getPlayerAccountVO(), commandList, "vip等级不够扣元宝扫荡副本" + raidid, GMIOEnum.OUT_CLEANOUT.usage());
		}

		CleanOutResponse.Builder cleanOutResponse = CleanOutResponse.newBuilder();
		cleanOutResponse.setRaidid(raidid);
		cleanOutResponse.setRaidtype(request.getRaidtype());

		for (int i = 1; i <= times; i++) {
			TimesResult.Builder timesResult = TimesResult.newBuilder();
			timesResult.setTimes(i);
			List<Reward> rewardList = RewardRes.getInstance().getRewardRateGroup(baseRaidInfo.getReward());
			rewardService.checkAndReward(userCached, rewardList, commandList);
			for (Reward reward : rewardList) {
				timesResult.addRaidRewardinfo(reward.getRaidRewardinfo());
			}

			cleanOutResponse.addTimesResult(timesResult);

			activityService.addDayDone(userCached, 1, commandList);
		}

		CommandUtil.packageNettyMessage(CSCommandConstant.RAID_CLEAN_OUTALL, cleanOutResponse.build().toByteArray(), commandList);

	}
}
