package com.dh.handler.mail;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.message.MessageProto.MailDelRequest;
import com.dh.game.vo.message.MessageProto.MailListRequest;
import com.dh.game.vo.message.MessageProto.MarkReadRequest;
import com.dh.game.vo.message.MessageProto.RewardedRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.MailProcessor;
import com.google.protobuf.InvalidProtocolBufferException;

@Component
public class MailHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(MailHandler.class);
	@Resource
	private MailProcessor mailProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.CMD_MAIL_LIST:
			handleMailList(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.CMD_MAIL_MARK_READ:
			handleMailRead(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.CMD_MAIL_DEL:
			handleMailDel(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.CMD_MAIL_REWARD:
			handleMailReward(nettyMessageVO, commandList);
			break;
		default:
			break;
		}

	}

	// private void handlemailRewardAll(NettyMessageVO nettyMessageVO,
	// List<NettyMessageVO> commandList) {
	// mailProcessor.rewardAll(nettyMessageVO,commandList);
	//
	// }

	/**
	 * 领取附件奖励
	 * 
	 * @date 2014年3月13日
	 */
	private void handleMailReward(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		RewardedRequest req = null;
		try {
			req = RewardedRequest.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码异常", e);
			return;
		}
		mailProcessor.mailRewarded(req, nettyMessageVO, commandList);

	}

	/**
	 * 标记邮件已读
	 * 
	 * @date 2014年3月13日
	 */
	private void handleMailRead(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		MarkReadRequest req = null;
		try {
			req = MarkReadRequest.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码异常", e);
			return;
		}
		mailProcessor.mailRead(req.getId(), nettyMessageVO, commandList);
	}

	/**
	 * 删除邮件
	 * 
	 * @date 2014年3月13日
	 */
	private void handleMailDel(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		MailDelRequest req = null;
		try {
			req = MailDelRequest.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码异常", e);
			return;
		}
		if (req.getIdsCount() > 0) {
			mailProcessor.delMail(req, nettyMessageVO, commandList);
		}
	}

	/**
	 * 获取邮件列表
	 * 
	 * @date 2014年3月13日
	 */
	private void handleMailList(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		MailListRequest req = null;
		try {
			req = MailListRequest.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码异常", e);
			return;
		}
		mailProcessor.getMailList(req, nettyMessageVO, commandList);
	}

}
