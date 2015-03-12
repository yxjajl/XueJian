package com.dh.queue;

import com.dh.netty.NettyMessageVO;
import com.dh.s2s.queue.BaseQueue;

public class ArrayCommandQueue {
	private final static int MAXSIZE1 = 7;

	@SuppressWarnings("unchecked")
	public static BaseQueue<NettyMessageVO>[] arrQueue = new BaseQueue[MAXSIZE1 + 1];// 需要是2的倍数

	static {
		for (int i = 0; i < arrQueue.length; i++) {
			arrQueue[i] = new BaseQueue<NettyMessageVO>();
		}
	}

	public static int getMod(int value) {
		if (value < 0) {
			value *= -1;
		}
		return MAXSIZE1 & value;
	}

	public static void main(String[] args) {
		for(int i = -100;i < 100; i ++) 
		System.out.println(ArrayCommandQueue.getMod(-7));
	}
}
