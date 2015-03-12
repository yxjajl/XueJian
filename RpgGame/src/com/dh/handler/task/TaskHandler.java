package com.dh.handler.task;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.task.PlayerTask.ReceiveTaskRequest;
import com.dh.handler.ICommandHandler;
import com.dh.netty.NettyMessageVO;
import com.dh.processor.TaskProcessor;

@Component
public class TaskHandler implements ICommandHandler {
	private static Logger logger = Logger.getLogger(TaskHandler.class);
	@Resource
	private TaskProcessor taskProcessor;

	@Override
	public void handleMessage(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		ReceiveTaskRequest res = null;
		try {
			res = ReceiveTaskRequest.parseFrom(nettyMessageVO.getData());
		} catch (Exception e) {
			logger.error("TaskHandler协议解码异常",e);
			throw e;
		}
		switch (nettyMessageVO.getCommandCode()) {
		case CSCommandConstant.TASK_RECEIVE:
			taskProcessor.receiveTask(res, nettyMessageVO, commandList);
			break;
		case CSCommandConstant.TASK_REWARD:
			taskProcessor.submitRewardProcessor(res, nettyMessageVO, commandList);
			break;
		default:
			break;
		}
	}
}
