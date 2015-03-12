package com.dh.queue;

import com.dh.netty.NettyMessageVO;
import com.dh.s2s.queue.BaseQueue;

public class LocalCommandQueue extends BaseQueue<NettyMessageVO> {
	private static LocalCommandQueue INSTANCE = new LocalCommandQueue();

	private LocalCommandQueue() {
	}

	public static LocalCommandQueue getInstance() {
		return INSTANCE;
	}
}
