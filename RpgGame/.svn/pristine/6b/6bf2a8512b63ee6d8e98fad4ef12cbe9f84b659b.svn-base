package com.dh.processor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.constants.ItemConstants;
import com.dh.enums.GMIOEnum;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.constant.TaskConstant;
import com.dh.game.vo.base.BaseHoleVO;
import com.dh.game.vo.base.BaseItemVO;
import com.dh.game.vo.base.BaseProteVO;
import com.dh.game.vo.item.ItemProto.GemPickRequest;
import com.dh.game.vo.item.ItemProto.GemSetRequest;
import com.dh.game.vo.item.ItemProto.OpenHoleRequest;
import com.dh.game.vo.item.ItemProto.PROTECTTYPE;
import com.dh.game.vo.item.ItemProto.ProtectionRequest;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.game.vo.user.PlayerKnapsackVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.HoleRes;
import com.dh.resconfig.ItemRes;
import com.dh.resconfig.ProtectionRes;
import com.dh.service.HeroService;
import com.dh.service.KnapsackService;
import com.dh.service.PlayerAccountService;
import com.dh.util.CommandUtil;
import com.dh.util.MyClassLoaderUtil;
import com.dh.util.RandomUtil;
import com.dh.vo.user.UserCached;

@Component
public class GemProcessor {

	@Resource
	private KnapsackService knapsackService;
	@Resource
	private PlayerAccountService playerAccountService;
	@Resource
	private HeroService heroService;

