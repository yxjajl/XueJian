package com.dh.vo.user;

import java.util.ArrayList;
import java.util.List;

import com.dh.game.vo.base.BaseRaidInfo;
import com.dh.game.vo.user.PlayerEliteRaidVO;
import com.dh.game.vo.user.PlayerRaidVO;
import com.dh.resconfig.RaidRes;
import com.dh.util.Tool;

public class UserRaid implements IClear {
	public final static int BATTLE_NON = 0; // 无战斗状态
	public final static int BATTLE_RUN = 1; // 战斗中F
	public final static int BATTLE_END = 2; // 战斗已结束

	public int status = BATTLE_NON; // 当前状态
	public long battleStartTime = 0; // 战斗开始时间
	public String validateKey; // 交验码
	public int raidid; // 当前挑战的副本id
	public int diff; // 当前挑战的副本难度
	public int score=0;

	private List<PlayerRaidVO> raidList;
	private List<PlayerEliteRaidVO> eliteRaidList = new ArrayList<PlayerEliteRaidVO>();
	private int curChapter = -1;
	private int curEliteChapter = -1;
	private long lastCleanOutTime = 0; // 最近一次的扫荡时间

	public int getCurChapter() {
		if (curChapter == -1) {
			curChapter = 1;
			int maxRaid = raidList.get(raidList.size() - 1).getRaidid();

			BaseRaidInfo baseRaidInfo = RaidRes.getInstance().getBaseRaidInfo(maxRaid);
			if (baseRaidInfo != null) {
				curChapter = baseRaidInfo.getChapterid();
			}
		}
		return curChapter;
	}

	public int getMaxRaid() {
		return raidList.get(raidList.size() - 1).getRaidid();
	}

	public int getCurrEliteChapter() {
		if (curEliteChapter == -1) {
			curEliteChapter = 1;
			if (eliteRaidList.size() == 0) {
				return curEliteChapter;
			}
			int maxRaid = eliteRaidList.get(eliteRaidList.size() - 1).getRaidid();

			BaseRaidInfo baseRaidInfo = RaidRes.getInstance().getBaseRaidInfo(maxRaid);
			if (baseRaidInfo != null) {
				curEliteChapter = baseRaidInfo.getChapterid();
			}
		}
		return curEliteChapter;
	}

	/**
	 * 添加副本
	 * 
	 * @param playerRaidVO
	 * @param baseRaidInfo
	 */
	public void appendRaid(PlayerRaidVO playerRaidVO, BaseRaidInfo baseRaidInfo) {
		curChapter = baseRaidInfo.getChapterid();
		raidList.add(playerRaidVO);
	}

	public void appendEliteRaid(PlayerEliteRaidVO playerEliteRaidVO, BaseRaidInfo baseRaidInfo) {
		curEliteChapter = baseRaidInfo.getChapterid();
		eliteRaidList.add(playerEliteRaidVO);
	}

	/**
	 * 战斗开始
	 * 
	 * @param _raidid
	 * @param _diff
	 */
	public void battleStart(int _raidid, int _diff) {
		clearCurRaid();
		status = BATTLE_RUN;
		battleStartTime = System.currentTimeMillis();
		validateKey = Tool.generKey();
		raidid = _raidid;
		diff = _diff;
	}

	/**
	 * 战斗结束
	 * 
	 * @param _raidid
	 * @param _diff
	 * @param key
	 * @return
	 */
	public boolean validate(int _raidid, int _diff, String key,int _result,int score,int addtion) {
		if (BATTLE_RUN != status) {
			return false;
		}
		String v = Tool.MD5(raidid + "&" + validateKey+"&"+_result+"&"+score+"&"+addtion);// raidid + "&" + diff +
														// "&" + validateKey
		System.out.println("sv = " + v);
		System.out.println("cv = " + key);
		if (_raidid == raidid && _diff == diff && v.equals(key)) {
			return true;
		}
		return false;
	}

	// 清理
	public void clearCurRaid() {
		status = BATTLE_NON;
		battleStartTime = 0;
		validateKey = "";
		raidid = 0;
		diff = 0;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getBattleStartTime() {
		return battleStartTime;
	}

	public void setBattleStartTime(long battleStartTime) {
		this.battleStartTime = battleStartTime;
	}

	public String getValidateKey() {
		return validateKey;
	}

	public void setValidateKey(String validateKey) {
		this.validateKey = validateKey;
	}

	public int getRaidid() {
		return raidid;
	}

	public void setRaidid(int raidid) {
		this.raidid = raidid;
	}

	public int getDiff() {
		return diff;
	}

	public void setDiff(int diff) {
		this.diff = diff;
	}

	public List<PlayerRaidVO> getRaidList() {
		return raidList;
	}

	public void setRaidList(List<PlayerRaidVO> raidList) {
		this.raidList = raidList;
	}

	public List<PlayerEliteRaidVO> getEliteRaidList() {
		return eliteRaidList;
	}

	public void setEliteRaidList(List<PlayerEliteRaidVO> eliteRaidList) {
		this.eliteRaidList = eliteRaidList;
	}

	public long getLastCleanOutTime() {
		return lastCleanOutTime;
	}

	public void setLastCleanOutTime(long lastCleanOutTime) {
		this.lastCleanOutTime = lastCleanOutTime;
	}

	/**
	 * 取副本
	 * 
	 * @param raidid
	 * @return
	 */
	public PlayerRaidVO getPlayerRaidVO(int raidid) {
		if (raidList != null && raidList.size() > 0) {
			for (PlayerRaidVO playerRaidVO : raidList) {
				if (playerRaidVO.getRaidid() == raidid) {
					return playerRaidVO;
				}
			}
		}
		return null;
	}

	/**
	 * 取精英副本
	 * 
	 * @param raidid
	 * @return
	 */
	public PlayerEliteRaidVO getPlayerEliteRaidVO(int raidid) {
		if (eliteRaidList != null && eliteRaidList.size() > 0) {
			for (PlayerEliteRaidVO playerEliteRaidVO : eliteRaidList) {
				if (playerEliteRaidVO.getRaidid() == raidid) {
					return playerEliteRaidVO;
				}
			}
		}
		return null;
	}

	public PlayerEliteRaidVO getFirstPlayerEliteRaidVO() {
		if (eliteRaidList != null && eliteRaidList.size() > 0) {
			return eliteRaidList.get(0);
		}
		return null;
	}

	public void clear() {
		if (raidList != null) {
			raidList.clear();
			raidList = null;
		}

		if (eliteRaidList != null) {
			eliteRaidList.clear();
			eliteRaidList = null;
		}
	}

}
