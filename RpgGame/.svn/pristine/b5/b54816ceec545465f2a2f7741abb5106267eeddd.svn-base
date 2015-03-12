package com.dh.handler.item;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.item.ItemProto.GemPickRequest;
import com.dh.game.vo.item.ItemProto.GemSetRequest;
import com.dh.game.vo.item.ItemProto.OpenHoleRequest;
import com.dh.game.vo.item.ItemProto.ProtectionRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ExceptionProcessor;
import com.dh.processor.GemProcessor;

@Component
public class GemHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(GemHandler.class);

	@Resource
	private GemProcessor gemProcessor;
	
	@Resource
	private ExceptionProcessor exceptionProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {

		switch (nettyMessageVO.getCommandCode()) {

		case CSCommandConstant.GEMSET_ITEM: // 装备合成
			gemSetItem(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.GEMPICK_ITEM:// 摘除宝石
			gemPickItem(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.OPEN_HOLE:// 开孔
			openHole(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.PROTECT_ITEM:// 护佑
			protectItem(nettyMessageVO, commandList);
			break;
		default:
			logger.error("error Commandcode " + nettyMessageVO.getCommandCode());
			exceptionProcessor.errCommandPro(nettyMessageVO);
			break;
		}
	}

	/**
	 * 护佑
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void protectItem(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		ProtectionRequest request = null;
		try {
			request = ProtectionRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("协议解码异常", e);
			throw e;
		}
		gemProcessor.protectItem(request, nettyMessageVO, commandList);
	}

	/**
	 * 开孔
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void openHole(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		OpenHoleRequest request = null;
		try {
			request = OpenHoleRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("协议解码异常", e);
			throw e;
		}
		gemProcessor.openHole(request, nettyMessageVO, commandList);
	}

	/**
	 * 镶嵌
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void gemSetItem(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		GemSetRequest request = null;
		try {
			request = GemSetRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("协议解码异常", e);
			throw e;
		}
		gemProcessor.gemSetItem(request, nettyMessageVO, commandList);
	}

	/**
	 * 摘除
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void gemPickItem(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		GemPickRequest request = null;
		try {
			request = GemPickRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("协议解码异常", e);
			throw e;
		}
		gemProcessor.gemPickItem(request, nettyMessageVO, commandList);
	}

}
