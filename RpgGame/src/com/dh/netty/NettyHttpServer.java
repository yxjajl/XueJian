package com.dh.netty;

//import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
//import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
//import static io.netty.handler.codec.http.HttpMethod.GET;
//import static io.netty.handler.codec.http.HttpMethod.POST;
//import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
//import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
//import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelFutureListener;
//import io.netty.channel.ChannelHandler.Sharable;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundMessageHandlerAdapter;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.codec.http.DefaultFullHttpResponse;
//import io.netty.handler.codec.http.FullHttpResponse;
//import io.netty.handler.codec.http.HttpHeaders;
//import io.netty.handler.codec.http.HttpRequest;
//import io.netty.handler.codec.http.HttpRequestDecoder;
//import io.netty.handler.codec.http.HttpResponseEncoder;
//import io.netty.handler.codec.http.HttpResponseStatus;
//import io.netty.util.CharsetUtil;
//
//import org.apache.log4j.Logger;

public class NettyHttpServer { //implements Runnable 
//	// public final static String ZIFUURL = "/cgi-bin/pay";
//	// public final static String TASKMARKETURL = "/cgi-bin/provide.cgi";
//	// @Resource
//	// private NettyServerInitializer nettyServerInitializer;
//	// @Resource
//	// private RechargeService rechargeService;
//	// @Resource
//	// private MailService mailService;
//	// @Resource
//	// private PlayerService playerService;
//
//	public void run() {
//
//		ServerBootstrap b = new ServerBootstrap();
//		try {
//			b.group(new NioEventLoopGroup(), new NioEventLoopGroup());
//			b.channel(NioServerSocketChannel.class);
//			b.localAddress(843);
//			b.childHandler(new HttpNettyServerInitializer(this));
//			b.bind().sync().channel().closeFuture().sync();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//		}
//
//	}
//
//	public static void main(String[] args) throws Exception {
//		new NettyHttpServer().run();
//	}
//
//}
//
//class HttpNettyServerInitializer extends ChannelInitializer<SocketChannel> {
//	private HttpNettyServerHandler handler;
//
//	public HttpNettyServerInitializer(NettyHttpServer nettyHttpServer) {
//		super();
//		handler = new HttpNettyServerHandler(nettyHttpServer);
//	}
//
//	@Override
//	public void initChannel(SocketChannel ch) throws Exception {
//		ChannelPipeline p = ch.pipeline();
//		p.addLast("frameDecoder", new HttpRequestDecoder());
//		p.addLast("frameEncoder", new HttpResponseEncoder());
//		p.addLast("handler", handler);
//	}
//
//}
//
//@Sharable
//class HttpNettyServerHandler extends ChannelInboundMessageHandlerAdapter<HttpRequest> {
//	private static Logger logger = Logger.getLogger(NettyServerHandler.class);
//
//	public HttpNettyServerHandler(NettyHttpServer nettyHttpServer) {
//		super();
//	}
//
//	@Override
//	public void freeInboundBuffer(ChannelHandlerContext ctx) throws Exception {
//		super.freeInboundBuffer(ctx);
//		System.out.println("用户关闭");
//	}
//
//	@Override
//	protected void messageReceived(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception {
//		if ("/favicon.ico".equals(httpRequest.getUri())) {
//			sendHttpResponse(channelHandlerContext, httpRequest, new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND));
//			return;
//		}
//
//		// 处理错误 的请求
//		if (!httpRequest.getDecoderResult().isSuccess()) {
//			sendHttpResponse(channelHandlerContext, httpRequest, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
//		}
//
//		// 处理Get请求
//		if (httpRequest.getMethod() == GET || httpRequest.getMethod() == POST) {
//			FullHttpResponse fullHttpResponse = callbacksendgood();
//			if (fullHttpResponse != null) {
//				sendHttpResponse(channelHandlerContext, httpRequest, fullHttpResponse);
//			}
//		}
//
//	}
//
//	private static void sendHttpResponse(ChannelHandlerContext channelHandlerContext, HttpRequest req, FullHttpResponse res) {
//		if (res.getStatus().code() != 200) {
//			res.data().writeBytes(Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8));
//			setContentLength(res, res.data().readableBytes());
//		}
//
//		ChannelFuture f = channelHandlerContext.channel().write(res);
//		if (!isKeepAlive(req) || res.getStatus().code() != 200) {
//			f.addListener(ChannelFutureListener.CLOSE);
//		}
//
//	}
//
//	private final static String xml = "<cross-domain-policy> " + "<allow-access-from domain=\"*\" to-ports=\"1025-29999\"/>" + "</cross-domain-policy> ";;
//
//	public FullHttpResponse callbacksendgood() {
//		ByteBuf bbf = Unpooled.copiedBuffer(xml, CharsetUtil.UTF_8);
//
//		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, bbf);
//		response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
//		setContentLength(response, bbf.readableBytes());
//		return response;
//	}

}
