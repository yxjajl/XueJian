package com.dh.processor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.constants.GameRecordConstants;
import com.dh.constants.ItemConstants;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.constant.TaskConstant;
import com.dh.game.vo.base.BaseComposeVO;
import com.dh.game.vo.base.BaseEnhanceVO;
import com.dh.game.vo.base.BaseEquipStarVO;
import com.dh.game.vo.base.BaseItemVO;
import com.dh.game.vo.base.GameRecordVO;
import com.dh.game.vo.item.ItemProto.ComposeItemRequest;
import com.dh.game.vo.item.ItemProto.EquipChangeRequest;
import com.dh.game.vo.item.ItemProto.FitEquipRequest;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.user.PlayerAccountVO;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.game.vo.user.PlayerKnapsackVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.ComposeRes;
import com.dh.resconfig.EnhanceRes;
import com.dh.resconfig.EquipmentEnhanceCostRes;
import com.dh.resconfig.EquipmentStarRes;
import com.dh.resconfig.ItemRes;
import com.dh.service.ActivityService;
import com.dh.service.BaseInfoService;
import com.dh.service.ChatService;
import com.dh.service.HeroService;
import com.dh.service.KnapsackService;
import com.dh.service.PlayerAccountService;
import com.dh.util.CommandUtil;
import com.dh.util.MyClassLoaderUtil;
import com.dh.vo.user.UserCached;

@Component
public class ItemEquipProcessor {
	// @Resource
	// private PlayerKnapsackMapper playerKnapsackMapper;
	@Resource
	private KnapsackService knapsackService;
	@Resource
	private PlayerAccountService playerAccountService;
	@Resource
	private HeroService heroService;
	@Resource
	private BaseInfoService baseInfoService;
	@Resource
	private ActivityService activityService;

	// @Resource
	// private TasksService tasksService;

