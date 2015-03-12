package com.dh.service;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.springframework.stereotype.Component;

@Sharable
@Component
public class ServerXMLHandler extends SimpleChannelInboundHandler<String> {
	private final static String xml = "<cross-domain-policy> " + "<allow-access-from domain=\"*\" to-ports=\"1025-29999\"/>" + "</cross-domain-policy>\n\0 ";

	// byte[] bytes = xml.getBytes();

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String arg1) throws Exception {
		ByteBuf buf = Unpooled.copiedBuffer(xml.getBytes());
		// System.out.println(arg1);
		ctx.write(buf);
		ctx.flush();
		// System.out.println(buf);
	}

}
