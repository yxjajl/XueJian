package com.dh.handler.common;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.common.TestProto.TestRepeatInfo;
import com.dh.game.vo.common.TestProto.testRepeatResp;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.CommonProcess;

@Component
public class CommonHandler implements ICommandHandler {
	@Resource
	private CommonProcess commonProcess;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.BRIEF_INFO:// 宗主信息
			handleBriefInfo(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.COUNT_DOWN_BOX:
			handleCountDownBox(nettyMessageVO, commandList);
			break;
		default:
			break;
		}

	}

	private void handleCountDownBox(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		commonProcess.getCountDown(nettyMessageVO, commandList);
	}

	private void handleBriefInfo(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		commonProcess.getBriefInfo(nettyMessageVO, commandList);
	}

}
