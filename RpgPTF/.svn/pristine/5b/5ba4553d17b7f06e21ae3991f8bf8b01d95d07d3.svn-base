package com.dh.game.vo.user;

import java.util.Date;

import com.dh.game.vo.message.MessageProto.Rewards;

public class MailVO implements Comparable<MailVO> {
	private int id;
	private int playerId;
	private int isRead;
	private int isReward;
	private String title;// 邮件主题
	private String content;
	private Date recvTime;
	private String rewards;
	private String senderName;// 发送者名字
	private int senderId;
	private int isValid;
	private Rewards rewardsProto;// 对邮件奖励protobuf打包,在登录加载或者发送新邮件赋值
	private int lifeTime;

	// 邮件类型,一共四类

	// 邮件排序

	public MailVO(int playerId, int isRead, int isReward, String title, String content, Date recvTime, String rewards, String senderName, int senderId) {
		this.playerId = playerId;
		this.isRead = isRead;
		this.isReward = isReward;
		this.title = title;
		this.content = content;
		this.recvTime = recvTime;
		this.rewards = rewards;
		this.senderName = senderName;
		this.senderId = senderId;
	}

	public MailVO() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRewards() {
		return rewards;
	}

	public void setRewards(String rewards) {
		this.rewards = rewards;
	}

	public Date getRecvTime() {
		return recvTime;
	}

	public void setRecvTime(Date recvTime) {
		this.recvTime = recvTime;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public int getIsReward() {
		return isReward;
	}

	public void setIsReward(int isReward) {
		this.isReward = isReward;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public int getIsValid() {
		return isValid;
	}

	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}

	public int compareTo(MailVO arg0) {
		if (this.isRead != arg0.isRead) {
			return this.isRead - arg0.isRead;
		}
		return -this.getRecvTime().compareTo(arg0.getRecvTime());
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public Rewards getRewardsProto() {
		return rewardsProto;
	}

	public void setRewardsProto(Rewards rewardsProto) {
		this.rewardsProto = rewardsProto;
	}

	public int getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}

}
