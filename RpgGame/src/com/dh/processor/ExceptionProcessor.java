package com.dh.processor;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.netty.NettyMessageVO;
import com.dh.service.GMService;

@Component
public class ExceptionProcessor {
	private static Logger logger = Logger.getLogger(ExceptionProcessor.class);

	@Resource
	private GMService gMService;

	public void errCommandPro(NettyMessageVO nettyMessageVO) {
		logger.error("error Commandcode " + nettyMessageVO.getCommandCode());
		// try {
		// gMService.addBlackIP(GameUtil.getIpString(nettyMessageVO.getChannel().remoteAddress().toString()),
		// GameUtil.getIpString(nettyMessageVO.getChannel().remoteAddress().toString()),
		// "命令码超出正常范围", BLOCK_ENUM.BLOCK_ACC_IP_VALUE);
		// logger.error("命令码:" + nettyMessageVO.getCommandCode() + "异常IP(" +
		// nettyMessageVO.getChannel().remoteAddress() + ")访问,加入黑名单");
		// nettyMessageVO.getChannel().close();
		// return;
		// } catch (Exception e) {
		// logger.error("ip捕获异常" + e.getCause(), e);
		// return;
		// }
	}
}
