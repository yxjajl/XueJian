package com.dh.vo.user;

import java.util.ArrayList;
import java.util.List;

import com.dh.game.vo.user.MailVO;

public class UserMail implements IClear {
	private List<MailVO> mailList = new ArrayList<MailVO>();// 玩家邮件列表
	private boolean isNewMail = false;
	private long lastLoadTime;

	// private int maxMailId = 0;

	// public int getMaxMailId() {
	// return maxMailId;
	// }
	//
	// public void setMaxMailId(int maxMailId) {
	// this.maxMailId = maxMailId;
	// }

	public List<MailVO> getMailList() {
		return mailList;
	}

	public void setMailList(List<MailVO> mailList) {
		this.mailList = mailList;
	}

	public boolean isNewMail() {
		return isNewMail;
	}

	public void setNewMail(boolean isNewMail) {
		this.isNewMail = isNewMail;
	}

	@Override
	public void clear() {
		if (mailList != null) {
			mailList.clear();
		}
		mailList = null;
		isNewMail = false;
	}

	public long getLastLoadTime() {
		return lastLoadTime;
	}

	public void setLastLoadTime(long lastLoadTime) {
		this.lastLoadTime = lastLoadTime;
	}

}
