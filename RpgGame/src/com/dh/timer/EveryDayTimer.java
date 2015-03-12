package com.dh.timer;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dh.constants.GameRecordConstants;
import com.dh.constants.RoleLevelRank;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.base.GameRecordVO;
import com.dh.game.vo.user.PlayerOpenActVO;
import com.dh.netty.NettyMessageVO;
import com.dh.queue.LocalCommandQueue;
import com.dh.service.BaseInfoService;
import com.dh.service.WelfareService;
import com.dh.util.DateUtil;
import com.dh.vo.user.RoleLv;

/**
 * 每日凌晨定时任务
 * 
 * @author RickYu
 * 
 */
@Component
public class EveryDayTimer {
	private static Logger logger = Logger.getLogger(EveryDayTimer.class);
	private final static long NORMAL_OFFSET = 300000L;

	private final static NettyMessageVO NETTYMESSAGEVO = new NettyMessageVO();

	static {
		NETTYMESSAGEVO.setCommandCode(CSCommandConstant.SYS_SHOP_REFRESH);
	}

	@Resource
	private BaseInfoService baseInfoService;
	@Resource
	private WelfareService welfareService;

	// @Scheduled(cron = "1 1 0 * * ?")
	@Scheduled(cron = "10 0 0 * * ?")
	public void run() {
		logger.debug("=============ShopTimer===========");
		LocalCommandQueue.getInstance().put(NETTYMESSAGEVO);
		clear(); // 每日清理
		openactivy(); // 开服活动
	}

	public void openactivy() {
		try {
			// GameRecordConstants.LOPENSERVERTIME =
			// DateUtil.str2Date("2015-01-16",
			// DateUtil.DAY_FORMAT_STRING).getTime();
			if (isSpeckDay(1, GameRecordConstants.LOPENSERVERTIME)) {
				congjijieshuan(1, GameRecordConstants.OPENSERVERTIME1);
			} else if (isSpeckDay(3, GameRecordConstants.LOPENSERVERTIME)) {
				congjijieshuan(3, GameRecordConstants.OPENSERVERTIME3);
			} else if (isSpeckDay(7, GameRecordConstants.LOPENSERVERTIME)) {
				congjijieshuan(7, GameRecordConstants.OPENSERVERTIME7);
			}
		} catch (Exception e) {
			logger.error("openactivy = ", e);
		}
	}

	public void congjijieshuan(int stage, int openserverid) {
		GameRecordVO gameRecordVO = GameRecordConstants.getGameRecordVO(openserverid);
		if (gameRecordVO.getValue1() == 0) {
			gameRecordVO.setValue1(1);
			baseInfoService.updateGameRecordVO(gameRecordVO);
		}
		List<RoleLv> list = RoleLevelRank.getInstance().getOrderList(10);
		int order = 1;
		for (RoleLv roleLv : list) {
			welfareService.insertPlayerOpenActVO(createPlayerOpenActVO(2, stage, roleLv.playerId, order));
			if (order < 4) {
				order++;
			}
		}
	}

	public static PlayerOpenActVO createPlayerOpenActVO(int actid, int step, int playerId, int rank) {
		PlayerOpenActVO playerOpenActVO = new PlayerOpenActVO();

		playerOpenActVO.setActid(actid);
		playerOpenActVO.setStep(step);
		playerOpenActVO.setRank(rank);
		playerOpenActVO.setReward(0);
		playerOpenActVO.setPlayerId(playerId);
		playerOpenActVO.setState(0);

		return playerOpenActVO;
	}

	public static boolean isSpeckDay(int day, long oldTime) {
		long curTime = System.currentTimeMillis();
		System.out.println(new java.util.Date());
		System.out.println(new java.util.Date(oldTime));
		if (Math.abs(curTime - oldTime - day * DateUtil.DAY_MILLIS) <= NORMAL_OFFSET) {
			System.out.println("是第" + day + "天");
			return true;
		}
		System.out.println("不是第" + day + "天");
		return false;
	}

	public void clear() {

		GameRecordVO gameRecordVO = GameRecordConstants.getGameRecordVO(GameRecordConstants.TENZAOMU);
		gameRecordVO.setValue1(0);
		baseInfoService.updateGameRecordVO(gameRecordVO);

		gameRecordVO = GameRecordConstants.getGameRecordVO(GameRecordConstants.JJCTZ);
		gameRecordVO.setValue1(0);
		baseInfoService.updateGameRecordVO(gameRecordVO);

		gameRecordVO = GameRecordConstants.getGameRecordVO(GameRecordConstants.ENHANCE);
		gameRecordVO.setValue1(0);
		baseInfoService.updateGameRecordVO(gameRecordVO);

		gameRecordVO = GameRecordConstants.getGameRecordVO(GameRecordConstants.HEROUPLEVEL);
		gameRecordVO.setValue1(0);
		baseInfoService.updateGameRecordVO(gameRecordVO);

		GameRecordConstants.TENZAOMU_VALUE.set(0);// 求贤若渴 全服十连抽达到500次
		GameRecordConstants.JJCTZ_VALUE.set(0);// 身经百战 全服竞技场挑战达到1000次
		GameRecordConstants.ENHANCE_VALUE.set(0);// 千锤百炼 全服装备强化达到1000次
		GameRecordConstants.HEROUPLEVEL_VALUE.set(0);// 突飞猛进 全服英雄升级达到1000次

	}

	public static void main(String[] args) throws Exception {
		new EveryDayTimer().openactivy();
	}
}
