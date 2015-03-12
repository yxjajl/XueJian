package com.dh.handler.cdkey;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.activity.ActivityProto.UseCDKeyRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.CdKeyProcessor;
import com.dh.processor.ExceptionProcessor;
import com.google.protobuf.InvalidProtocolBufferException;

@Component
public class CDKeyHandler implements ICommandHandler {
	@Resource
	private CdKeyProcessor cdKeyProcessor;
	@Resource
	private ExceptionProcessor exceptionProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.CDKEY_ENTER:// 新手cdkey
			useCdkey(nettyMessageVO, commandList);
			break;
		default:
			exceptionProcessor.errCommandPro(nettyMessageVO);
			break;
		}

	}

	// 使用cdkey
	private void useCdkey(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		UseCDKeyRequest req = null;
		try {
			req = UseCDKeyRequest.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			LOGGER.error(DECODE_ERROR, e);
			return;
		}
		cdKeyProcessor.useCdkey(req, nettyMessageVO, commandList);
	}

}
