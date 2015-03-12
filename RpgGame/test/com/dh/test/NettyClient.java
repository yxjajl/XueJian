package com.dh.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class NettyClient {
	final static ByteBuf delimiter = Unpooled.copiedBuffer("\0".getBytes());

	public void connect(int port, String host) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioServerSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter)).addLast(new StringDecoder()).addLast(new NettyClientInHander()).addLast(new NettyClientOutHander());
			}
		});
		ChannelFuture future = b.connect(host, port).sync();
		future.channel().closeFuture().sync();
	}

	public static void main(String[] args) throws InterruptedException {
		new NettyClient().connect(843, "127.0.0.1");
	}
}

class NettyClientOutHander extends ChannelHandlerAdapter {

	protected void flush(ChannelHandlerContext ctx, ByteBuf buf, ChannelPromise arg2) throws Exception {
		ByteBuf b = Unpooled.copiedBuffer(("<cross-domain-policy> " + '\0').getBytes());
		for (int i = 0; i < 10; i++) {
			ctx.write(b);
		}
		ctx.flush();

	}

	public void freeOutboundBuffer(ChannelHandlerContext ctx) throws Exception {
	}

}

class NettyClientInHander extends SimpleChannelInboundHandler<String> {

	@Override
	protected void messageReceived(ChannelHandlerContext arg0, String arg1) throws Exception {
		System.err.println(arg1);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

}
