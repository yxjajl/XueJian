package com.dh.handler.friend;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.friend.FriendProto.FriendModReq;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ExceptionProcessor;
import com.dh.processor.FriendProcessor;
import com.google.protobuf.Message.Builder;

@Component
public class FriendHandler implements ICommandHandler {
	@Resource
	private FriendProcessor friendProcessor;
	@Resource
	private ExceptionProcessor exceptionProcessor;
	Builder req = null;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.FRIEND_LIST:
			handleFriendList(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.FRIEND_MOD:
			handleFriendMod(nettyMessageVO, commandList);
			break;
		default:
			exceptionProcessor.errCommandPro(nettyMessageVO);
			break;
		}

	}

	private void handleFriendMod(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		if ((req = ICommandHandler.ParseCheck.parseCheck(FriendModReq.newBuilder(), nettyMessageVO.getData())) != null) {
			friendProcessor.friendMod((FriendModReq.Builder) req, nettyMessageVO, commandList);
		}
	}

	private void handleFriendList(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		friendProcessor.getFriendList(nettyMessageVO, commandList);
	}

}
