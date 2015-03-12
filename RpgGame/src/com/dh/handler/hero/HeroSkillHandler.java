package com.dh.handler.hero;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.hero.HeroSkillProto.HeroSkillUpLevelRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ExceptionProcessor;
import com.dh.processor.HeroSkillProcessor;
import com.dh.util.ProperytiesUtil;

@Component
public class HeroSkillHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(HeroSkillHandler.class);
	@Resource
	private HeroSkillProcessor heroSkillProcessor;

	@Resource
	private ExceptionProcessor exceptionProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.HERO_SKILL_UPLEVEL: // 主动技能升级
			skillUpLevel(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.HERO_SKILL_REFRESH: // 被动技能刷新
			// skillRefresh(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.HERO_SKILL_REPLACE: // 被动技能替换
			// skillReplace(nettyMessageVO, commandList);
			break;

		default:
			logger.error("error Commandcode " + nettyMessageVO.getCommandCode());
			exceptionProcessor.errCommandPro(nettyMessageVO);
			break;
		}

	}

	// public void skillRefresh(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
	// SkillRefreshRequest request = null;
	// try {
	// request = SkillRefreshRequest.parseFrom(nettyMessageVO.getData());
	// } catch (Exception e) {
	// logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
	// throw e;
	// }
	// heroSkillProcessor.skillRefresh(request, nettyMessageVO, commandList);
	// }

	// public void skillReplace(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
	// SkillReplaceRequest request = null;
	// try {
	// request = SkillReplaceRequest.parseFrom(nettyMessageVO.getData());
	// } catch (Exception e) {
	// logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
	// throw e;
	// }
	// heroSkillProcessor.skillReplace(request, nettyMessageVO, commandList);
	// }

	public void skillUpLevel(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		HeroSkillUpLevelRequest request = null;
		try {
			request = HeroSkillUpLevelRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}
		heroSkillProcessor.skillUpLevel(request, nettyMessageVO, commandList);
	}

}
