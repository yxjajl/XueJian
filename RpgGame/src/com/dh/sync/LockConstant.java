package com.dh.sync;

public class LockConstant {
	public final static String LOCK_USERACCOUNT = "LOCK_USERACCOUNT"; // username帐号
//	public final static String LOCK_ACCOUNT = "LOCK_ACCOUNT"; //PlayerAccountVO

	public final static String LOCK_PLAYERVO = "LOCK_PLAYERVO"; //PlayerVO 钱和playervo 都用这个锁
	
	public final static String LOCK_ARENA = "LOCK_ARENA";//PlayerArenaVO;
	public final static String LOCK_ARENA_RECORD = "LOCK_ARENA_RECORD";//PlayerArenaVO;

	public final static String LOCK_RES_PLAYERVO = "LOCK_RES_PLAYERVO";// 锁定玩家资源点战斗情况
	
	public final static String LOCK_MANYPEOPLE = "LOCK_MANYPEOPLE"; //多人副本锁创建和删除

}
