package com.dh.handler.build;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.build.BuildProto.BuildUpLevelACCRequest;
import com.dh.game.vo.build.BuildProto.BuildUpLevelRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.BuildProcessor;
import com.dh.processor.ExceptionProcessor;
import com.dh.util.ProperytiesUtil;

@Component
public class BuildHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(BuildHandler.class);
	@Resource
	private BuildProcessor buildProcessor;
	@Resource
	private ExceptionProcessor exceptionProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.BUILD_YISIT_LIST:
			buildProcessor.buildYiSiTang(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.BUILD_YISIT_SALART:
			buildProcessor.getSalaryResponse(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.BUILD_UP_LEVEL:
			buildUpLevel(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.BUILD_UP_LEVEL_ACC: // 升级加速　
			buildUpLevelACC(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.DAYVIPGIFT: // vip每日奖励
			buildProcessor.dayVipGift(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.GET_DAYVIP: // 领取vip每日奖励
			buildProcessor.dayVipReward(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.CLEARSALARYCD: //清理俸禄cd
			buildProcessor.clearSalaryCD(nettyMessageVO, commandList);
			break;
		default:
			logger.error("error Commandcode " + nettyMessageVO.getCommandCode());
			exceptionProcessor.errCommandPro(nettyMessageVO);
			break;
		}
	}

	public void buildUpLevelACC(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		BuildUpLevelACCRequest request = null;
		try {
			request = BuildUpLevelACCRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("数据解析异常", e);
			throw e;
		}
		buildProcessor.buildUpLevelACC(request, nettyMessageVO, commandList);
	}

	public void buildUpLevel(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		BuildUpLevelRequest request = null;
		try {
			request = BuildUpLevelRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}
		buildProcessor.buildUpLevel(request, nettyMessageVO, commandList);
	}

}
