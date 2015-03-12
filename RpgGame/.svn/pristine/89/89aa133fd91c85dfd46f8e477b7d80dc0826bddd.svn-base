package com.dh.test;

import java.util.List;

import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.raid.RaidProto.RaidStartResponse;
import com.dh.netty.NettyMessageVO;

public class ParseData {
	// public static void FriendList(NettyMessageVO nettyMessageVO) throws Exception {
	// if (CSCommandConstant.FRIEND_LIST_DW != nettyMessageVO.getCommandCode()) {
	// return;
	// }
	// }

	public static void RaidStart(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		RaidStartResponse response = RaidStartResponse.parseFrom(nettyMessageVO.getData());

		NettyMessageVO message = new NettyMessageVO();
		message.setCommandCode(CSCommandConstant.RAID_END);
		message.setData(TestData.RaidEnd(response.getRaidid(), response.getValidateKey()));
		commandList.add(message);
	}

}
