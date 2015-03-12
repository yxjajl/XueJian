package com.dh.vo.user;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;

import com.dh.game.vo.user.PlayerAccountVO;
import com.dh.game.vo.user.PlayerSoldierTeamVO;
import com.dh.game.vo.user.PlayerTaskVO;
import com.dh.game.vo.user.PlayerTechVO;
import com.dh.game.vo.user.PlayerVO;

/**
 * 用户缓存
 * 
 * @author RickYu
 */
public class UserCached {
	private int playerId;
	private Channel channel;
	private long lastedAccessTime = 0;
	private PlayerVO playerVO;
	private PlayerAccountVO playerAccountVO; // 资源 游戏币
	private UserHero userHero = new UserHero(); // 英雄
	private UserRaid userRaid = new UserRaid(); // 副本
	private UserYuanZhen userYuanZhen = new UserYuanZhen();
	private UserMail userMail = new UserMail(); // 邮件
	private UserTimer userTimer = new UserTimer();
	private UserKnapsack userKnapsack = new UserKnapsack(); // 背包
	private UserShop userShop = new UserShop(); // 商城
	private UserReward userReward = new UserReward(); // 奖励
	private UserActivity userActivity = new UserActivity();
	private UserStreet userStreet;// 玩家江湖
	private UserFriend userFriend = new UserFriend();
	private UserGM userGM = new UserGM();// gm统计相关
	private UserLegion userLegion = new UserLegion();// 帮会
	// private PlayerStrongHoldVO playerStrongHoldVO = new PlayerStrongHoldVO();
	// // 场景
	private List<PlayerTaskVO> taskList = new ArrayList<PlayerTaskVO>();// 玩家任务列表
	private PlayerSoldierTeamVO playerSoldierTeamVO = null;

	private PlayerTechVO techVO = null;// 玩家科技
	private int[] optCount = null;// [0:科技升级计数,]

	private ChannelGroup channelGroup = null;

	/**
	 * 离线清理
	 * 
	 * @date 2014年2月26日
	 */
	public void despose() {
		userKnapsack.clear();
		userKnapsack = null;

		userRaid.clear();
		userRaid = null;

		userHero.clear();
		userHero = null;

		userMail.clear();
		userMail = null;

		userShop.clear();
		userShop = null;

		if (taskList != null) {
			taskList.clear();
			taskList = null;
		}

		userReward.clear();
		userReward = null;

		userTimer.clear();
		userTimer = null;

		userActivity.clear();
		userActivity = null;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public long getLastedAccessTime() {
		return lastedAccessTime;
	}

	public void setLastedAccessTime(long lastedAccessTime) {
		this.lastedAccessTime = lastedAccessTime;
	}

	public PlayerVO getPlayerVO() {
		return playerVO;
	}

	public void setPlayerVO(PlayerVO playerVO) {
		this.playerVO = playerVO;
	}

	public PlayerAccountVO getPlayerAccountVO() {
		return playerAccountVO;
	}

	public void setPlayerAccountVO(PlayerAccountVO playerAccountVO) {
		this.playerAccountVO = playerAccountVO;
	}

	public UserShop getUserShop() {
		return userShop;
	}

	public void setUserShop(UserShop userShop) {
		this.userShop = userShop;
	}

	public UserHero getUserHero() {
		return userHero;
	}

	public void setUserHero(UserHero userHero) {
		this.userHero = userHero;
	}

	public UserKnapsack getUserKnapsack() {
		return userKnapsack;
	}

	public void setUserKnapsack(UserKnapsack userKnapsack) {
		this.userKnapsack = userKnapsack;
	}

	public UserMail getUserMail() {
		return userMail;
	}

	public void setUserMail(UserMail userMail) {
		this.userMail = userMail;
	}

	public PlayerTechVO getTechVO() {
		return techVO;
	}

	public void setTechVO(PlayerTechVO techVO) {
		this.techVO = techVO;
	}

	public UserReward getUserReward() {
		return userReward;
	}

	public void setUserReward(UserReward userReward) {
		this.userReward = userReward;
	}

	public ChannelGroup getChannelGroup() {
		return channelGroup;
	}

	public void setChannelGroup(ChannelGroup channelGroup) {
		this.channelGroup = channelGroup;
	}

	public List<PlayerTaskVO> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<PlayerTaskVO> taskList) {
		this.taskList = taskList;
	}

	public PlayerSoldierTeamVO getPlayerSoldierTeamVO() {
		return playerSoldierTeamVO;
	}

	public void setPlayerSoldierTeamVO(PlayerSoldierTeamVO playerSoldierTeamVO) {
		this.playerSoldierTeamVO = playerSoldierTeamVO;
	}

	public UserRaid getUserRaid() {
		return userRaid;
	}

	public void setUserRaid(UserRaid userRaid) {
		this.userRaid = userRaid;
	}

	public UserTimer getUserTimer() {
		return userTimer;
	}

	public void setUserTimer(UserTimer userTimer) {
		this.userTimer = userTimer;
	}

	/**
	 * 计算当天操作计数
	 * 
	 * @param index
	 *            对应序数
	 */
	// public int getOptCount(int index) {
	// return optCount[index];
	// }

	public int[] getOptCount() {
		return optCount;
	}

	public void setOptCount(int[] optCount) {
		this.optCount = optCount;
	}

	public UserStreet getUserStreet() {
		return userStreet;
	}

	public void setUserStreet(UserStreet userStreet) {
		this.userStreet = userStreet;
	}

	public UserActivity getUserActivity() {
		return userActivity;
	}

	public void setUserActivity(UserActivity userActivity) {
		this.userActivity = userActivity;
	}

	public UserYuanZhen getUserYuanZhen() {
		return userYuanZhen;
	}

	public void setUserYuanZhen(UserYuanZhen userYuanZhen) {
		this.userYuanZhen = userYuanZhen;
	}

	public UserFriend getUserFriend() {
		return userFriend;
	}

	public void setUserFriend(UserFriend userFriend) {
		this.userFriend = userFriend;
	}

	public UserGM getUserGM() {
		return userGM;
	}

	public void setUserGM(UserGM userGM) {
		this.userGM = userGM;
	}

	public UserLegion getUserLegion() {
		return userLegion;
	}

	public void setUserLegion(UserLegion userLegion) {
		this.userLegion = userLegion;
	}

}
