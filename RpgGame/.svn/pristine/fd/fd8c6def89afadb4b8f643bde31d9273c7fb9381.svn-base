package com.dh.service;

import org.springframework.stereotype.Component;

@Component
public class AreaGroupService {
//	private final static int GROUP_MAX_SIZE = 10;
//	@Resource
//	private PlayerCityMapper playerCityMapper;
//	@Resource
//	private SqlBuild sqlBuild;
//	@Resource
//	private SqlSaveThread sqlSaveThread;
//
//	public final static TIntObjectMap<TIntObjectMap<ChannelGroup>> CITY_GROUPID_CHANNELGROUP_MAP = new TIntObjectHashMap<TIntObjectMap<ChannelGroup>>();
//
//	public void loadPlayerCity(UserCached userCached) {
//		PlayerStrongHoldVO playerStrongHoldVO = playerCityMapper.getPlayerCity(userCached.getPlayerId());
//		userCached.setPlayerStrongHoldVO(playerStrongHoldVO);
//	}
//
//	public void updatePlayerStrongHoldVO(PlayerStrongHoldVO playerStrongHoldVO) {
//		sqlSaveThread.putSql(sqlBuild.getSql("com.dh.dao.PlayerCityMapper.updatePlayerCity", playerStrongHoldVO));
//	}
//
//	/**
//	 * 开启新城市
//	 * 
//	 * @param userCached
//	 * @param commandList
//	 */
//	public void appendCity(UserCached userCached, int taskid, List<NettyMessageVO> commandList) {
//		int newCity = CityRes.getInstance().getCityIdByOpenTask(taskid);
//		if (newCity != 0 && userCached.getPlayerStrongHoldVO().getOpencity().indexOf("," + newCity) < 0) {
//			userCached.getPlayerStrongHoldVO().setOpencity(userCached.getPlayerStrongHoldVO().getOpencity() + "," + newCity);
//			updatePlayerStrongHoldVO(userCached.getPlayerStrongHoldVO());
////			commandList.add(CommandUtil.packagePlayerCity(userCached));
//		}
//	}
//
//	/**
//	 * 将用户加入到指定城市的空闲群组中去
//	 * 
//	 * @param city
//	 * @param channel
//	 * @return
//	 */
//	public synchronized ChannelGroup enterCity(int city, Channel channel) {
//		ChannelGroup result = findFreeCityGroupMap(city);
//		result.add(channel);
//		return result;
//	}
//
//	/**
//	 * 找一个人数有空闲的群组，如果没有，则创建新的群组
//	 * 
//	 * @param city
//	 * @return
//	 */
//	public ChannelGroup findFreeCityGroupMap(int city) {
//		ChannelGroup result = null;
//		TIntObjectMap<ChannelGroup> cityGroupMap = CITY_GROUPID_CHANNELGROUP_MAP.get(city);
//		if (cityGroupMap != null) {
//			ChannelGroup tempChannelGroup = null;
//			for (Integer groupId : cityGroupMap.keys()) {
//				tempChannelGroup = cityGroupMap.get(groupId);
//				if (tempChannelGroup.size() >= GROUP_MAX_SIZE) {
//					continue;
//				} else {
//					result = tempChannelGroup;
//				}
//			}
//		}
//
//		if (result == null) {
//			int n = 1;
//			if (cityGroupMap != null) {
//				n = cityGroupMap.size() + 1;
//			} else {
//				cityGroupMap = new TIntObjectHashMap<ChannelGroup>();
//				CITY_GROUPID_CHANNELGROUP_MAP.put(city, cityGroupMap);
//			}
//			result = new DefaultChannelGroup();
//			cityGroupMap.put(n, result);
//
//		}
//
//		return result;
//	}
//
//	/**
//	 * 清理Channel
//	 * 
//	 * @param channelGroup
//	 * @param channel
//	 */
//	public synchronized void clearChannel(ChannelGroup channelGroup, Channel channel) {
//		channelGroup.remove(channel);
//	}
//
//	/**
//	 * 广播消息 (不含自己)
//	 * 
//	 * @param channelGroup
//	 * @param nettyMessageVO
//	 */
//	public void broadMessage(ChannelGroup channelGroup, NettyMessageVO nettyMessageVO, int channel_id) {
//		if (channelGroup != null) {
//			for (Channel c : channelGroup) {
//				if (c != null && (c.id() != channel_id) && c.isActive())
//					c.write(nettyMessageVO);
//			}
//		}
//	}
//
//	/**
//	 * 进入场景广播
//	 * 
//	 * @param userCached
//	 */
//	public void areaEnterBroadcast(UserCached userCached) {
//		AreaOperationEnterResponse.Builder builder = AreaOperationEnterResponse.newBuilder();
//
//		NettyMessageVO enterMessageVO = new NettyMessageVO();
//		enterMessageVO.setCommandCode(CSCommandConstant.AREA_BROACAST_ENTER);
//
//		builder.addAreaEnterBroadcast(getEnterBroadcastInfo(userCached));
//		enterMessageVO.setData(builder.build().toByteArray());
//
//		broadMessage(userCached.getChannelGroup(), enterMessageVO, userCached.getChannel());
//
//	}
//
//	/**
//	 * 打包场景channelGroup内所有玩家数据
//	 * 
//	 * @param channelGroup
//	 * @return
//	 */
//	public NettyMessageVO getAreaPlayerData(ChannelGroup channelGroup) throws Exception {
//		AreaOperationEnterResponse.Builder builder = AreaOperationEnterResponse.newBuilder(); // GROUP_MAX_SIZE
//		if (channelGroup != null && channelGroup.size() != 0) {
//			for (Channel ch : channelGroup) {
//				// 过滤掉自己
//				// if (channel.getId().intValue() == ch.getId().intValue()) {
//				// continue;
//				// }
//				int playerId = ServerHandler.get(ch.id());
//				UserCached tempUserCached = ServerHandler.getUserCached(playerId);
//				builder.addAreaEnterBroadcast(getEnterBroadcastInfo(tempUserCached));
//			}
//		} else {
//			return null;
//		}
//		NettyMessageVO nettyMessageVO = new NettyMessageVO();
//		nettyMessageVO.setCommandCode(CSCommandConstant.AREA_BROACAST_ENTER);
//		nettyMessageVO.setData(builder.build().toByteArray());
//		return nettyMessageVO;
//	}
//
//	/**
//	 * 打完玩家信息
//	 * 
//	 * @param userCached
//	 * @return
//	 */
//	private AreaEnterBroadcast getEnterBroadcastInfo(UserCached userCached) {
//		AreaEnterBroadcast.Builder builder = AreaEnterBroadcast.newBuilder();
//		builder.setPlayerId(userCached.getPlayerId());
//		builder.setPosx(userCached.getPlayerStrongHoldVO().getPosx());
//		builder.setPosy(userCached.getPlayerStrongHoldVO().getPosy());
//		builder.setName(userCached.getPlayerVO().getName());
//		builder.setFactionName("");
//		builder.setHeadIcon(userCached.getPlayerVO().getHeadIcon());
//		return builder.build();
//	}
//
//	/**
//	 * 离开指定场景中的指定组广播
//	 * 
//	 * 
//	 * @param leaveAreaId
//	 * @param broadcastChGroup
//	 * @param myChannel
//	 * @param playerId
//	 */
//	public void areaLeaveBroadcast(ChannelGroup broadcastChGroup, Channel myChannel, int playerId) {
//		if (broadcastChGroup == null || myChannel == null) {
//			return;
//		}
//
//		AreaOperationLeaveResponse.Builder builder = AreaOperationLeaveResponse.newBuilder();
//		builder.setPlayerId(playerId);
//
//		NettyMessageVO leaveMessageVO = new NettyMessageVO();
//		leaveMessageVO.setCommandCode(CSCommandConstant.AREA_BROACAST_LEAVE);
//		leaveMessageVO.setData(builder.build().toByteArray());
//		broadMessage(broadcastChGroup, leaveMessageVO, myChannel.id());
//	}
//
//	/**
//	 * 封装广播数据
//	 * 
//	 * @param action
//	 * @param posx
//	 * @param posy
//	 * @param look
//	 * @return
//	 */
//	public void areaMoveBroadcast(int playerId, short targetX, short targetY, ChannelGroup broadcastChGroup, Channel myChannel) {
//		AreaOperationMoveResponse.Builder builder = AreaOperationMoveResponse.newBuilder();
//		builder.setPlayerId(playerId);
//		builder.setTargetx(targetX);
//		builder.setTargety(targetY);
//		NettyMessageVO moveMessageVO = new NettyMessageVO();
//		moveMessageVO.setCommandCode(CSCommandConstant.AREA_BROACAST_MOVE);
//		moveMessageVO.setData(builder.build().toByteArray());
//		broadMessage(broadcastChGroup, moveMessageVO, myChannel.id());
//	}
//
//	public void print() {
//		System.out.println("CITY_GROUPID_CHANNELGROUP_MAP.size " + CITY_GROUPID_CHANNELGROUP_MAP.size());
//		for (int value : CITY_GROUPID_CHANNELGROUP_MAP.keys()) {
//			System.out.println("city[" + value + "] : " + CITY_GROUPID_CHANNELGROUP_MAP.get(value).size());
//		}
//	}
//
//	public static void main(String[] args) {
//		AreaGroupService areaGroupService = new AreaGroupService();
//		areaGroupService.enterCity(1, null);
//	}

}
