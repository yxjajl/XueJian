package com.dh.sync;

public class SyncLock {
	private long timestamp = System.currentTimeMillis();
	private String key;

	public SyncLock(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	public void update() {
		this.timestamp = System.currentTimeMillis();
	}

	public boolean canClean() {
		return System.currentTimeMillis() - this.timestamp > 3600000L;
	}
}