package com.dh.service;

import io.netty.channel.Channel;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.dh.Cache.RedisMap;
import com.dh.Cache.ServerHandler;
import com.dh.constants.ActivityConstant;
import com.dh.constants.CommonConstants;
import com.dh.dao.PlayerTimerMapper;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.constant.RedisKey;
import com.dh.game.vo.BaseProto.BUILDID;
import com.dh.game.vo.activity.ActivityProto.DayChangeResp;
import com.dh.game.vo.activity.ActivityProto.DayShareResponse;
import com.dh.game.vo.activity.ActivityProto.PushScoreResp;
import com.dh.game.vo.activity.ActivityProto.RewardOnlineResp;
import com.dh.game.vo.base.BaseBuildLevelVO;
import com.dh.game.vo.base.BaseOnlineReward;
import com.dh.game.vo.hero.HeroTiredProto.WareResponse;
import com.dh.game.vo.user.PlayerAccountVO;
import com.dh.game.vo.user.PlayerBuildVO;
import com.dh.game.vo.user.PlayerTimerVO;
import com.dh.main.InitLoad;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.BuildLevelRes;
import com.dh.resconfig.DayChangeRewardRes;
import com.dh.resconfig.OnlineRewardRes;
import com.dh.resconfig.VipLevelRes;
import com.dh.sqlexe.SqlSaveThread;
import com.dh.sync.LockConstant;
import com.dh.sync.SyncLock;
import com.dh.sync.SyncUtil;
import com.dh.util.CodeTool;
import com.dh.util.CommandUtil;
import com.dh.util.DateUtil;
import com.dh.util.SqlBuild;
import com.dh.util.VOUtil;
import com.dh.vo.user.UserCached;

@Component
public class PlayerTimerService {
	private final static int WATERFRESHTIME = 300; // 泉水 每300秒刷一次
	private static final Logger LOGGER = Logger.getLogger(PlayerTimerService.class);
	@Resource
	private PlayerTimerMapper playerTimerMapper;
	@Resource
	private SqlBuild sqlBuild;
	@Resource
	private SqlSaveThread sqlSaveThread;

	public void loadPlayerTimer(UserCached userCached) throws Exception {
		PlayerTimerVO playerTimerVO = playerTimerMapper.getPlayerTimer(userCached.getPlayerId());

		if (playerTimerVO.getGuide() == null || playerTimerVO.getGuide().isEmpty()) {
			playerTimerVO.setGuideArray(new char[CommonConstants.GUIDE_NUM]);
			playerTimerVO.setGuide(CodeTool.encodeCharsToStr(playerTimerVO.getGuideArray()));
			updateGuide(playerTimerVO);
		} else {
			playerTimerVO.setGuideArray(CodeTool.decodeStrToChars(playerTimerVO.getGuide()));
		}
		if (playerTimerVO.getDayDoneStr().isEmpty()) {
			int[] dayDoneArray = new int[ActivityConstant.DAY_DONE_NUM];
			playerTimerVO.setDayDoneArray(dayDoneArray);
			playerTimerVO.setDayDoneScore(0);
			playerTimerVO.setDayDoneRewards(0);
			updateDayDone(playerTimerVO);
		} else {
			String[] array = playerTimerVO.getDayDoneStr().split(",");
			int[] intArray = new int[ActivityConstant.DAY_DONE_NUM];
			for (int i = 0; i < array.length; i++) {
				intArray[i] = Integer.parseInt(array[i]);
			}
			playerTimerVO.setDayDoneArray(intArray);
		}
		//
		if (InitLoad.DEBUG_OFF_GUIDE) {
			Arrays.fill(playerTimerVO.getGuideArray(), 'ÿ');
			playerTimerVO.setGuide(CodeTool.encodeCharsToStr(playerTimerVO.getGuideArray()));
		}

		userCached.getUserTimer().setPlayerTimerVO(playerTimerVO);

		this.flushKnaspack(userCached);
	}

	public void dayFreshOnLogin(PlayerTimerVO playerTimerVO) {
		playerTimerVO.dayClear();
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.dayFreshOnLogin", playerTimerVO));
	}

