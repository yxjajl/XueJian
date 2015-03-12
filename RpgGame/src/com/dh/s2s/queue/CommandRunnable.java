package com.dh.s2s.queue;

import java.util.ArrayList;
import java.util.List;

import com.dh.netty.NettyMessageVO;
import com.dh.s2s.socket.S2sClient;

/**
 * 执行消息队列
 * 
 */
public class CommandRunnable extends AbstractCommandRunnable {

	private List<S2sClient> serverList = new ArrayList<S2sClient>();

	public CommandRunnable(BaseQueue<NettyMessageVO> baseQueue,String name) {
		super(baseQueue,name);
	}

	@Override
	public void sendMessage(NettyMessageVO nettyMessageVO) {
		for (S2sClient s2sClient : serverList) {
			System.out.println("===sendMessage= to otherserver==");
			sendMessage(s2sClient, nettyMessageVO);
		}
	}

	public void sendMessage(S2sClient s2sClient, NettyMessageVO nettyMessageVO) {
//		if (s2sClient.isInActive()) {
//			s2sClient.init();
//		}
//		s2sClient.sendMessage(nettyMessageVO);
	}

	public void addServers(S2sClient s2sClient) {
		serverList.add(s2sClient);
	}

}
