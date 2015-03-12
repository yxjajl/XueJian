package com.dh.handler.raid;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.raid.YuanZhenProto.YuanZhenDetailRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.YuanZhenProcessor;
import com.dh.util.ProperytiesUtil;

@Component
public class YuanZhenHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(YuanZhenHandler.class);

	@Resource
	private YuanZhenProcessor yuanZhenProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.YUZHENUI: // 进入远征
			yuanZhenProcessor.getYuanZhenData(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.YUZHENDETAIL: // 远征副本信息
			yuanZhenDetail(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.REFRESHYUANZHEN: // 刷新远征
			yuanZhenProcessor.refreshYuZhen(nettyMessageVO, commandList);
			break;
		default:
			logger.error("error Commandcode " + nettyMessageVO.getCommandCode());
			break;
		}
	}

	public void yuanZhenDetail(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		YuanZhenDetailRequest request = null;
		try {
			request = YuanZhenDetailRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}

		yuanZhenProcessor.yuanZhenDetail(request, nettyMessageVO, commandList);
	}
}
