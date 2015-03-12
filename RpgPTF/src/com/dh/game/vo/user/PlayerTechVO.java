package com.dh.game.vo.user;

import java.util.Date;

import com.dh.game.vo.base.BaseTechVO;

/** 玩家科技 */
public class PlayerTechVO {
	private int playerId;
	// 数组序列[0-11]分别对应0:英雄HP,1:英雄ATK,2:英雄DEF,3:士兵HP,4:士兵ATK,5:士兵DEF,6:空军HP,7:空军ATK,8:空军DEF,9:装甲HP,10:装甲ATK,11:装甲DEF,
	private Date[] cdArray;// 冷却时间队列,每天0点清空
	private BaseTechVO[] baseTechVOs;

	public BaseTechVO[] getBaseTechVOs() {
		return baseTechVOs;
	}

	public void setBaseTechVOs(BaseTechVO[] baseTechVOs) {
		this.baseTechVOs = baseTechVOs;
	}

	public Date[] getCdArray() {
		return cdArray;
	}

	public void setCdArray(Date[] cdArray) {
		this.cdArray = cdArray;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

}
