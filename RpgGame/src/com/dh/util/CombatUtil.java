package com.dh.util;

import org.apache.log4j.Logger;

import com.dh.constants.ItemConstants;
import com.dh.constants.SkillConstants;
import com.dh.game.vo.base.BaseEnhanceVO;
import com.dh.game.vo.base.BaseGrowthVO;
import com.dh.game.vo.base.BaseItemVO;
import com.dh.game.vo.base.BaseProteVO;
import com.dh.game.vo.base.HeroPreNameVO;
import com.dh.game.vo.base.PassivesSkillVO;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.game.vo.user.PlayerKnapsackVO;
import com.dh.resconfig.HeroPreNameRes;
import com.dh.vo.user.UserCached;

/**
 * 计算士兵英雄的终极属性和战斗力
 * 
 * @author RickYu
 * 
 */
public class CombatUtil {
	private final static Logger LOGGER = Logger.getLogger(CombatUtil.class);
	public final static float FUJIANGJIACHENG = 0.025f; // 副将加成

	public static void equipCombat(PlayerKnapsackVO playerKnapsackVO) {
		if (playerKnapsackVO.getBaseItemVO().getType() != ItemConstants.ITEM_TYPE_EQPI) {
			return;
		}

		int hp = playerKnapsackVO.getBaseItemVO().getHp();
		int def = playerKnapsackVO.getBaseItemVO().getDef();
		int mdef = playerKnapsackVO.getBaseItemVO().getMdef();
		int atk = playerKnapsackVO.getBaseItemVO().getAtk();
		int matk = playerKnapsackVO.getBaseItemVO().getMatk();
		int hit = playerKnapsackVO.getBaseItemVO().getHit();
		int dodge = playerKnapsackVO.getBaseItemVO().getDodge();
		int cir_rate = playerKnapsackVO.getBaseItemVO().getCir_rate();

		int growth_hp = 0;
		int growth_def = 0;
		int growth_mdef = 0;
		int growth_atk = 0;
		int growth_matk = 0;
		int growth_hit = 0;
		int growth_dodge = 0;
		int growth_cir_rate = 0;

		BaseEnhanceVO baseEnhanceVO = playerKnapsackVO.getBaseEnhanceVO();
		if (baseEnhanceVO != null) {
			growth_hp = baseEnhanceVO.getHp() * (playerKnapsackVO.getEnhance());
			growth_def = baseEnhanceVO.getDef() * (playerKnapsackVO.getEnhance());
			growth_mdef = baseEnhanceVO.getMdef() * (playerKnapsackVO.getEnhance());
			growth_atk = baseEnhanceVO.getAtk() * (playerKnapsackVO.getEnhance());
			growth_matk = baseEnhanceVO.getMatk() * (playerKnapsackVO.getEnhance());
			growth_hit = baseEnhanceVO.getHit() * (playerKnapsackVO.getEnhance());
			growth_dodge = baseEnhanceVO.getDodge() * (playerKnapsackVO.getEnhance());
			growth_cir_rate = baseEnhanceVO.getCir_rate() * (playerKnapsackVO.getEnhance());
		}

		BaseProteVO baseProteVO = playerKnapsackVO.getBaseProteVO();
		int protect = baseProteVO.getStats();

		playerKnapsackVO.setAdd_hp(growth_hp + growth_hp * protect / 100);
		playerKnapsackVO.setAdd_def(growth_def + growth_def * protect / 100);
		playerKnapsackVO.setAdd_mdef(growth_mdef + growth_mdef * protect / 100);
		playerKnapsackVO.setAdd_atk(growth_atk + growth_atk * protect / 100);
		playerKnapsackVO.setAdd_matk(growth_matk + growth_matk * protect / 100);
		playerKnapsackVO.setAdd_hit(growth_hit + growth_hit * protect / 100);
		playerKnapsackVO.setAdd_dodge(growth_dodge + growth_dodge * protect / 100);
		playerKnapsackVO.setAdd_cir_rate(growth_cir_rate);
		// playerKnapsackVO.setAdd_cir_rate(cir_rate * protect / 100);

		hp = (hp + growth_hp);
		def = (def + growth_def);
		mdef = (mdef + growth_mdef);
		atk = (atk + growth_atk);
		matk = (matk + growth_matk);
		hit = (hit + growth_hit);
		dodge = (dodge + growth_dodge);
		cir_rate = (cir_rate + growth_cir_rate);

		hp += (hp) * protect / 100;
		def += (def) * protect / 100;
		mdef += (mdef) * protect / 100;
		atk += (atk) * protect / 100;
		matk += (matk) * protect / 100;
		hit += (hit) * protect / 100;
		dodge += (dodge) * protect / 100;
		cir_rate += (cir_rate) * protect / 100;

		for (BaseItemVO gemItemVO : playerKnapsackVO.getGem()) {
			if (gemItemVO != null) {
				hp += gemItemVO.getHp();
				def += gemItemVO.getDef();
				mdef += gemItemVO.getMdef();
				atk += gemItemVO.getAtk();
				matk += gemItemVO.getMatk();
				hit += gemItemVO.getHit();
				dodge += gemItemVO.getDodge();
				cir_rate += gemItemVO.getCir_rate();
			}
		}

		playerKnapsackVO.setFinal_hp(hp);
		playerKnapsackVO.setFinal_def(def);
		playerKnapsackVO.setFinal_mdef(mdef);
		playerKnapsackVO.setFinal_atk(atk);
		playerKnapsackVO.setFinal_matk(matk);
		playerKnapsackVO.setFinal_hit(hit);
		playerKnapsackVO.setFinal_dodge(dodge);
		playerKnapsackVO.setFinal_cir_rate(cir_rate);

		int combat = computerCombat(hp, atk, def, matk, mdef, cir_rate, hit, dodge);
		playerKnapsackVO.setCombat(combat);
	}