	/**
	 * 装备护佑
	 * 
	 * @param request
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void protectItem(ProtectionRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		int heroId = request.getHeroId();
		int itemId = request.getEquipItemId();

		PlayerHeroVO playerHeroVO = null;
		if (heroId > 0) {
			playerHeroVO = userCached.getUserHero().getPlayerHeroVOById(heroId);
			if (playerHeroVO == null) {
				throw new GameException(AlertEnum.HERO_NOT_FOUND);
			}
		}

		PlayerKnapsackVO playerKnapsackVO = null;
		if (heroId > 0) {
			playerKnapsackVO = knapsackService.findplayerKnapsackById(playerHeroVO.getEquipList(), itemId);
		} else {
			playerKnapsackVO = knapsackService.findplayerKnapsackById(userCached.getUserKnapsack().getKnapsackList(), itemId);
		}

		if (playerKnapsackVO == null || playerKnapsackVO.getBaseItemVO().getType() != ItemConstants.ITEM_TYPE_EQPI) {
			throw new GameException(AlertEnum.EQUIP_NOT_FOUND);
		}

		if (playerKnapsackVO.getBaseItemVO().getSubType() == ItemConstants.ITEM_SUBTYPE_SIPING) { // 饰品暂不参与护佑(护佑不在暴击)
			throw new GameException(AlertEnum.NOT_PROECT_SIPING);
		}

		BaseProteVO baseProteVO = playerKnapsackVO.getBaseProteVO();

		if (baseProteVO.getProtectionExp() == 0) {
			// throw new Exception("护佑已经到顶了，不能再升了" + baseProteVO.getId());
			throw new GameException(AlertEnum.PROCTION_LIMIT);
		}

		// equipmentStar
		if (playerKnapsackVO.getBaseItemVO().getStar() < baseProteVO.getEquipmentStar()) {
			throw new GameException(AlertEnum.EQUIPMENT_STAR_ENOUGH);
		}

		if (PROTECTTYPE.RUNE == request.getType()) { // 符文
			playerAccountService.hasEnoughMoney(userCached, baseProteVO.getMoney());
			if (!knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), baseProteVO.getItemId(), baseProteVO.getItemNumber())) {
				throw new GameException(AlertEnum.ITEM_NUM_NOT);
			}

			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, baseProteVO.getItemExp(), userCached.getPlayerAccountVO(), commandList, "装备护佑");
			knapsackService.removeItem(userCached, baseProteVO.getItemId(), baseProteVO.getItemNumber(), commandList);

			int curExp = playerKnapsackVO.getProtectExp();
			curExp += baseProteVO.getItemExp();

			if (curExp >= baseProteVO.getProtectionExp()) { // 升级
				curExp = 0;
				playerKnapsackVO.setProtectLevel(playerKnapsackVO.getProtectLevel() + 1);
				baseProteVO = ProtectionRes.getInstance().getBaseProteVO(playerKnapsackVO.getProtectLevel());
				playerKnapsackVO.setBaseProteVO(baseProteVO);
			}

			playerKnapsackVO.setProtectExp(curExp);

		} else { // 元宝
			playerAccountService.hasEnoughRMBAndGift(userCached, baseProteVO.getRmb());

			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_RMB, baseProteVO.getRmb(), userCached.getPlayerAccountVO(), commandList, "装备护佑", GMIOEnum.OUT_EQUIP_PROTECT.usage());

			int value = baseProteVO.getRmbExp();

			// 暴击
			if (RandomUtil.randomInt(100) <= baseProteVO.getCritRate()) {
				value = baseProteVO.getCritNumber();
			}

			int curExp = playerKnapsackVO.getProtectExp();
			curExp += value;

			if (curExp >= baseProteVO.getProtectionExp()) { // 升级
				curExp = 0;
				playerKnapsackVO.setProtectLevel(playerKnapsackVO.getProtectLevel() + 1);
				baseProteVO = ProtectionRes.getInstance().getBaseProteVO(playerKnapsackVO.getProtectLevel());
				playerKnapsackVO.setBaseProteVO(baseProteVO);
			}

			playerKnapsackVO.setProtectExp(curExp);
		}

		List<PlayerKnapsackVO> updateList = new ArrayList<PlayerKnapsackVO>();
		updateList.add(playerKnapsackVO);
		knapsackService.saveKnapsack(null, updateList, null, commandList);
		// 镶嵌完成
		CommandUtil.packageNettyMessage(CSCommandConstant.PROTECT_ITEM, null, commandList);

		// 重新计算属性
		if (heroId > 0 && playerHeroVO != null) {
			// 更新英雄属性
			heroService.updateHeroCombat(userCached, playerHeroVO, commandList);
		}

		MyClassLoaderUtil.getInstance().getTaskCheck()
				.changTaskByReQType(userCached, TaskConstant.TASK_NEQ_NHUYOU, playerKnapsackVO.getBaseItemVO().getSubType(), playerKnapsackVO.getProtectLevel(), commandList);
	}

	/**
	 * 开孔
	 * 
	 * @param request
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void openHole(OpenHoleRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		int heroId = request.getHeroId();
		int itemId = request.getEquipItemId();
		int pos = request.getPos() - 1;

		PlayerHeroVO playerHeroVO = null;
		if (heroId > 0) {
			playerHeroVO = userCached.getUserHero().getPlayerHeroVOById(heroId);
			if (playerHeroVO == null) {
				throw new GameException(AlertEnum.HERO_NOT_FOUND);
			}
		}

		PlayerKnapsackVO playerKnapsackVO = null;
		if (heroId > 0) {
			playerKnapsackVO = knapsackService.findplayerKnapsackById(playerHeroVO.getEquipList(), itemId);
		} else {
			playerKnapsackVO = knapsackService.findplayerKnapsackById(userCached.getUserKnapsack().getKnapsackList(), itemId);
		}

		if (playerKnapsackVO == null || playerKnapsackVO.getBaseItemVO().getType() != ItemConstants.ITEM_TYPE_EQPI) {
			throw new GameException(AlertEnum.EQUIP_NOT_FOUND);
		}

		int gem = getGemByPos(playerKnapsackVO, pos);

		if (gem != -1) {
			throw new GameException(AlertEnum.ALREADY_OPENHOLE);
		}

		BaseHoleVO baseHoleVO = HoleRes.getInstance().getBaseHoleVO(pos);

		if (playerKnapsackVO.getBaseItemVO().getStar() < baseHoleVO.getOpenstar()) {
			throw new GameException(AlertEnum.EQUIPMENT_STAR_ENOUGH);
		}

		if (!knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), baseHoleVO.getItemid(), baseHoleVO.getNum())) {
			throw new GameException(AlertEnum.ITEM_NUM_NOT);
		}

		knapsackService.removeItem(userCached, baseHoleVO.getItemid(), baseHoleVO.getNum(), commandList);
		gemSetByPos(playerKnapsackVO, 0, pos);

		List<PlayerKnapsackVO> updateList = new ArrayList<PlayerKnapsackVO>();
		updateList.add(playerKnapsackVO);
		knapsackService.saveKnapsack(null, updateList, null, commandList);

		// 镶嵌完成
		CommandUtil.packageNettyMessage(CSCommandConstant.GEMSET_ITEM, null, commandList);

		// 重新计算属性
		if (heroId > 0 && playerHeroVO != null) {
			// 更新英雄属性
			heroService.updateHeroCombat(userCached, playerHeroVO, commandList);
		}
	}

	/**
	 * 物品镶嵌
	 * 
	 * @param request
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void gemSetItem(GemSetRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		int heroId = request.getHeroId();
		int itemId = request.getEquipItemId();
		int gemCfgId = request.getGemCfgId();
		PlayerHeroVO playerHeroVO = null;
		if (heroId > 0) {
			playerHeroVO = userCached.getUserHero().getPlayerHeroVOById(heroId);
			if (playerHeroVO == null) {
				throw new GameException(AlertEnum.HERO_NOT_FOUND);
			}
		}

		PlayerKnapsackVO playerKnapsackVO = null;
		if (heroId > 0) {
			playerKnapsackVO = knapsackService.findplayerKnapsackById(playerHeroVO.getEquipList(), itemId);
		} else {
			playerKnapsackVO = knapsackService.findplayerKnapsackById(userCached.getUserKnapsack().getKnapsackList(), itemId);
		}

		if (playerKnapsackVO == null || playerKnapsackVO.getBaseItemVO().getType() != ItemConstants.ITEM_TYPE_EQPI) {
			throw new GameException(AlertEnum.EQUIP_NOT_FOUND);
		}

		// 判断宝石类型是否满足要求
		if (!isUseGem(playerKnapsackVO, gemCfgId)) {
			throw new GameException(AlertEnum.NOT_SET_GEMTYPE);
		}

		int pos = getEquipmentGemPos(playerKnapsackVO);

		if (!knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), gemCfgId, 1)) {
			throw new GameException(AlertEnum.ITEM_NUM_NOT);
		}

		knapsackService.removeItem(userCached, gemCfgId, 1, commandList);
		gemSetByPos(playerKnapsackVO, gemCfgId, pos);

		List<PlayerKnapsackVO> updateList = new ArrayList<PlayerKnapsackVO>();
		updateList.add(playerKnapsackVO);
		knapsackService.saveKnapsack(null, updateList, null, commandList);

		// 镶嵌完成
		CommandUtil.packageNettyMessage(CSCommandConstant.GEMSET_ITEM, null, commandList);

		// 重新计算属性
		if (heroId > 0 && playerHeroVO != null) {
			// 更新英雄属性
			heroService.updateHeroCombat(userCached, playerHeroVO, commandList);
		}
	}

	/**
	 * 摘除
	 * 
	 * @param request
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void gemPickItem(GemPickRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		int heroId = request.getHeroId();
		int itemId = request.getEquipItemId();
		int pos = request.getPos() - 1;
		PlayerHeroVO playerHeroVO = null;
		if (heroId > 0) {
			playerHeroVO = userCached.getUserHero().getPlayerHeroVOById(heroId);
			if (playerHeroVO == null) {
				throw new GameException(AlertEnum.HERO_NOT_FOUND);
			}
		}

		PlayerKnapsackVO playerKnapsackVO = null;
		if (heroId > 0) {
			playerKnapsackVO = knapsackService.findplayerKnapsackById(playerHeroVO.getEquipList(), itemId);
		} else {
			playerKnapsackVO = knapsackService.findplayerKnapsackById(userCached.getUserKnapsack().getKnapsackList(), itemId);
		}

		if (playerKnapsackVO == null || playerKnapsackVO.getBaseItemVO().getType() != ItemConstants.ITEM_TYPE_EQPI) {
			throw new GameException(AlertEnum.EQUIP_NOT_FOUND);
		}

		int gemCfgId = getGemByPos(playerKnapsackVO, pos);

		if (gemCfgId <= 0) {
			throw new GameException(AlertEnum.NOT_FOUND_GEM);
		}

		if (!knapsackService.hasKnapsackGird(userCached, 1)) {
			throw new GameException(AlertEnum.KNAPSACK_FULL);
		}

		// BaseHoleVO baseHoleVO = HoleRes.getInstance().getBaseHoleVO(pos);

		gemSetByPos(playerKnapsackVO, 0, pos);

		BaseItemVO baseItemVO = ItemRes.getInstance().getGemByCfgId(gemCfgId);
		knapsackService.addNewItem(userCached, baseItemVO, 1, commandList);

		List<PlayerKnapsackVO> updateList = new ArrayList<PlayerKnapsackVO>();
		updateList.add(playerKnapsackVO);
		knapsackService.saveKnapsack(null, updateList, null, commandList);
		// 镶嵌完成
		CommandUtil.packageNettyMessage(CSCommandConstant.GEMPICK_ITEM, null, commandList);

		// 重新计算属性
		if (heroId > 0 && playerHeroVO != null) {
			// 更新英雄属性
			heroService.updateHeroCombat(userCached, playerHeroVO, commandList);
		}
	}

	/**
	 * 取一个空位
	 * 
	 * @param playerKnapsackVO
	 * @return
	 * @throws GameException
	 */
	public static int getEquipmentGemPos(PlayerKnapsackVO playerKnapsackVO) throws GameException {
		if (playerKnapsackVO.getGem1() == 0) {
			return 0;
		}

		if (playerKnapsackVO.getGem2() == 0) {
			return 1;
		}

		if (playerKnapsackVO.getGem3() == 0) {
			return 2;
		}

		if (playerKnapsackVO.getGem4() == 0) {
			return 3;
		}

		throw new GameException(AlertEnum.NOT_POS_GEM);
	}

