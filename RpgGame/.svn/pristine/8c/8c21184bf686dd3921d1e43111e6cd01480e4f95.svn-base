package com.dh.handler.activity;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.activity.ActivityProto.AnsCheckReq;
import com.dh.game.vo.activity.ActivityProto.AnsNextReq;
import com.dh.game.vo.activity.ActivityProto.AnsSkillReq;
import com.dh.game.vo.activity.ActivityProto.DayDoneRewardReq;
import com.dh.game.vo.activity.ActivityProto.SignReq;
import com.dh.game.vo.activity.ActivityProto.SignRewardReq;
import com.dh.game.vo.activity.WorldBossProto.BossDetailReq;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ActivityProcessor;
import com.dh.processor.ExceptionProcessor;
import com.google.protobuf.InvalidProtocolBufferException;

@Component
public class ActivityHandler implements ICommandHandler {
	@Resource
	private ActivityProcessor activityProcessor;
	@Resource
	private ExceptionProcessor exceptionProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.ACTIVITY_DAY_SIGN_DETAILS:// 获取签到详情
			handleDaySignDetail(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ACTIVITY_DAY_SIGN:// 签到
			handleDaySign(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ACTIVITY_DAY_REWARD:// 领取签到奖励
			handleSignReward(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ACTIVITY_WORLD_BOSS:
			handleBoss(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ACTIVITY_BOSS_DETAIL:
			handleBossDetail(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ACTIVITY_BOSS_CD_ACCR:
			handleBossCdAccr(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ACTIVITY_BOSS_ADDTION:
			handleBossAddtion(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ACTIVITY_BOSS_EXIT:// 退出世界boss场景
			handleBossExit(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ACTIVITY_DAY_CHARGE:
			handleDayChange(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ACTIVITY_DAY_CHARGE_REWARD:
			handleDayChangeReward(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ACTIVITY_CHARGE_LIST:
			handleChargeList(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ACTIVITY_DAY_DONE:
			System.out.println("");
			handleDayDone(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ACTIVITY_DAY_DONE_REWARD:
			handeDayDoneReward(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ACTIVITY_DAY_SHARE: // 每日分享
			activityProcessor.dayShare(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ACTIVITY_ANS_NEXT:// 答题
			handleAnsNext(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ACTIVITY_ANS_CHECK:// 对答案
			handleAnsCheck(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ACTIVITY_ANS_SKILL:// 使用技能
			handleAnsSkill(nettyMessageVO, commandList);
			break;
		default:
			exceptionProcessor.errCommandPro(nettyMessageVO);
			break;
		}

	}

	private void handleAnsSkill(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		AnsSkillReq req = null;
		try {
			req = AnsSkillReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			LOGGER.error(DECODE_ERROR, e);
			return;
		}
		activityProcessor.ansSkill(req, nettyMessageVO, commandList);
	}

	private void handleAnsCheck(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		AnsCheckReq req = null;
		try {
			req = AnsCheckReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			LOGGER.error(DECODE_ERROR, e);
			return;
		}
		activityProcessor.ansCheck(req, nettyMessageVO, commandList);
	}

	private void handleAnsNext(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		AnsNextReq req = null;
		try {
			req = AnsNextReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			LOGGER.error(DECODE_ERROR, e);
			return;
		}
		activityProcessor.ansNext(req, nettyMessageVO, commandList);
	}

	private void handeDayDoneReward(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		DayDoneRewardReq req = null;
		try {
			req = DayDoneRewardReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			LOGGER.error(DECODE_ERROR, e);
			return;
		}
		activityProcessor.dayDoneReward(req, nettyMessageVO, commandList);
	}

	private void handleDayDone(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		activityProcessor.getDayDone(nettyMessageVO, commandList);
	}

	private void handleChargeList(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) {
		activityProcessor.chargeList(nettyMessageVO, commandList);
	}

	private void handleDayChangeReward(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		activityProcessor.dayChangeReward(nettyMessageVO, commandList);

	}

	private void handleDayChange(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		activityProcessor.dayChange(nettyMessageVO, commandList);

	}

	private void handleBossExit(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) {
		activityProcessor.bossExit(nettyMessageVO, commandList);
	}

	private void handleBossAddtion(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		activityProcessor.bossAddtion(nettyMessageVO, commandList);
	}

	private void handleBossCdAccr(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		activityProcessor.BossCdAccr(nettyMessageVO, commandList);
	}

	private void handleBossDetail(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		BossDetailReq req = null;
		try {
			req = BossDetailReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			LOGGER.error(DECODE_ERROR, e);
			return;
		}
		activityProcessor.getBossDetail(req, nettyMessageVO, commandList);
	}

	private void handleBoss(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) {
		activityProcessor.getBoss(nettyMessageVO, commandList);
	}

	private void handleSignReward(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		SignRewardReq req = null;
		try {
			req = SignRewardReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			LOGGER.error("协议解码失败", e);
			return;
		}
		activityProcessor.signReward(req, nettyMessageVO, commandList);
	}

	private void handleDaySign(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		SignReq req = null;
		try {
			req = SignReq.parseFrom(nettyMessageVO.getData());
		} catch (InvalidProtocolBufferException e) {
			LOGGER.error("协议解码失败", e);
			return;
		}
		activityProcessor.sign(req, nettyMessageVO, commandList);
	}

	private void handleDaySignDetail(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		activityProcessor.getDaySign(nettyMessageVO, commandList);
	}

}
