package com.dh.vo.user;

import java.util.ArrayList;
import java.util.List;

import com.dh.game.vo.user.PlayerFriendVO;

public class UserFriend implements IClear {
	private List<PlayerFriendVO> friends = new ArrayList<PlayerFriendVO>();
	// private List<PlayerFriendVO> enemies = new ArrayList<PlayerFriendVO>();
	private List<PlayerFriendVO> blacks = new ArrayList<PlayerFriendVO>();

	// private List<PlayerFriendVO> applies = new ArrayList<PlayerFriendVO>();
	// private List<PlayerFriendVO> recents = new ArrayList<PlayerFriendVO>();

	public boolean removeOldRelation(boolean isBlack, int otherId) {
		List<PlayerFriendVO> list;
		if (isBlack) {
			list = blacks;
		} else {
			list = friends;
		}
		for (PlayerFriendVO playerFriendVO : list) {
			if (playerFriendVO.getOtherId() == otherId) {
				list.remove(playerFriendVO);
				return true;
			}
		}
		return false;
	}

	public boolean isInFriend(boolean isBlack, int otherId) {
		List<PlayerFriendVO> list;
		if (isBlack) {
			list = blacks;
		} else {
			list = friends;
		}
		for (PlayerFriendVO playerFriendVO : list) {
			if (playerFriendVO.getOtherId() == otherId) {
				return true;
			}
		}
		return false;
	}

	public List<PlayerFriendVO> getFriends() {
		return friends;
	}

	public void setFriends(List<PlayerFriendVO> friends) {
		this.friends = friends;
	}

	public List<PlayerFriendVO> getBlacks() {
		return blacks;
	}

	public void setBlacks(List<PlayerFriendVO> blacks) {
		this.blacks = blacks;
	}

	@Override
	public void clear() {
		friends.clear();
		blacks.clear();
		friends = null;
		blacks = null;
	}

}
