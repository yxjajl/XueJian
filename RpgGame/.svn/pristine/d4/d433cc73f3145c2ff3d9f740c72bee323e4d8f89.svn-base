package com.dh.processor;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.constants.CommonCommand;
import com.dh.constants.CommonConstants;
import com.dh.constants.HeroConstants;
import com.dh.constants.ItemConstants;
import com.dh.enums.GMIOEnum;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.base.BaseHeroInfoVO;
import com.dh.game.vo.base.BaseItemVO;
import com.dh.game.vo.base.BaseShopDisCountVO;
import com.dh.game.vo.base.BaseShopVO;
import com.dh.game.vo.item.ShopProto.ShopItemListResponse;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.user.PlayerAccountVO;
import com.dh.game.vo.user.PlayerKnapsackVO;
import com.dh.game.vo.user.PlayerShopVO;
import com.dh.game.vo.user.PlayerTimerVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.ExpeditionShopRes;
import com.dh.resconfig.HeroRes;
import com.dh.resconfig.ItemRes;
import com.dh.resconfig.JJCShopRes;
import com.dh.resconfig.ScoreShopRes;
import com.dh.resconfig.ShopRes;
import com.dh.resconfig.VipLevelRes;
import com.dh.service.HeroService;
import com.dh.service.KnapsackService;
import com.dh.service.PlayerAccountService;
import com.dh.service.PlayerService;
import com.dh.service.PlayerTimerService;
import com.dh.service.RewardService;
import com.dh.service.ShopService;
import com.dh.service.StreetService;
import com.dh.service.TasksService;
import com.dh.sync.LockConstant;
import com.dh.sync.SyncLock;
import com.dh.sync.SyncUtil;
import com.dh.util.CommandUtil;
import com.dh.util.DateUtil;
import com.dh.util.GameUtil;
import com.dh.util.VOUtil;
import com.dh.vo.user.UserCached;

@SuppressWarnings("deprecation")
@Component
public class ShopProcessor {
	private static Logger logger = Logger.getLogger(ShopProcessor.class);
	// private final static int EXPLOIT_SHOP_MAXNUM = 9; // 功勋商城物品总数
	// private final static int[] EXPLOIT_REFRESH_TIME = { 9, 12, 18, 21 }; //
	// 功勋商城的刷新时间

	@Resource
	private PlayerAccountService playerAccountService;

	@Resource
	private KnapsackService knapsackService;

	@Resource
	private RewardService rewardService;

	@Resource
	private PlayerService playerService;

	@Resource
	private HeroService heroService;

	@Resource
	private TasksService tasksService;

	@Resource
	private ShopService shopService;

	@Resource
	private PlayerTimerService playerTimerService;
	@Resource
	private StreetService streetService;