	/**
	 * 计算英雄属性
	 * 
	 * @param playerHeroVO
	 */
	public static int heroCombat(PlayerHeroVO playerHeroVO, UserCached userCached, int hp, int atk, int def, int matk, int mdef, int cir_rate, int hit, int dodge) {

		if (playerHeroVO.getEquipList() != null && playerHeroVO.getEquipList().size() > 0) {
			for (PlayerKnapsackVO playerKnapsackVO : playerHeroVO.getEquipList()) {
				hp += playerKnapsackVO.getFinal_hp();
				def += playerKnapsackVO.getFinal_def();
				mdef += playerKnapsackVO.getFinal_mdef();
				atk += playerKnapsackVO.getFinal_atk();
				matk += playerKnapsackVO.getFinal_matk();
				hit += playerKnapsackVO.getFinal_hit();
				dodge += playerKnapsackVO.getFinal_dodge();
				cir_rate += playerKnapsackVO.getFinal_cir_rate();
			}
		}

		int combat = computerCombat(hp, atk, def, matk, mdef, cir_rate, hit, dodge);
		return combat;
	}

	public static void heroCombat(PlayerHeroVO playerHeroVO, UserCached userCached) {
		BaseGrowthVO baseGrowthVO = playerHeroVO.getBaseGrowthVO();
		int hp = playerHeroVO.getHp();
		int def = playerHeroVO.getDef();
		int mdef = playerHeroVO.getMdef();
		int atk = playerHeroVO.getAtk();
		int matk = playerHeroVO.getMatk();
		int hit = playerHeroVO.getHit();
		int dodge = playerHeroVO.getDodge();
		int cir_rate = playerHeroVO.getCir_rate();

		if (playerHeroVO.getPrefix() != 0) {
			int cc = 1;
			HeroPreNameVO HeroPreNameVO = null;
			if (playerHeroVO.getPrefix() < 0) {
				cc = -1;
				HeroPreNameVO = HeroPreNameRes.getInstance().getHeroPreNameVO(-playerHeroVO.getPrefix());
			} else {
				HeroPreNameVO = HeroPreNameRes.getInstance().getHeroPreNameVO(playerHeroVO.getPrefix());
			}

			switch (HeroPreNameVO.getType()) {
			case 1:
				hp = ((int) (hp + hp * cc * HeroPreNameVO.getPercent() / 100));
				break;
			case 2:
				def = ((int) (def + def * cc * HeroPreNameVO.getPercent() / 100) + 0);
				break;
			case 3:
				mdef = ((int) (mdef + mdef * cc * HeroPreNameVO.getPercent() / 100));
				break;
			case 4:
				atk = ((int) (atk + atk * cc * HeroPreNameVO.getPercent() / 100));
				break;
			case 5:
				matk = ((int) (matk + matk * cc * HeroPreNameVO.getPercent() / 100));
				break;
			case 6:
				hit = ((int) (hit + hit * cc * HeroPreNameVO.getPercent() / 100));
				break;
			case 7:
				dodge = ((int) (dodge + dodge * cc * HeroPreNameVO.getPercent() / 100));
				break;
			case 8:
				cir_rate = ((int) (cir_rate + cir_rate * cc * HeroPreNameVO.getPercent() / 100));
				break;
			}
		}

		hp += baseGrowthVO.getHp() * (playerHeroVO.getLevel() - 1);
		def += baseGrowthVO.getDef() * (playerHeroVO.getLevel() - 1);
		mdef += baseGrowthVO.getMdef() * (playerHeroVO.getLevel() - 1);
		atk += baseGrowthVO.getAtk() * (playerHeroVO.getLevel() - 1);
		matk += baseGrowthVO.getMatk() * (playerHeroVO.getLevel() - 1);
		hit += baseGrowthVO.getHit() * (playerHeroVO.getLevel() - 1);
		dodge += baseGrowthVO.getDodge() * (playerHeroVO.getLevel() - 1);
		cir_rate += baseGrowthVO.getCir_rate() * (playerHeroVO.getLevel() - 1);

		if (playerHeroVO.getEquipList() != null && playerHeroVO.getEquipList().size() > 0) {
			for (PlayerKnapsackVO playerKnapsackVO : playerHeroVO.getEquipList()) {
				hp += playerKnapsackVO.getFinal_hp();
				def += playerKnapsackVO.getFinal_def();
				mdef += playerKnapsackVO.getFinal_mdef();
				atk += playerKnapsackVO.getFinal_atk();
				matk += playerKnapsackVO.getFinal_matk();
				hit += playerKnapsackVO.getFinal_hit();
				dodge += playerKnapsackVO.getFinal_dodge();
				cir_rate += playerKnapsackVO.getFinal_cir_rate();
			}
		}

		// 被动技能
		if (playerHeroVO.getNpassivesSkill() != null && playerHeroVO.getNpassivesSkill().length > 0) {
			for (PassivesSkillVO psv : playerHeroVO.getNpassivesSkill()) {
				if (psv != null && psv.getType() == SkillConstants.PS_TYPE_INIT && psv.getBufType() > 0) {
					// 加属性
					switch (psv.getBufType()) {
					case SkillConstants.PS_ID_HP: // 生命上限
						hp = hp * psv.getValue() / 10000 + hp;
						break;
					case SkillConstants.PS_ID_MATK:// 技能攻击上限
						matk = matk * psv.getValue() / 10000 + matk;
						break;
					case SkillConstants.PS_ID_ATK:// 物攻上限
						atk = atk * psv.getValue() / 10000 + atk;
						break;
					case SkillConstants.PS_ID_DEF:// 物防上限
						def = def * psv.getValue() / 10000 + def;
						break;
					case SkillConstants.PS_ID_MDEF:// 技能防御上限
						mdef = mdef * psv.getValue() / 10000 + mdef;
						break;
					case SkillConstants.PS_ID_DODGE:// 闪避上限
						dodge = dodge * psv.getValue() / 10000 + dodge;
						break;
					case SkillConstants.PS_ID_HIT:// 命中上限
						hit = hit * psv.getValue() / 10000 + hit;
						break;
					default:
						LOGGER.error("错误的初始被动技能 :" + psv.getId() + "," + psv.getValue());
						break;
					}

				}
			}
		}

		playerHeroVO.setFinal_hp(hp);
		playerHeroVO.setFinal_def(def);
		playerHeroVO.setFinal_mdef(mdef);
		playerHeroVO.setFinal_atk(atk);
		playerHeroVO.setFinal_matk(matk);
		playerHeroVO.setFinal_hit(hit);
		playerHeroVO.setFinal_dodge(dodge);
		playerHeroVO.setFinal_cir_rate(cir_rate);

		int combat = computerCombat(hp, atk, def, matk, mdef, cir_rate, hit, dodge);
		playerHeroVO.setCombat(combat);
	}

	public static int computerCombat(int hp, int atk, int def, int matk, int mdef, int cir_rate, int hit, int dodge) {
		return (int) (hp * 1 + atk * 1 + def * 1 + matk * 1 + mdef * 1 + cir_rate * 0.07 + hit * 0.15 + dodge * 0.15);
	}

	public static boolean playerCombat(UserCached userCached) {
		int oldCombat = userCached.getPlayerVO().getCombat();
		int combat = 0; // 角色战斗力

		if (CodeTool.isNotEmpty(userCached.getUserHero().getAtkHeroList())) {
			for (PlayerHeroVO playerHeroVO : userCached.getUserHero().getAtkHeroList()) {
				combat += playerHeroVO.getCombat();
			}
		}

		if (oldCombat != combat) {
			userCached.getPlayerVO().setCombat(combat);
			return true;
		}

		return false;
	}
}