	/**
	 * 穿装备
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void takOnEquipment(FitEquipRequest res, UserCached userCached, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int heroId = res.getHeroId();
		int itemId = res.getItemId();
		PlayerHeroVO playerHeroVO = userCached.getUserHero().getPlayerHeroVOById(heroId);
		if (playerHeroVO == null) {
			throw new GameException(AlertEnum.HERO_NOT_FOUND);
		}
		PlayerKnapsackVO playerKnapsackVO = knapsackService.findplayerKnapsackById(userCached.getUserKnapsack().getKnapsackList(), itemId);
		if (playerKnapsackVO == null || playerKnapsackVO.getBaseItemVO().getType() != ItemConstants.ITEM_TYPE_EQPI) {
			throw new GameException(AlertEnum.EQUIP_NOT_FOUND);
		}
		knapsackService.takeOnEquipment(userCached, playerHeroVO, nettyMessageVO, playerKnapsackVO, commandList);

		// 更新英雄属性
		// commandList.add(CommandUtil.updateHeroADH(playerHeroVO));
		heroService.updateHeroCombat(userCached, playerHeroVO, commandList);

		MyClassLoaderUtil.getInstance().getTaskCheck().changTaskByReQType(userCached, TaskConstant.TASK_TAKE_EQUIP, playerKnapsackVO.getBaseItemVO().getSubType(), 1, commandList);

		// 给指定英雄配带武器(指定位置的装备)
		MyClassLoaderUtil.getInstance().getTaskCheck()
				.changTaskByReQType(userCached, TaskConstant.TASK_TAKE_EQUIP_POSITION, playerHeroVO.getCfgId(), playerKnapsackVO.getBaseItemVO().getSubType(), commandList);

	}

	/**
	 * 脱装备
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void takOffEquipment(FitEquipRequest res, UserCached userCached, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {

		int heroId = res.getHeroId();
		int itemId = res.getItemId();
		PlayerHeroVO playerHeroVO = userCached.getUserHero().getPlayerHeroVOById(heroId);
		if (playerHeroVO == null) {
			throw new GameException(AlertEnum.HERO_NOT_FOUND);
		}

		PlayerKnapsackVO playerKnapsackVO = knapsackService.findplayerKnapsackById(playerHeroVO.getEquipList(), itemId);

		if (playerKnapsackVO == null) {
			throw new GameException(AlertEnum.EQUIP_NOT_FOUND);
		}
		knapsackService.takeOffEquipment(userCached, playerHeroVO, nettyMessageVO, playerKnapsackVO, commandList);
		// // 更新英雄属性
		// commandList.add(CommandUtil.updateHeroADH(playerHeroVO));
		heroService.updateHeroCombat(userCached, playerHeroVO, commandList);

	}

	/**
	 * 装备强化
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void enchanceEquipment(EquipChangeRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		int heroId = request.getHeroId();
		int itemId = request.getItemId();
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

		if (playerKnapsackVO.getEnhance() >= playerKnapsackVO.getBaseItemVO().getEnhanceLimit()) {
			throw new GameException(AlertEnum.ENHANCE_LIMIT);
		}

		// if (playerKnapsackVO.getEnhance() >
		// userCached.getPlayerVO().getLevel()) {
		// throw new GameException(AlertEnum.ENHANCE_CANT_GRANT_ROLELEVEL);
		// }

		int costMoney = EquipmentEnhanceCostRes.getInstance().getEnhanceCostVO(playerKnapsackVO.getEnhance()).getCostMoney();
		playerAccountService.hasEnoughMoney(userCached, costMoney);

		if (userCached.getPlayerVO().getVip() >= 7) {
			costMoney = (int) (costMoney * 80 / 100);
		}

		// 强化扣钱
		if (costMoney > 0) {
			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, costMoney, userCached.getPlayerAccountVO(), commandList, "英雄装备强化" + playerKnapsackVO.getCfgId());
		}
		// 扣材料

		playerKnapsackVO.setEnhance(playerKnapsackVO.getEnhance() + 1);

		List<PlayerKnapsackVO> updateList = new ArrayList<PlayerKnapsackVO>();
		updateList.add(playerKnapsackVO);
		knapsackService.saveKnapsack(null, updateList, null, commandList);

		// 强化完成
		CommandUtil.packageNettyMessage(CSCommandConstant.ENHANCE_EQUIP, null, commandList);

		// 重新计算属性
		if (heroId > 0 && playerHeroVO != null) {
			// 更新英雄属性
			heroService.updateHeroCombat(userCached, playerHeroVO, commandList);
		}

		// 装备强化
		MyClassLoaderUtil.getInstance().getTaskCheck()
				.changTaskByReQType(userCached, TaskConstant.TASK_ENHANLV_EQ, playerKnapsackVO.getBaseItemVO().getSubType(), playerKnapsackVO.getEnhance(), commandList);

		// 全服强化累计计数
		GameRecordVO gameRecordVO = GameRecordConstants.getGameRecordVO(GameRecordConstants.ENHANCE);
		gameRecordVO.setValue1(GameRecordConstants.ENHANCE_VALUE.incrementAndGet());
		baseInfoService.updateGameRecordVO(gameRecordVO);
		activityService.addDayDone(userCached, 11, commandList);
	}

	/**
	 * 装备强化 一键强化
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void enchanceEquipment2(EquipChangeRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		int heroId = request.getHeroId();
		int itemId = request.getItemId();
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

		if (playerKnapsackVO.getEnhance() >= playerKnapsackVO.getBaseItemVO().getEnhanceLimit()) {
			throw new GameException(AlertEnum.ENHANCE_LIMIT);
		}

		int enhance = playerKnapsackVO.getEnhance();
		int costMoney = 0;
		PlayerAccountVO playerAccountVO = userCached.getPlayerAccountVO();
		int tempMoney = 0;
		int maxEnhance = playerKnapsackVO.getBaseItemVO().getEnhanceLimit();// Math.min(playerKnapsackVO.getBaseItemVO().getEnhanceLimit(),
																			// userCached.getPlayerVO().getLevel());
		for (int i = playerKnapsackVO.getEnhance(); i < maxEnhance; i++) {
			tempMoney = EquipmentEnhanceCostRes.getInstance().getEnhanceCostVO(i).getCostMoney();
			if (costMoney + tempMoney > playerAccountVO.getGmMoney()) {
				break;
			}

			costMoney += tempMoney;
			enhance = i + 1;
		}

		playerAccountService.hasEnoughMoney(userCached, costMoney);

		if (userCached.getPlayerVO().getVip() >= 7) {
			costMoney = (int) (costMoney * 80 / 100);
		}

		// 强化扣钱
		if (costMoney > 0) {
			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, costMoney, userCached.getPlayerAccountVO(), commandList, "英雄装备强化" + playerKnapsackVO.getCfgId());
		}
		// 扣材料

		playerKnapsackVO.setEnhance(enhance);

		List<PlayerKnapsackVO> updateList = new ArrayList<PlayerKnapsackVO>();
		updateList.add(playerKnapsackVO);
		knapsackService.saveKnapsack(null, updateList, null, commandList);

		// 强化完成
		CommandUtil.packageNettyMessage(CSCommandConstant.ENHANCE_EQUIP, null, commandList);

		// 重新计算属性
		if (heroId > 0 && playerHeroVO != null) {
			// 更新英雄属性
			heroService.updateHeroCombat(userCached, playerHeroVO, commandList);
		}

		// 装备强化任务
		MyClassLoaderUtil.getInstance().getTaskCheck()
				.changTaskByReQType(userCached, TaskConstant.TASK_ENHANLV_EQ, playerKnapsackVO.getBaseItemVO().getSubType(), playerKnapsackVO.getEnhance(), commandList);

		// 全服强化累计计数
		GameRecordVO gameRecordVO = GameRecordConstants.getGameRecordVO(GameRecordConstants.ENHANCE);
		gameRecordVO.setValue1(GameRecordConstants.ENHANCE_VALUE.incrementAndGet());
		baseInfoService.updateGameRecordVO(gameRecordVO);
		activityService.addDayDone(userCached, 11, commandList);
	}

	/**
	 * 装备升星
	 * 
	 * @param request
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void upStarEquipment(EquipChangeRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		int heroId = request.getHeroId();
		int itemId = request.getItemId();
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
		int oldStar = playerKnapsackVO.getBaseItemVO().getStar();
		BaseEquipStarVO baseEquipStarVO = EquipmentStarRes.getInstance().getBaseEquipStarVO(playerKnapsackVO.getCfgId());
		if (baseEquipStarVO == null) {
			throw new GameException(AlertEnum.HERO_LEVEL_LIMIT);
		}

		if (baseEquipStarVO.getReqMoney() > 0) {
			playerAccountService.hasEnoughMoney(userCached, baseEquipStarVO.getReqMoney());
		}

		if (!knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), baseEquipStarVO.getItemid(), baseEquipStarVO.getItemnum())) {
			throw new GameException(AlertEnum.ITEM_NUM_NOT);
		}

		BaseItemVO baseItemVO = ItemRes.getInstance().getEquipByCfgId(baseEquipStarVO.getResCfgId());

		if (heroId > 0 && baseItemVO.getReqLevel() > playerHeroVO.getLevel()) {
			if (!knapsackService.hasKnapsackGird(userCached, 1)) {
				throw new GameException(AlertEnum.KNAPSACK_FULL);
			}
		}

		// 强化扣钱
		if (baseEquipStarVO.getReqMoney() > 0) {
			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, baseEquipStarVO.getReqMoney(), userCached.getPlayerAccountVO(), commandList, "英雄装备升星" + playerKnapsackVO.getCfgId());
		}
		// // 扣材料

		knapsackService.removeItem(userCached, baseEquipStarVO.getItemid(), baseEquipStarVO.getItemnum(), commandList);

		playerKnapsackVO.setCfgId(baseEquipStarVO.getResCfgId());

		BaseEnhanceVO baseEnhanceVO = EnhanceRes.getInstance().getBaseEnhanceVO(playerKnapsackVO.getCfgId());
		playerKnapsackVO.setBaseItemVO(baseItemVO);
		playerKnapsackVO.setBaseEnhanceVO(baseEnhanceVO);

		List<PlayerKnapsackVO> updateList = new ArrayList<PlayerKnapsackVO>();
		updateList.add(playerKnapsackVO);
		knapsackService.saveKnapsack(null, updateList, null, commandList);

		// 重新计算属性
		if (heroId > 0 && playerHeroVO != null) {
			CommandUtil.updateHeroAndPackageCommand(playerHeroVO, userCached, commandList);
		}

		if (heroId > 0 && baseItemVO.getReqLevel() > playerHeroVO.getLevel()) {
			// 脱装备
			knapsackService.takeOffEquipment(userCached, playerHeroVO, nettyMessageVO, playerKnapsackVO, commandList);
		}
		if (heroId > 0) {
			heroService.updateHeroCombat(userCached, playerHeroVO, commandList);
		}

		// 强化完成
		CommandUtil.packageNettyMessage(CSCommandConstant.UPSTAR_EQUIP, null, commandList);

		// 装备升星
		MyClassLoaderUtil.getInstance().getTaskCheck()
				.changTaskByReQType(userCached, TaskConstant.TASK_EQUIP_UPSTAR, playerKnapsackVO.getCfgId(), playerKnapsackVO.getBaseItemVO().getStar(), commandList);

		// 拥有N件M星以上装备
		MyClassLoaderUtil.getInstance().getTaskCheck().changTaskByReQType(userCached, TaskConstant.TASK_NEQ_NSTAR, commandList);
		if (baseItemVO.getStar() >= 5) {
			ChatService.sendSysMsg(ChatService.sendEquipUpdate(userCached.getPlayerVO().getName(), baseItemVO.getName(), oldStar, baseItemVO.getStar()), ChatService.TAGS[0]);
		}

	}

	/**
	 * 物品合成
	 * 
	 * @param request
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void composeItem(ComposeItemRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		int itemCfgId = request.getItemResultId(); // 要合成的成品id
		int num = request.getNumber();// 合多少个

		BaseComposeVO baseComposeVO = ComposeRes.getInstance().getBaseComposeVO(itemCfgId);
		// 看看钱够不够
		int costMoney = num * baseComposeVO.getMoney();
		playerAccountService.hasEnoughMoney(userCached, costMoney);

		// 物品数量不足
		if (!knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), baseComposeVO.getMaterial(), baseComposeVO.getNumber(), num)) {
			throw new GameException(AlertEnum.ITEM_NUM_NOT);
		}

		BaseItemVO baseItemVO = ItemRes.getInstance().getBaseItemVO(itemCfgId);

		if (!knapsackService.hasKnapsackGird(userCached, num, baseItemVO.getMaxnum())) {
			throw new GameException(AlertEnum.KNAPSACK_FULL);
		}

		playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, costMoney, userCached.getPlayerAccountVO(), commandList, "物品合成");
		knapsackService.removeItem(userCached, baseComposeVO.getMaterial(), baseComposeVO.getNumber(), num, commandList);

		knapsackService.addNewItem(userCached, baseItemVO, num, commandList);

		CommandUtil.packageNettyMessage(CSCommandConstant.COMPOSE_ITEM, null, commandList);

		if (baseItemVO.getType() == ItemConstants.ITEM_TYPE_EQPI) {
			// 合成Ｎ件Ｍ星装备
			MyClassLoaderUtil.getInstance().getTaskCheck().changTaskByReQTypeAcc2(userCached, TaskConstant.TASK_COMPOSE_EQ, baseItemVO.getStar(), num, commandList);
		}
	}

}
