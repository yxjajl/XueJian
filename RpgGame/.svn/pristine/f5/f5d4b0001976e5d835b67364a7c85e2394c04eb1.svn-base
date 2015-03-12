package com.dh.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dh.main.InitLoad;

@Component
public class NettyServer {

	@Resource
	private InitLoad initLoad;
	@Resource
	private NettyServerInitializer nettyServerInitializer;

	public void run(int port) throws Exception {

		initLoad.init();
		// ServerBootstrap b = new ServerBootstrap();
		// try {
		// b.group(new NioEventLoopGroup(), new NioEventLoopGroup());
		// b.option(ChannelOption.TCP_NODELAY, true);
		// b.option(ChannelOption.SO_KEEPALIVE, true);
		// b.channel(NioServerSocketChannel.class);
		// b.localAddress(port);
		// b.childHandler(nettyServerInitializer);
		// b.bind().sync().channel().closeFuture().sync();
		// } finally {
		// b.shutdown();
		// }

		// final SslContext sslCtx;
		// if (SSL) {
		// SelfSignedCertificate ssc = new SelfSignedCertificate();
		// sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
		// } else {
		// sslCtx = null;
		// }

		// EventLoopGroup bossGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
		// EventLoopGroup workerGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();

		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO)).childHandler(nettyServerInitializer);

			// Bind and start to accept incoming connections.
			ChannelFuture f = b.bind(port).sync();

			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to gracefully
			// shut down your server.
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
			// workerGroup.shutdown();
			// bossGroup.shutdown();
		}

	}
}
