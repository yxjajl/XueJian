package com.dh.game.vo.base;

public class BaseEnhanceVO {
	private int cfgId;
	private int hp;
	private int def;
	private int mdef;
	private int atk;
	private int matk;
	private int hit; // 命中
	private int dodge; // 闪避
	private int cir_rate; // 暴击率

	public int getCfgId() {
		return cfgId;
	}

	public void setCfgId(int cfgId) {
		this.cfgId = cfgId;
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

}