	/**
	 * 折品商品列表
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void shopList(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		int remainTime = shopService.getShopRemainTime();
		ShopItemListResponse.Builder shopItemListResponse = ShopItemListResponse.newBuilder();
		shopItemListResponse.setRemaintTime(remainTime);
		shopService.getShopItemList(userCached, shopItemListResponse);

		CommandUtil.packageNettyMessage(CSCommandConstant.SHOP_LIST, shopItemListResponse.build().toByteArray(), commandList);
	}

	// 主动刷新
	public void refreshExploit(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		// int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		// UserCached userCached = ServerHandler.getUserCached(playerId);
		//
		// int exploit = 30;
		// playerAccountService.hasEnoughExploit(userCached, exploit);
		// playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_EXPLOIT,
		// exploit, userCached.getPlayerAccountVO(), commandList, "主动刷新功勋商店");
		// getExploitList(userCached, true, commandList);

	}

	// public void exploitList(NettyMessageVO nettyMessageVO,
	// List<NettyMessageVO> commandList) throws Exception {
	// int playerId = ServerHandler.get(nettyMessageVO.getChannel());
	// UserCached userCached = ServerHandler.getUserCached(playerId);
	// getExploitList(userCached, false, commandList);
	// }

	// 远征商丫
	public boolean shoppingYuanZhen(int serialId, int number, int type, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		int itemCfgId = 0;
		int price = 0;

		BaseShopVO baseShopVO = ExpeditionShopRes.getInstance().getMoneyBaseShopVO(serialId);

		if (baseShopVO == null) {
			throw new GameException(AlertEnum.ITEM_NOT_FOUND);
		}

		itemCfgId = baseShopVO.getItem_id();
		price = baseShopVO.getPrice() * number;

		BaseItemVO baseItemVO = ItemRes.getInstance().getBaseItemVO(itemCfgId);
		if (baseItemVO == null) {
			commandList.add(CommonCommand.ITEM_NOT_EXIST);
			return false;
		}

		// 判断有没有 背包空间
		if (!knapsackService.hasKnapsackGird(userCached, number, baseItemVO.getMaxnum())) {
			commandList.add(CommonCommand.KNAPSACK_FULL);
			return false;
		}

		if (!knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), baseShopVO.getMoney_type(), price)) {
			throw new GameException(AlertEnum.YZ_MONEY_NOT_ENOUGH);
		}

		// 扣钱
		knapsackService.removeItem(userCached, baseShopVO.getMoney_type(), price, commandList);

		// 加装备
		knapsackService.addNewItem(userCached, baseItemVO, number, commandList);
		return true;
	}

	// 积分商城
	public boolean scoreShop(int serialId, int number, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		int itemCfgId = 0;
		PLAYER_PROPERTY currencyType = PLAYER_PROPERTY.PROPERTY_SCORE;
		int price = 0;

		BaseShopVO baseShopVO = ScoreShopRes.getInstance().getMoneyBaseShopVO(serialId);

		if (baseShopVO == null) {
			throw new GameException(AlertEnum.ITEM_NOT_FOUND);
		}

		itemCfgId = baseShopVO.getItem_id();
		price = baseShopVO.getPrice() * number;

		BaseItemVO baseItemVO = ItemRes.getInstance().getBaseItemVO(itemCfgId);
		if (baseItemVO == null) {
			commandList.add(CommonCommand.ITEM_NOT_EXIST);
			return false;
		}

		playerAccountService.hasEnoughScore(userCached, price);

		// 判断有没有 背包空间
		if (!knapsackService.hasKnapsackGird(userCached, number, baseItemVO.getMaxnum())) {
			commandList.add(CommonCommand.KNAPSACK_FULL);
			return false;
		}

		// PlayerTimerVO playerTimerVO =
		// userCached.getUserTimer().getPlayerTimerVO();
		// String exploitShop = playerTimerVO.getExploitShop();
		// exploitShop = exploitShop.replace(String.valueOf(serialId), "-" +
		// serialId);
		// playerTimerVO.setExploitShop(exploitShop);
		// playerTimerService.updateExploitShop(playerTimerVO);

		// 购买
		// 扣钱
		playerAccountService.deductCurrency(currencyType, price, userCached.getPlayerAccountVO(), commandList, "购买物品" + itemCfgId);

		// 加装备
		knapsackService.addNewItem(userCached, baseItemVO, number * baseShopVO.getNumber(), commandList);
		return true;
	}

	// 功勋商店
	public boolean shoppingExploit(int serialId, int number, int type, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		int itemCfgId = 0;
		PLAYER_PROPERTY currencyType = PLAYER_PROPERTY.PROPERTY_EXPLOIT;
		int price = 0;

		BaseShopDisCountVO baseShopDisCountVO = JJCShopRes.getInstance().getBaseShop(serialId);

		if (baseShopDisCountVO == null) {
			throw new GameException(AlertEnum.ITEM_NOT_FOUND);
		}

		number = number * baseShopDisCountVO.getNumber();
		itemCfgId = baseShopDisCountVO.getItem_id();
		price = baseShopDisCountVO.getTotal_exploit() * number;

		BaseItemVO baseItemVO = ItemRes.getInstance().getBaseItemVO(itemCfgId);
		if (baseItemVO == null) {
			commandList.add(CommonCommand.ITEM_NOT_EXIST);
			return false;
		}

		playerAccountService.hasEnoughExploit(userCached, price);

		// 判断有没有 背包空间
		if (!knapsackService.hasKnapsackGird(userCached, number, baseItemVO.getMaxnum())) {
			commandList.add(CommonCommand.KNAPSACK_FULL);
			return false;
		}

		// PlayerTimerVO playerTimerVO =
		// userCached.getUserTimer().getPlayerTimerVO();
		// String exploitShop = playerTimerVO.getExploitShop();
		// exploitShop = exploitShop.replace(String.valueOf(serialId), "-" +
		// serialId);
		// playerTimerVO.setExploitShop(exploitShop);
		// playerTimerService.updateExploitShop(playerTimerVO);

		// 购买
		// 扣钱
		playerAccountService.deductCurrency(currencyType, price, userCached.getPlayerAccountVO(), commandList, "购买物品" + itemCfgId);

		// 加装备
		knapsackService.addNewItem(userCached, baseItemVO, number, commandList);
		return true;
	}

	/**
	 * 购买物品
	 * 
	 * @param serialId
	 * @param number
	 * @param label
	 * @param nettyMessageVO
	 * @param commandList
	 * @return
	 * @throws Exception
	 */

