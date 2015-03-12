package com.dh.constants;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingQueue;

import com.dh.Cache.RedisMap;
import com.dh.game.vo.activity.ActivityProto.AnsRankInfo;
import com.dh.game.vo.activity.ActivityProto.AnsRankList;
import com.dh.game.vo.activity.ActivityProto.ChargeNoticeList;
import com.dh.game.vo.activity.ActivityProto.ChargeNoticeList.Builder;
import com.dh.game.vo.activity.ActivityProto.ChargeNoticiInfo;
import com.dh.game.vo.base.BaseAnsVO;
import com.dh.game.vo.user.PlayerActyVO;
import com.dh.game.vo.user.PlayerVO;
import com.dh.game.vo.user.RechargeInfoVO;
import com.dh.resconfig.AnsRes;

public class ActivityConstant {

	public final static int DAY_DONE_LIMIT = 105;
	// public static int VIP_DAY_SIGN_COUNT = 25;
	public final static int NOT_VIP_DAY_SIGN_COUNT = 5;
	public final static int RESIGN_COST = 50;
	public final static int BOSS_CD_ACCR_RMB_COST = 20;// boss加速人民币消耗
	public final static int BOSS_POWER_COST = 5;// boss体力消耗
	public final static int BOSS_ADDTION_COST = 10;// boss号令值增加
	public final static int DAY_DONE_NUM = 14;// 每日必做总数
	public final static int ANS_SCORE_PER = 20;// 每题增加分数
	public final static int ANS_PER_REWARD_RATE = 500;// 每题奖励与玩家当前等级比率
	public final static int BOSS_HP_RATE = 2;
	public static final Queue<RechargeInfoVO> CHARGE_QUEUE = new LinkedBlockingQueue<RechargeInfoVO>(20);
	public static Object LOCK = new Object();
	private static Builder chargeNoticeList = null;
	public final static int CHARGE_LIST_SIZE = 20;
	private final static Object ANS_MUTEX = new Object();// 答案锁
	private final static Object ACTY_OPEN_MUTEX = new Object();// 活动开关锁
	private static int ACTY_STATUS = 0;
	private static List<BaseAnsVO> ANS_LIST = null;
	public final static int ANS_REAWRD_PER_QUESTION = 91014;// 每题奖励

	private final static TIntObjectHashMap<PlayerActyVO> OLD_ACTY_MAP = new TIntObjectHashMap<PlayerActyVO>();// 临时存储playerVO,用于从set中删除元素
	private final static List<PlayerActyVO> RANK_LIST = new ArrayList<PlayerActyVO>();

	// public final static TreeSet<PlayerActyVO> ANS_SORT_SET = new
	// TreeSet<PlayerActyVO>(new Comparator<PlayerActyVO>() {
	// @Override
	// public int compare(PlayerActyVO o1, PlayerActyVO o2) {
	// if (o1.getPlayerid() == o2.getPlayerid()) {
	// return 0;
	// }
	// if (o1.getScore() != o2.getScore()) {
	// return -o1.getScore() + o2.getScore();
	// }
	// if (o1.getLastansdate() != o2.getLastansdate()) {
	// return o1.getLastansdate() > o2.getLastansdate() ? 1 : -1;
	// }
	// return 1;
	// }
	// });

	public static void initAnswers() {
		synchronized (ANS_MUTEX) {
			OLD_ACTY_MAP.clear();
			ANS_LIST = AnsRes.getInstance().createQuestions();
		}
	}

	public static boolean isActyOpen(int index) {
		synchronized (ACTY_OPEN_MUTEX) {
			return ((ACTY_STATUS >> index) & 0x1) == 1;
		}
	}

	public static int getActyStatus() {
		synchronized (ACTY_OPEN_MUTEX) {
			return ACTY_STATUS;
		}
	}

	/** 如果这个活动已经参加过,返回关闭后该活动的状态 */
	public static int getAndCloseActyStatus(int index) {
		synchronized (ACTY_OPEN_MUTEX) {
			return ACTY_STATUS & (~(0x1 << index));
		}
	}

	/**
	 * 根据序数关闭活动
	 * */
	public static int endActy(int index) {
		synchronized (ACTY_OPEN_MUTEX) {
			ACTY_STATUS &= ~(0x1 << index);
			return ACTY_STATUS;
		}
	}

	/**
	 * 根据序数关闭活动
	 * */
	public static int startActy(int index) {
		synchronized (ACTY_OPEN_MUTEX) {
			ACTY_STATUS |= (0x1 << index);
			return ACTY_STATUS;
		}
	}

	/**
	 * 如果玩家回答正确,刷新排行榜
	 * 
	 * @param playerActyVO
	 */

