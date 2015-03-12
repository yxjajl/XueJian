package com.dh.handler.street;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.street.StreetProto.BoxRewardReq;
import com.dh.game.vo.street.StreetProto.EnemyCenterReq;
import com.dh.game.vo.street.StreetProto.FactoryMakeReq;
import com.dh.game.vo.street.StreetProto.FactoryUpReq;
import com.dh.game.vo.street.StreetProto.GridAtkFreshReq;
import com.dh.game.vo.street.StreetProto.GridFreshReq;
import com.dh.game.vo.street.StreetProto.HuntReq;
import com.dh.game.vo.street.StreetProto.LogRewardReq;
import com.dh.game.vo.street.StreetProto.OpenGridReq;
import com.dh.game.vo.street.StreetProto.ResInfoReq;
import com.dh.game.vo.street.StreetProto.ResRewardReq;
import com.dh.game.vo.street.StreetProto.StreetDefendReq;
import com.dh.game.vo.street.StreetProto.StreetDefendTeamReq;
import com.dh.game.vo.street.StreetProto.StreetQuitDefReq;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ExceptionProcessor;
import com.dh.processor.StreetProcessor;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message.Builder;

/**
 * 江湖
 * 
 * @author dingqu-pc100
 * 
 */
@Component
public class StreetHandler implements ICommandHandler {
	public final static Logger logger = Logger.getLogger(StreetHandler.class);
	Builder req = null;
	@Resource
	private StreetProcessor streetProcessor;
	@Resource
	private ExceptionProcessor exceptionProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.STREET_GRID_ATK_UPDATE:
			handleGridAtkFresh(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_RES_BASEINFO:
			handleResBaseInfo(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_HOME:// 主界面
			handleHome(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_OPEN:
			handleOpenGrid(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_HUNT:// 搜素
			handleHunt(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_RES_INFO:
			handleResInfo(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_DEFEND:// 驻守
			handleDefend(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_QUIT_DEFEND:
			handleQuitDefend(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_FACTORY:// 军器坊
			handleFactory(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_FACTORY_UP:// 军器升级
			handleFactoryUP(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_FACTORY_MAKE:// 军器制造
			handleFactoryMake(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_LOG_BRIEF:// 简报
			handleBriefLog(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_LOG_BATTLE:// 战斗日报
			handleBattleLog(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_LOG_ENEMY:// 仇家日报
			handleAnemyLog(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_RES_REWARD:// 按类型领取
			handleResReward(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_BOX_REWARD:// 开启宝箱
			handleBoxReward(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_RES_REWARD_ALL:
			handleResRewardAll(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_DEFEND_TEAM:
			handleDefendTeam(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_GRID_FRESH:
			handleGridFresh(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_LOG_REWARD:
			handleLogReward(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.STREET_ENEMY_CENTER:// 复仇获取对方门派信息
			handleEnemyCenter(nettyMessageVO, commandList);
			break;

		default:
			exceptionProcessor.errCommandPro(nettyMessageVO);
			break;
		}
	}

	private void handleResBaseInfo(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		streetProcessor.getResBaseInfo(nettyMessageVO, commandList);

	}

	private void handleGridAtkFresh(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		GridAtkFreshReq req = null;
		try {
			req = GridAtkFreshReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		streetProcessor.getGridAtkFresh(req, nettyMessageVO, commandList);

	}

	private void handleResInfo(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		ResInfoReq req = null;
		try {
			req = ResInfoReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		streetProcessor.getResInfo(req, nettyMessageVO, commandList);

	}

	/**
	 * 获得对方门派信息
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	private void handleEnemyCenter(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		EnemyCenterReq req = null;
		try {
			req = EnemyCenterReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		streetProcessor.enemyCenter(req, nettyMessageVO, commandList);
	}

	private void handleLogReward(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		LogRewardReq req = null;
		try {
			req = LogRewardReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error(AlertEnum.DATA_PARSE_ERROR, e);
			return;
		}
		streetProcessor.logReward(req, nettyMessageVO, commandList);
	}

	private void handleGridFresh(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		GridFreshReq req = null;
		try {
			req = GridFreshReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码错误", e);
			return;
		}
		streetProcessor.monsterAndBoxFresh(req, nettyMessageVO, commandList);
	}

	private void handleDefendTeam(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		StreetDefendTeamReq req = null;
		try {
			req = StreetDefendTeamReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码错误", e);
			return;
		}
		streetProcessor.getDefendTeam(req, nettyMessageVO, commandList);
	}

	private void handleResRewardAll(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		streetProcessor.ResRewardAll(nettyMessageVO, commandList);
	}

	private void handleBoxReward(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		BoxRewardReq req = null;
		try {
			req = BoxRewardReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码错误", e);
			return;
		}
		streetProcessor.boxReward(req, nettyMessageVO, commandList);
	}

	private void handleResReward(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		ResRewardReq req = null;
		try {
			req = ResRewardReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码错误", e);
			return;
		}
		streetProcessor.resReward(req, nettyMessageVO, commandList);

	}

	private void handleAnemyLog(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		streetProcessor.enemyLog(nettyMessageVO, commandList);
	}

	private void handleBattleLog(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		streetProcessor.battltLog(nettyMessageVO, commandList);
	}

	private void handleBriefLog(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		streetProcessor.briefLog(nettyMessageVO, commandList);
	}

	private void handleFactoryUP(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		FactoryUpReq req = null;
		try {
			req = FactoryUpReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码错误", e);
			return;
		}
		streetProcessor.factoryUp(req, nettyMessageVO, commandList);
	}

	private void handleFactoryMake(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		FactoryMakeReq req = null;
		try {
			req = FactoryMakeReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码错误", e);
			return;
		}
		streetProcessor.factoryMake(req, nettyMessageVO, commandList);
	}

	private void handleFactory(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		streetProcessor.getFactory(nettyMessageVO, commandList);
	}

	private void handleDefend(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		StreetDefendReq req = null;
		try {
			req = StreetDefendReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码错误" + e.getMessage(), e);
			return;
		}
		streetProcessor.configDefend(req, nettyMessageVO, commandList);
	}

	private void handleQuitDefend(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		StreetQuitDefReq req = null;
		try {
			req = StreetQuitDefReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码错误" + e.getMessage(), e);
			return;
		}
		streetProcessor.quitDef(req, nettyMessageVO, commandList);
	}

	private void handleHunt(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		HuntReq req = null;
		try {
			req = HuntReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码错误" + e.getMessage(), e);
			return;
		}
		streetProcessor.hunt(req, nettyMessageVO, commandList);
	}

	private void handleOpenGrid(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		OpenGridReq req = null;
		try {
			req = OpenGridReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			logger.error("协议解码错误" + e.getMessage(), e);
			return;
		}
		streetProcessor.openGrid(req, nettyMessageVO, commandList);
	}

	private void handleHome(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		streetProcessor.enterHome(nettyMessageVO, commandList);
	}

}