	public boolean itemBuy(int serialId, int number, int label, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		int itemCfgId = 0;
		int money_type = ShopRes.TYPE_RMB;
		int price = 0;
		String strCurDate = DateUtil.formatDate(new Date(), DateUtil.DAY_FORMAT_STRING);

		if (label == 0) {
			BaseShopVO baseShopVO = ShopRes.getInstance().getMoneyBaseShopVO(serialId);
			if (baseShopVO == null) {
				commandList.add(CommonCommand.ITEM_NOT_EXIST);
				return false;
			}

			itemCfgId = baseShopVO.getItem_id();
			price = baseShopVO.getPrice() * number;
			money_type = baseShopVO.getMoney_type();
		} else {
			BaseShopDisCountVO baseShopDisCountVO = shopService.findBaseShopDisCountVO(serialId);
			if (baseShopDisCountVO == null) {
				throw new GameException(AlertEnum.SELL_ITEM_TIMEOUT);
			}

			if (baseShopDisCountVO.getTotal_limit() == 0) {
				throw new GameException(AlertEnum.SELL_ITEM_FINISH);
			}

			if (userCached.getUserShop().getShopSingle(serialId, strCurDate) + number > baseShopDisCountVO.getBuy_limit()) {
				throw new GameException(AlertEnum.SELL_ITEM_FINISH);
			}
			itemCfgId = baseShopDisCountVO.getItem_id();
			price = baseShopDisCountVO.getPrice_rmb() * number;
			money_type = ShopRes.TYPE_RMB;
		}

		BaseItemVO baseItemVO = ItemRes.getInstance().getBaseItemVO(itemCfgId);
		if (baseItemVO == null) {
			commandList.add(CommonCommand.ITEM_NOT_EXIST);
			return false;
		}
		switch (money_type) {
		case ShopRes.TYPE_MONEY:
			playerAccountService.hasEnoughMoney(userCached, price);
			break;
		case ShopRes.TYPE_RMB:
			playerAccountService.hasEnoughOnlyRMB(userCached, price);
			break;
		case ShopRes.TYPE_RMB_AND_GIFT:
			playerAccountService.hasEnoughRMBAndGift(userCached, price);
			break;
		}

		// 判断有没有 背包空间
		if (!knapsackService.hasKnapsackGird(userCached, number, baseItemVO.getMaxnum())) {
			commandList.add(CommonCommand.KNAPSACK_FULL);
			return false;
		}
		if (label == 1) { // 折扣商品
			PlayerShopVO playerShopVO = userCached.getUserShop().findPlayerShopVO(serialId, strCurDate);
			if (playerShopVO == null) {
				playerShopVO = new PlayerShopVO();
				playerShopVO.setPlayerId(userCached.getPlayerId());
				playerShopVO.setNum(number);
				playerShopVO.setLastUpdateDate(strCurDate);
				playerShopVO.setItemId(serialId);
				userCached.getUserShop().getShopList().add(playerShopVO);
			} else {
				playerShopVO.setNum(playerShopVO.getNum() + number);
			}
			userCached.getUserShop().getShopList().add(playerShopVO);
			shopService.insertPlayerShop(playerShopVO);

			BaseShopDisCountVO baseShopDisCountVO = shopService.findBaseShopDisCountVO(serialId);
			baseShopDisCountVO.setTotal_limit(baseShopDisCountVO.getTotal_limit() - number);

			// 折扣商品不能用礼金，只能用元宝
			playerAccountService.deductCurrencyOnlyRMB(price, userCached.getPlayerAccountVO(), commandList, "购买物品" + itemCfgId, GMIOEnum.OUT_BUY_ITEM.usage(), baseItemVO.getCfgId(), number);
		} else {

			switch (money_type) {
			case ShopRes.TYPE_MONEY:
				playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, price, userCached.getPlayerAccountVO(), commandList, "购买物品" + itemCfgId);
				break;
			case ShopRes.TYPE_RMB:
				playerAccountService.deductCurrencyOnlyRMB(price, userCached.getPlayerAccountVO(), commandList, "购买物品" + itemCfgId, GMIOEnum.OUT_BUY_ITEM.usage(), baseItemVO.getCfgId(), number);
				break;
			case ShopRes.TYPE_RMB_AND_GIFT:
				playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_RMB, price, userCached.getPlayerAccountVO(), commandList, "购买物品" + itemCfgId, GMIOEnum.OUT_BUY_ITEM.usage(),
						baseItemVO.getCfgId(), number);
				break;
			}
			// PLAYER_PROPERTY currencyType = PLAYER_PROPERTY.PROPERTY_RMB;

		}

		// 加装备
		knapsackService.addNewItem(userCached, baseItemVO, number, commandList);
		return true;
	}

	/**
	 * 商品出售
	 * 
	 * @date 2014年3月14日
	 */
	public boolean itemSell(int itemId, int count, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		List<PlayerKnapsackVO> kList = userCached.getUserKnapsack().getKnapsackList();
		PlayerKnapsackVO playerKnapsackVO = knapsackService.findplayerKnapsackById(kList, itemId);
		if (playerKnapsackVO == null) {
			commandList.add(CommonCommand.ITEM_NOT_EXIST);
			return false;
		}

		if (count < 1 || count > playerKnapsackVO.getBaseItemVO().getMaxnum()) {
			StringBuffer sf = new StringBuffer();
			sf.append("用户买了很多的东西 = ").append(playerId).append(",").append(count);
			logger.error(sf.toString());
			throw new Exception(sf.toString());
		}

		if (playerKnapsackVO.getItemid() == itemId) {
			if (playerKnapsackVO.getNumber() < count) {
				commandList.add(CommonCommand.ITEM_NOT_ENOUGH);
				return false;
			}
			int sellMoney = playerKnapsackVO.getBaseItemVO().getSellMoney();
			playerAccountService.addCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, sellMoney * count, userCached.getPlayerAccountVO(), commandList, "出售物品" + playerKnapsackVO.getBaseItemVO().getName());
			knapsackService.removeItem(userCached, playerKnapsackVO, count, commandList);
		}
		return true;
	}

	/**
	 * 道具使用
	 * 
	 * @param itemId
	 * @param nettyMessageVO
	 * @param commandList
	 * @return
	 * @throws Exception
	 */
	public boolean itemUse(int itemId, int num, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		List<PlayerKnapsackVO> kList = userCached.getUserKnapsack().getKnapsackList();
		PlayerKnapsackVO playerKnapsackVO = knapsackService.findplayerKnapsackById(kList, itemId);
		if (playerKnapsackVO == null) {
			throw new GameException(AlertEnum.ITEM_NOT_FOUND);
		}

		if (playerKnapsackVO.getBaseItemVO().getUsable() == 0) {
			throw new GameException(AlertEnum.ITEM_NOT_USE);
		}

		if (num > playerKnapsackVO.getNumber()) {
			throw new GameException(AlertEnum.ITEM_NUM_NOT);
		}

		int use = playerKnapsackVO.getBaseItemVO().getType();
		if (use == ItemConstants.ITEM_TYPE_GIFT) {// 礼包使用
			if (playerKnapsackVO.getBaseItemVO().getReqLevel() > userCached.getPlayerVO().getLevel()) {
				throw new GameException(AlertEnum.ROLE_LEVEL_NO);
			}
			rewardService.reward(userCached, playerKnapsackVO.getBaseItemVO().getPvalue(), commandList, GMIOEnum.IN_ITEM_PACK.usage());
			num = 1;// 一次只能送一个宝箱
		} else if (use == ItemConstants.ITEM_TYPE_CONS) {// 消耗品
			type_cons(userCached, num, playerKnapsackVO, commandList);
		} else if (use == ItemConstants.ITEM_TYPE_CARD) {// 英雄卡
			hero_card(userCached, num, playerKnapsackVO, commandList);
		} else if (use == ItemConstants.ITEM_TYPE_EQSS) {// 碎片
			num = sshc(userCached, playerKnapsackVO, commandList);
		}
		if (num > 0) {
			knapsackService.removeItem(userCached, playerKnapsackVO, num, commandList);
		}
		return true;
	}

	/**
	 * 英雄碎片合成
	 * 
	 * @param userCached
	 * @param playerKnapsackVO
	 * @param commandList
	 * @throws Exception
	 */
	public int sshc(UserCached userCached, PlayerKnapsackVO playerKnapsackVO, List<NettyMessageVO> commandList) throws Exception {
		BaseItemVO baseItemVO = playerKnapsackVO.getBaseItemVO();
		if (baseItemVO.getSubType() != ItemConstants.ITEM_SUBTYPE_HEROPIECE) {
			StringBuilder sb = new StringBuilder();
			sb.append("并非英雄碎片不能合成 ").append(baseItemVO.getType()).append(",").append(baseItemVO.getSubType());
			throw new Exception(sb.toString());
		}
		int num = baseItemVO.getPvalue2();
		// 物品数量不足
		if ((!knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), baseItemVO.getCfgId(), num)) || num == 0) {
			throw new GameException(AlertEnum.ITEM_NUM_NOT);
		}

		int cfgId = baseItemVO.getPvalue();
		int level = 1;
		BaseHeroInfoVO baseHeroInfoVO = HeroRes.getInstance().getBaseHeroInfoVO(cfgId);
		if (baseHeroInfoVO == null) {
			logger.error("找不到对应英雄卡的英雄配置 Itemid=" + playerKnapsackVO.getBaseItemVO().getCfgId() + ", cfgId=" + cfgId);
		} else {
			if (userCached.getUserHero().getHeroList().size() + 1 > HeroConstants.HERO_MAX_NUM) {
				throw new GameException(AlertEnum.HERO_NUM_LIMIT);
			}
			knapsackService.removeItem(userCached, baseItemVO.getCfgId(), num, commandList);
			heroService.addPlayerHero(baseHeroInfoVO, level, userCached, false, commandList);
		}

		return 0;

	}

	/**
	 * 消耗品处理
	 * 
	 * @param userCached
	 * @param playerKnapsackVO
	 * @param commandList
	 * @throws Exception
	 */
	public void type_cons(UserCached userCached, int num, PlayerKnapsackVO playerKnapsackVO, List<NettyMessageVO> commandList) throws Exception {
		int value = playerKnapsackVO.getBaseItemVO().getPvalue() * num;
		// BaseHeroRecruitVO baseHeroRecruitVO = null;
		switch (playerKnapsackVO.getBaseItemVO().getSubType()) {
		// case ItemConstants.ITEM_SUBTYPE_MZML:// 普通招募令
		// baseHeroRecruitVO =
		// HeroRecruitRes.getInstance().getBaseHeroRecruitVO(RECRUIT_TYPE.TYPE_MONEY);
		// rewardService.reward(userCached, baseHeroRecruitVO.getRewardId(),
		// commandList);
		// break;
		// case ItemConstants.ITEM_SUBTYPE_RZML:// 元宝招募令
		// baseHeroRecruitVO =
		// HeroRecruitRes.getInstance().getBaseHeroRecruitVO(RECRUIT_TYPE.TYPE_RMB);
		// rewardService.reward(userCached, baseHeroRecruitVO.getRewardId(),
		// commandList);
		// break;
		// case ItemConstants.ITEM_SUBTYPE_TZML:// 十连抽令牌
		// if (!knapsackService.hasKnapsackGird(userCached, 10))
		// throw new Exception(AlertEnum.KNAPSACK_FULL);
		// baseHeroRecruitVO =
		// HeroRecruitRes.getInstance().getBaseHeroRecruitVO(RECRUIT_TYPE.TYPE_RMB10);
		// for (int i = 0; i < 10; i++) {
		// rewardService.reward(userCached, baseHeroRecruitVO.getRewardId(),
		// commandList);
		// }
		// break;
		case ItemConstants.ITEM_SUBTYPE_EXP:// 经验丹

			if (userCached != null && userCached.getPlayerAccountVO().getExpc() >= userCached.getPlayerVO().getBaseLevelVO().getMaxExpc()) {
				throw new GameException(AlertEnum.EXPC_FULL);
			}

			playerAccountService.addCurrency(PLAYER_PROPERTY.PROPERTY_EXPC, value, userCached.getPlayerAccountVO(), commandList, "经验丹");
			break;
		case ItemConstants.ITEM_SUBTYPE_POWER:// 体力药剂
			playerAccountService.addCurrency(PLAYER_PROPERTY.PROPERTY_POWER, value, userCached.getPlayerAccountVO(), commandList, "体力丹");
			break;
		case ItemConstants.ITEM_SUBTYPE_MONEY:// 消耗品_银两
			playerAccountService.addCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, value, userCached.getPlayerAccountVO(), commandList, "银两丹");
			break;
		case ItemConstants.ITEM_SUBTYPE_RMB:// 消耗品_元宝
			playerAccountService.addCurrency(PLAYER_PROPERTY.PROPERTY_RMB, value, userCached.getPlayerAccountVO(), commandList, "元宝丹");
			break;
		case ItemConstants.ITEM_SUBTYPE_ZEXP:// 战队经验丹
			playerService.addPlayerExp(userCached, value, commandList);
			break;
		case ItemConstants.ITEM_SUBTYPE_PVP:// 消耗品_元宝
			playerAccountService.addCurrency(PLAYER_PROPERTY.PROPERTY_PVP, value, userCached.getPlayerAccountVO(), commandList, "竞技符");
			break;
		case ItemConstants.ITEM_SUBTYPE_DADDY:
			streetService.useDaddy(userCached, commandList);
			break;
		case ItemConstants.ITEM_SUBTYPE_QIANKUNBAO:// 乾坤包
			openKnaspack(userCached, num, commandList);
			break;

		// case ItemConstants.ITEM_SUBTYPE_CDF:// 冷却符
		// break;
		}
	}

	public void openKnaspack(UserCached userCached, int num, List<NettyMessageVO> commandList) throws Exception {

		playerTimerService.flushKnaspack(userCached);

		SyncLock syncLock = SyncUtil.getInstance().getLock(LockConstant.LOCK_PLAYERVO + userCached.getPlayerId());
		synchronized (syncLock) {
			PlayerAccountVO playerAccountVO = userCached.getPlayerAccountVO();
			PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();

			if ((userCached.getPlayerAccountVO().getKnapsack() + userCached.getPlayerAccountVO().getKnapsack2()) > CommonConstants.KNASPACK_MAX_GIRD) {
				throw new GameException(AlertEnum.KNAPSACK_LIMIT);
			}

			if (playerAccountVO.getKnapsack2() >= 0) {
				playerAccountVO.setKnapsack(playerAccountVO.getKnapsack() + playerAccountVO.getKnapsack2());
				playerAccountVO.setKnapsack2(0);

			}

			{
				// int n = num - playerAccountVO.getKnapsack2();
				// int cost = n * CommonConstants.KNASPACK_RMB;
				// playerAccountService.hasEnoughRMB(userCached, cost);
				//
				// playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_RMB,
				// cost, playerAccountVO, commandList, "开启背包" + n);

				playerAccountVO.setKnapsack(playerAccountVO.getKnapsack() + num);
				playerAccountVO.setKnapsack2(0);

				long remainTime = (1 + userCached.getPlayerAccountVO().getKnapsack() + userCached.getPlayerAccountVO().getKnapsack2() - CommonConstants.KNASPACK_STARTGIRD)
						* CommonConstants.KNASPACK_STEP + CommonConstants.KNASPACK_STAR;
				playerTimerVO.setKnpsackTime(remainTime);

				playerTimerService.updateknpsackTime(playerTimerVO);
			}

		}
		playerAccountService.updatePlayerAccountKnapsack(userCached.getPlayerAccountVO());
		CommandUtil.packageNettyMessage(CSCommandConstant.UPDATE_MYKNAPSACK, VOUtil.getMyKnapsack(userCached).build().toByteArray(), commandList);
	}

	/**
	 * 英雄卡
	 * 
	 * @param userCached
	 * @param playerKnapsackVO
	 * @param commandList
	 * @throws Exception
	 */
	public void hero_card(UserCached userCached, int num, PlayerKnapsackVO playerKnapsackVO, List<NettyMessageVO> commandList) throws Exception {
		int cfgId = playerKnapsackVO.getBaseItemVO().getPvalue();
		int level = playerKnapsackVO.getBaseItemVO().getPvalue2();
		BaseHeroInfoVO baseHeroInfoVO = HeroRes.getInstance().getBaseHeroInfoVO(cfgId);
		if (baseHeroInfoVO == null) {
			logger.error("找不到对应英雄卡的英雄配置 Itemid=" + playerKnapsackVO.getBaseItemVO().getCfgId() + ", cfgId=" + cfgId);
		} else {

			if (userCached.getUserHero().getHeroList().size() + num > HeroConstants.HERO_MAX_NUM) {
				throw new GameException(AlertEnum.HERO_NUM_LIMIT);
			}
			heroService.addPlayerHero(baseHeroInfoVO, level, userCached, false, commandList);
		}
	}

	public void buyPower(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();
		int countTotal = VipLevelRes.getInstance().getVipLevelVO(userCached.getPlayerVO().getVip()).getTime();
		int countLeft = countTotal - playerTimerVO.getBuyPowerCount();
		if (countLeft <= 0) {
			throw new GameException(AlertEnum.REQ_COUNT);
		}
		int cost = GameUtil.calPowerBuyCost(playerTimerVO.getBuyPowerCount() + 1);
		playerAccountService.hasEnoughRMBAndGift(userCached, cost);
		playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_RMB, cost, userCached.getPlayerAccountVO(), commandList, "购买行动力", GMIOEnum.OUT_BUY_POWER.usage());
		playerAccountService.addCurrency(PLAYER_PROPERTY.PROPERTY_POWER, 50, userCached.getPlayerAccountVO(), commandList, "购买行动力获得");

		playerTimerVO.setBuyPowerCount(playerTimerVO.getBuyPowerCount() + 1);
		playerTimerService.updatePowerBuy(playerTimerVO);
		nettyMessageVO.setData(VOUtil.packBuyPower(userCached).build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	// public void itemSplit(int itemId, NettyMessageVO nettyMessageVO,
	// List<NettyMessageVO> commandList) {
	// int playerId = ServerHandler.get(nettyMessageVO.getChannel());
	// UserCached userCached = ServerHandler.getUserCached(playerId);
	// List<PlayerKnapsackVO> kList =
	// userCached.getUserKnapsack().getKnapsackList();
	// NettyMessageVO resMsg = new NettyMessageVO();
	// resMsg.setCommandCode(nettyMessageVO.getCommandCode());
	// ItemUserResp.Builder res = ItemUserResp.newBuilder();
	//
	// }

}
