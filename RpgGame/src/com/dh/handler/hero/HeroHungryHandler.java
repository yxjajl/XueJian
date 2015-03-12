package com.dh.handler.hero;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.hero.HeroTiredProto.HeroRestRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ExceptionProcessor;
import com.dh.processor.HeroHangProcessor;
import com.dh.util.ProperytiesUtil;

@Component
public class HeroHungryHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(HeroHandler.class);

	@Resource
	private HeroHangProcessor heroHangProcessor;
	@Resource
	private ExceptionProcessor exceptionProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.HERO_HUNGRY_DETAIL:
			heroHangProcessor.getHeroHangDetail(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.HERO_HUNGRY_SLEEP:
			heroHangSleep(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.HERO_QUICK_SLEEP:
			heroQuickSleep(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.HERO_HUNGRY_BACK: //返回英雄队列
			hungryBack(nettyMessageVO, commandList);

			break;
		default:
			logger.error("error Commandcode " + nettyMessageVO.getCommandCode());
			exceptionProcessor.errCommandPro(nettyMessageVO);
			break;
		}

	}

	/**
	 * 英雄休息
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void hungryBack(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		HeroRestRequest request = null;
		try {
			request = HeroRestRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}
		heroHangProcessor.hungryBack(request, nettyMessageVO, commandList);
	}

	/**
	 * 英雄休息
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void heroHangSleep(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		HeroRestRequest request = null;
		try {
			request = HeroRestRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}
		heroHangProcessor.heroHangSleep(request, nettyMessageVO, commandList);
	}

	/**
	 * 英雄休息加速
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void heroQuickSleep(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		HeroRestRequest request = null;
		try {
			request = HeroRestRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}
		heroHangProcessor.heroQuickSleep(request, nettyMessageVO, commandList);
	}
}
