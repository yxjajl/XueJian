package com.dh.constants;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import com.dh.game.constant.CSCommandConstant;
import com.dh.netty.NettyMessageVO;
import com.dh.vo.user.UserCached;

public class RegisterRoleConstant {
	public final static Queue<UserCached> preBornPlayers = new LinkedList<UserCached>();
	public static AtomicInteger currentNum = new AtomicInteger(0);// 当前具有缓存数目
	public final static int HUNGRY_NUM = 500;// 需要补充usercached数目
	// public final static int FULL_NUM = 100;// 补充满usercached数目

	public final static NettyMessageVO NETTYMESSAGEVO = new NettyMessageVO();

	static {
		NETTYMESSAGEVO.setCommandCode(CSCommandConstant.SYS_APPEND_REGISTERROLE);
	}
}
