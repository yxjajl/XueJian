package com.dh.vo.user;
public class RoleLv implements Comparable<RoleLv> {
	public int playerId;
	public int level;
	public long time;

	public RoleLv(int _playerId, int _level) {
		playerId = _playerId;
		level = _level;
		time = System.currentTimeMillis();
	}

	@Override
	public int compareTo(RoleLv o) {
		if (level != o.level) {
			return -(level - o.level);
		}

		if (time > o.time) {
			return -1;
		} else if (time == o.time) {
			return 0;
		}
		return 1;
	}
}