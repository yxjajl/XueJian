package com.dh.handler.item;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.item.ItemProto.ComposeItemRequest;
import com.dh.game.vo.item.ItemProto.EquipChangeRequest;
import com.dh.game.vo.item.ItemProto.FitEquipRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ExceptionProcessor;
import com.dh.processor.ItemEquipProcessor;
import com.dh.vo.user.UserCached;

/**
 * 装备处理类
 * 
 * @author Administrator
 * 
 */
@Component
public class ItemEquipHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(ItemEquipHandler.class);

	@Resource
	private ItemEquipProcessor itemEquipProcessor;
	@Resource
	private ExceptionProcessor exceptionProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {

		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.HERO_ON_EQUIP: // 穿上英雄装备
			takOnEquipment(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.HERO_OFF_EQUIP: // 脱掉英雄装备
			takOffEquipment(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ENHANCE_EQUIP: // 装备强化
			enchanceEquipment(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.ENHANCE_EQUIP_EASY: // 装备强化
			enchanceEquipment2(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.UPSTAR_EQUIP:// 装备升星
			upStarEquipment(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.COMPOSE_ITEM: // 装备合成
			composeItem(nettyMessageVO, commandList);
			break;
		default:
			logger.error("error Commandcode " + nettyMessageVO.getCommandCode());
			exceptionProcessor.errCommandPro(nettyMessageVO);
			break;
		}
	}

	/**
	 * 穿 装
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void takOnEquipment(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		FitEquipRequest res = null;
		try {
			res = FitEquipRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("协议解码异常", e);
			throw e;
		}
		itemEquipProcessor.takOnEquipment(res, userCached, nettyMessageVO, commandList);
	}

	/**
	 * 脱装
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void takOffEquipment(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		FitEquipRequest res = null;
		try {
			res = FitEquipRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("协议解码异常", e);
			throw e;
		}
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		itemEquipProcessor.takOffEquipment(res, userCached, nettyMessageVO, commandList);
	}

	/**
	 * 装备强化
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void enchanceEquipment(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		EquipChangeRequest request = null;
		try {
			request = EquipChangeRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("协议解码异常", e);
			throw e;
		}
		itemEquipProcessor.enchanceEquipment(request, nettyMessageVO, commandList);
	}

	/**
	 * 装备强化
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void enchanceEquipment2(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		EquipChangeRequest request = null;
		try {
			request = EquipChangeRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("协议解码异常", e);
			throw e;
		}
		itemEquipProcessor.enchanceEquipment2(request, nettyMessageVO, commandList);
	}

	/**
	 * 装备升星
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void upStarEquipment(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		EquipChangeRequest request = null;
		try {
			request = EquipChangeRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("协议解码异常", e);
			throw e;
		}
		itemEquipProcessor.upStarEquipment(request, nettyMessageVO, commandList);
	}

	/**
	 * 物品合成
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void composeItem(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		ComposeItemRequest request = null;
		try {
			request = ComposeItemRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("协议解码异常", e);
			throw e;
		}
		itemEquipProcessor.composeItem(request, nettyMessageVO, commandList);
	}

}
