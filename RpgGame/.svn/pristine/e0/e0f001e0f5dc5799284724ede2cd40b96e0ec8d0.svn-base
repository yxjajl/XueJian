package com.dh.handler;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.gm.GMProto.BlackIPReq;
import com.dh.game.vo.gm.GMProto.BlackPlayerReq;
import com.dh.game.vo.gm.GMProto.CheckItemReq;
import com.dh.game.vo.gm.GMProto.ReloadRedisReq;
import com.dh.game.vo.gm.GMProto.SendSysMailReq;
import com.dh.game.vo.gm.GMProto.SendSysMsgReq;
import com.dh.game.vo.gm.GMProto.SysNoticeReq;
import com.dh.netty.NettyMessageVO;
import com.dh.netty.NettyServerHandler;
import com.dh.processor.GMProcessor;
import com.dh.service.BaseInfoService;
import com.google.protobuf.InvalidProtocolBufferException;

@Component
public class GMHandler implements ICommandHandler {
	@Resource
	private GMProcessor gmProcessor;
	@Resource
	private BaseInfoService baseInfoService;
	private static final Logger LOGGER = Logger.getLogger(GMHandler.class);

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.GM_RELOAD_REDIS:
			handeReloadRedis(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RELOAD_CSV:
			baseInfoService.reloadCSV(new String(nettyMessageVO.getData()));
			break;
		case CSCommandConstant.SYS_STOP:
			String ss = new String(nettyMessageVO.getData());
			if (!ss.equals("admin")) {
				return;
			}
			if (NettyServerHandler.System_status == 0) {
				NettyServerHandler.System_status = 1;
			} else {
				NettyServerHandler.System_status = 0;
			}
			System.err.println("NettyServerHandler.System_status   ==0 Start====1 Stop====== " + NettyServerHandler.System_status);
			LOGGER.error("NettyServerHandler.System_status   ==0 Start====1 Stop====== " + NettyServerHandler.System_status);
			break;
		case CSCommandConstant.GM_SEND_SYS_MSG:
			handleSendSysMsg(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.GM_SEND_SYS_MAIL:
			handleSysMail(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.GM_CHECK_ITEM_EXIST:
			handleCheckItemExist(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.GM_BLACK_PLAYER:
			handleBlackPlayer(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.GM_BLACK_IP:
			handleBlackIP(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.GM_SEND_SYS_NOTICE:
			handleSysNotice(nettyMessageVO, commandList);
			break;
		default:
			break;
		}

	}

	private void handleSysNotice(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		SysNoticeReq req = null;
		try {
			req = SysNoticeReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			LOGGER.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		gmProcessor.sendSysNotice(req, nettyMessageVO, commandList);

	}

	private void handleBlackIP(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		BlackIPReq req = null;
		try {
			req = BlackIPReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			LOGGER.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		gmProcessor.addBlackIP(req, nettyMessageVO, commandList);

	}

	private void handleBlackPlayer(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		BlackPlayerReq req = null;
		try {
			req = BlackPlayerReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			LOGGER.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		gmProcessor.addBlackPlayers(req, nettyMessageVO, commandList);

	}

	private void handleCheckItemExist(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) {
		CheckItemReq req = null;
		try {
			req = CheckItemReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			LOGGER.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		gmProcessor.checkItemExist(req, nettyMessageVO, commandList);
	}

	private void handleSysMail(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		SendSysMailReq req = null;
		try {
			req = SendSysMailReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			LOGGER.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		gmProcessor.sendMail(req, nettyMessageVO, commandList);

	}

	private void handleSendSysMsg(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) {
		SendSysMsgReq req = null;
		try {
			req = SendSysMsgReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			LOGGER.error(AlertEnum.DATA_PARSE_ERROR);
			return;
		}
		gmProcessor.sendSysMsg(req, nettyMessageVO, commandList);

	}

	private void handeReloadRedis(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		ReloadRedisReq req = null;
		try {
			req = ReloadRedisReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			LOGGER.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		gmProcessor.reloadRedis(req, nettyMessageVO, commandList);
	}

}
