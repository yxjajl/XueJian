package com.dh.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
	@Resource
	private NettyServerHandler nettyServerHandler;

	// private boolean isInit = false;

	public NettyServerInitializer() {

	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		p.addLast("frameDecoder", new MyMessageDecoder());
		p.addLast("frameEncoder", new MyMessageEncoder());
		p.addLast("handler", nettyServerHandler);
	}

}
