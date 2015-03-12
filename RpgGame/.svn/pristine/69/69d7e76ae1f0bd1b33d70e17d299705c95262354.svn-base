package com.dh.handler.raid;

import java.util.ArrayList;
import java.util.List;

import com.dh.constants.ManyPeopleRaidConstants;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.vo.user.PlayerVO;

public class ManyPeopleTeam {
	private int raidid;
	private int levelLimit;
	private boolean isAutoStart;
	private String password;
	private int teamId; // 用房主的playerId
	private int status = ManyPeopleRaidConstants.TEAM_STATUS_CREATE; // 初始为组队状态
	private List<PlayerVO> playerList = new ArrayList<PlayerVO>(2);
	private int num = 1;

	public synchronized void join(PlayerVO clientPlayerVO) throws Exception {
		if (num >= 3) {
			throw new GameException(AlertEnum.TEAM_FULL);
		}
		playerList.add(clientPlayerVO);
		num++;
		// 发消息通知
	}

	public synchronized void leave(int playerId) {
		int n = 0;
		for (PlayerVO playerVO : playerList) {
			if (playerVO.getPlayerId() == playerId) {
				playerList.remove(playerVO);
				break;
			}
			n++;
		}

		if (n == 0) {
			// 房主
		}

		// if (masterPlayerVO.getPlayerId() == playerId) {
		// // 队伍解散
		// clear();
		// } else if (playerList.get(0).getPlayerId() == playerId) {
		// playerList.remove(0);
		// num--;
		// // 发消息通知
		// } else if (playerList.get(1).getPlayerId() == playerId) {
		// playerList.remove(1);
		// num--;
		// // 发消息通知
		// } else {
		// // 没有这个人
		// }
	}

	public void clear() {
		status = ManyPeopleRaidConstants.TEAM_STATUS_CREATE; // 初始为组队状态
		playerList.clear();
		num = 1;
	}

	public int getRaidid() {
		return raidid;
	}

	public void setRaidid(int raidid) {
		this.raidid = raidid;
	}

	public int getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}

	public boolean isAutoStart() {
		return isAutoStart;
	}

	public void setAutoStart(boolean isAutoStart) {
		this.isAutoStart = isAutoStart;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
