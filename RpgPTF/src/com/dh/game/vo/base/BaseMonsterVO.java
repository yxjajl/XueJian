package com.dh.game.vo.base;

import com.dh.game.vo.BaseProto.FinalHero;

public class BaseMonsterVO {
	private int cfgId;
	private int url;
	private String name;
	private int star;
	private int level;
	private int race;
	private int hp;
	private int def;
	private int mdef;
	private int atk;
	private int matk;
	private int hit;
	private int dodge;
	private int cir;
	private int atk_speed;
	private int atk_range;
	private int skill_id;
	private int move_speed;
	private int beat;
	private int type;

	private int blood;

	private String passiveskill;

	private int hpX;
	private int hpY;
	private int commonatk;
	private int skilllevel;

	private FinalHero.Builder finalHero;

	public int getSkilllevel() {
		return skilllevel;
	}

	public void setSkilllevel(int skilllevel) {
		this.skilllevel = skilllevel;
	}

	public int getCommonatk() {
		return commonatk;
	}

	public void setCommonatk(int commonatk) {
		this.commonatk = commonatk;
	}

	public int getHpX() {
		return hpX;
	}

	public void setHpX(int hpX) {
		this.hpX = hpX;
	}

	public int getHpY() {
		return hpY;
	}

	public void setHpY(int hpY) {
		this.hpY = hpY;
	}

	public int getBlood() {
		return blood;
	}

	public void setBlood(int blood) {
		this.blood = blood;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCfgId() {
		return cfgId;
	}

	public void setCfgId(int cfgId) {
		this.cfgId = cfgId;
	}

	public int getUrl() {
		return url;
	}

	public void setUrl(int url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRace() {
		return race;
	}

	public void setRace(int race) {
		this.race = race;
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

	public int getCir() {
		return cir;
	}

	public void setCir(int cir) {
		this.cir = cir;
	}

	public int getAtk_speed() {
		return atk_speed;
	}

	public void setAtk_speed(int atk_speed) {
		this.atk_speed = atk_speed;
	}

	public int getAtk_range() {
		return atk_range;
	}

	public void setAtk_range(int atk_range) {
		this.atk_range = atk_range;
	}

	public int getSkill_id() {
		return skill_id;
	}

	public void setSkill_id(int skill_id) {
		this.skill_id = skill_id;
	}

	public int getMove_speed() {
		return move_speed;
	}

	public void setMove_speed(int move_speed) {
		this.move_speed = move_speed;
	}

	public int getBeat() {
		return beat;
	}

	public void setBeat(int beat) {
		this.beat = beat;
	}

	public String getPassiveskill() {
		return passiveskill;
	}

	public void setPassiveskill(String passiveskill) {
		this.passiveskill = passiveskill;
	}

	public FinalHero.Builder getFinalHero() {
		return finalHero;
	}

	public void setFinalHero(FinalHero.Builder finalHero) {
		this.finalHero = finalHero;
	}

}
