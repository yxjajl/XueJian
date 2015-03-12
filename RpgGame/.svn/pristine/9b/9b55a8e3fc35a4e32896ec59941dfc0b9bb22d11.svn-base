package com.dh.handler.area;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;

@Component
public class AreaOperationHandler implements ICommandHandler {
	// private static Logger LOGGER =
	// Logger.getLogger(AreaOperationHandler.class);
	public final static int START_POS_X = 150;
	public final static int START_POS_Y = 380;

	//
	// @Resource
	// private AreaGroupService areaGroupService;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		// AreaOperationRequest areaOperationRequest = null;
		//
		// try {
		// areaOperationRequest =
		// AreaOperationRequest.parseFrom(nettyMessageVO.getData());
		// } catch (InvalidProtocolBufferException e) {
		// LOGGER.error("数据解析错误,error! ,commandCode:" +
		// nettyMessageVO.getCommandCode(), e);
		// throw new GameException("AreaOperationHandler异常");
		// }
		//
		// if (areaOperationRequest != null) {
		//
		// // if
		// (CityRes.getInstance().getCityByCfgID(areaOperationRequest.getAreaid())
		// == null) {
		// // throw new GameException("不存在的地图id " +
		// areaOperationRequest.getAreaid());
		// // }
		//
		// Channel myChannel = nettyMessageVO.getChannel();
		// int playerId = ServerHandler.get(myChannel.id());
		// // System.out.println("进入地图 : " + playerId + "," +
		// areaOperationRequest.getAreaid());
		// UserCached userCached = ServerHandler.getUserCached(playerId);
		// PlayerStrongHoldVO playerStrongHoldVO =
		// userCached.getPlayerStrongHoldVO();
		// if (AreaOperationProto.AreaOperation.AREA_ENTER ==
		// areaOperationRequest.getOperation()) {
		//
		// if (userCached.getChannelGroup() != null) {
		// areaGroupService.areaLeaveBroadcast(userCached.getChannelGroup(),
		// userCached.getChannel(), userCached.getPlayerId());
		// areaGroupService.clearChannel(userCached.getChannelGroup(),
		// userCached.getChannel());
		//
		// // 重置当前客户端 场景信息
		// playerStrongHoldVO.setPosx(START_POS_X);
		// playerStrongHoldVO.setPosy(START_POS_Y);
		// userCached.setChannelGroup(null);
		// }
		//
		// int cityId = areaOperationRequest.getAreaid();
		//
		// // 广播给组内其他玩家，当前客户端进入
		// ChannelGroup broadcastChGroup = areaGroupService.enterCity(cityId,
		// myChannel);
		//
		// // System.out.println("broadcastChGroup join " + playerId + "," +
		// broadcastChGroup.getName());
		//
		// if (playerStrongHoldVO.getPosx() == 0 || playerStrongHoldVO.getPosy()
		// == 0 || playerStrongHoldVO.getStronghold() != cityId) {
		// playerStrongHoldVO.setPosx(START_POS_X);
		// playerStrongHoldVO.setPosy(START_POS_Y);
		// }
		// int oldCity = userCached.getPlayerStrongHoldVO().getStronghold();
		// playerStrongHoldVO.setPosx(playerStrongHoldVO.getPosx());
		// playerStrongHoldVO.setPosy(playerStrongHoldVO.getPosy());
		// playerStrongHoldVO.setStronghold(cityId);
		// userCached.setChannelGroup(broadcastChGroup);
		//
		// areaGroupService.areaEnterBroadcast(userCached);
		// if(cityId != oldCity) {
		// areaGroupService.updatePlayerStrongHoldVO(userCached.getPlayerStrongHoldVO());
		// }
		//
		// // 将当前组内其他玩家的形象及位置信息,返回给当前客户端
		// NettyMessageVO tempMessageVO =
		// areaGroupService.getAreaPlayerData(broadcastChGroup);
		// if (tempMessageVO != null) {
		// commandList.add(tempMessageVO);
		// }
		//
		// } else if (AreaOperationProto.AreaOperation.AREA_LEAVE ==
		// areaOperationRequest.getOperation()) {
		//
		// areaGroupService.areaLeaveBroadcast(userCached.getChannelGroup(),
		// userCached.getChannel(), userCached.getPlayerId());
		// areaGroupService.clearChannel(userCached.getChannelGroup(),
		// userCached.getChannel());
		//
		// // 重置当前客户端 场景信息
		// playerStrongHoldVO.setPosx(START_POS_X);
		// playerStrongHoldVO.setPosy(START_POS_Y);
		// userCached.setChannelGroup(null);
		// } else if (AreaOperationProto.AreaOperation.AREA_LEAVE_AND_ENTER ==
		// areaOperationRequest.getOperation()) {
		//
		// } else if (AreaOperationProto.AreaOperation.AREA_MOVE ==
		// areaOperationRequest.getOperation()) {
		// short targetX = (short) areaOperationRequest.getPosx();
		// short targetY = (short) areaOperationRequest.getPosy();
		// areaGroupService.areaMoveBroadcast(playerId, targetX, targetY,
		// userCached.getChannelGroup(), userCached.getChannel());
		// playerStrongHoldVO.setPosx(targetX);
		// playerStrongHoldVO.setPosy(targetY);
		// //
		// areaGroupService.updatePlayerStrongHoldVO(userCached.getPlayerStrongHoldVO());
		// }
		// } else {
		// throw new GameException("场景操作参数错误");
		// }
	}
}