	/**
	 * 刷新开启背包格子的时间和格子数
	 * 
	 * @param userCached
	 * @throws Exception
	 */
	public void flushKnaspack(UserCached userCached) throws Exception {
		if ((userCached.getPlayerAccountVO().getKnapsack() + userCached.getPlayerAccountVO().getKnapsack2()) >= CommonConstants.KNASPACK_MAX_GIRD) {
			return;
		}

		SyncLock syncLock = SyncUtil.getInstance().getLock(LockConstant.LOCK_PLAYERVO + userCached.getPlayerId());
		synchronized (syncLock) {
			boolean isChange = false;
			long curTime = System.currentTimeMillis();
			// System.err.println("我已经在线1:" + curTime + "," +
			// userCached.getPlayerVO().getLastLoginTime());
			long tt = (curTime - userCached.getPlayerVO().getLastLoginTime()); // 在线时间
			// System.err.println("我已经在线:" + tt);
			if (tt < 0) {
				tt = 0;
			}
			if (tt > 0) { // 至少超过1秒 才做刷新操作
				PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();
				int knapsack2 = userCached.getPlayerAccountVO().getKnapsack2();
				long remainTime = playerTimerVO.getKnpsackTime();
				while (tt >= remainTime) {
					if ((userCached.getPlayerAccountVO().getKnapsack() + knapsack2) >= CommonConstants.KNASPACK_MAX_GIRD) {
						break;
					}
					tt = tt - remainTime;
					knapsack2++;
					remainTime = (1 + userCached.getPlayerAccountVO().getKnapsack() + knapsack2 - CommonConstants.KNASPACK_STARTGIRD) * CommonConstants.KNASPACK_STEP + CommonConstants.KNASPACK_STAR;
					isChange = true;
				}

				remainTime -= tt;
				userCached.getPlayerVO().setLastLoginTime(curTime);
				playerTimerVO.setKnpsackTime(remainTime);
				userCached.getPlayerAccountVO().setKnapsack2(knapsack2);

				if (isChange) {
					updatePlayerAccountKnapsack(userCached.getPlayerAccountVO());
				}
				updateknpsackTime(userCached.getUserTimer().getPlayerTimerVO());
			}
		} // end lock
	}

	public void addOnlineReward(UserCached userCached) {
		PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();
		long curTime = System.currentTimeMillis();
		int tt;
		if (playerTimerVO.getLastRewardTime() != 0) {
			tt = (int) ((curTime - playerTimerVO.getLastRewardTime()) / 1000);
			playerTimerVO.setLastRewardTime(0);
		} else {
			tt = (int) ((curTime - userCached.getPlayerVO().getLastLoginDate().getTime()) / 1000);
		}
		int onlineTime = playerTimerVO.getOnlineTime() + tt;
		playerTimerVO.setOnlineTime(Math.min(onlineTime, (int) TimeUnit.DAYS.toSeconds(1)));
		// System.out.println("关闭浏览器后累计时间:" + playerTimerVO.getOnlineTime());
		updateOnlineReward(playerTimerVO);
	}

	// 刷新挂机消耗泉水，
	public int refreshWater(UserCached userCached) throws Exception {
		PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();
		PlayerBuildVO playerBuildVO = userCached.getUserTimer().getPlayerBuildVO();
		BaseBuildLevelVO baseBuildLevelVO = BuildLevelRes.getInstance().getBaseBuildLevelVO(playerBuildVO.getB2(), BUILDID.YANGXINGDIAN.getNumber());
		int max = baseBuildLevelVO.getTimes(); // 养心殿神水最大值

		max += VipLevelRes.getInstance().getMaxWater(userCached.getPlayerVO().getVip());

		if (playerTimerVO.getHerorestTime() >= max) {
			return 0;
		}

		long curTime = System.currentTimeMillis();
		long lastFreshTime = playerTimerVO.getHerorestD().getTime();
		int dd = (int) ((curTime - lastFreshTime) / 1000);
		int x = dd / WATERFRESHTIME;

		if (x > 0) {

			playerTimerVO.setHerorestTime(playerTimerVO.getHerorestTime() + x);

			if (playerTimerVO.getHerorestTime() > max) {
				playerTimerVO.setHerorestTime(max);
			}
			playerTimerVO.setHerorestD(DateUtil.add(playerTimerVO.getHerorestD(), Calendar.SECOND, x * WATERFRESHTIME));
			updateHeroHungryTime(playerTimerVO);
		}

		int mm = (int) ((WATERFRESHTIME * 1000 + playerTimerVO.getHerorestD().getTime() - curTime) / 1000);
		return Math.abs(mm);
	}

