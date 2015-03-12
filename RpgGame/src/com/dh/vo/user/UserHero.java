package com.dh.vo.user;

import java.util.ArrayList;
import java.util.List;

import com.dh.constants.CommonConstants;
import com.dh.game.vo.base.PassivesSkillVO;
import com.dh.game.vo.user.PlayerHeroHangVO;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.service.HeroHangService;

public class UserHero implements IClear {
	private int maxHeroId = 0;
	private List<PlayerHeroVO> heroList = new ArrayList<PlayerHeroVO>(); // 英雄列表
	private List<PlayerHeroHangVO> heroHangList; // 英雄挂机休息
	// private HeroTeamVO heroTeamVO; // 阵容
	private List<PlayerHeroVO> atkHeroList = new ArrayList<PlayerHeroVO>(5); // 副本阵容
	// private List<PlayerHeroVO> raidHeroList = new ArrayList<PlayerHeroVO>(5);
	// // 副本阵容
	// private List<PlayerHeroVO> pvpAtkHeroList = new
	// ArrayList<PlayerHeroVO>(5); // 竞技攻击阵容
	// private List<PlayerHeroVO> defHeros = null;

	private String atkLine;

	private int replaceSkillplayerId = 0; // 更新技能的英雄id
	private PassivesSkillVO[] replaceList = null; // 技能列表

	/**
	 * 找英雄
	 * 
	 * @param cfgId
	 * @return
	 */
	public PlayerHeroVO getPlayerHeroVOById(int id) {
		if (heroList != null && heroList.size() > 0) {
			for (PlayerHeroVO playerHeroVO : heroList) {
				if (playerHeroVO.getId() == id) {
					return playerHeroVO;
				}
			}
		}
		return null;
	}

	public int getCombat() {
		int combat = 0;
		for (PlayerHeroVO playerHeroVO : atkHeroList) {
			combat += playerHeroVO.getCombat();
		}

		return combat;
	}

	/**
	 * 查找英雄数量
	 * 
	 * @param cfgId
	 * @return
	 */
	public int getPlayerHeroNumByCfgId(int id, int cfgId, int level) {
		int result = 0;
		if (heroList != null && heroList.size() > 0) {
			for (PlayerHeroVO playerHeroVO : heroList) {
				if (playerHeroVO.getId() != id && playerHeroVO.getCfgId() == cfgId && playerHeroVO.getLevel() >= level && playerHeroVO.getHang_status() == CommonConstants.HANG_STATUS_NORMAL) { // 空闲英雄
					result++;
				}
			}
		}
		return result;
	}

	public PlayerHeroVO getPlayerHeroByCfgIdAndLevel(int id, int cfgId, int level) {
		if (heroList != null && heroList.size() > 0) {
			for (PlayerHeroVO playerHeroVO : heroList) {
				if (playerHeroVO.getId() != id && playerHeroVO.getCfgId() == cfgId && playerHeroVO.getLevel() >= level && playerHeroVO.getHang_status() == CommonConstants.HANG_STATUS_NORMAL) { // 空闲英雄
					return playerHeroVO;
				}
			}
		}
		return null;
	}

	/**
	 * 获取空闲的英雄休息位
	 * 
	 * @return
	 */
	public PlayerHeroHangVO getFreeHeroHang() {
		for (PlayerHeroHangVO playerHeroHangVO : heroHangList) {
			if (playerHeroHangVO.getIsHang() == HeroHangService.HERO_STATUS_OFF) {
				return playerHeroHangVO;
			}
		}
		return null;
	}

	public PlayerHeroHangVO getFreeHeroHang(int heroHangId) {
		PlayerHeroHangVO temp = null;
		for (PlayerHeroHangVO playerHeroHangVO : heroHangList) {
			if (heroHangId == playerHeroHangVO.getHeroHangId()) {
				temp = playerHeroHangVO;
				break;
			}
		}
		if (temp != null && temp.getIsHang() == HeroHangService.HERO_STATUS_OFF) {
			return temp;
		}

		return null;
	}

	public PlayerHeroHangVO getHeroHangById(int heroHangId) {
		PlayerHeroHangVO temp = null;
		for (PlayerHeroHangVO playerHeroHangVO : heroHangList) {
			if (heroHangId == playerHeroHangVO.getHeroHangId()) {
				temp = playerHeroHangVO;
				break;
			}
		}

		return temp;
	}

	public PlayerHeroHangVO getHeroHangByHeroId(int heroId) {
		PlayerHeroHangVO temp = null;
		for (PlayerHeroHangVO playerHeroHangVO : heroHangList) {
			if (heroId == playerHeroHangVO.getHeroId()) {
				temp = playerHeroHangVO;
				break;
			}
		}

		return temp;
	}

