package com.dh.queue;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dh.exception.GameException;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.common.CommonProto.ClientAlert;
import com.dh.handler.HandlerProcessor;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.s2s.queue.AbstractCommandRunnable;
import com.dh.s2s.queue.BaseQueue;

/**
 * 执行消息队列
 * 
 */
public class LocalCommandRunnable extends AbstractCommandRunnable {
	private static final Logger logger = Logger.getLogger(LocalCommandRunnable.class);

	public LocalCommandRunnable(BaseQueue<NettyMessageVO> baseQueue, String name) {
		super(baseQueue, name);
	}

	@Override
	public void sendMessage(NettyMessageVO nettyMessageVO) {
		long now = System.currentTimeMillis();
		nettyMessageVO.setNow(now);
		long d1 = System.currentTimeMillis();
		List<NettyMessageVO> commandList = new ArrayList<NettyMessageVO>();
		ICommandHandler commandHandler = HandlerProcessor.getInstance().getCommandHandler(nettyMessageVO.getCommandCode());
		try {
			if (commandHandler == null) {
				throw new Exception("找不到handler或错误的指令码: " + nettyMessageVO.getCommandCode());
			} else {
				logger.debug("收到指令 : " + nettyMessageVO.getCommandCode());
				commandHandler.handleMessage(nettyMessageVO, commandList);

			}
		} catch (GameException e) {
			logger.warn("异常" + e.toString(), e);
			try {
				ClientAlert.Builder clientAlert = ClientAlert.newBuilder();
				clientAlert.setMsg(e.getMessage());
				NettyMessageVO tempNettyMessageVO = new NettyMessageVO();
				tempNettyMessageVO.setCommandCode(CSCommandConstant.ALERT_COMMAND);
				tempNettyMessageVO.setData(clientAlert.build().toByteArray());
				nettyMessageVO.getChannel().writeAndFlush(tempNettyMessageVO);
			} catch (Exception ee) {
				// ee.printStackTrace();
				logger.error("" + e.getCause(), e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("" + nettyMessageVO.getChannel().remoteAddress().toString() + e.getCause(), e);
		} finally {

			if (commandList != null && commandList.size() > 0) {
				// int playerid =
				// ServerHandler.get(nettyMessageVO.getChannel());
				for (NettyMessageVO tempMessage : commandList) {
					// tempMessage.setUserid(playerid);
					logger.debug("发送数据指令 " + tempMessage.getCommandCode() + ", length = " + tempMessage.getDataLength());
					nettyMessageVO.getChannel().writeAndFlush(tempMessage);
				}
			}

			commandList.clear();
			commandList = null;
		}

		long d2 = System.currentTimeMillis();

		long d3 = (d2 - d1);
		if (d3 > 30) {
			String msg = nettyMessageVO.getCommandCode() + ", USE " + d3;
			System.err.println(msg + ", length= " + nettyMessageVO.getDataLength());
			logger.error(msg);
		}

	}

}