	/**
	 * 改变对应位置的宝石cfgid
	 * 
	 * @param playerKnapsackVO
	 * @param gemCfgId
	 * @param pos
	 */
	public static void gemSetByPos(PlayerKnapsackVO playerKnapsackVO, int gemCfgId, int pos) {
		if (pos == 0) {
			playerKnapsackVO.setGem1(gemCfgId);
		} else if (pos == 1) {
			playerKnapsackVO.setGem2(gemCfgId);
		} else if (pos == 2) {
			playerKnapsackVO.setGem3(gemCfgId);
		} else if (pos == 3) {
			playerKnapsackVO.setGem4(gemCfgId);
		}

		ItemRes.getInstance().loadGem(playerKnapsackVO);
	}

	/**
	 * 取某个位置的宝石信息
	 * 
	 * @param playerKnapsackVO
	 * @param pos
	 * @return
	 * @throws Exception
	 */
	public static int getGemByPos(PlayerKnapsackVO playerKnapsackVO, int pos) throws Exception {
		if (pos == 0) {
			return playerKnapsackVO.getGem1();
		} else if (pos == 1) {
			return playerKnapsackVO.getGem2();
		} else if (pos == 2) {
			return playerKnapsackVO.getGem3();
		} else if (pos == 3) {
			return playerKnapsackVO.getGem4();
		}

		throw new Exception("错误的宝石孔位置 " + pos);
	}

	/**
	 * 判断能否镶嵌此类型的宝石
	 * 
	 * @param playerKnapsackVO
	 * @param gemCfgId
	 * @return
	 */
	public static boolean isUseGem(PlayerKnapsackVO playerKnapsackVO, int gemCfgId) {
		int subtype = ItemRes.getInstance().getGemByCfgId(gemCfgId).getSubType();
		int[] arrGemType = playerKnapsackVO.getBaseItemVO().getArrGempType();
		if (arrGemType == null) {
			return false;
		}

		for (int value : arrGemType) {
			if (value == subtype) {
				return true;
			}
		}
		return false;
	}
}
