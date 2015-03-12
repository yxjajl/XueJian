package com.dh.game.vo.base;

public class BaseConfigVO implements Comparable<BaseConfigVO> {
	private int id;
	private int index;
	private int params;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getParams() {
		return params;
	}

	public void setParams(int params) {
		this.params = params;
	}

	@Override
	public int compareTo(BaseConfigVO arg0) {
		return this.getIndex() - arg0.getIndex();
	}

}
