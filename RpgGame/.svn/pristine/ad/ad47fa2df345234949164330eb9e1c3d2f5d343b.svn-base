package com.dh.queue;

import com.dh.netty.NettyMessageVO;
import com.dh.s2s.queue.BaseQueue;

public class ClientCommandQueue extends BaseQueue<NettyMessageVO> {
	private static ClientCommandQueue INSTANCE = new ClientCommandQueue();

	private ClientCommandQueue() {
	}

	public static ClientCommandQueue getInstance() {
		return INSTANCE;
	}
}
