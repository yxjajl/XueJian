package com.dh.handler.arena;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.item.ArenaProto.ArenaRankRequest;
import com.dh.game.vo.item.ArenaProto.LookDefanceTeamRequest;
import com.dh.game.vo.item.ArenaProto.RewardJCCRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ArenaProcessor;
import com.dh.processor.ExceptionProcessor;
import com.dh.util.ProperytiesUtil;

@Component
public class ArenaHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(ArenaHandler.class);
	@Resource
	private ArenaProcessor arenaProcessor;
	@Resource
	private ExceptionProcessor exceptionProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.ARENA_DETAIL: // 竞技场详情
			arenaProcessor.getArenaDetail(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ARENA_LOOK_TEAM: // 查看对手阵容
			lookEnemTeam(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ARENA_REFRESH_ENEM: // 换一批对手
			arenaProcessor.refreshEnem(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ARENA_CD_ENEM: // 清理CD 换一批对手CD
			arenaProcessor.enemCDZero(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ARENA_UPDATETEAM: // 更新防守阵容
			// updateDefanceTeam(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ARENA_REWARD:// 奖励
			rewardJJC(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ARENA_CD_FAIL: // 战斗ＣＤ
			arenaProcessor.failCDZero(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ARENA_BATTLE_RECORD:// 战报
			arenaProcessor.getBattleRecord(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ARENA_SHOP_PVP:// 增加pvp值
			arenaProcessor.addPvp(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ARENA_RANK_LIST:
			getArenaRank(nettyMessageVO, commandList);
			break;
		default:
			exceptionProcessor.errCommandPro(nettyMessageVO);
			break;
		}
	}

	/**
	 * 领取奖励
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void getArenaRank(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		ArenaRankRequest request = null;
		try {
			request = ArenaRankRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}

		arenaProcessor.getArenaRank(request, nettyMessageVO, commandList);
	}

	/**
	 * 领取奖励
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void rewardJJC(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		RewardJCCRequest request = null;
		try {
			request = RewardJCCRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}

		arenaProcessor.rewardJJC(request, nettyMessageVO, commandList);
	}

	/**
	 * 查看阵容
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void lookEnemTeam(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		LookDefanceTeamRequest request = null;
		try {
			request = LookDefanceTeamRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}

		arenaProcessor.lookEnemTeam(request, nettyMessageVO, commandList);
	}

	// /**
	// * 更新防守阵容
	// *
	// * @param nettyMessageVO
	// * @param commandList
	// * @throws Exception
	// */
	// public void updateDefanceTeam(NettyMessageVO nettyMessageVO,
	// List<NettyMessageVO> commandList) throws Exception {
	// UpdateDefanceTeamRequest request = null;
	// try {
	// request = UpdateDefanceTeamRequest.parseFrom(nettyMessageVO.getData());
	// } catch (Exception e) {
	// logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
	// throw e;
	// }
	//
	// arenaProcessor.updateDefanceTeam(request, nettyMessageVO, commandList);
	// }

}
