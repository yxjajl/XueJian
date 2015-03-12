package com.dh.game.vo.user;


public class BossLogVO implements Comparable<BossLogVO>{
	private int playerId;
	private String name;
	private int hunt;
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

	public int getHunt() {
		return hunt;
	}

	public void setHunt(int hunt) {
		this.hunt = hunt;
	}

	public int getAddtion() {
		return addtion;
	}

	public void setAddtion(int addtion) {
		this.addtion = addtion;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof BossLogVO) {
			return ((BossLogVO) arg0).getPlayerId() == this.getPlayerId();
		}

		return super.equals(arg0);
	}

	public int compareTo(BossLogVO bossLogVO) {
		if (bossLogVO.getHunt() > this.getHunt()) {
			return 1;
		}
		if (bossLogVO.getHunt() < this.getHunt()) {
			return -1;
		}
		return 0;
	}
	//
	// System.out.println("playerId:" + this.getPlayerId() +
	// "\tbosslogVO playerId:" + bossLogVO.getPlayerId());
	// if (bossLogVO.getPlayerId() == this.getPlayerId()) {
	// return 0;
	// }
	// if (bossLogVO.getHunt() != this.getHunt()) {
	// return bossLogVO.getHunt() < this.getHunt() ? -1 : 1;
	// }
	// if (bossLogVO.getAddtion() != this.getAddtion()) {
	// return bossLogVO.getAddtion() < this.getAddtion() ? -1 : 1;
	// }
	// return 0;
	// }

	// @Override
	// public int compare(BossLogVO arg0, BossLogVO arg1) {

	// if (arg0.getPlayerId() == arg1.getPlayerId()) {
	// return 0;
	// }
	//
	// // if (arg0.getPlayerId() > arg1.getPlayerId()) {
	// // return 1;
	// // }
	// //
	// // return -1;
	//
	// if (arg0.getHunt() != arg1.getHunt()) {
	// return arg0.getHunt() > arg1.getHunt() ? -1 : 1;
	// }
	// if (arg0.getAddtion() != arg1.getAddtion()) {
	// return arg0.getAddtion() > arg1.getAddtion() ? 1 : -1;
	// }
	//
	// return arg0.getName().compareTo(arg1.getName());
	// }

	// @Override
	// public int compare(BossLogVO arg0, BossLogVO arg1) {
	// return 0;
	// }

}
