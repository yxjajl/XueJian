package com.dh.handler;

import java.util.List;

import org.apache.log4j.Logger;

import com.dh.netty.NettyMessageVO;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message.Builder;

public interface ICommandHandler {
	Logger LOGGER = Logger.getLogger(ICommandHandler.class);
	public final static String DECODE_ERROR = "协议解码异常";

	public abstract void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception;

	class ParseCheck {
		public static Builder parseCheck(Builder builder, byte[] data) {
			Builder result;
			try {
				result = builder.mergeFrom(data).buildPartial().toBuilder();
				if (result.isInitialized() && true) {
					return result;
				}
			} catch (InvalidProtocolBufferException e) {
				LOGGER.error("builde parse error" + builder.getClass());
			}
			return null;
		}
	}

}
