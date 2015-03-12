package com.dh.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.dh.netty.MyMessageDecoder;
import com.dh.netty.MyMessageEncoder;
import com.dh.netty.NettyMessageVO;
import com.dh.s2s.queue.BaseQueue;

/**
 * Sends one message when a connection is open and echoes back any received data
 * to the server. Simply put, the echo client initiates the ping-pong traffic
 * between the echo client and server by sending the first message to the
 * server.
 */

public class GMClient {

	private String host = "";
	private final int port = 9090;
	private Channel sendchannel;
	private BaseQueue<NettyMessageVO> queues;

	public GMClient(String host, BaseQueue<NettyMessageVO> queues) {
		this.queues = queues;
		this.host = host;
	}

	public static void main(String[] args) throws Exception {
		// final String host = "211.149.218.52";

		// GMClient ec = new GMClient(host, port);
		// ec.run();

		// LoginHandler loginHandler = new LoginHandler();
		// loginHandler.loginDisPlay(ec);
		// Thread.sleep(300);

	}

	public void run() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			System.err.println(host);
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new MyChannelInitializer_GM(this));

			ChannelFuture f = b.connect(host, port).sync();
			sendchannel = f.channel();
		} finally {

			// group.shutdown();
		}
	}

	public static NettyMessageVO getNettyMessageVO(short commandid, byte[] temp) {
		NettyMessageVO builder = new NettyMessageVO();
		builder.setCommandCode(commandid);
		builder.setData(temp);
		return builder;
	}

	public boolean sendMessage(short commandid, byte[] temp) {
		if (sendchannel != null && sendchannel.isActive()) {
			sendchannel.write(getNettyMessageVO(commandid, temp));
			sendchannel.flush();
			return true;
		} else {
			return false;
		}
	}

	public BaseQueue<NettyMessageVO> getQueues() {
		return queues;
	}

	public void setQueues(BaseQueue<NettyMessageVO> queues) {
		this.queues = queues;
	}

	public Channel getSendchannel() {
		return sendchannel;
	}

	public void setSendchannel(Channel sendchannel) {
		this.sendchannel = sendchannel;
	}

}

class MyChannelInitializer_GM extends ChannelInitializer<SocketChannel> {
	private GMClient echoClient;

	public MyChannelInitializer_GM(GMClient ec) {
		echoClient = ec;
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast("protobufDecoder", new MyMessageDecoder());
		ch.pipeline().addLast("protobufEncoder", new MyMessageEncoder());
		ch.pipeline().addLast("handler", new TestServerHandler_GM(echoClient));
	}
}

class TestServerHandler_GM extends SimpleChannelInboundHandler<NettyMessageVO> {
	private GMClient echoClient;

	public TestServerHandler_GM(GMClient ec) {
		echoClient = ec;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, NettyMessageVO nettyMessageVO) throws Exception {
		System.out.println("===messageReceived===:" + ctx.channel().remoteAddress());
		System.out.println("commandcode: " + nettyMessageVO.getCommandCode());
		nettyMessageVO.setChannel(ctx.channel());
		echoClient.getQueues().put(nettyMessageVO);
		// if (CSCommandConstant.USER_LOGIN == nettyMessageVO.getCommandCode())
		// {
		// PlayerLoginResponse resp =
		// PlayerLoginResponse.parseFrom(nettyMessageVO.getData());
		// if (resp.getResult() == EXIST_STATUS.PLAYER_EXIST) {
		// new DisplayerHandler().display(resp);
		// }
		//
		// }
	}

	public GMClient getEchoClient() {
		return echoClient;
	}

	public void setEchoClient(GMClient echoClient) {
		this.echoClient = echoClient;
	}
}
