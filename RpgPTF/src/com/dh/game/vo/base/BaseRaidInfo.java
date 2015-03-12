package com.dh.game.vo.base;

import java.util.List;

/**
 * 副本信息
 * 
 * @author Administrator
 * 
 */
public class BaseRaidInfo {
	private int raidid;// 副本id
	private String name; // 副本名称
	private int diff; // 难度
	private int req_level; // 等级要求
	private int req_raid; // 前置副本
	private int chapterid; // 章节
	private int reward; // 过关奖励
	private int perfectreward;

	private String file1;
	private String file2;
	private String file3;

	private String dec; // 胜利说明
	private int combat;// 战斗力描述
	private String url;
	private String icon;
	private String hero;
	private int[] heros;
	private int type;
	private int energy;

	private List<Integer> setMonsIds1;
	private List<Integer> setMonsIds2;
	private List<Integer> setMonsIds3;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getRaidid() {
		return raidid;
	}

	public void setRaidid(int raidid) {
		this.raidid = raidid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDiff() {
		return diff;
	}

	public void setDiff(int diff) {
		this.diff = diff;
	}

	public int getReq_level() {
		return req_level;
	}

	public void setReq_level(int req_level) {
		this.req_level = req_level;
	}

	public int getReq_raid() {
		return req_raid;
	}

	public void setReq_raid(int req_raid) {
		this.req_raid = req_raid;
	}

	public int getChapterid() {
		return chapterid;
	}

	public void setChapterid(int chapterid) {
		this.chapterid = chapterid;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public String getFile1() {
		return file1;
	}

	public void setFile1(String file1) {
		this.file1 = file1;
	}

	public String getFile2() {
		return file2;
	}

	public void setFile2(String file2) {
		this.file2 = file2;
	}

	public String getFile3() {
		return file3;
	}

	public void setFile3(String file3) {
		this.file3 = file3;
	}

	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}

	public int getCombat() {
		return combat;
	}

	public void setCombat(int combat) {
		this.combat = combat;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPerfectreward() {
		return perfectreward;
	}

	public void setPerfectreward(int perfectreward) {
		this.perfectreward = perfectreward;
	}

	public String getHero() {
		return hero;
	}

	public void setHero(String hero) {
		this.hero = hero;
	}

	public int[] getHeros() {
		return heros;
	}

	public void setHeros(int[] heros) {
		this.heros = heros;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public List<Integer> getSetMonsIds1() {
		return setMonsIds1;
	}

	public void setSetMonsIds1(List<Integer> setMonsIds1) {
		this.setMonsIds1 = setMonsIds1;
	}

	public List<Integer> getSetMonsIds2() {
		return setMonsIds2;
	}

	public void setSetMonsIds2(List<Integer> setMonsIds2) {
		this.setMonsIds2 = setMonsIds2;
	}

	public List<Integer> getSetMonsIds3() {
		return setMonsIds3;
	}

	public void setSetMonsIds3(List<Integer> setMonsIds3) {
		this.setMonsIds3 = setMonsIds3;
	}

}
