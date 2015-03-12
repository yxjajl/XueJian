package com.dh.processor;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.constants.GameRecordConstants;
import com.dh.game.vo.base.GameRecordVO;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_UNIT_TYPE;
import com.dh.game.vo.user.PlayerAccountVO;
import com.dh.netty.NettyMessageVO;
import com.dh.service.ArenaService;
import com.dh.service.BaseInfoService;
import com.dh.service.LegionService;
import com.dh.service.PlayerAccountService;
import com.dh.service.PlayerTimerService;
import com.dh.service.ShopService;
import com.dh.service.WelfareService;
import com.dh.util.CommandUtil;
import com.dh.util.DateUtil;
import com.dh.util.GameUtil;
import com.dh.vo.user.UserCached;

@Component
public class TimerProcessor {
	private static Logger logger = Logger.getLogger(TimerProcessor.class);
	@Resource
	private ShopService shopService;
	@Resource
	private ArenaService arenaService;
	@Resource
	private BaseInfoService baseInfoService;
	@Resource
	private PlayerTimerService playerTimerService;
	@Resource
	private PlayerAccountService playerAccountService;
	@Resource
	private WelfareService welfareService;
	@Resource
	private LegionService legionService;

	/**
	 * 商城折扣商品刷新
	 * 
	 * @throws Exception
	 */
	public void shopRefresh() throws Exception {
		shopService.deleteAllPlayerShop();
		shopService.refreshShop();
		// shopService.freshBuyPower();
		playerTimerService.freshEveryDayTimer();
		refreshPvp();
		legionService.freshLegionDay();
	}

	/**
	 * 竞技场每日结算
	 * 
	 * @throws Exception
	 */
	public void arenaRefresh() throws Exception {
		GameRecordVO gameRecordVO = null;
		Date lastUpdateDate = null;
		long curTime = System.currentTimeMillis();

		/**
		 * 每日刷新
		 */
		gameRecordVO = GameRecordConstants.getGameRecordVO(GameRecordConstants.ARENAEVERYDAYBLANCE);
		lastUpdateDate = DateUtil.str2Date(gameRecordVO.getStrv1(), DateUtil.DAY_FORMAT_STRING);

		if (curTime - lastUpdateDate.getTime() >= (ArenaService.DAUINTERVAL - 60000L)) {
			gameRecordVO.setStrv1(DateUtil.formatDate(new Date(), DateUtil.DAY_FORMAT_STRING));
			baseInfoService.updateGameRecordVO(gameRecordVO);
			// 开始进行每日结算
			arenaService.jiesuan();
		}
		ArenaService.nextFreshTime1 = GameUtil.computerNextReFreshTime2(lastUpdateDate.getTime(), ArenaService.DAUINTERVAL);

	}

	public void powerAndPvpRefresh() throws Exception {
		Object[] arrObj = ServerHandler.USERCACHEDMAP.values();
		int playerId = 0;
		for (Object obj : arrObj) {
			try {
				UserCached userCached = (UserCached) obj;
				playerId = userCached.getPlayerId();
				// addpower
				playerAccountService.freshPlayerPower(userCached);

				PlayerAccountVO playerAccountVO = userCached.getPlayerAccountVO();

				NettyMessageVO nettyMessageVO = CommandUtil.packageAnyProperties(PLAYER_UNIT_TYPE.UNIT_PLAYER, playerAccountVO.getPlayerId(), PLAYER_PROPERTY.PROPERTY_POWER,
						playerAccountVO.getPower());
				ServerHandler.sendMessageToPlayer(nettyMessageVO, playerAccountVO.getPlayerId());
			} catch (Exception e) {
				logger.error("powerAndPvpRefresh error" + playerId, e);
			}
		}
	}

	public void refreshPvp() throws Exception {
		Object[] arrObj = ServerHandler.USERCACHEDMAP.values();
		int playerId = 0;
		for (Object obj : arrObj) {
			try {
				UserCached userCached = (UserCached) obj;
				playerId = userCached.getPlayerId();

				PlayerAccountVO playerAccountVO = userCached.getPlayerAccountVO();
				playerAccountService.clearPvp(userCached);

				NettyMessageVO nettyMessageVO = CommandUtil.packageAnyProperties(PLAYER_UNIT_TYPE.UNIT_PLAYER, playerAccountVO.getPlayerId(), PLAYER_PROPERTY.PROPERTY_PVP, playerAccountVO.getPvp());
				ServerHandler.sendMessageToPlayer(nettyMessageVO, playerAccountVO.getPlayerId());
			} catch (Exception e) {
				logger.error("powerAndPvpRefresh error" + playerId, e);
			}
		}
	}

}
