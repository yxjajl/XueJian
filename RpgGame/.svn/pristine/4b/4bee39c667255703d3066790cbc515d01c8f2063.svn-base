package com.dh.handler.raid;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.raid.ManyPeopleProto.CreateManyPeopleTeamReq;
import com.dh.game.vo.raid.ManyPeopleProto.ManyPeopleListRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.ManyPeopleTeamProcessor;

/**
 * 多人副本组队
 * 
 * @author RickYu
 * 
 */
@Component
public class ManyPeopleTeamHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(ManyPeopleTeamHandler.class);
	@Resource
	private ManyPeopleTeamProcessor manyPeopleTeamProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.LIST_MANYPEOPLE_TEAM:// 队伍列表
			getRaidTeamList(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.CREATE_MANYPEOPLE_TEAM: // 创建
			createTeam(nettyMessageVO, commandList);
			break;
		case CSCommandConstant.LEAVE_MANYPEOPLE_TEAM: // 离开队伍
			break;
		case CSCommandConstant.JOIN_MANYPEOPLE_TEAM: // 加入队伍
			break;
		case CSCommandConstant.KICKOUT_MANYPEOPLE_TEAM: // 踢人
			break;
		default:
			logger.error("error Commandcode " + nettyMessageVO.getCommandCode());
			break;
		}

	}

	/**
	 * 队伍列表
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void getRaidTeamList(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {

		ManyPeopleListRequest request = null;
		try {
			request = ManyPeopleListRequest.parseFrom(nettyMessageVO.getData());
			manyPeopleTeamProcessor.getManyPeopleteamList(request, nettyMessageVO, commandList);
		} catch (Exception e) {
			logger.error("数据解析异常", e);
			throw e;
		}

	}

	/**
	 * 创建队伍
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void createTeam(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {

		CreateManyPeopleTeamReq request = null;
		try {
			request = CreateManyPeopleTeamReq.parseFrom(nettyMessageVO.getData());
			manyPeopleTeamProcessor.createTeam(request, nettyMessageVO, commandList);
		} catch (Exception e) {
			logger.error("数据解析异常", e);
			throw e;
		}

	}

}
