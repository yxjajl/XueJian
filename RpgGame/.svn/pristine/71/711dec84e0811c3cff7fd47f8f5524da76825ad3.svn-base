package com.dh.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.dh.vo.user.RoleLv;

public class RoleLevelRank {
	public final static int RoleLevelRank_MAXSIZE = 20;
	private List<RoleLv> RoleLevelRankList = new ArrayList<RoleLv>(RoleLevelRank_MAXSIZE + 1);
	private List<Integer> RoleList = new ArrayList<Integer>(RoleLevelRank_MAXSIZE);
	private final static RoleLevelRank INSTANCE = new RoleLevelRank();

	public synchronized void putRoleLevel(int playerId, int level) {
		if (RoleList.contains(playerId)) {
			// update
		} else {
			RoleLevelRankList.add(new RoleLv(playerId, level));
			Collections.sort(RoleLevelRankList);
			while ((RoleLevelRankList.size() > RoleLevelRank_MAXSIZE)) {
				RoleLevelRankList.remove(RoleLevelRank_MAXSIZE);
			}
		}
	}

	public List<RoleLv> getOrderList(final int n) {
		List<RoleLv> result = new ArrayList<RoleLv>();
		for (int i = 0; i < n; i++) {
			result.add(RoleLevelRankList.get(i));
		}
		return result;
	}

	private RoleLevelRank() {
	}

	public static RoleLevelRank getInstance() {
		return INSTANCE;
	}

	public static void main(String[] args) throws Exception {
		for (int i = 1; i <= 30; i++) {
			RoleLevelRank.getInstance().putRoleLevel(i, 30 + i);
		}

		for (RoleLv a : RoleLevelRank.getInstance().getOrderList(10)) {
			System.out.println(a.playerId + "," + a.level + "," + a.time);
		}
	}

}
