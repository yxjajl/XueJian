package com.dh.game.vo.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.dh.game.vo.base.BaseLegionVO;
import com.dh.game.vo.legion.LegionProto.MEM_TYPE;

public class LegionVO implements Comparable<LegionVO> {
	private int id;
	private String legionName; // 军团名
	private int playerId; // 团长id
	private String playerNick; // 团长名字
	private int legionLevel; // 军团等级
	private String notice = ""; // 公告
	private String descs = "";// 介绍
	private Date createTime;
	private int destroyTime;
	private int money;// 万为单位
	private int material;
	private String qq = "";
	private int rank;
	private List<LegionMemVO> mems = new ArrayList<LegionMemVO>();
	private List<LegionMemVO> join_mems = new ArrayList<LegionMemVO>();
	private List<LegionMemVO> really_mems = new ArrayList<LegionMemVO>();
	private List<LegionLogVO> logs = new ArrayList<LegionLogVO>();
	private int[] bossKiller = new int[20];// boss击杀,存放击杀玩家playerId
	private String killerStr;
	private int killIndex;// 当前最高等级

	private int maxLogId;
	private BaseLegionVO baseLegionVO = null;
	private Object mem_mutex = new Object();
	private Object money_mutex = new Object();// 捐献银两锁
	private Object material_mutex = new Object();// 捐献建设令锁
	private Object level_mutex = new Object();// 升级锁
	private Object logId_mutex = new Object();
	private Object boss_mutex = new Object();// boss击杀锁
	private int combat;

	public MEM_TYPE isReq(int memId) {
		synchronized (mem_mutex) {
			for (LegionMemVO legionMemVO : join_mems) {
				if (legionMemVO.getPlayerId() == memId) {
					return MEM_TYPE.MEM_TYPE_REQ;
				}
			}
			return MEM_TYPE.MEM_TYPE_NONE;
		}
	}

	public int getBossStatus(int bossId) {
		synchronized (boss_mutex) {
			if (bossId > 20) {
				return 0;
			}
			return bossKiller[bossId - 1];
		}
	}

	public void killBoss(int bossId, int playerId) {
		synchronized (boss_mutex) {
			bossKiller[bossId - 1] = playerId;
		}
	}

	public void clearBossStatus() {
		synchronized (boss_mutex) {
			Arrays.fill(bossKiller, 0);
		}
	}

	/**
	 * 获取管理成员数量
	 * 
	 * @return
	 */
	public int getManageNum(MEM_TYPE type) {
		int num = 0;
		for (LegionMemVO legionMemVO : mems) {
			if (legionMemVO.getType() == type.getNumber()) {
				num++;
			}
		}
		return num++;
	}

	public void initMems() {
		for (LegionMemVO legionMemVO : mems) {
			if (legionMemVO.getType() == MEM_TYPE.MEM_TYPE_REQ_VALUE) {
				join_mems.add(legionMemVO);
			} else {
				really_mems.add(legionMemVO);
			}

		}
	}

	public int getAndIncrLogId() {
		synchronized (logId_mutex) {
			maxLogId++;
			return maxLogId;
		}
	}

	public int addMoney(int num) {
		synchronized (money_mutex) {
			return money += num;
		}
	}

	public int addMaterial(int num) {
		synchronized (material_mutex) {
			return material += num;
		}
	}

	public void addLevel(BaseLegionVO baseLegionVO) {
		synchronized (level_mutex) {
			this.legionLevel = baseLegionVO.getId();
			this.baseLegionVO = baseLegionVO;
		}
	}

	public void removeJoin(LegionMemVO legionMemVO) {
		synchronized (mem_mutex) {
			join_mems.remove(legionMemVO);
		}
	}

	public void addReallyMem(LegionMemVO legionMemVO) {
		synchronized (mem_mutex) {
			really_mems.add(legionMemVO);
		}
	}

