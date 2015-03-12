package com.dh.handler.raid;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.raid.RaidProto.BATTLE_TYPE;
import com.dh.game.vo.raid.RaidProto.ChapterScoreRewardRequest;
import com.dh.game.vo.raid.RaidProto.EliRaidTimesBuyReq;
import com.dh.game.vo.raid.RaidProto.HeroTeam;
import com.dh.game.vo.raid.RaidProto.MERaidAccrCDReq;
import com.dh.game.vo.raid.RaidProto.MERaidAddCountReq;
import com.dh.game.vo.raid.RaidProto.OpenBoxRequest;
import com.dh.game.vo.raid.RaidProto.RaidEndRequest;
import com.dh.game.vo.raid.RaidProto.RaidPrepareRequest;
import com.dh.game.vo.raid.RaidProto.RaidProgressRequest;
import com.dh.game.vo.raid.RaidProto.RaidStartRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ExceptionProcessor;
import com.dh.processor.RaidProcessor;
import com.dh.util.ProperytiesUtil;
import com.google.protobuf.Message.Builder;

@Component
public class RaidHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(RaidHandler.class);
	@Resource
	private RaidProcessor raidProcessor;
	@Resource
	private ExceptionProcessor exceptionProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		// RAID_PROGRESS
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.RAID_PROGRESS: // 副本进度列表
			raidProgress(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RAID_ELI_PROGRESS:
			raidEliProgress(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RAID_CHAPTER_LIST:// 副本章节列表
			raidChapterList(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RAID_ELICHAPTER_LIST:
			raidProcessor.raidEliChapterList(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RAID_DETAIL:
			raidDetail(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RAID_UPDATE_HEROTEAM:
			raidUpdateHeroTeam(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RAID_START:// 开始副本
			raidStart(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RAID_END:// 结束副本
			raidEnd(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.OPENBOX:// 翻牌
			openBox(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RAID_UI_INFO:// 副本可攻打次数等
			raidProcessor.raidUIInfo(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RAID_FIVEREWARD: // 令取首破奖励
			fivereward(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RAID_BUY_TIMES: // 购买精英副本次数
			raidBuyTimes(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RAID_SCORE_REWARD:
			raidScoreReward(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RAID_ME_DETAIL:// Money and Expc raid
			getMEDetail(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RAID_ME_ADDCOUNT:
			addMECount(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.RAID_ME_ACCR_CD:
			accrMECD(nettyMessageVO, commandList);
			break;
		default:
			logger.error("error Commandcode " + nettyMessageVO.getCommandCode());
			exceptionProcessor.errCommandPro(nettyMessageVO);
			break;
		}
	}

	private void accrMECD(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		Builder req = null;
		if ((req = ICommandHandler.ParseCheck.parseCheck(MERaidAccrCDReq.newBuilder(), nettyMessageVO.getData())) != null) {
			raidProcessor.accrMECD((MERaidAccrCDReq.Builder) req, nettyMessageVO, commandList);
		}
	}

	private void addMECount(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		Builder req = null;
		if ((req = ICommandHandler.ParseCheck.parseCheck(MERaidAddCountReq.newBuilder(), nettyMessageVO.getData())) != null) {
			raidProcessor.addMERaidCount((MERaidAddCountReq.Builder) req, nettyMessageVO, commandList);
		}
	}

	private void getMEDetail(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		raidProcessor.GetMEDetail(nettyMessageVO, commandList);
	}

	public void raidScoreReward(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		ChapterScoreRewardRequest request = null;
		try {
			request = ChapterScoreRewardRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}

		raidProcessor.raidScoreReward(request, nettyMessageVO, commandList);

	}

	public void raidBuyTimes(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		EliRaidTimesBuyReq request = null;
		try {
			request = EliRaidTimesBuyReq.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}

		raidProcessor.raidBuyTimes(request, nettyMessageVO, commandList);

	}

	public void raidDetail(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		RaidPrepareRequest request = null;
		try {
			request = RaidPrepareRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}

		raidProcessor.battleDetail(request, nettyMessageVO, commandList);

	}

	public void raidUpdateHeroTeam(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		HeroTeam request = null;
		try {
			request = HeroTeam.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}
		raidProcessor.raidUpdateHeroTeam(request, nettyMessageVO, commandList);
	}

	/**
	 * 领取首次完美过关奖励
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void fivereward(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		RaidStartRequest request = null;
		try {
			request = RaidStartRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}
		if (request.getType() == BATTLE_TYPE.RAID) {
			raidProcessor.fivereward(request, nettyMessageVO, commandList);
		} else if (request.getType() == BATTLE_TYPE.ELITE) {
			raidProcessor.fiveElireward(request, nettyMessageVO, commandList);
		}

	}

	/**
	 * 副本开始
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void raidStart(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		RaidStartRequest request = null;
		try {
			request = RaidStartRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("数据解析异常", e);
			exceptionProcessor.errCommandPro(nettyMessageVO);
			throw e;
		}
		raidProcessor.raidStart(request, nettyMessageVO, commandList);
	}

	/*
	 * 副本结束
	 */
	public void raidEnd(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		RaidEndRequest request = null;
		try {
			request = RaidEndRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}
		raidProcessor.raidEnd(request, nettyMessageVO, commandList);
	}

	/**
	 * 副本详情获取
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void raidChapterList(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		raidProcessor.raidChapterList(nettyMessageVO, commandList);

	}

	/**
	 * 副本进度
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void raidProgress(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		RaidProgressRequest request = null;
		try {
			request = RaidProgressRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}
		raidProcessor.raidProgress(request, nettyMessageVO, commandList);

	}

	public void raidEliProgress(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		RaidProgressRequest request = null;
		try {
			request = RaidProgressRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}
		raidProcessor.raidEliProgress(request, nettyMessageVO, commandList);

	}

	/**
	 * 翻牌
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void openBox(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		OpenBoxRequest request = null;
		try {
			request = OpenBoxRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
			throw e;
		}
		raidProcessor.openBox(request, nettyMessageVO, commandList);

	}

}
