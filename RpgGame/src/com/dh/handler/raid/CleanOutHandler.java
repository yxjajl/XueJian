package com.dh.handler.raid;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.raid.CleanOutProto.CleanOutRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.CleanOutProcessor;
import com.dh.util.ProperytiesUtil;

@Component
public class CleanOutHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(RaidHandler.class);

	@Resource
	private CleanOutProcessor cleanOutProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		// RAID_PROGRESS
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.RAID_CLEAN_OUT: // 扫荡
			raidCleanOut(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RAID_CLEAN_OUTALL: // 扫荡
			raidCleanOutAll(nettyMessageVO, commandList);
			break;
		default:
			logger.error("error Commandcode " + nettyMessageVO.getCommandCode());
			break;
		}
	}

	//

	public void raidCleanOut(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {

		CleanOutRequest request = null;
		try {
			request = CleanOutRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}
		cleanOutProcessor.raidCleanOut(request, nettyMessageVO, commandList);
	}

	public void raidCleanOutAll(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {

		CleanOutRequest request = null;
		try {
			request = CleanOutRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}
		cleanOutProcessor.raidCleanOutAll(request, nettyMessageVO, commandList);
	}

}
