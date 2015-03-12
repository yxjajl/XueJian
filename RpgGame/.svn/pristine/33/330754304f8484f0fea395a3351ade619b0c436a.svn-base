package com.dh.constants;

import gnu.trove.map.TIntIntMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.dh.game.vo.base.BaseGMListVO;
import com.dh.game.vo.base.BaseGMNoticeVO;
import com.dh.game.vo.gm.GMProto.BLOCK_ENUM;
import com.dh.service.ChatService;

public class GMConstants {
	private final static Map<String, BaseGMListVO> GM_LIST_MAP = new HashMap<String, BaseGMListVO>();
	private final static Object black_mutex = new Object();
	private final static List<BaseGMNoticeVO> GM_NOTICE_LIST = new ArrayList<BaseGMNoticeVO>();
	private final static Map<Integer, Integer> accecc_period = new HashMap<Integer, Integer>();

	private static int id = 1;

	/**
	 * 访问频率统计
	 */
	public static boolean isTooFrequency(int playerId) {
		int time = accecc_period.get(playerId);
		if (time > 20) {
			return true;
		} else {
			accecc_period.put(playerId, time + 1);
			return false;
		}
	}

	public static void clearFrequency(int playerId) {
		accecc_period.remove(playerId);
	}

	public static void addGMNotice(BaseGMNoticeVO baseGMNoticeVO) {
		synchronized (GM_NOTICE_LIST) {
			baseGMNoticeVO.setId(id++);
			GM_NOTICE_LIST.add(baseGMNoticeVO);
		}
	}

	/**
	 * 遍历消息并发送
	 */
	public static void iteratorAndSendNotice() {
		synchronized (GM_NOTICE_LIST) {
			ListIterator<BaseGMNoticeVO> it = GM_NOTICE_LIST.listIterator();
			BaseGMNoticeVO gmNotice;
			while (it.hasNext()) {
				gmNotice = it.next();
				if (gmNotice.getSumMin() + 1 >= gmNotice.getPeriod()) {// 发送消息,当前次数加1后满足条件
					ChatService.sendSysMsg(gmNotice.getMsg(), ChatService.TAGS[1], ChatService.getSysNoticeName());
					gmNotice.setCount(gmNotice.getCount() - 1);
					gmNotice.setSumMin(0);// 次数清零
					if (gmNotice.getCount() <= 0) {// 发送次数已经用完
						it.remove();
					}
				} else {
					gmNotice.setSumMin(gmNotice.getSumMin() + 1);// 增加累计分数,由于一分钟调用一次
				}
			}
		}
	}

	public static void addGMlist(BaseGMListVO baseGMListVO) {
		GM_LIST_MAP.put(baseGMListVO.getMac(), baseGMListVO);
	}

	public static boolean isBlack(String mac, int type) {
		BaseGMListVO bgv = GM_LIST_MAP.get(mac);
		if (bgv != null && bgv.getType() == type) {
			return true;
		}
		return false;
	}

	public static boolean isBlackIP(String mac) {
		BaseGMListVO bgv = GM_LIST_MAP.get(mac);
		if (bgv != null) {
			if (bgv.getType() == BLOCK_ENUM.BLOCK_IP_VALUE || bgv.getType() == BLOCK_ENUM.BLOCK_ACC_IP_VALUE) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 添加到黑名单中
	 */
	public static BaseGMListVO addBlack(String mac, String ip, String reason, int type) {
		synchronized (black_mutex) {
			BaseGMListVO baseGMListVO = new BaseGMListVO();
			baseGMListVO.setMac(mac);
			baseGMListVO.setIp(ip);
			baseGMListVO.setReason(reason);
			baseGMListVO.setType(type);
			GM_LIST_MAP.put(mac, baseGMListVO);
			return baseGMListVO;
		}
	}

	public static BaseGMListVO getBlackByMac(String mac) {
		return GM_LIST_MAP.get(mac);
	}

	public static void removeBlack(String mac) {
		synchronized (black_mutex) {
			GM_LIST_MAP.remove(mac);
		}
	}

	public static boolean canntSpeak(int group) {
		return group >= BLOCK_ENUM.BLOCK_SPEAK_VALUE;
	}

	public static boolean canntLogin(int group) {
		return group >= BLOCK_ENUM.BLOCK_OPENID_VALUE;
	}
}
