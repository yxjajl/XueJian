package com.dh.constants;

import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.login.CheckRoleNameProto.CheckRoleNameResponse;
import com.dh.game.vo.login.PlayerLoginProto.EXIST_STATUS;
import com.dh.game.vo.login.PlayerLoginProto.PlayerLoginResponse;
import com.dh.netty.NettyMessageVO;
import com.dh.util.CommandUtil;

@Deprecated
public class CommonCommand {

	public final static NettyMessageVO OTHERLOGINMESSAGE = CommandUtil.packageAlertMsg(AlertEnum.LOGIN_ON_OTHER); // 用户在其它地方登陆
	public final static NettyMessageVO NO_USER_MSG = new NettyMessageVO(); // 无此用户
	public final static NettyMessageVO LOGIN_EXCEPTION_MSG = CommandUtil.packageAlertMsg(AlertEnum.LOGIN_EXCEPTION); // 登陆异常
	//
	public final static NettyMessageVO EXISTS_ROLENAME = new NettyMessageVO(); // 角色名重复或非法
	public final static NettyMessageVO NOEXISTS_ROLENAME = new NettyMessageVO();
	public final static NettyMessageVO ROLE_LEVEL_NO = CommandUtil.packageAlertMsg(AlertEnum.ROLE_LEVEL_NO);

	public final static NettyMessageVO USER_NOT_EXIST = CommandUtil.packageAlertMsg(AlertEnum.ROLE_NOT_FOUND);
	//
	public final static NettyMessageVO ROLENAMESHORT = CommandUtil.packageAlertMsg(AlertEnum.NICK_SHORT);
	public final static NettyMessageVO ROLENAMELONG = CommandUtil.packageAlertMsg(AlertEnum.NICK_LONG);
	// public final static NettyMessageVO REQ_RMB =
	// CommandUtil.packageAlertMsg("元宝不足");;// 玩家RMB不足
	// public final static NettyMessageVO REQ_MONEY =
	// CommandUtil.packageAlertMsg("银两不足");;// 玩家金币不足
	// public final static NettyMessageVO REQ_EXPLOIT =
	// CommandUtil.packageAlertMsg("功勋不足");;// 玩家金币不足
	// public final static NettyMessageVO REQ_PVPV =
	// CommandUtil.packageAlertMsg("竞技值不足");;// 玩家金币不足
	//
	public final static NettyMessageVO KNAPSACK_FULL = CommandUtil.packageAlertMsg(AlertEnum.KNAPSACK_FULL);;// 背包已满
	public final static NettyMessageVO ITEM_NOT_EXIST = CommandUtil.packageAlertMsg(AlertEnum.ITEM_NOT_FOUND);;// 物品不存在
	public final static NettyMessageVO ITEM_NOT_ENOUGH = CommandUtil.packageAlertMsg(AlertEnum.ITEM_NUM_NOT);;// 物品数量不够
	public final static NettyMessageVO REWARD_GROUP_NOT_EXIT = CommandUtil.packageAlertMsg(AlertEnum.REWARD_NOT_FOUND);;// 奖励规则组不存在
	//
	public final static NettyMessageVO REWARD_ERROR = CommandUtil.packageAlertMsg(AlertEnum.REWARD_EXCEPTION);;// 领取奖励失败
	// public final static NettyMessageVO MAX_LEVEL_ARRIVED =
	// CommandUtil.packageAlertMsg("等级已经达到上限");// 已经达到最大等级 ""
	public final static NettyMessageVO CD_FINISHED_ERROR = CommandUtil.packageAlertMsg(AlertEnum.CD_OVER_NO_S);// 冷却已经结束,无需加速
	// 邮件奖励不存在
	// public final static NettyMessageVO

	static {

		PlayerLoginResponse.Builder playerLoginResponse = PlayerLoginResponse.newBuilder();
		playerLoginResponse.setResult(EXIST_STATUS.PLAYER_NOT_EXIST);
		playerLoginResponse.setMsg("不存在的帐号");
		NO_USER_MSG.setCommandCode(CSCommandConstant.USER_LOGIN);
		NO_USER_MSG.setData(playerLoginResponse.build().toByteArray());

		// 角色名已存在
		CheckRoleNameResponse.Builder checkRoleNameResponse = CheckRoleNameResponse.newBuilder();
		checkRoleNameResponse.setResult(0);
		CommandUtil.packageNettyMessage(EXISTS_ROLENAME, CSCommandConstant.CHECK_ROLENAME, checkRoleNameResponse.build().toByteArray());

		// 角色名可以使用
		checkRoleNameResponse = CheckRoleNameResponse.newBuilder();
		checkRoleNameResponse.setResult(1);
		CommandUtil.packageNettyMessage(NOEXISTS_ROLENAME, CSCommandConstant.CHECK_ROLENAME, checkRoleNameResponse.build().toByteArray());

	}

	public static void test() {
		System.out.println("load Common Command !!!");
	}
}
