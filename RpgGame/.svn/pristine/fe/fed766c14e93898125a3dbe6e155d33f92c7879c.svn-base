package com.dh.sync;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class SyncUtil {
	private static Logger LOGGER = Logger.getLogger(SyncUtil.class);
	private ConcurrentMap<String, SyncLock> LOCK_MAP = new ConcurrentHashMap<String, SyncLock>();

	private static SyncUtil INSTANCE = new SyncUtil();

	public static SyncUtil getInstance() {
		return INSTANCE;
	}

	private SyncUtil() {
		Thread t = new Thread("LockManager-Cleaner") {
			public void run() {
				while (true)
					try {
						TimeUnit.MINUTES.sleep(30);
						// Thread.sleep(1800000L);
						try {
							clean();
						} catch (Exception e) {
							LOGGER.error("", e);
						}
					} catch (Exception e) {
						LOGGER.error("", e);
					}
			}
		};
		t.setDaemon(true);
		t.start();
	}

	public synchronized void clean() {
		for (SyncLock lock : LOCK_MAP.values()) {
			if (lock.canClean()) {
				LOCK_MAP.remove(lock.getKey());
			}
		}
	}

	public SyncLock getLock(String key) {
		SyncLock lock = LOCK_MAP.get(key);
		if (lock == null) {
			synchronized (this) {
				if (lock == null) {
					lock = new SyncLock(key);
					LOCK_MAP.put(key, lock);
				}
			}
		}
		return lock;
	}
}
