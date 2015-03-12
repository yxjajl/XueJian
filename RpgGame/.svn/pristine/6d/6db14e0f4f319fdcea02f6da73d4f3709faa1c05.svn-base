package com.dh.handler.grab;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.grab.GrabProto.GrabListRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ExceptionProcessor;
import com.dh.processor.GrabProcesso;
import com.dh.util.ProperytiesUtil;

@Component
public class GrabHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(GrabHandler.class);

	@Resource
	private GrabProcesso grabProcesso;
	@Resource
	private ExceptionProcessor exceptionProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.DUOBAO_LIST: // 夺宝
			// heroLevelUp(nettyMessageVO, commandList);
			getEnemList(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.DUOBAO_HUAN: // 换一批对手
			// heroLevelUp(nettyMessageVO, commandList);
			getEnemList2(nettyMessageVO, commandList);
			break;

		default:
			logger.error("未知的指令码 " + nettyMessageVO.getCommandCode());
			exceptionProcessor.errCommandPro(nettyMessageVO);
			break;
		}
	}

	public void getEnemList(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		GrabListRequest request = null;
		try {
			request = GrabListRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}
		grabProcesso.getEnemList(request, nettyMessageVO, commandList);
	}

	/**
	 * 换一批对手
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void getEnemList2(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		GrabListRequest request = null;
		try {
			request = GrabListRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}
		grabProcesso.getEnemList2(request, nettyMessageVO, commandList);
	}

}
