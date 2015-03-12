package com.dh.handler.task;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.dh.game.vo.user.PlayerTaskVO;
import com.dh.netty.NettyMessageVO;
import com.dh.vo.user.UserCached;

public interface TaskCheck {
	public void init(ApplicationContext act);

	public void changTaskByReQTypeAcc(UserCached userCached, int reqType, int target, int num,List<NettyMessageVO> commandList);

	public void changTaskByReQTypeAcc2(UserCached userCached, int reqType, int target, int num,List<NettyMessageVO> commandList);

	public void changTaskByReQType(UserCached userCached, int reqType, int target, int num,List<NettyMessageVO> commandList);

	public void changTaskByReQType(UserCached userCached, int reqType,List<NettyMessageVO> commandList);
	
	public void receiveReady(UserCached userCached, PlayerTaskVO playerTask);// 接任务时的预判

	public void specProcess(UserCached userCached, PlayerTaskVO playerTask, List<NettyMessageVO> commandList) throws Exception;

	// 角色升级特别处理
	public void specProcess(UserCached userCached, int oldlevel, int newlevel, List<NettyMessageVO> commandList) throws Exception;
}
