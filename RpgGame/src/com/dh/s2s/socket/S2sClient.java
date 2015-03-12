package com.dh.s2s.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.dh.netty.NettyMessageVO;
import com.dh.s2s.vo.ServerVO;

public class S2sClient {

//	private int port;
//	private String host;
//	private Channel channel;
//
//	public S2sClient(ServerVO serverVO) {
//		this.host = serverVO.host;
//		this.port = serverVO.port;
//	}
//
//	public synchronized void init() {
//		if(channel != null) {
//			return ;
//		}
//		EventLoopGroup group = new NioEventLoopGroup();
//		try {
//
//			Bootstrap b = new Bootstrap();
//			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
//				@Override
//				public void initChannel(SocketChannel ch) throws Exception {
//					ch.pipeline().addLast("protobufDecoder", new S2sMessageDecoder());
//					ch.pipeline().addLast("protobufEncoder", new S2sMessageEncoder());
//					ch.pipeline().addLast("handler", new S2sServerHandler());
//				}
//			});
//
//			// Start the client.
//			ChannelFuture f = b.connect(host, port).sync();
//			channel = f.channel();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	public boolean isInActive() {
//		return channel == null;
//	}
//
//	public void sendMessage(NettyMessageVO nettyMessageVO) {
//		if (channel != null) {
//			channel.write(nettyMessageVO);
//		}
//	}

}
