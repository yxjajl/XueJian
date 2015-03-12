package com.dh.handler.legion;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.hero.HeroProto.HeroStarUpPreRequest;
import com.dh.game.vo.legion.LegionProto.LegionCreateReq;
import com.dh.game.vo.legion.LegionProto.LegionDestroyReq;
import com.dh.game.vo.legion.LegionProto.LegionDonateReq;
import com.dh.game.vo.legion.LegionProto.LegionEditReq;
import com.dh.game.vo.legion.LegionProto.LegionJoinReq;
import com.dh.game.vo.legion.LegionProto.LegionListReq;
import com.dh.game.vo.legion.LegionProto.LegionMemManageReq;
import com.dh.game.vo.legion.LegionProto.LegionShopBuyReq;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ExceptionProcessor;
import com.dh.processor.LegionProcessor;
import com.dh.util.ProperytiesUtil;

@Component
public class LegionHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(LegionHandler.class);
	@Resource
	private LegionProcessor legionProcessor;
	@Resource
	private ExceptionProcessor exceptionProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.LEGION_LIST: // 英雄升级
			handleLegionList(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEGION_CREATE:
			handleLegionCreate(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEGION_HOME:
			hanldeLegionHome(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEGION_JION:
			handleLegionJion(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEGION_JION_LIST:
			handleLegionJionList(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEGION_MEM_MANAGE:
			handleMemManage(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEGION_MEM_LIST:// 成员列表
			handleMemList(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEGION_DONATE:
			handleDonate(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEGION_REWARD_MEM:// 个人帮贡领取
			handleRewardMem(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEGION_REWARD_DAY:// 领取帮派每日礼包
			handleDayReward(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEGION_SHOP_BUY:
			handleShop(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEGION_LOGS:
			handleLogs(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEGION_EDIT:// 编辑军团信息
			handleEdit(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEGION_DESTROY:// 解散军团
			handleDestroy(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEGION_BOSS_DETAIL:// boss详情界面
			handleBossDetail(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEGION_BOSS_REWARD:// boss领奖
			handleBossReward(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEGION_BOSS_CD_ACCR:// boss加速
			handleBossAccr(nettyMessageVO, commandList);
			break;
		default:
			logger.error("error Commandcode " + nettyMessageVO.getCommandCode());
			exceptionProcessor.errCommandPro(nettyMessageVO);
			break;
		}
	}

	private void handleBossAccr(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		legionProcessor.legionBossCdAccr(nettyMessageVO, commandList);
	}

	private void handleBossReward(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		legionProcessor.bossReawrd(nettyMessageVO, commandList);
	}

	private void handleBossDetail(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		legionProcessor.getBossDetail(nettyMessageVO, commandList);
	}

	private void handleDestroy(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		LegionDestroyReq req = null;
		try {
			req = LegionDestroyReq.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		legionProcessor.destroy(req, nettyMessageVO, commandList);
	}

	private void handleEdit(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		LegionEditReq req = null;
		try {
			req = LegionEditReq.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		legionProcessor.legionEdit(req, nettyMessageVO, commandList);
	}

	private void handleDayReward(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		legionProcessor.dayReward(nettyMessageVO, commandList);
	}

	private void handleRewardMem(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		legionProcessor.rewardMem(nettyMessageVO, commandList);
	}

	private void handleLogs(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		legionProcessor.getLogs(nettyMessageVO, commandList);
	}

	private void handleShop(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		LegionShopBuyReq req = null;
		try {
			req = LegionShopBuyReq.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		legionProcessor.legionShop(req, nettyMessageVO, commandList);
	}

	private void handleDonate(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		LegionDonateReq req = null;
		try {
			req = LegionDonateReq.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		legionProcessor.legionDonate(req, nettyMessageVO, commandList);
	}

	private void handleMemList(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		legionProcessor.memList(nettyMessageVO, commandList);
	}

	private void handleMemManage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		LegionMemManageReq req = null;
		try {
			req = LegionMemManageReq.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		legionProcessor.legionMemManage(req, nettyMessageVO, commandList);
	}

	private void handleLegionJionList(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		legionProcessor.joinList(nettyMessageVO, commandList);
	}

	private void handleLegionJion(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		LegionJoinReq req = null;
		try {
			req = LegionJoinReq.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		legionProcessor.legionJoin(req, nettyMessageVO, commandList);
	}

	private void hanldeLegionHome(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		legionProcessor.legionHome(nettyMessageVO, commandList);
	}

	private void handleLegionCreate(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		LegionCreateReq req = null;
		try {
			req = LegionCreateReq.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		legionProcessor.legionCreate(req, nettyMessageVO, commandList);
	}

	private void handleLegionList(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {

		LegionListReq req = null;
		try {
			req = LegionListReq.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		legionProcessor.legionList(req, nettyMessageVO, commandList);
	}

}
