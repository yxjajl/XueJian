package com.dh.netty;


public class LanjieTool {
	// private final static Map<String, LanjieVO> lanjjieMap = new HashMap<String, LanjieVO>(1000);
	// private final static String LOCK_LANJIE = "LOCK_LANJIE";

	public static boolean lanjie(NettyMessageVO nettyMessageVO) {
		// synchronized (LOCK_LANJIE) {
		// String removeAddress = nettyMessageVO.getChannel().remoteAddress().toString();// GameUtil.getIpString();
		// LanjieVO lanjieVO = lanjjieMap.get(removeAddress);
		// if (lanjieVO == null) {
		// lanjieVO = new LanjieVO();
		// lanjieVO.times = System.currentTimeMillis();
		// lanjieVO.sendtimes = 1;
		// lanjjieMap.put(removeAddress, lanjieVO);
		// return false;
		// } else {
		// long curTime = System.currentTimeMillis();
		// if (Math.abs(curTime - lanjieVO.times) > 1000) {
		// lanjieVO.times = curTime;
		// lanjieVO.sendtimes = 0;
		// lanjieVO.acc = 0;
		// } else {
		// lanjieVO.sendtimes++;
		// if (lanjieVO.sendtimes >= 8) {
		// System.err.println("指令太频繁............." + lanjieVO.acc);
		// lanjieVO.acc++;
		// if (lanjieVO.acc >= 3) {
		// return true;
		// }
		// }
		// }
		// }
		// }

		return false;
	}

	public static void clear(String str) {
		// String removeAddress = str;// GameUtil.getIpString(str);
		// synchronized (LOCK_LANJIE) {
		// lanjjieMap.remove(removeAddress);
		// }
	}

}

class LanjieVO {
	public long times = 0;
	public long sendtimes = 0;
	public int acc = 0;
}