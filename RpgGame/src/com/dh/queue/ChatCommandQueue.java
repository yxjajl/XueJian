package com.dh.queue;

import com.dh.netty.NettyMessageVO;
import com.dh.s2s.queue.BaseQueue;

public class ChatCommandQueue extends BaseQueue<NettyMessageVO> {
	private static ChatCommandQueue INSTANCE = new ChatCommandQueue();

	private ChatCommandQueue() {
	}

	public static ChatCommandQueue getInstance() {
		return INSTANCE;
	}
}