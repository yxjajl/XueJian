package com.dh.constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dh.util.RandomUtil;

public class ArenaConstants {
	private static final ConcurrentHashMap<Integer, Integer> combatMap = new ConcurrentHashMap<Integer, Integer>();

	// public static String[] ARENA_RANK3 = new String[3];

	public static void put(int playerId, int combat) {
		combatMap.put(playerId, combat);
	}

	// public static void clearRank3() {
	// ARENA_RANK3 = new String[3];
	// }

	public static List<Map.Entry<Integer, Integer>> find(int start, int end, List<Integer> oldList) {
		List<Map.Entry<Integer, Integer>> result = new ArrayList<Map.Entry<Integer, Integer>>();
		int n = 0;
		for (Map.Entry<Integer, Integer> entry : combatMap.entrySet()) {
			if (entry.getValue() >= start && entry.getValue() < end) {
				if (oldList == null) {
					result.add(entry);
					n++;
				} else if (!oldList.contains(entry.getKey())) {
					result.add(entry);
					n++;
				}

				if (n > 30) {
					break;
				}
			}
		}
		return result;
	}

	public static String findMinCombat() {
		List<Integer> list = findMinCombat(4);
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < list.size(); i++) {
			if (i != 0) {
				sb.append(",");
			}

			sb.append(list.get(i));
		}

		return sb.toString();
	}

	public static List<Integer> findMinCombat(int num) {
		List<Integer> oldList = new ArrayList<Integer>();
		for (int i = 0; i < num; i++) {
			findMinCombat(oldList);
		}
		return oldList;
	}

	public static void findMinCombat(List<Integer> oldList) {
		int combat = 1000000;
		int playerId = -1;
		for (Map.Entry<Integer, Integer> entry : combatMap.entrySet()) {
			if (entry.getValue() < combat && (!oldList.contains(entry.getKey()))) {
				combat = entry.getValue();
				playerId = entry.getKey();
			}
		}

		oldList.add(playerId);
	}

	public static int findPlayerId(int index, int combat, List<Integer> oldList) {
		int result = -1;
		int start = combat;
		int end = combat;

		List<Map.Entry<Integer, Integer>> list = null;

		int n = 0;
		while (list == null || list.size() == 0) {
			if (1 <= index && index <= 15) {
				start = 0;
				end = 67680;
			} else if (15 < index && index <= 25) {
				start = 67680;
				end = 89774;
			} else if (25 < index && index <= 30) {
				start = 89774;
				end = 133960;
			} else {
				start = 133960;
				end = 10000000;
			}

			n++;
			if (n > 2) {
				end = 0;
				start = 10000000;
			}

			list = find(end, start, oldList);
		}

		int size = list.size();

		int r = RandomUtil.randomInt(size);

		result = list.get(r).getKey();

		return result;
	}
	// 1~10 X《本人历史最高战力-1500 X《本人历史最高战力-300
	// 11~20 X<本人历史最高战力-500 本人历史最高战力-300《X《本人历史最高战力
	// 21~30 X《本人历史最高战力 本人历史最高战力《X《本人历史最高战力+1000

}
