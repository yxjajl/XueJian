package com.dh.test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

public class TestTreeSet {
	public static void main(String[] args) throws Exception {
		testTreeSet2();
	}

	public static void testTreeSet2() {
		TreeSet<RankVO> RANK = new TreeSet<RankVO>(new TestTreeSet().new MyComparator());
		Map<Integer, RankVO> rankMap = new HashMap<Integer, RankVO>();
		add(RANK, createRankVO(2, 100), rankMap);
		add(RANK, createRankVO(2, 99), rankMap);
		add(RANK, createRankVO(2, 98), rankMap);
		add(RANK, createRankVO(2, 97), rankMap);
		add(RANK, createRankVO(5, 101), rankMap);
		add(RANK, createRankVO(6, 101), rankMap);
		add(RANK, createRankVO(7, 101), rankMap);
		add(RANK, createRankVO(6, 101), rankMap);
		add(RANK, createRankVO(7, 101), rankMap);
		add(RANK, createRankVO(8, 101), rankMap);
		for (int i = 0; i < 10; i++) {
			add(RANK, createRankVO(i, 100 - i), rankMap);
		}
		add(RANK, createRankVO(6, 101), rankMap);
		add(RANK, createRankVO(7, 101), rankMap);
		Iterator<RankVO> it = RANK.iterator();
		int i = 1;
		while (it.hasNext()) {
			RankVO logVO = it.next();
			System.err.println("index:\t" + (i++) + "\tplayerId:" + logVO.getPlayerId() + "\thunt:" + logVO.getHurt());
		}
	}

	public static void add(TreeSet<RankVO> RANK, RankVO rankVO, Map<Integer, RankVO> rankMap) {
		RankVO oldRank = rankMap.get(rankVO.getPlayerId());
		if (oldRank != null) {
			RANK.remove(oldRank);
		}
		RANK.add(rankVO);
		rankMap.put(rankVO.getPlayerId(), rankVO);
	}

	public static RankVO createRankVO(int playerId, int hurt) {
		RankVO rankVO = new TestTreeSet().new RankVO();
		rankVO.setPlayerId(playerId);
		rankVO.setHurt(hurt);
		return rankVO;
	}

	class RankVO {
		private int playerId;
		private String name;
		private int hurt;
		private int addtion;

		public int getPlayerId() {
			return playerId;
		}

		public void setPlayerId(int playerId) {
			this.playerId = playerId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAddtion() {
			return addtion;
		}

		public void setAddtion(int addtion) {
			this.addtion = addtion;
		}

		public int getHurt() {
			return hurt;
		}

		public void setHurt(int hurt) {
			this.hurt = hurt;
		}
	}

	class MyComparator implements Comparator<RankVO> {

		@Override
		public int compare(RankVO o1, RankVO o2) {
			if (o1.getPlayerId() == o2.getPlayerId()) {
				return 0;
			} else {
				if (o1.getHurt() != o2.getHurt()) {
					return o2.getHurt() - o1.getHurt();
				} else {
					return o1.getPlayerId() - o2.getPlayerId();
				}
			}
		}

	}
}
