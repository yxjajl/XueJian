package com.dh.processor;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.constants.CommonConstants;
import com.dh.enums.GMIOEnum;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.item.ShopProto.OpenGuidRequest;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.user.PlayerAccountVO;
import com.dh.game.vo.user.PlayerTimerVO;
import com.dh.netty.NettyMessageVO;
import com.dh.service.PlayerAccountService;
import com.dh.service.PlayerTimerService;
import com.dh.sync.LockConstant;
import com.dh.sync.SyncLock;
import com.dh.sync.SyncUtil;
import com.dh.util.CommandUtil;
import com.dh.util.VOUtil;
import com.dh.vo.user.UserCached;

@Component
public class PropertyProcessor {
	@Resource
	private PlayerAccountService playerAccountService;
	@Resource
	private PlayerTimerService playerTimerService;

	/**
	 * 获得玩家最新行动力
	 */
	public void updatePlayerPower(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		playerAccountService.freshPlayerPower(userCached);
		CommandUtil.packPlayerPower(userCached, commandList);
	}

	// 更新背包信息
	public void updateMyknapsack(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		playerTimerService.flushKnaspack(userCached);
		CommandUtil.packageNettyMessage(CSCommandConstant.UPDATE_MYKNAPSACK, VOUtil.getMyKnapsack(userCached).build().toByteArray(), commandList);
	}

	/**
	 * 开格子
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void openMyknapsack(OpenGuidRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		int num = request.getGridIndex();
		playerTimerService.flushKnaspack(userCached);

		SyncLock syncLock = SyncUtil.getInstance().getLock(LockConstant.LOCK_PLAYERVO + userCached.getPlayerId());
		synchronized (syncLock) {
			PlayerAccountVO playerAccountVO = userCached.getPlayerAccountVO();
			PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();

			if ((userCached.getPlayerAccountVO().getKnapsack() + userCached.getPlayerAccountVO().getKnapsack2()) > CommonConstants.KNASPACK_MAX_GIRD) {
				throw new GameException(AlertEnum.KNAPSACK_LIMIT);
			}

			if (playerAccountVO.getKnapsack2() >= num) {
				playerAccountVO.setKnapsack(playerAccountVO.getKnapsack() + num);
				playerAccountVO.setKnapsack2(playerAccountVO.getKnapsack2() - num);

			} else {
				int n = num - playerAccountVO.getKnapsack2();
				int cost = n * CommonConstants.KNASPACK_RMB;
				playerAccountService.hasEnoughRMBAndGift(userCached, cost);

				playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_RMB, cost, playerAccountVO, commandList, "开启背包" + n, GMIOEnum.OUT_BUY_KNAP.usage(), n);

				playerAccountVO.setKnapsack(playerAccountVO.getKnapsack() + num);
				playerAccountVO.setKnapsack2(0);

				// long remainTime = n * CommonConstants.KNASPACK_STEP;
				long remainTime = (1 + userCached.getPlayerAccountVO().getKnapsack() + userCached.getPlayerAccountVO().getKnapsack2() - CommonConstants.KNASPACK_STARTGIRD)
						* CommonConstants.KNASPACK_STEP + CommonConstants.KNASPACK_STAR;
				playerTimerVO.setKnpsackTime(remainTime);

				playerTimerService.updateknpsackTime(playerTimerVO);
			}

		}
		playerAccountService.updatePlayerAccountKnapsack(userCached.getPlayerAccountVO());
		CommandUtil.packageNettyMessage(CSCommandConstant.UPDATE_MYKNAPSACK, VOUtil.getMyKnapsack(userCached).build().toByteArray(), commandList);
	}

}
