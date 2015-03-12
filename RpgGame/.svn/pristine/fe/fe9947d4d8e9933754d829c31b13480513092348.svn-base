package com.dh.handler.chat;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.message.MessageProto.ChatRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ChatProcessor;
import com.google.protobuf.InvalidProtocolBufferException;

@Component
public class ChatHandler implements ICommandHandler {
	private final static Logger LOGGER = Logger.getLogger(ChatHandler.class);
	@Resource
	private ChatProcessor chatProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		// case CSCommandConstant.CHAT_LIST:
		// handleChatList(nettyMessageVO, commandList);
		// break;
		case CSCommandConstant.CMD_CHAT:
			handleChatSend(nettyMessageVO, commandList);
			break;
		default:
			break;
		}

	}
	private void handleChatSend(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		ChatRequest req = null;
		try {
			req = ChatRequest.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			LOGGER.error("解码错误", e);
			return;
		}
		chatProcessor.chatSend(req, nettyMessageVO, commandList);
	}

}