	public void packageWater(int water, int maxwater, int waterRemainTime, List<NettyMessageVO> commandList) {
		WareResponse.Builder builder = WareResponse.newBuilder();
		builder.setWater(water);
		builder.setMaxwater(maxwater);
		builder.setWaterRemainTime(waterRemainTime);
		CommandUtil.packageNettyMessage(CSCommandConstant.WATER_REFRESH, builder.build().toByteArray(), commandList);
	}

	// /**
	// * 用元宝开启N个格子背包
	// *
	// * @param userCached
	// * @param n
	// * @throws Exception
	// */
	// public void flushKnaspack(UserCached userCached, int num) throws
	// Exception {
	// boolean isChange = false;
	// long curTime = System.currentTimeMillis();
	// long tt = (curTime - userCached.getPlayerVO().getLastLoginTime()); //
	// 在线时间
	// if (tt < 0) {
	// tt = 0;
	// }
	// if (tt > 0) { // 至少超过1秒 才做刷新操作
	// PlayerTimerVO playerTimerVO =
	// userCached.getUserTimer().getPlayerTimerVO();
	// int knapsack2 = userCached.getPlayerAccountVO().getKnapsack2();
	// long remainTime = playerTimerVO.getKnpsackTime();
	// while (tt >= remainTime) {
	// tt = tt - remainTime;
	// knapsack2++;
	// remainTime = (1 + userCached.getPlayerAccountVO().getKnapsack() +
	// knapsack2 - CommonConstants.KNASPACK_STARTGIRD) *
	// CommonConstants.KNASPACK_STEP + CommonConstants.KNASPACK_STAR;
	// isChange = true;
	// }
	//
	// userCached.getPlayerVO().setLastLoginTime(curTime);
	//
	// if (num > knapsack2) {
	//
	// int reqMoney = (num - knapsack2) * CommonConstants.KNASPACK_RMB;
	//
	// knapsack2 = 0;
	// remainTime = (1 + userCached.getPlayerAccountVO().getKnapsack() + num -
	// CommonConstants.KNASPACK_STARTGIRD) * CommonConstants.KNASPACK_STEP +
	// CommonConstants.KNASPACK_STAR;
	// } else {
	// knapsack2 = knapsack2 - num;
	// }
	//
	// remainTime -= tt;
	// userCached.getPlayerAccountVO().setKnapsack2(knapsack2);
	// playerTimerVO.setKnpsackTime(remainTime);
	// if (isChange) {
	// updatePlayerAccountKnapsack(userCached.getPlayerAccountVO());
	// }
	// updateKnpsackTimer(userCached.getUserTimer().getPlayerTimerVO());
	// }
	// }

	// 招蓦CD
	public void updatePlayerTimer(PlayerTimerVO playerTimerVO) throws Exception {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updatePlayerTimer", playerTimerVO));
	}

	// 建筑CD
	public void updateYIPlayerTimer(PlayerTimerVO playerTimerVO) throws Exception {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateYIPlayerTimer", playerTimerVO));
	}

	// 薪资
	public void updateSalaryTime(PlayerTimerVO playerTimerVO) throws Exception {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateSalaryTime", playerTimerVO));
	}

	// 英雄休息时间和次数
	public void updateHeroHungryTime(PlayerTimerVO playerTimerVO) throws Exception {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateHeroHungryTime", playerTimerVO));
	}

