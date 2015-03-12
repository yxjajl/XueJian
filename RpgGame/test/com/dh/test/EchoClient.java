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

import java.util.ArrayList;

import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.message.MessageProto.RewardedResponse;
import com.dh.netty.MyMessageDecoder;
import com.dh.netty.MyMessageEncoder;
import com.dh.netty.NettyMessageVO;

/**
 * Sends one message when a connection is open and echoes back any received data
 * to the server. Simply put, the echo client initiates the ping-pong traffic
 * between the echo client and server by sending the first message to the
 * server.
 */
public class EchoClient {

	private final String host;
	private final int port;
	private Channel sendchannel;

	public EchoClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public static void main(String[] args) throws Exception {

		// Parse options.
		final String host = "119.29.13.83"; // 211.149.218.52
											// 211.149.229.104
		// final String host = "211.149.218.52"; //外网测试服
		// final String host = "182.254.184.29"; // 正式服
		// final String host = "203.195.173.232"; // 正式服
		final int port = 8000;
		EchoClient ec = new EchoClient(host, port);
		ec.run();

		String userNname_v = "yxjtest";

		// ec.sendMessage((short) 2, null);
		ec.sendMessage((short) -2, null);
		ec.sendMessage((short) CSCommandConstant.USER_LOGIN, TestData.LoginData(userNname_v, "11"));
		// ec.sendMessage((short) 3574, "1".getBytes());
		// ec.sendMessage((short) CSCommandConstant.USER_LOGIN, aa);
		// ec.sendMessage((short) CSCommandConstant.CMD_MAIL_REWARD,
		// TestData.getMailReward());
		// ec.sendMessage((short) CSCommandConstant.RECHARGE_TEST,
		// TestData.rechargeTest(1237448, 101));

		// ec.sendMessage((short) CSCommandConstant.ITEM_USE,
		// TestData.ItemUse(32, 10));

		// ec.sendMessage((short) CSCommandConstant.ITEM_SELL,
		// TestData.sellItem(7, -300000000));

		// ec.sendMessage((short) CSCommandConstant.SHOP_GOLDINGOT_REQ,
		// TestData.getUrlParameter());
		// Thread.sleep(300);
		// ec.sendMessage((short)
		// CSCommandConstant.GM_RELOAD_REDIS,TestData.ffff3());
		// ec.sendMessage((short) CSCommandConstant.SYS_STOP,
		// "admin".getBytes());
		// ec.sendMessage((short) CSCommandConstant.RELOAD_CSV,
		// "JJCRankRes".getBytes());
		// ec.sendMessage((short) CSCommandConstant.RELOAD_CSV,
		// "JJCShopRes".getBytes());
		// ec.sendMessage((short) CSCommandConstant.RELOAD_CSV,
		// "JJCSuccRes".getBytes());
		// ec.sendMessage((short) CSCommandConstant.RECHARGE_RMB,
		// TestData.Recharge(1582453, 2000));
		//
		// ec.sendMessage((short) CSCommandConstant.YUZHENUI, null);
		// ec.sendMessage((short) CSCommandConstant.YUZHENDETAIL,
		// TestData.YuanZhenData(1));

		// ec.sendMessage((short) CSCommandConstant.RAID_START,
		// TestData.LoginData(userNname_v, ""));
		// ec.sendMessage((short) CSCommandConstant.SYS_RELOAD_SCRIPT, null);

		// ec.sendMessage((short) CSCommandConstant.STREET_OPEN,
		// TestData.opendData(31));

		// ec.sendMessage((short) CSCommandConstant.STREET_HUNT,
		// TestData.huntData(31));

		// required int32 resId = 1;
		// repeated int32 heroId = 2;
		// repeated int32 machineId = 3;
		// optional int32 fzId = 4;
		// ec.sendMessage((short) CSCommandConstant.TASK_REWARD,
		// TestData.ReceiveTask(1));
		ec.flush();

	}

	public void run() throws Exception {
		// Configure the client.
		EventLoopGroup group = new NioEventLoopGroup();
		try {

			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new MyChannelInitializer(this));

			// Start the client.
			ChannelFuture f = b.connect(host, port).sync();
			sendchannel = f.channel();
			// f.channel().closeFuture().sync();
			// f.channel().close();
		} finally {
			// group.shutdownGracefully();
			// Shut down the event loop to terminate all threads.
			// group.shutdown();
		}
	}

	public void send1() {

	}

	public void send2() {

	}

	public static NettyMessageVO getNettyMessageVO(short commandid, byte[] temp) {
		NettyMessageVO builder = new NettyMessageVO();
		builder.setCommandCode(commandid);
		builder.setData(temp);
		return builder;
	}

	public void sendMessage(short commandid, byte[] temp) {
		sendchannel.write(getNettyMessageVO(commandid, temp));

		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public void flush() {
		sendchannel.flush();
	}

}

class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
	private EchoClient echoClient;

	public MyChannelInitializer(EchoClient ec) {
		echoClient = ec;
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		// ch.pipeline().addLast("frameDecoder", new
		// ProtobufVarint32FrameDecoder());
		ch.pipeline().addLast("protobufDecoder", new MyMessageDecoder());

		// ch.pipeline().addLast("frameEncoder", new
		// ProtobufVarint32LengthFieldPrepender());
		ch.pipeline().addLast("protobufEncoder", new MyMessageEncoder());

		ch.pipeline().addLast("handler", new TestServerHandler(echoClient));
	}
}

class TestServerHandler extends SimpleChannelInboundHandler<NettyMessageVO> {
	private EchoClient echoClient;

	public TestServerHandler(EchoClient ec) {
		echoClient = ec;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, NettyMessageVO nettyMessageVO) throws Exception {
		ArrayList<NettyMessageVO> commandList = new ArrayList<NettyMessageVO>();
		System.out.println("===messageReceived===");
		System.out.println("datalength: " + nettyMessageVO.getDataLength());
		System.out.println("commandcode: " + nettyMessageVO.getCommandCode());
		// ParseData.FriendList(nettyMessageVO);
		// ParseData.FriendOnline(nettyMessageVO);
		// ParseData.AgreeAddFriend(nettyMessageVO);
		if (CSCommandConstant.CMD_MAIL_REWARD == nettyMessageVO.getCommandCode()) {
			RewardedResponse response = RewardedResponse.parseFrom(nettyMessageVO.getData());
			System.out.println(response);
			// ParseData.RaidStart(nettyMessageVO, commandList);
			// for (NettyMessageVO message : commandList) {
			// echoClient.sendMessage(message.getCommandCode(),
			// message.getData());
			// }
			// commandList.clear();
		}
	}
}