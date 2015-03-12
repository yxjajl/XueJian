package com.dh.handler.recharge;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.gm.GMProto.RechargeTestReq;
import com.dh.game.vo.item.YunZiFuProto.GoldIngotsRequest;
import com.dh.game.vo.item.YunZiFuProto.YellowLevelGiftRequest;
import com.dh.game.vo.recharge.RechargeProto.RechargeRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ExceptionProcessor;
import com.dh.processor.LoginProcessor;
import com.dh.processor.RechargeProcessor;
import com.dh.util.CommandUtil;
import com.dh.vo.user.UserCached;

@Component
public class RechargeHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(RechargeHandler.class);

	@Resource
	private RechargeProcessor rechargeProcessor;
	@Resource
	private ExceptionProcessor exceptionProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.SHOP_GOLDINGOT_REQ: // 把钱打入用户帐号
			getUrlParameter(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.GET_YELLOW_TOKEN:
			rechargeProcessor.getYellowToken(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RECHARGE_RMB: // 把钱打入用户帐号
			recharge(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RECHARGE_TEST:
			rechargeTest(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.GET_YELLOW_NEW_GIFT: // 领取黄钻新手礼包
			rechargeProcessor.getYellowNewGift(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.GET_YELLOW_LEVEL_GIFT: // 领取黄钻新手礼包
			getYellowLevelGift(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.GET_YELLOW_DAY_GIFT: // 领取黄钻日礼包
			rechargeProcessor.getYellowDayGift(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.GET_YELLOW_DETAIL: // 黄钻信息（加载）
			int playerId = ServerHandler.get(nettyMessageVO.getChannel());
			UserCached userCached = ServerHandler.getUserCached(playerId);
			LoginProcessor.loadZqGame(userCached.getPlayerVO());
			CommandUtil.packageYellowGiftInfo(userCached, commandList);
			break;
		default:
			exceptionProcessor.errCommandPro(nettyMessageVO);
			break;
		}
	}

	public void rechargeTest(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		RechargeTestReq req = null;
		try {
			req = RechargeTestReq.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		rechargeProcessor.rechargeTest(req, nettyMessageVO, commandList);
	}

	public void getYellowLevelGift(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		YellowLevelGiftRequest request = null;
		try {
			request = YellowLevelGiftRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("数据解析异常", e);
		}
		rechargeProcessor.getYellowLevelGift(request, nettyMessageVO, commandList);
	}

	// Q点购买元宝 申请urlparam
	public void getUrlParameter(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		GoldIngotsRequest request = null;
		try {
			request = GoldIngotsRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("数据解析异常", e);
		}
		rechargeProcessor.getUrlParameter(request, nettyMessageVO, commandList);
	}

	// 假充值
	public void recharge(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		RechargeRequest request = null;
		try {
			request = RechargeRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("数据解析异常", e);
		}
		rechargeProcessor.recharge(request, nettyMessageVO);
	}

}
