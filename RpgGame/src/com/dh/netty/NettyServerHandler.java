/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.dh.netty;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.SocketAddress;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.constants.GMConstants;
import com.dh.game.vo.gm.GMProto.BLOCK_ENUM;
import com.dh.queue.ArrayCommandQueue;
import com.dh.queue.ChatCommandQueue;
import com.dh.queue.GMCommandQueue;
import com.dh.queue.LocalCommandQueue;
import com.dh.service.GMService;
import com.dh.service.PlayerService;
import com.dh.service.PlayerTimerService;
import com.dh.util.GameUtil;
import com.dh.util.Tool;
import com.dh.vo.user.UserCached;

@Component
@Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<NettyMessageVO> {
	public final static int MODE_NORMAL = 0;
	public final static int MODE_DEBUG = 1;
	public final static int MODE_STOP = 2;

	public static volatile int System_status = 1;

	private static Logger logger = Logger.getLogger(NettyServerHandler.class);
	public static int mode = MODE_NORMAL;
	// @Resource
	// private HandlerRegister handlerRegister;
	// @Resource
	// private AreaGroupService areaGroupService;
	@Resource
	PlayerTimerService playerTimerService;
	@Resource
	PlayerService playerService;
	@Resource
	private GMService gmService;

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
		// TODO Auto-generated method stub
		super.connect(ctx, remoteAddress, localAddress, promise);
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
		// TODO Auto-generated method stub
		super.disconnect(ctx, promise);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.netty.channel.ChannelInboundMessageHandlerAdapter#messageReceived(
	 * io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, NettyMessageVO nettyMessageVO) throws Exception {

		if (nettyMessageVO == null) {
			return;
		}

		nettyMessageVO.setChannel(ctx.channel());

		int playerid = ServerHandler.get(ctx.channel());
		if (playerid > 0) {
			UserCached userCached = ServerHandler.getUserCached(playerid);
			if (userCached != null) {
				userCached.setLastedAccessTime(System.currentTimeMillis());
			}
		}

		int CSCommondCode = nettyMessageVO.getCommandCode();
		if (CSCommondCode < 1000 || CSCommondCode >= 10000) {
			try {
				gmService.addBlackIP(GameUtil.getIpString(ctx.channel().remoteAddress().toString()), GameUtil.getIpString(ctx.channel().remoteAddress().toString()), "命令码超出正常范围",
						BLOCK_ENUM.BLOCK_IP_VALUE);
				logger.error("命令码:" + CSCommondCode + "异常IP(" + ctx.channel().remoteAddress() + ")访问,加入黑名单");
				ctx.channel().close();
				return;
			} catch (Exception e) {
				logger.error(Tool.concatString("ip捕获异常", e.getCause()), e);
				return;
			}
		}

		if (Tool.between(nettyMessageVO.getCommandCode(), 1100, 1199)) { // 100以内的特别指令
			GMCommandQueue.getInstance().put(nettyMessageVO);
			return;
		}

		if (LanjieTool.lanjie(nettyMessageVO)) {
			try {
				gmService.addBlackIP(GameUtil.getIpString(nettyMessageVO.getChannel().remoteAddress().toString()), GameUtil.getIpString(nettyMessageVO.getChannel().remoteAddress().toString()),
						"命令码超出正常范围", BLOCK_ENUM.BLOCK_ACC_IP_VALUE);
				logger.error("命令码:" + CSCommondCode + "异常IP(" + ctx.channel().remoteAddress() + ")访问,加入黑名单");
				ctx.channel().close();
				return;
			} catch (Exception e) {
				;
				logger.error(Tool.concatString("ip捕获异常", e.getCause()), e);
				return;
			}
		}

		if (System_status == 1) {
			return;
		}

		if (nettyMessageVO.getCommandCode() < 100) { // 100以内的特别指令
			LocalCommandQueue.getInstance().put(nettyMessageVO);
		} else if (Tool.between(nettyMessageVO.getCommandCode(), 9800, 9899)) {
			LocalCommandQueue.getInstance().put(nettyMessageVO);
		} else if (Tool.between(nettyMessageVO.getCommandCode(), 2100, 2299)) {
			ChatCommandQueue.getInstance().put(nettyMessageVO);
		} else {
			int n = ArrayCommandQueue.getMod(nettyMessageVO.getChannel().hashCode());
			ArrayCommandQueue.arrQueue[n].put(nettyMessageVO);
		}
		// else if (nettyMessageVO.getCommandCode() >= 9800 &&
		// nettyMessageVO.getCommandCode() <= 9899) {
		// LoginCommandQueue.getInstance().put(nettyMessageVO);
		// } else if (nettyMessageVO.getCommandCode() >= 9000 &&
		// nettyMessageVO.getCommandCode() <= 9099) { // 聊天和好友
		// // int userid = ServerHandler.get(nettyMessageVO.getChannel());
		// // nettyMessageVO.setUserid(userid);
		// LocalCommandQueue.getInstance().put(nettyMessageVO);
		// } else if (nettyMessageVO.getCommandCode() >= 5000 &&
		// nettyMessageVO.getCommandCode() <= 5099) {// 场景移动
		// MoveCommandQueue.getInstance().put(nettyMessageVO);
		// } else {
		// // if (mode == MODE_NORMAL) {
		// // processMsg(nettyMessageVO);
		// // } else if (specialProcessor(nettyMessageVO)) {
		// //
		// // } else {
		// // logger.debug("系统维护中");
		// // }
		// LocalCommandQueue.getInstance().put(nettyMessageVO);
		// }

	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.debug(Tool.concatString("用户", ctx.channel().remoteAddress(), "连接-->>"));
		try {
			if (GMConstants.isBlackIP(GameUtil.getIpString(ctx.channel().remoteAddress().toString()))) {
				ctx.channel().close();
			}
		} catch (Exception e) {
			logger.error(Tool.concatString("ip捕获异常", e.getCause()), e);
		}
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// ServerHandler.removePlayerChannelByChannel(ctx.channel());
		int playerid = ServerHandler.get(ctx.channel());
		System.out.println(Tool.concatString("用户(playerId=", playerid, ")关闭"));
		if (playerid > 0) {
			UserCached userCached = ServerHandler.getUserCached(playerid);
			playerTimerService.addOnlineReward(userCached);
			playerTimerService.flushKnaspack(userCached);
			userCached.getPlayerVO().setIs_online(0);
			playerService.updatePlayerOnline(userCached.getPlayerVO());
			ServerHandler.removeBossChannel(ctx.channel());
			ServerHandler.removeFromLegionChannel(userCached.getPlayerVO().getLegionId(), ctx.channel());
		}

		LanjieTool.clear(ctx.channel().remoteAddress().toString());
		// // 清理场景
		// if (userCached != null && userCached.getChannelGroup() != null) {
		// areaGroupService.areaLeaveBroadcast(userCached.getChannelGroup(),
		// userCached.getChannel(), playerid);
		// areaGroupService.clearChannel(userCached.getChannelGroup(),
		// userCached.getChannel());
		//
		// // 重置当前客户端 场景信息
		// userCached.getPlayerStrongHoldVO().setPosx(AreaOperationHandler.START_POS_X);
		// userCached.getPlayerStrongHoldVO().setPosy(AreaOperationHandler.START_POS_Y);
		// userCached.setChannelGroup(null);
		// }
		// }
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		try {
			super.exceptionCaught(ctx, cause);
			// NettyMessageVO nmv = new NettyMessageVO();
			// nmv.setCommandCode((short) -1);
			// ctx.channel().writeAndFlush(nmv);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			logger.error("" + e.getMessage(), e);
		}
	}

	// public void processMsg(NettyMessageVO nettyMessageVO) {
	// ICommandHandler commandHandler =
	// HandlerProcessor.getInstance().getCommandHandler(nettyMessageVO.getCommandCode());
	// List<NettyMessageVO> commandList = new ArrayList<NettyMessageVO>();
	// try {
	// if (commandHandler == null) {
	// throw new Exception("找不到handler或错误的指令码: " +
	// nettyMessageVO.getCommandCode());
	// } else {
	// commandHandler.handleMessage(nettyMessageVO, commandList);
	// for (NettyMessageVO tempMessage : commandList) {
	// logger.debug("发送数据指令 " + tempMessage.getCommandCode());
	// nettyMessageVO.getChannel().write(tempMessage);
	// }
	// }
	// } catch (GameException e) {
	// logger.error(e);
	// // 异常处理
	// NettyMessageVO alertNettyMessageVO = new NettyMessageVO();
	// ClientAlert.Builder clientAlert = ClientAlert.newBuilder();
	// clientAlert.setMsg(e.getMessage());
	// alertNettyMessageVO.setCommandCode(CSCommandConstant.ALERT_COMMAND);
	// alertNettyMessageVO.setData(clientAlert.build().toByteArray());
	// nettyMessageVO.getChannel().write(alertNettyMessageVO);
	// } catch (Exception e) {
	// logger.error(e);
	// } finally {
	// commandList.clear();
	// commandList = null;
	// }
	//
	// }
}
