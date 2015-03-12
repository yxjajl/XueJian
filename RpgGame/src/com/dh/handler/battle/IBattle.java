package com.dh.handler.battle;

import java.util.List;

import com.dh.game.vo.raid.RaidProto.RaidEndRequest;
import com.dh.game.vo.raid.RaidProto.RaidPrepareRequest;
import com.dh.game.vo.raid.RaidProto.RaidStartRequest;
import com.dh.netty.NettyMessageVO;
import com.dh.vo.user.UserCached;

public interface IBattle {

	public void battleDetail(RaidPrepareRequest request, UserCached userCached, List<NettyMessageVO> commandList) throws Exception; // 战前数据详情

	public void battleStart(UserCached userCached, RaidStartRequest request, List<NettyMessageVO> commandList) throws Exception;// 战斗开始前处理

	public void battleEnd(UserCached userCached, RaidEndRequest request, List<NettyMessageVO> commandList) throws Exception; // 战斗结束处理
}
