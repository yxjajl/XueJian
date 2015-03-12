package com.dh.handler.soldier;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.SoldierProcessor;

@Component
public class SoldierHandler implements ICommandHandler {
//	private static Logger logger = Logger.getLogger(SoldierHandler.class);
	@Resource
	private SoldierProcessor soldierProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		switch (nettyMessageVO.getCommandCode()) {
//		case CSCommandConstant.SOLDIER_PREPARE_UPLEVEL: // 副本进度列表
//			prePareSoldier(nettyMessageVO, commandList);
//			break;
//		case CSCommandConstant.SOLDIER_UPLEVEL: // 士兵升级
//			soldierUpLevel(nettyMessageVO, commandList);
//			break;
//		case CSCommandConstant.PRE_HERO_UPDATE: // 获取英雄升级信息
//			prepareHeroUpLevel(nettyMessageVO, commandList);
//			break;
//		case CSCommandConstant.HERO_UPDATE: // 英雄升级
//			heroUpLevel(nettyMessageVO, commandList);
//			break;
//		case CSCommandConstant.HERO_UNLOCK:
//			unlockHero(nettyMessageVO, commandList);
//			break;
//		case CSCommandConstant.MONSTER_GET_ALL: // 取所有怪物的数据
//			getAllMonster(nettyMessageVO, commandList);
//			break;
//		case CSCommandConstant.SOLDIER_TEAM_UPDATE: // 修改上阵士兵数据
//			updateSoldierTeam(nettyMessageVO, commandList);
//			break;
//		case CSCommandConstant.PREPARE_ASSISTOR: // 取副将加成数据
//			prepareAssitor(nettyMessageVO, commandList);
//			break;
//		case CSCommandConstant.UPDATE_ASSITOR: // 更新副将
//			updateAssitor(nettyMessageVO, commandList);
//			break;
//		case CSCommandConstant.HERO_PRE_ENHANCE: // 强化界面初始化
//			heroPreEnhance(nettyMessageVO, commandList);
//			break;
//		case CSCommandConstant.HERO_ENHANCE: // 英雄强化
//			heroEnhance(nettyMessageVO, commandList);
//			break;
//		case CSCommandConstant.HERO_ENHANCE_SAVE: // 保存强化结果
//			heroEnhanceSave(nettyMessageVO, commandList);
//			break;
//		case CSCommandConstant.HERO_TRANS_LIST:// 请求传承列表
//			handleTransList(nettyMessageVO, commandList);
//			break;
//		case CSCommandConstant.HERO_TRANS:// 确认传承
//			handleTrans(nettyMessageVO, commandList);
//			break;
//		default:
//			logger.error("error Commandcode " + nettyMessageVO.getCommandCode());
//			break;
//		}
	}

//	private void handleTrans(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		HeroTransReq req = null;
//		try {
//			req = HeroTransReq.parseFrom(nettyMessageVO.getData());
//		} catch (InvalidProtocolBufferException e) {
//			logger.error("hero trans req parse error");
//			return;
//		}
//		soldierProcessor.heroTrans(req, nettyMessageVO, commandList);
//
//	}
//
//	private void handleTransList(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) {
//		HeroTransListReq req = null;
//		try {
//			req = HeroTransListReq.parseFrom(nettyMessageVO.getData());
//		} catch (InvalidProtocolBufferException e) {
//			logger.error("hero trans parse error ");
//			return;
//		}
//		soldierProcessor.getHeroTransList(req, nettyMessageVO, commandList);
//
//	}
//
//	/**
//	 * 解锁英雄
//	 * 
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void unlockHero(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		UnLockHeroRequest request = null;
//		try {
//			request = UnLockHeroRequest.parseFrom(nettyMessageVO.getData());
//		} catch (Exception e) {
//			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
//			throw e;
//		}
//		soldierProcessor.unlockHero(request, nettyMessageVO, commandList);
//	}
//
//	/**
//	 * 获取副将数据
//	 * 
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void prepareAssitor(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		soldierProcessor.prepareAssitor(nettyMessageVO, commandList);
//	}
//
//	/**
//	 * 更新副将
//	 * 
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void updateAssitor(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		UpdateAssistorRequest request = null;
//		try {
//			request = UpdateAssistorRequest.parseFrom(nettyMessageVO.getData());
//		} catch (Exception e) {
//			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
//			throw e;
//		}
//		soldierProcessor.updateAssitor(request, nettyMessageVO, commandList);
//	}
//
//	/**
//	 * 进入士兵升级界面
//	 * 
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void prePareSoldier(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		PrePareSoldierRequest request = null;
//		try {
//			request = PrePareSoldierRequest.parseFrom(nettyMessageVO.getData());
//		} catch (Exception e) {
//			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
//			throw e;
//		}
//		soldierProcessor.prePareSoldier(request, nettyMessageVO, commandList);
//	}
//
//	/**
//	 * 士兵升级
//	 * 
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void soldierUpLevel(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		PrePareSoldierRequest request = null;
//		try {
//			request = PrePareSoldierRequest.parseFrom(nettyMessageVO.getData());
//
//		} catch (Exception e) {
//			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
//			throw e;
//		}
//		soldierProcessor.soldierUpLevel(request, nettyMessageVO, commandList);
//	}
//
//	/**
//	 * 英雄升级
//	 * 
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void heroUpLevel(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		HeroUpdateRequest request = null;
//		try {
//			request = HeroUpdateRequest.parseFrom(nettyMessageVO.getData());
//		} catch (Exception e) {
//			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
//			throw e;
//		}
//		soldierProcessor.heroUpLevel(request, nettyMessageVO, commandList);
//	}
//
//	/**
//	 * 获取升级信息
//	 * 
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void prepareHeroUpLevel(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		HeroUpdateRequest request = null;
//		try {
//			request = HeroUpdateRequest.parseFrom(nettyMessageVO.getData());
//		} catch (Exception e) {
//			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
//			throw e;
//		}
//		soldierProcessor.prepareHeroUpLevel(request, nettyMessageVO, commandList);
//	}
//
//	/**
//	 * 保存上阵英雄士兵数据
//	 * 
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void updateSoldierTeam(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		SoldierTeam request = null;
//		try {
//			request = SoldierTeam.parseFrom(nettyMessageVO.getData());
//		} catch (Exception e) {
//			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
//			throw e;
//		}
//		soldierProcessor.updateSoldierTeam(request, nettyMessageVO, commandList);
//	}
//
//	/**
//	 * 获取所有怪物的基础数据
//	 * 
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void getAllMonster(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		commandList.add(CommonCommand.GETALLMONSTER);
//	}
//
//	/**
//	 * 强化界面初始化
//	 * 
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void heroPreEnhance(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		PreHeroEnhanceRequest request = null;
//		try {
//			request = PreHeroEnhanceRequest.parseFrom(nettyMessageVO.getData());
//		} catch (Exception e) {
//			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
//			throw e;
//		}
//		soldierProcessor.heroPreEnhance(request, nettyMessageVO, commandList);
//	}
//
//	/**
//	 * 强化计算
//	 * 
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void heroEnhance(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		HeroEnhanceRequest request = null;
//		try {
//			request = HeroEnhanceRequest.parseFrom(nettyMessageVO.getData());
//		} catch (Exception e) {
//			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
//			throw e;
//		}
//		soldierProcessor.heroEnhance(request, nettyMessageVO, commandList);
//	}
//
//	/**
//	 * 强化数据保存
//	 * 
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void heroEnhanceSave(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		HeroEnhanceSaveRequest request = null;
//		try {
//			request = HeroEnhanceSaveRequest.parseFrom(nettyMessageVO.getData());
//		} catch (Exception e) {
//			logger.error(ProperytiesUtil.getAlertMsg(AlertEnum.DATA_PARSE_ERROR), e);
//			throw e;
//		}
//		soldierProcessor.heroEnhanceSave(request, nettyMessageVO, commandList);
//	}

}