	//
	// public List<PlayerHeroVO> getDefHeroByResId(int resId) {
	// List<PlayerHeroVO> defHeros = new ArrayList<PlayerHeroVO>();
	// for (PlayerHeroVO playerHeroVO : heroList) {
	// if (playerHeroVO.getLineStatus() == resId) {
	// defHeros.add(playerHeroVO);
	// }
	// }
	// return defHeros;
	// }

	/**
	 * 生成英雄id
	 * 
	 * @return
	 */
	public int generHeroId() {
		maxHeroId++;
		return maxHeroId;
	}

	/**
	 * 初始化攻击队形
	 */
	public void initTeam() {
		atkHeroList.clear();
		if (heroList != null) {
			int n = 0;
			for (PlayerHeroVO playerHeroVO : heroList) {
				if (playerHeroVO.getLineStatus() == CommonConstants.ATK_LINE_START) {
					if (n >= 5) {
						playerHeroVO.setLineStatus(CommonConstants.HERO_LINE_FREE);
						System.err.println("=======initTeam==========error grant 5");
						continue;
					}
					atkHeroList.add(playerHeroVO);
					n++;
				}
			}
		}
	}

	public void clearTeam() {
		if (heroList != null) {
			for (PlayerHeroVO playerHeroVO : atkHeroList) {
				playerHeroVO.setLineStatus(CommonConstants.HERO_LINE_FREE);
			}
		}
	}

	/**
	 * @param resId
	 * @return
	 */
	public List<PlayerHeroVO> getDefendHerosByResId(int resId) {
		List<PlayerHeroVO> tempHeros = new ArrayList<PlayerHeroVO>(5);
		for (PlayerHeroVO playerHeroVO : heroList) {
			if (playerHeroVO.getPlayerId() > 0) {
				if (playerHeroVO.getLineStatus() == resId) {
					tempHeros.add(playerHeroVO);
				}
			} else {// 机器人
				tempHeros.add(playerHeroVO);
			}

		}
		return tempHeros;
	}

	public void deletePlayerHero(PlayerHeroVO playerHeroVO) {
		heroList.remove(playerHeroVO);
		atkHeroList.remove(playerHeroVO);

	}

	/**
	 * 查找
	 * 
	 * @param id
	 * @return
	 */
	public PassivesSkillVO findReplacePassiveSkillVO(int id) {
		if (replaceList == null) {
			return null;
		}
		for (PassivesSkillVO temp : replaceList) {
			if (temp != null && temp.getId() == id) {
				return temp;
			}
		}

		return null;
	}

	public List<PlayerHeroVO> getHeroList() {
		return heroList;
	}

	public void setHeroList(List<PlayerHeroVO> heroList) {
		this.heroList = heroList;
	}

	public void setMaxHeroId(int maxHeroId) {
		this.maxHeroId = maxHeroId;
	}

	public List<PlayerHeroHangVO> getHeroHangList() {
		return heroHangList;
	}

	public void setHeroHangList(List<PlayerHeroHangVO> heroHangList) {
		this.heroHangList = heroHangList;
	}

	public int getMaxHeroId() {
		return maxHeroId;
	}

	public int getReplaceSkillplayerId() {
		return replaceSkillplayerId;
	}

	public void setReplaceSkillplayerId(int replaceSkillplayerId) {
		this.replaceSkillplayerId = replaceSkillplayerId;
	}

	public PassivesSkillVO[] getReplaceList() {
		return replaceList;
	}

	public void setReplaceList(PassivesSkillVO[] replaceList) {
		this.replaceList = replaceList;
	}

	@Override
	public void clear() {
		if (heroList != null) {
			heroList.clear();
			heroList = null;
		}

		if (heroHangList != null) {
			heroHangList.clear();
			heroHangList = null;
		}

		atkHeroList.clear();
	}

	public String getAtkLine() {
		return atkLine;
	}

	public void setAtkLine(String atkLine) {
		this.atkLine = atkLine;
	}

	public List<PlayerHeroVO> getAtkHeroList() {
		return atkHeroList;
	}

	public void setAtkHeroList(List<PlayerHeroVO> atkHeroList) {
		this.atkHeroList = atkHeroList;
	}

	// public List<PlayerHeroVO> getDefHeros() {
	// return defHeros;
	// }
	//
	// public void setDefHeros(List<PlayerHeroVO> defHeros) {
	// this.defHeros = defHeros;
	// }
}
