package com.dh.s2s.queue;

import com.dh.netty.NettyMessageVO;

public class CommandQueue extends BaseQueue<NettyMessageVO> {
	private static CommandQueue INSTANCE = new CommandQueue();

	private CommandQueue() {
	}

	public static CommandQueue getInstance() {
		return INSTANCE;
	}
}
