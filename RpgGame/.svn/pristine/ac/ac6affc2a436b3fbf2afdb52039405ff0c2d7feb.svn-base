package com.dh.handler.property;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.item.ShopProto.OpenGuidRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.PropertyProcessor;
import com.dh.util.ProperytiesUtil;

/**
 * 2014年4月4日
 */
@Component
public class PropertyHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(PropertyHandler.class);
	@Resource
	private PropertyProcessor propertyProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.CMD_UPDATE_POWER:
			handleUpdatePower(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.UPDATE_MYKNAPSACK:
			updateMyknapsack(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.OPEN_MYKNAPSACK:
			openMyknapsack(nettyMessageVO, commandList);
			break;
		}

	}

	private void handleUpdatePower(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		propertyProcessor.updatePlayerPower(nettyMessageVO, commandList);
	}

	private void updateMyknapsack(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		propertyProcessor.updateMyknapsack(nettyMessageVO, commandList);
	}

	private void openMyknapsack(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		OpenGuidRequest request = null;
		try {
			request = OpenGuidRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}
		propertyProcessor.openMyknapsack(request, nettyMessageVO, commandList);
	}
}