	public void addMem(LegionMemVO legionMemVO) {
		if (legionMemVO.getPlayerId()==1001355) {
			System.out.println("fuck");
		}
		synchronized (mem_mutex) {
			mems.add(legionMemVO);
			if (legionMemVO.getType() == MEM_TYPE.MEM_TYPE_REQ_VALUE) {
				join_mems.add(legionMemVO);
			} else {
				really_mems.add(legionMemVO);
			}

		}
	}

	public void removeMem(LegionMemVO legionMemVO) {
		synchronized (mem_mutex) {
			mems.remove(legionMemVO);
			if (legionMemVO.getType() == MEM_TYPE.MEM_TYPE_REQ_VALUE) {
				join_mems.remove(legionMemVO);
			} else {
				really_mems.remove(legionMemVO);
			}

		}
	}

	public LegionMemVO getMem(int memId) {
		synchronized (mem_mutex) {
			for (LegionMemVO legionMemVO : mems) {
				if (legionMemVO.getPlayerId() == memId) {
					return legionMemVO;
				}
			}
		}
		return null;
	}

	@Override
	public int compareTo(LegionVO arg0) {
		if (this.getLegionLevel() != arg0.getLegionLevel()) {
			return this.getLegionLevel() > arg0.getLegionLevel() ? -1 : 1;
		}
		if (this.getMaterial() != arg0.getMaterial()) {
			return this.getMaterial() > arg0.getMaterial() ? -1 : 1;
		}
		if (this.getMoney() != arg0.getMoney()) {
			return this.getMoney() > arg0.getMoney() ? -1 : 1;
		}
		return 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLegionName() {
		return legionName;
	}

	public void setLegionName(String legionName) {
		this.legionName = legionName;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getPlayerNick() {
		return playerNick;
	}

	public void setPlayerNick(String playerNick) {
		this.playerNick = playerNick;
	}

	public int getLegionLevel() {
		return legionLevel;
	}

	public void setLegionLevel(int legionLevel) {
		this.legionLevel = legionLevel;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<LegionMemVO> getRealyMems() {
		return this.really_mems;
	}

	public List<LegionMemVO> getMems() {
		return mems;
	}

	public void setMems(List<LegionMemVO> mems) {
		this.mems = mems;
	}

	public BaseLegionVO getBaseLegionVO() {
		return baseLegionVO;
	}

	public void setBaseLegionVO(BaseLegionVO baseLegionVO) {
		this.baseLegionVO = baseLegionVO;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getMaterial() {
		return material;
	}

	public void setMaterial(int material) {
		this.material = material;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getDestroyTime() {
		return destroyTime;
	}

	public void setDestroyTime(int destroyTime) {
		this.destroyTime = destroyTime;
	}

	public Object getMem_mutex() {
		return mem_mutex;
	}

	public void setMem_mutex(Object mem_mutex) {
		this.mem_mutex = mem_mutex;
	}

	public int getMaxLogId() {
		return maxLogId;
	}

	public void setMaxLogId(int maxLogId) {
		this.maxLogId = maxLogId;
	}

	public List<LegionLogVO> getLogs() {
		return logs;
	}

	public void setLogs(List<LegionLogVO> logs) {
		this.logs = logs;
	}

	public List<LegionMemVO> getJoin_mems() {
		return join_mems;
	}

	public void setJoin_mems(List<LegionMemVO> join_mems) {
		this.join_mems = join_mems;
	}

	public int[] getBossKiller() {
		return bossKiller;
	}

	public void setBossKiller(int[] bossKiller) {
		this.bossKiller = bossKiller;
	}

	public String getKillerStr() {
		return killerStr;
	}

	public void setKillerStr(String killerStr) {
		this.killerStr = killerStr;
	}

	public int getKillIndex() {
		return killIndex;
	}

	public void setKillIndex(int killIndex) {
		this.killIndex = killIndex;
	}

	public int getCombat() {
		return combat;
	}

	public void setCombat(int combat) {
		this.combat = combat;
	}
}