package com.dh.handler.battle;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;

@Component
public class BattleHandler implements ICommandHandler {
//	private static Logger logger = Logger.getLogger(BattleHandler.class);
	
	
	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		
	}
}
