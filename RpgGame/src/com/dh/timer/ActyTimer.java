package com.dh.timer;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.gm.GMProto.ActyAnsReq;
import com.dh.netty.NettyMessageVO;
import com.dh.queue.LocalCommandQueue;

@Component
public class ActyTimer {
	private static Logger logger = Logger.getLogger(ActyTimer.class);

	private final static NettyMessageVO NETTYMESSAGEVO = new NettyMessageVO();
	public final static int[] ANS_START_TIMES = { 13, 14 };// 开关时间
	static {
		NETTYMESSAGEVO.setCommandCode(CSCommandConstant.SYS_ACTY_FRESH);
	}

	// 定时触发
	@Scheduled(cron = "0 0 13,14 * * ?")
	public void run() {
		logger.debug("====================" + this.getClass().getSimpleName() + "====================");
		if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == ANS_START_TIMES[0]) {// 开启回答问题
			NETTYMESSAGEVO.setData(ActyAnsReq.newBuilder().setStatus(1).build().toByteArray());
		} else if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == ANS_START_TIMES[1]) {// 关闭
			NETTYMESSAGEVO.setData(ActyAnsReq.newBuilder().setStatus(0).build().toByteArray());
		}
		LocalCommandQueue.getInstance().put(NETTYMESSAGEVO);
	}
}
