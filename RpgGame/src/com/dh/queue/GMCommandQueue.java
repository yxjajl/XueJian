package com.dh.queue;

import com.dh.netty.NettyMessageVO;
import com.dh.s2s.queue.BaseQueue;

public class GMCommandQueue extends BaseQueue<NettyMessageVO> {
	private static GMCommandQueue INSTANCE = new GMCommandQueue();

	private GMCommandQueue() {
	}

	public static GMCommandQueue getInstance() {
		return INSTANCE;
	}
}
