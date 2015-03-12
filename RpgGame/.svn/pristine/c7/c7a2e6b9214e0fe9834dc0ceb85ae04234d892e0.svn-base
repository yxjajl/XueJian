package com.dh.processor;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.constants.ManyPeopleRaidConstants;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.raid.ManyPeopleProto.CreateManyPeopleTeamReq;
import com.dh.game.vo.raid.ManyPeopleProto.ManyPeopleListRequest;
import com.dh.game.vo.raid.ManyPeopleProto.ManyPeopleListResp;
import com.dh.netty.NettyMessageVO;
import com.dh.util.CommandUtil;
import com.dh.vo.user.UserCached;

@Component
public class ManyPeopleTeamProcessor {

	/**
	 * 队伍列表
	 * 
	 * @param request
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void getManyPeopleteamList(ManyPeopleListRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		ManyPeopleListResp.Builder build = ManyPeopleRaidConstants.getManyPeopleListResp(ManyPeopleRaidConstants.teamList(request.getRaidid()));
		CommandUtil.packageNettyMessage(CSCommandConstant.LIST_MANYPEOPLE_TEAM, build.build().toByteArray(), commandList);
	}

	public void createTeam(CreateManyPeopleTeamReq request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		ManyPeopleRaidConstants.createTeam(request.getRaidid(), request.getReqLevel(), request.getIsAutoStart(), request.getPassword(), userCached.getPlayerVO());
		// CommandUtil.packageNettyMessage(CSCommandConstant.CREATE_MANYPEOPLE_TEAM, build.build().toByteArray(), commandList);
	}
}
