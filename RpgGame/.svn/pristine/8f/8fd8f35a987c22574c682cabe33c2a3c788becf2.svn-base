package com.dh.s2s.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import com.dh.netty.NettyMessageVO;

public class S2sMessageEncoder extends io.netty.handler.codec.MessageToByteEncoder<NettyMessageVO> {

	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessageVO msg, ByteBuf out) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("s2s send mesg +"+ msg.getCommandCode() + " , " + msg.getUserid());
		int dataLength = msg.getDataLength();
		out.ensureWritable(8 + dataLength);
		out.writeShort((short)dataLength);
		out.writeInt(msg.getUserid());
		out.writeShort(msg.getCommandCode());
		if (dataLength > 0) {
			out.writeBytes(msg.getData());
		}
	}

}