	// 背包CD
	public void updateknpsackTime(PlayerTimerVO playerTimerVO) throws Exception {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateknpsackTime", playerTimerVO));
	}

	// 建筑等级
	public void updateBuild(PlayerBuildVO playerBuildVO) throws Exception {
		sqlSaveThread.putSql(playerBuildVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateBuild", playerBuildVO));
	}

	// 背包格子数
	public void updatePlayerAccountKnapsack(PlayerAccountVO playerAccountVO) throws Exception {
		sqlSaveThread.putSql(playerAccountVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerAccountMapper.updatePlayerAccountKnapsack", playerAccountVO));
	}

	// 功勋商店
	// public void updateExploitShop(PlayerTimerVO playerTimerVO) throws
	// Exception {
	// sqlSaveThread.putSql(sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateExploitShop",
	// playerTimerVO));
	// }

	public void updateFirstGrab(PlayerTimerVO playerTimerVO) throws Exception {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateFirstGrab", playerTimerVO));
	}

	// 夺宝敌人列表
	public void updateGrabTime(PlayerTimerVO playerTimerVO) throws Exception {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateGrabTime", playerTimerVO));
	}

	// vip每日奖励
	public void updateVipReward(PlayerTimerVO playerTimerVO) throws Exception {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateVipReward", playerTimerVO));
	}

	// 新手引导
	public void updateGuide(PlayerTimerVO playerTimerVO) throws Exception {
		String sql = sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateGuide", playerTimerVO);
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sql);
	}

	// 在线奖励
	public void updateOnlineReward(PlayerTimerVO playerTimerVO) {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateOnlineReward", playerTimerVO));
	}

	// 行动力奖励
	public void updatePowerBuy(PlayerTimerVO playerTimerVO) {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateBuyPowerCount", playerTimerVO));
	}

	public void updateMERaid(PlayerTimerVO playerTimerVO) {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateMERaid", playerTimerVO));
	}

	public void updateYZInfo(PlayerTimerVO playerTimerVO) throws Exception {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateYZInfo", playerTimerVO));
	}

	// 竞技值购买次数
	public void updatejjcBuyTimes(PlayerTimerVO playerTimerVO) throws Exception {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updatejjcBuyTimes", playerTimerVO));
	}

	/**
	 * 更新黄钻新手礼包领取状态
	 * 
	 * @param playerTimerVO
	 * @throws Exception
	 */
	public void updateYellowNewGift(PlayerTimerVO playerTimerVO) throws Exception {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateYellowNewGift", playerTimerVO));
	}

	// 黄钻每日礼包
	public void updateYellowDayGift(PlayerTimerVO playerTimerVO) throws Exception {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateYellowDayGift", playerTimerVO));
	}

	public void updateEliRaidBuyTimes(PlayerTimerVO playerTimerVO) throws Exception {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateEliRaidBuyTimes", playerTimerVO));
	}

	public void updateDayChange(PlayerTimerVO playerTimerVO) {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateDayChange", playerTimerVO));
	}

	public void updateLegionTime(PlayerTimerVO playerTimerVO) {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateLegionTime", playerTimerVO));
	}

	public void updateDayDoneReward(PlayerTimerVO playerTimerVO) {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateDayDoneReward", playerTimerVO));
	}

	public void updateDayDoneStr(PlayerTimerVO playerTimerVO) {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateDayDoneStr", playerTimerVO));
	}

	public void updateDayDoneScore(PlayerTimerVO playerTimerVO) {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateDayDoneScore", playerTimerVO));
	}

	public void updateCDKEY(PlayerTimerVO playerTimerVO) {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateCDKEY", playerTimerVO));
	}

	public void updateDayShare(PlayerTimerVO playerTimerVO) {
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateDayShare", playerTimerVO));
	}

	public void updateDayDone(PlayerTimerVO playerTimerVO) {
		playerTimerVO.setDayDoneStr(CodeTool.arrayToString(playerTimerVO.getDayDoneArray(), ","));
		sqlSaveThread.putSql(playerTimerVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.updateDayDone", playerTimerVO));
	}

	/** 对playerTimerVO的每日刷新 */
	public void freshEveryDayTimer() {
		Object[] arrObj = ServerHandler.USERCACHEDMAP.values();
		int playerId = 0;
		/** 在线礼包 */
		NettyMessageVO onlineMsg = new NettyMessageVO();
		onlineMsg.setCommandCode(CSCommandConstant.REWARD_ONLINE);
		BaseOnlineReward nextOnlineReward = OnlineRewardRes.getInstance().getOnlineRewardById(1);
		onlineMsg.setData(RewardOnlineResp.newBuilder().setRewardInfos(VOUtil.packRewardInfos(nextOnlineReward.getRewards())).setCountDown(nextOnlineReward.getTime() * 60).setHadRewardAll(false)
				.build().toByteArray());
		/** 每日充值 **/
		DayChangeRewardRes.getInstance().NextDayChange();
		NettyMessageVO dayChangeMsg = new NettyMessageVO();
		dayChangeMsg.setCommandCode(CSCommandConstant.ACTIVITY_DAY_CHARGE);
		dayChangeMsg.setData(DayChangeResp.newBuilder().setChangeStatus(0).setRewardId(DayChangeRewardRes.getInstance().getRewardIndex()).build().toByteArray());

		/** 体力购买次数 */
		NettyMessageVO powerBuyMsg = new NettyMessageVO();
		powerBuyMsg.setCommandCode(CSCommandConstant.SHOP_BUY_POWER);

		/** 每日活跃度 */
		NettyMessageVO dayDoneMsg = new NettyMessageVO();
		dayDoneMsg.setCommandCode(CSCommandConstant.ACTIVITY_DAY_DONE_UPDATE);
		dayDoneMsg.setData(PushScoreResp.newBuilder().setScore(0).setRewards(0).build().toByteArray());
		for (Object obj : arrObj) {
			try {
				UserCached userCached = (UserCached) obj;
				playerId = userCached.getPlayerId();
				userCached.getPlayerVO().setLastLoginDate(new Date());
				userCached.getPlayerVO().setScores(userCached.getPlayerVO().getScores() + 1); // 登陆天数
				RedisMap.hset(RedisKey.PLAYERVO_MAP, String.valueOf(userCached.getPlayerId()), JSON.toJSONString(userCached.getPlayerVO()));
				System.err.println("update ==== " + userCached.getPlayerId());
				sqlSaveThread.putSql(userCached.getPlayerId(), sqlBuild.getSql("com.dh.dao.PlayerMapper.updatePlayer", userCached.getPlayerVO()));
				PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();
				playerTimerVO.dayClear();

				powerBuyMsg.setData(VOUtil.packBuyPower(userCached).build().toByteArray());

				PlayerTimerVO pt = userCached.getUserTimer().getPlayerTimerVO();
				DayShareResponse.Builder builder = DayShareResponse.newBuilder();
				builder.setDayShare(pt.getDayShare());
				NettyMessageVO dayShareMsg = new NettyMessageVO();
				dayShareMsg.setCommandCode(CSCommandConstant.ACTIVITY_DAY_SHARE);
				dayShareMsg.setData(builder.build().toByteArray());

				Channel ch = userCached.getChannel();
				if (ch != null && ch.isActive()) {
					ch.write(onlineMsg);
					ch.write(dayChangeMsg);
					ch.write(powerBuyMsg);
					ch.write(dayDoneMsg);
					ch.write(dayDoneMsg);
					ch.write(dayShareMsg);
					ch.flush();
				}
			} catch (Exception e) {
				LOGGER.error("everyday fresh,playerId:" + playerId + "\n" + e.getCause(), e);
			}
		}
		freshAllTimerEveryDayDB();
	}

	public void freshAllTimerEveryDayDB() {
		sqlSaveThread.putSql(0, sqlBuild.getSql("com.dh.dao.PlayerTimerMapper.freshTimerEveryDay", null));
	}

}
