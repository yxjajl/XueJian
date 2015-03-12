package com.dh.game.vo.user;

public class PlayerHeroDefVO {
	private int id;
	private int playerId;
	private String name;
	private int cfgId;
	private int level;
	private int star;

	private int hp;
	private int def;
	private int mdef;
	private int atk;
	private int matk;
	private int hit;
	private int dodge;
	private int cir_rate;
	private int combat = 0;
	private String passivesSkill = "";
	private int skillLevel = 1;

	private int defendStatus;

	private int yzhp;
	private int yzanger;

	public int getYzhp() {
		return yzhp;
	}

	public void setYzhp(int yzhp) {
		this.yzhp = yzhp;
	}

	public int getYzanger() {
		return yzanger;
	}

	public void setYzanger(int yzanger) {
		this.yzanger = yzanger;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getCfgId() {
		return cfgId;
	}

	public void setCfgId(int cfgId) {
		this.cfgId = cfgId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getDef() {
		return def;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public int getMdef() {
		return mdef;
	}

	public void setMdef(int mdef) {
		this.mdef = mdef;
	}

	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}

	public int getMatk() {
		return matk;
	}

	public void setMatk(int matk) {
		this.matk = matk;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public int getDodge() {
		return dodge;
	}

	public void setDodge(int dodge) {
		this.dodge = dodge;
	}

	public int getCir_rate() {
		return cir_rate;
	}

	public void setCir_rate(int cir_rate) {
		this.cir_rate = cir_rate;
	}

	public int getCombat() {
		return combat;
	}

	public void setCombat(int combat) {
		this.combat = combat;
	}

	public String getPassivesSkill() {
		return passivesSkill;
	}

	public void setPassivesSkill(String passivesSkill) {
		this.passivesSkill = passivesSkill;
	}

	public int getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}

	public int getDefendStatus() {
		return defendStatus;
	}

	public void setDefendStatus(int defendStatus) {
		this.defendStatus = defendStatus;
	}

}