	public static void addAndUpPlayerAns1(PlayerActyVO playerActyVO) {
		synchronized (ANS_MUTEX) {
		}
	}

	public static void addAndUpPlayerAns(PlayerActyVO playerActyVO) {
		synchronized (ANS_MUTEX) {
			if (RANK_LIST.remove(playerActyVO)) {// 替换本身的
				RANK_LIST.add(playerActyVO);
			} else {
				if (RANK_LIST.size() >= 10) {
					RANK_LIST.remove(9);
					RANK_LIST.add(playerActyVO);
				} else {
					RANK_LIST.add(playerActyVO);
				}
			}
			Collections.sort(RANK_LIST, new Mycomparator());
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			PlayerActyVO actyVO = new PlayerActyVO();
			actyVO.setPlayerid(i);
			actyVO.setScore(new Random().nextInt(100));
			addAndUpPlayerAns(actyVO);
		}
		for (int i = 0; i < 10; i++) {
			PlayerActyVO actyVO = new PlayerActyVO();
			actyVO.setPlayerid(i);
			actyVO.setScore(new Random().nextInt(100));
			addAndUpPlayerAns(actyVO);
		}
		for (int i = 0; i < 10; i++) {
			PlayerActyVO actyVO = new PlayerActyVO();
			actyVO.setPlayerid(i);
			actyVO.setScore(new Random().nextInt(100));
			addAndUpPlayerAns1(actyVO);
		}
		for (int i = 0; i < 10; i++) {
			PlayerActyVO actyVO = new PlayerActyVO();
			actyVO.setPlayerid(i);
			actyVO.setScore(new Random().nextInt(100));
			addAndUpPlayerAns1(actyVO);
		}
		for (PlayerActyVO actyVO : RANK_LIST) {
			System.out.print("playerId:" + actyVO.getPlayerid() + "\tscore" + actyVO.getScore());
		}
	}

	public static BaseAnsVO getQuestionByIndex(int index) {
		return ANS_LIST.get(index - 1);
	}

	public static AnsRankList.Builder getTop10AnsBuild() {
		Iterator<PlayerActyVO> it = RANK_LIST.iterator();
		int i = 1;
		PlayerActyVO actyVO;
		PlayerVO playerVO = null;
		AnsRankList.Builder rankBuild = AnsRankList.newBuilder();
		while (it.hasNext() && i <= 10) {
			actyVO = it.next();
			playerVO = RedisMap.getPlayerVObyId(actyVO.getPlayerid());
			rankBuild.addRanks(AnsRankInfo.newBuilder().setRank(i).setScore(actyVO.getScore()).setName(playerVO.getName()));
			i++;
		}
		return rankBuild;
	}

	/** 获得答题前三玩家 */
	public static List<PlayerActyVO> getTop3AnsList() {
		Iterator<PlayerActyVO> it = RANK_LIST.iterator();
		int i = 1;
		ArrayList<PlayerActyVO> top3List = new ArrayList<PlayerActyVO>(3);
		PlayerActyVO actyVO;
		while (it.hasNext() && i <= 3) {
			actyVO = it.next();
			top3List.add(actyVO);
			i++;
		}
		return top3List;
	}

	public static void addCharge(RechargeInfoVO rechargeInfoVO) {
		synchronized (LOCK) {
			while (CHARGE_QUEUE.size() >= CHARGE_LIST_SIZE) {
				CHARGE_QUEUE.poll();
			}
			CHARGE_QUEUE.offer(rechargeInfoVO);
			// System.out.println("当前size: " + CHARGE_QUEUE.size());
		}
		createChargeList();
	}

	public static Builder getChargeNoticeList() {
		synchronized (LOCK) {
			return chargeNoticeList;
		}
	}

	public static void createChargeList() {
		synchronized (LOCK) {
			chargeNoticeList = ChargeNoticeList.newBuilder();
			Iterator<RechargeInfoVO> it = CHARGE_QUEUE.iterator();
			while (it.hasNext()) {
				RechargeInfoVO r = it.next();
				chargeNoticeList.addChargeNoticiInfo(ChargeNoticiInfo.newBuilder().setName(r.getName()).setNum(r.getGoldingot()));
			}
		}

	}
}

class Mycomparator implements Comparator<PlayerActyVO> {

	@Override
	public int compare(PlayerActyVO o1, PlayerActyVO o2) {
		if (o1.getPlayerid() == o2.getPlayerid()) {
			return 0;
		}
		if (o1.getScore() != o2.getScore()) {
			return -o1.getScore() + o2.getScore();
		}
		if (o1.getLastansdate() != o2.getLastansdate()) {
			return o1.getLastansdate() > o2.getLastansdate() ? 1 : -1;
		}
		return 1;
	}

}
