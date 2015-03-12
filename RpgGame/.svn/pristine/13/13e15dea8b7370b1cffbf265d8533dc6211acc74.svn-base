package com.dh.processor;

import org.springframework.stereotype.Component;

@Component
public class SoldierProcessor {
//	@Resource
//	private SoldierService soldierService;
//	@Resource
//	private HeroService heroService;
//	@Resource
//	private KnapsackService knapsackService;
//	@Resource
//	private PlayerAccountService playerAccountService;
//	@Resource
//	private TasksService tasksService;
//
//	/**
//	 * 进入士兵升级界面
//	 * 
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void prePareSoldier(PrePareSoldierRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
//		UserCached userCached = ServerHandler.getUserCached(playerId);
//
//		PlayerSoldierVO playerSoldierVO = userCached.getPlayerSoldierVOByCfgId(request.getCfgId());
//		if (playerSoldierVO == null) {
//			throw new GameException("没有这种士兵" + request.getCfgId());
//		}
//
//		BaseSoldierInfoVO baseSoldierInfoVO = SoldierRes.getInstance().getSoldierByCfgIDAndLevel(playerSoldierVO.getCfgId(), playerSoldierVO.getLevel() + 1);
//
//		PrePareSoldierResponse.Builder prePareSoldierResponse = PrePareSoldierResponse.newBuilder();
//		prePareSoldierResponse.setCfgId(request.getCfgId());
//		if (baseSoldierInfoVO == null) {
//			prePareSoldierResponse.setAddHp(0);
//			prePareSoldierResponse.setAddAtk(0);
//			prePareSoldierResponse.setAddDef(0);
//			prePareSoldierResponse.setCostMoney(0);
//		} else {
//			prePareSoldierResponse.setAddHp(baseSoldierInfoVO.getHp() - playerSoldierVO.getBaseSoldierInfoVO().getHp());
//			prePareSoldierResponse.setAddAtk(baseSoldierInfoVO.getAtk() - playerSoldierVO.getBaseSoldierInfoVO().getAtk());
//			prePareSoldierResponse.setAddDef(baseSoldierInfoVO.getDef() - playerSoldierVO.getBaseSoldierInfoVO().getDef());
//			prePareSoldierResponse.setCostMoney(0);
//			Material.Builder material = null;
//			for (int i = 0; i < baseSoldierInfoVO.getReq_itemid().length; i++) {
//				if (baseSoldierInfoVO.getReq_itemid()[i] > 0) {
//					material = Material.newBuilder();
//					material.setCfgId(baseSoldierInfoVO.getReq_itemid()[i]);
//					material.setNumber(baseSoldierInfoVO.getReq_itemnum()[i]);
//					prePareSoldierResponse.addMaterial(material);
//				}
//			}
//
//		}
//
//		NettyMessageVO tempNettyMessageVO = new NettyMessageVO();
//		tempNettyMessageVO.setCommandCode(CSCommandConstant.SOLDIER_PREPARE_UPLEVEL);
//		tempNettyMessageVO.setData(prePareSoldierResponse.build().toByteArray());
//		commandList.add(tempNettyMessageVO);
//	}
//
//	/**
//	 * 士兵升级
//	 * 
//	 * @param request
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void soldierUpLevel(PrePareSoldierRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
//		UserCached userCached = ServerHandler.getUserCached(playerId);
//		PlayerSoldierVO playerSoldierVO = userCached.getPlayerSoldierVOByCfgId(request.getCfgId());
//		if (playerSoldierVO == null) {
//			throw new GameException("没有这种士兵" + request.getCfgId());
//		}
//
//		BaseSoldierInfoVO baseSoldierInfoVO = SoldierRes.getInstance().getSoldierByCfgIDAndLevel(playerSoldierVO.getCfgId(), playerSoldierVO.getLevel() + 1);
//		if (baseSoldierInfoVO == null) {
//			throw new GameException("士兵种已经是最高级了" + request.getCfgId());
//		}
//		int itemid = 0;
//		int number = 0;
//		for (int i = 0; i < playerSoldierVO.getBaseSoldierInfoVO().getReq_itemid().length; i++) {
//			itemid = playerSoldierVO.getBaseSoldierInfoVO().getReq_itemid()[i];
//			number = playerSoldierVO.getBaseSoldierInfoVO().getReq_itemnum()[i];
//			if (itemid > 0 && (!knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), itemid, number))) {
//				throw new GameException("材料不足" + itemid);
//			}
//		}
//
//		// 扣材料
//		for (int i = 0; i < playerSoldierVO.getBaseSoldierInfoVO().getReq_itemid().length; i++) {
//			itemid = playerSoldierVO.getBaseSoldierInfoVO().getReq_itemid()[i];
//			number = playerSoldierVO.getBaseSoldierInfoVO().getReq_itemnum()[i];
//			knapsackService.removeItem(userCached, itemid, number, commandList);
//		}
//
//		playerSoldierVO.setLevel(baseSoldierInfoVO.getLevel());
//		playerSoldierVO.setBaseSoldierInfoVO(baseSoldierInfoVO);
//
//		soldierService.updateSoldier(playerSoldierVO);
//
//		CombatUtil.soldierCombat(playerSoldierVO, userCached);
//		commandList.add(CommandUtil.packageSoldierInfo(playerSoldierVO, 0));
//
//		// 角色总战斗力
//		CombatUtil.playerCombat(userCached);
//		CommandUtil.packPlayerCombat(userCached, commandList);
//
//		prePareSoldier(request, nettyMessageVO, commandList);
//
//		String cfgId = String.valueOf(request.getCfgId()); // 将各种士兵升到num级
//		char a = cfgId.charAt(0);
//		if (a == '1') { // 步兵
//			tasksService.changTaskByReQType(userCached, TaskConstant.TASK_SODIER_UPGRADE, playerSoldierVO.getLevel(), 1);
//		} else if (a == '2') {// 空军
//			tasksService.changTaskByReQType(userCached, TaskConstant.TASK_HAVE_AIRFORCE, playerSoldierVO.getLevel(), 1);
//		} else if (a == '3') {// 装甲
//			tasksService.changTaskByReQType(userCached, TaskConstant.TASK_HAVE_ARMOURED, playerSoldierVO.getLevel(), 1);
//		}
//	}
//
//	/**
//	 * 获取升级信息
//	 * 
//	 * @param request
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void prepareHeroUpLevel(HeroUpdateRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
//		UserCached userCached = ServerHandler.getUserCached(playerId);
//		PlayerHeroVO playerHeroVO = userCached.getPlayerHeroVOByCfgId(request.getHeroCfgId());
//		if (playerHeroVO == null) {
//			throw new GameException("不存在的英雄" + request.getHeroCfgId());
//		}
//
//		BaseLevelVO baseLevelVO = LevelRes.getInstance().getHeroBaseLevelVO(playerHeroVO.getBaseLevelVO().getId(), playerHeroVO.getLevel() + 1);
//		if (baseLevelVO == null) {
//			throw new GameException("英雄已经达到最高级了" + request.getHeroCfgId());
//		}
//
//		int countExp = 0;
//		if (request.getItemidCount() > 0) {
//			HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
//			for (int cfgId : request.getItemidList()) {
//				Integer vaInteger = map.get(cfgId);
//				if (vaInteger == null) {
//					vaInteger = new Integer(0);
//				}
//				vaInteger++;
//				map.put(cfgId, vaInteger);
//				BaseItemVO baseItemVO = ItemRes.getInstance().getBaseItemVO(cfgId);
//				if (baseItemVO == null || baseItemVO.getItem_uses() != ShopConstants.ITEM_USE_FULU) {
//					throw new GameException("能量水晶数据错误" + cfgId);
//				}
//
//				countExp += baseItemVO.getReward_id();// 经验放在reward里(特别处理)
//			}
//
//			for (int cfgId : map.keySet()) {
//				int num = map.get(cfgId);
//				if (!knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), cfgId, num)) {
//					throw new GameException("缺少足够的能量水晶" + cfgId);
//				}
//			}
//
//		}
//
//		if (countExp > 0) {
//			PrePareHeroUpdateResponse.Builder prePareHeroUpdateResponse = PrePareHeroUpdateResponse.newBuilder();
//
//			int tempExp = playerHeroVO.getExp() + countExp;
//			int level = playerHeroVO.getLevel();
//			int oldLevel = level;
//
//			BaseLevelVO tempBaseLevelVO = null;
//			baseLevelVO = playerHeroVO.getBaseLevelVO();
//			tempBaseLevelVO = baseLevelVO;
//			if (baseLevelVO == null) {
//				return;
//			}
//			while (tempExp >= tempBaseLevelVO.getExp()) {
//				level++;
//				tempExp = tempExp - baseLevelVO.getExp();
//				baseLevelVO = LevelRes.getInstance().getHeroBaseLevelVO(playerHeroVO.getBaseHeroInfoVO().getRole(), level);
//				if (baseLevelVO == null) { // 升到最高级了
//					tempExp = 0;
//					level--;
//					break;
//				}
//				tempBaseLevelVO = baseLevelVO;
//			}
//
//			if (oldLevel == level) {
//				prePareHeroUpdateResponse.setLevel(0);
//				prePareHeroUpdateResponse.setHp(0);
//				prePareHeroUpdateResponse.setAtk(0);
//				prePareHeroUpdateResponse.setDef(0);
//				prePareHeroUpdateResponse.setExp(countExp);
//				prePareHeroUpdateResponse.setCost(countExp);
//				prePareHeroUpdateResponse.setMaxexp(tempBaseLevelVO.getExp());
//				prePareHeroUpdateResponse.setTempexp(tempExp);
//
//			} else {
//				prePareHeroUpdateResponse.setLevel(level - oldLevel);
//				prePareHeroUpdateResponse.setHp(baseLevelVO.getHp() - playerHeroVO.getBaseLevelVO().getHp());
//				prePareHeroUpdateResponse.setAtk(baseLevelVO.getAtk() - playerHeroVO.getBaseLevelVO().getAtk());
//				prePareHeroUpdateResponse.setDef(baseLevelVO.getDef() - playerHeroVO.getBaseLevelVO().getDef());
//				prePareHeroUpdateResponse.setExp(countExp);
//				prePareHeroUpdateResponse.setCost(countExp);
//				prePareHeroUpdateResponse.setMaxexp(tempBaseLevelVO.getExp());
//				prePareHeroUpdateResponse.setTempexp(tempExp);
//			}
//
//			NettyMessageVO tempNettyMessageVO = new NettyMessageVO();
//			tempNettyMessageVO.setCommandCode(CSCommandConstant.PRE_HERO_UPDATE);
//			tempNettyMessageVO.setData(prePareHeroUpdateResponse.build().toByteArray());
//			commandList.add(tempNettyMessageVO);
//
//		}
//	}
//
//	/**
//	 * 英雄升级
//	 * 
//	 * @param request
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void heroUpLevel(HeroUpdateRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
//		UserCached userCached = ServerHandler.getUserCached(playerId);
//		PlayerHeroVO playerHeroVO = userCached.getPlayerHeroVOByCfgId(request.getHeroCfgId());
//		if (playerHeroVO == null) {
//			throw new GameException("不存在的英雄" + request.getHeroCfgId());
//		}
//
//		if (playerHeroVO.getLevel() >= userCached.getPlayerVO().getLevel()) {
//			throw new GameException("英雄等级不能超过角色等级");
//		}
//
//		BaseLevelVO baseLevelVO = LevelRes.getInstance().getHeroBaseLevelVO(playerHeroVO.getBaseLevelVO().getId(), playerHeroVO.getLevel() + 1);
//		if (baseLevelVO == null) {
//			throw new GameException("英雄已经达到最高级了" + request.getHeroCfgId());
//		}
//
//		int countExp = 0;
//		if (request.getItemidCount() > 0) {
//			HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
//			for (int cfgId : request.getItemidList()) {
//				Integer vaInteger = map.get(cfgId);
//				if (vaInteger == null) {
//					vaInteger = new Integer(0);
//				}
//				vaInteger++;
//				map.put(cfgId, vaInteger);
//				BaseItemVO baseItemVO = ItemRes.getInstance().getBaseItemVO(cfgId);
//				if (baseItemVO == null || baseItemVO.getItem_uses() != ShopConstants.ITEM_USE_FULU) {
//					throw new GameException("能量水晶数据错误" + cfgId);
//				}
//
//				countExp += baseItemVO.getReward_id();// 经验放在reward里(特别处理)
//
//			}
//
//			for (int cfgId : map.keySet()) {
//				int num = map.get(cfgId);
//				if (!knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), cfgId, num)) {
//					throw new GameException("缺少足够的能量水晶" + cfgId);
//				}
//
//				// countExp += (ItemRes.getInstance().getBaseItemVO(cfgId).getReward_id() * num); // 经验放在reward里(特别处理)
//			}
//
//			if (!userCached.getPlayerAccountVO().hasEnoughMoney(countExp)) {
//				throw new GameException("钞票不足");
//			}
//
//			playerAccountService.deductMoney(countExp, userCached.getPlayerAccountVO(), commandList, "英雄升级");
//			// 扣材料
//			for (int cfgId : map.keySet()) {
//				int nn = map.get(cfgId);
//				knapsackService.removeItem(userCached, cfgId, nn, commandList);
//			}
//		}
//
//		if (countExp > 0) {
//			heroService.addHeroExp(userCached, playerHeroVO, countExp, commandList);
//		}
//		nettyMessageVO.setData(null);
//		commandList.add(nettyMessageVO);
//	}
//
//	/**
//	 * 修改士兵和英雄上阵配置
//	 * 
//	 * @param request
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void updateSoldierTeam(SoldierTeam request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
//		UserCached userCached = ServerHandler.getUserCached(playerId);
//		PlayerSoldierTeamVO playerSoldierTeamVO = userCached.getPlayerSoldierTeamVO();
//
//		boolean isUpdateHero = false;
//		if (playerSoldierTeamVO.getHeroid() != request.getHeroCfgId()) {
//			isUpdateHero = true;
//		}
//
//		playerSoldierTeamVO.setHeroid(request.getHeroCfgId());
//
//		StringBuffer buffer = new StringBuffer();
//		int n = 0;
//
//		for (int value : request.getSoldierCfgIdList()) {
//			if (n > 0) {
//				buffer.append(",");
//			}
//
//			if (value > 0 && Tool.intInArrTime(value, request.getSoldierCfgIdList()) > 1) {
//				throw new GameException("重复的设置上阵士兵");
//			}
//
//			if (value > 0 && userCached.getPlayerSoldierVOByCfgId(value) == null) {
//				throw new GameException("无效的上阵士兵设置" + value);
//			}
//
//			buffer.append(value);
//			n++;
//			if (n > playerSoldierTeamVO.getSoldierLength()) {
//				break;
//			}
//		}
//		// 长度不够补0
//		if (n < playerSoldierTeamVO.getSoldierLength()) {
//			for (int i = n; i < playerSoldierTeamVO.getSoldierLength(); i++) {
//				buffer.append(",").append(0);
//			}
//		}
//
//		playerSoldierTeamVO.setSoldierline(buffer.toString());
//
//		if (isUpdateHero) {
//			PlayerHeroVO playerHeroVO = userCached.getPlayerHeroVOByCfgId(userCached.getPlayerSoldierTeamVO().getHeroid());
//			if (playerHeroVO != null) {
//				CommandUtil.updateHeroAndPackageCommand(playerHeroVO, userCached, commandList);
//			}
//		}
//
//		soldierService.updateSoldierTeam(playerSoldierTeamVO);
//
//		nettyMessageVO.setData(null);
//		commandList.add(nettyMessageVO);
//		tasksService.changTaskByReQTypes(userCached, TaskConstant.TASK_HERO_BATTLE, request.getHeroCfgId());// 设置“KungFu·Chen”出战
//	}
//
//	/**
//	 * 取副将加成数据
//	 * 
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void prepareAssitor(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
//		UserCached userCached = ServerHandler.getUserCached(playerId);
//
//		int atk = 0;
//		int def = 0;
//		int hp = 0;
//
//		if (userCached.getAssistorList() != null && userCached.getAssistorList().size() > 0) {
//			for (PlayerHeroVO assitor : userCached.getAssistorList()) {
//				atk += (assitor.getFinal_atk() * CombatUtil.FUJIANGJIACHENG);
//				def += (assitor.getFinal_def() * CombatUtil.FUJIANGJIACHENG);
//				hp += (assitor.getFinal_hp() * CombatUtil.FUJIANGJIACHENG);
//			}
//		}
//
//		PrepareAssistorResponse.Builder prepareAssistorResponse = PrepareAssistorResponse.newBuilder();
//		prepareAssistorResponse.setAtk(atk);
//		prepareAssistorResponse.setDef(def);
//		prepareAssistorResponse.setHp(hp);
//		prepareAssistorResponse.setCd(100);
//		prepareAssistorResponse.setCri(0);
//		prepareAssistorResponse.setMovespeed(0);
//
//		String[] strArr = userCached.getPlayerSoldierTeamVO().getAssistorline().split(",");
//		for (String temp : strArr) {
//			int cfgId = Integer.valueOf(temp);
//			prepareAssistorResponse.addAssistor(cfgId);
//			if (cfgId == -1) {
//				prepareAssistorResponse.addActiveLevel(40);
//			} else {
//				prepareAssistorResponse.addActiveLevel(0);
//			}
//		}
//
//		NettyMessageVO tempNettyMessageVO = new NettyMessageVO();
//		tempNettyMessageVO.setCommandCode(CSCommandConstant.PREPARE_ASSISTOR);
//		tempNettyMessageVO.setData(prepareAssistorResponse.build().toByteArray());
//		commandList.add(tempNettyMessageVO);
//
//	}
//
//	/**
//	 * 修改副将
//	 * 
//	 * @param request
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void updateAssitor(UpdateAssistorRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
//		UserCached userCached = ServerHandler.getUserCached(playerId);
//		if (userCached.getAssistorList() != null) {
//			userCached.getAssistorList().clear();
//		} else {
//			userCached.setAssistorList(new ArrayList<PlayerHeroVO>(2));
//		}
//		StringBuffer buffer = new StringBuffer();
//		int i = 0;
//		for (int cfgId : request.getAssistorList()) {
//			if (cfgId > 0) {
//
//				if (cfgId > 0 && Tool.intInArrTime(cfgId, request.getAssistorList()) > 1) {
//					throw new GameException("重复的副将");
//				}
//
//				PlayerHeroVO assitor = userCached.getPlayerHeroVOByCfgId(cfgId);
//				if (assitor == null) {
//					throw new GameException("无效的英雄id" + cfgId);
//				}
//				userCached.getAssistorList().add(assitor);
//			}
//			if (i > 0) {
//				buffer.append(",");
//			}
//			buffer.append(cfgId);
//			i++;
//		}
//		userCached.getPlayerSoldierTeamVO().setAssistorline(buffer.toString());
//		soldierService.updateAssistor(userCached.getPlayerSoldierTeamVO());
//
//		PlayerHeroVO playerHeroVO = userCached.getPlayerHeroVOByCfgId(userCached.getPlayerSoldierTeamVO().getHeroid());
//		if (playerHeroVO != null) {
//			CombatUtil.heroCombat(playerHeroVO, userCached.getAssistorList());
//			CommandUtil.updateHeroAndPackageCommand(playerHeroVO, userCached, commandList);
//		}
//
//		prepareAssitor(nettyMessageVO, commandList);
//	}
//
//	/**
//	 * 解锁英雄
//	 * 
//	 * @param request
//	 * @param nettyMessageVO
//	 * @param commandList
//	 * @throws Exception
//	 */
//	public void unlockHero(UnLockHeroRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
//		UserCached userCached = ServerHandler.getUserCached(playerId);
//		int heroCgfId = request.getHeroCfgId();
//		PlayerHeroVO playerHeroVO = userCached.getPlayerHeroVOByCfgId(heroCgfId);
//		if (playerHeroVO == null) {
//			throw new GameException("不存在的英雄");
//		}
//
//		if (playerHeroVO.getLockStatus() == 1) {
//			throw new GameException("此英雄已解锁");
//		}
//
//		if (!userCached.getPlayerAccountVO().hasEnoughMoney(playerHeroVO.getBaseHeroInfoVO().getCost_feat())) {
//			throw new GameException("钞票不足");
//		}
//		playerHeroVO.setLockStatus(1);
//		playerAccountService.deductMoney(playerHeroVO.getBaseHeroInfoVO().getCost_feat(), userCached.getPlayerAccountVO(), commandList, "英雄解锁" + heroCgfId);
//		heroService.addPlayerHero(playerHeroVO);
//
//		UnLockHeroResponse.Builder unLockHeroResponse = UnLockHeroResponse.newBuilder();
//		unLockHeroResponse.setHeroCfgId(heroCgfId);
//		unLockHeroResponse.build();
//		NettyMessageVO tempNettyMessageVO = new NettyMessageVO();
//		tempNettyMessageVO.setCommandCode(CSCommandConstant.HERO_UNLOCK);
//		tempNettyMessageVO.setData(unLockHeroResponse.build().toByteArray());
//		commandList.add(tempNettyMessageVO);
//		List<PlayerHeroVO> heroList = userCached.getHeroList();
//		tasksService.changTaskByReQType(userCached, TaskConstant.TASK_HERO_NUM, heroList.size());// 拥有num个英雄
//	}
//
//	public void heroPreEnhance(PreHeroEnhanceRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
//		UserCached userCached = ServerHandler.getUserCached(playerId);
//		PlayerHeroVO playerHeroVO = userCached.getPlayerHeroVOByCfgId(request.getHeroCfgId());
//		if (playerHeroVO == null) {
//			throw new GameException("不存在的英雄");
//		}
//
//		if (playerHeroVO.getLockStatus() == 0) {
//			throw new GameException("此英雄未解锁");
//		}
//
//		playerHeroVO.setTempatk(0);
//		playerHeroVO.setTempdef(0);
//		playerHeroVO.setTemphp(0);
//
//		PreHeroEnhanceResponse.Builder rreHeroEnhanceResponse = PreHeroEnhanceResponse.newBuilder();
//		rreHeroEnhanceResponse.setHeroCfgId(request.getHeroCfgId());
//		rreHeroEnhanceResponse.setHp(playerHeroVO.getHp());
//		rreHeroEnhanceResponse.setAtk(playerHeroVO.getAtk());
//		rreHeroEnhanceResponse.setDef(playerHeroVO.getDef());
//		rreHeroEnhanceResponse.setMaxatk(playerHeroVO.getBaseLevelVO().getAtk() * 2);
//		rreHeroEnhanceResponse.setMaxdef(playerHeroVO.getBaseLevelVO().getDef() * 2);
//		rreHeroEnhanceResponse.setMaxhp(playerHeroVO.getBaseLevelVO().getHp() * 2);
//		rreHeroEnhanceResponse.setCostmoney(1000);
//		rreHeroEnhanceResponse.setCostrmb(100);
//
//		NettyMessageVO tempNettyMessageVO = new NettyMessageVO();
//		tempNettyMessageVO.setCommandCode(CSCommandConstant.HERO_PRE_ENHANCE);
//		tempNettyMessageVO.setData(rreHeroEnhanceResponse.build().toByteArray());
//		commandList.add(tempNettyMessageVO);
//	}
//
//	public void heroEnhance(HeroEnhanceRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
//		UserCached userCached = ServerHandler.getUserCached(playerId);
//		PlayerHeroVO playerHeroVO = userCached.getPlayerHeroVOByCfgId(request.getHeroCfgId());
//		if (playerHeroVO == null) {
//			throw new GameException("不存在的英雄");
//		}
//
//		if (playerHeroVO.getLockStatus() == 0) {
//			throw new GameException("此英雄未解锁");
//		}
//
//		int costmoney = 1000;
//		int costrmb = 100;
//		if (request.getType() == 1) {
//			if (!userCached.getPlayerAccountVO().hasEnoughMoney(costmoney)) {
//				throw new GameException("钞票不足");
//			}
//			playerAccountService.deductMoney(costmoney, userCached.getPlayerAccountVO(), commandList, "英雄属性强化" + request.getHeroCfgId());
//		} else {
//			if (!userCached.getPlayerAccountVO().hasEnoughRMB(costrmb)) {
//				throw new GameException("金币不足");
//			}
//			playerAccountService.deductRMB(costrmb, userCached.getPlayerAccountVO(), commandList, "英雄属性强化" + request.getHeroCfgId());
//		}
//
//		// if (playerHeroVO.getAtk() >= maxAtk && playerHeroVO.getDef() >=
//		// maxDef && playerHeroVO.getHp() >= maxHp) {
//		// throw new GameException("属性都已达到上限");
//		// }
//		enhanceTool(playerHeroVO);
//
//		HeroEnhanceResponse.Builder heroEnhanceResponse = HeroEnhanceResponse.newBuilder();
//		heroEnhanceResponse.setHeroCfgId(request.getHeroCfgId());
//		heroEnhanceResponse.setHp(playerHeroVO.getTemphp());
//		heroEnhanceResponse.setAtk(playerHeroVO.getTempatk());
//		heroEnhanceResponse.setDef(playerHeroVO.getTempdef());
//
//		System.out.println("强化 hp,atk,def = " + playerHeroVO.getTemphp() + "," + playerHeroVO.getTempatk() + playerHeroVO.getTempdef());
//
//		NettyMessageVO tempNettyMessageVO = new NettyMessageVO();
//		tempNettyMessageVO.setCommandCode(CSCommandConstant.HERO_ENHANCE);
//		tempNettyMessageVO.setData(heroEnhanceResponse.build().toByteArray());
//		commandList.add(tempNettyMessageVO);
//	}
//
//	public static void enhanceTool(PlayerHeroVO playerHeroVO) {
//		int maxAtk = playerHeroVO.getBaseLevelVO().getAtk() * 2;
//		int maxDef = playerHeroVO.getBaseLevelVO().getDef() * 2;
//		int maxHp = playerHeroVO.getBaseLevelVO().getHp() * 2;
//
//		int random = RandomUtil.randomInt(100);
//
//		if (random < 40) { // 3选2
//			int r = RandomUtil.randomInt(3);
//			getRandomProperty(r, playerHeroVO);
//			getRandomProperty(r + 1, playerHeroVO);
//		} else if (random < 80) { // 3选1
//			int r = RandomUtil.randomInt(3);
//			getRandomProperty(r, playerHeroVO);
//		} else { // 全加
//			playerHeroVO.setTempatk(Tool.computerProp(maxAtk, playerHeroVO.getAtk()));
//			playerHeroVO.setTempdef(Tool.computerProp(maxDef, playerHeroVO.getDef()));
//			playerHeroVO.setTemphp(Tool.computerProp(maxHp, playerHeroVO.getHp()));
//		}
//	}
//
//	public static void getRandomProperty(int value, PlayerHeroVO playerHeroVO) {
//		int maxAtk = playerHeroVO.getBaseLevelVO().getAtk() * 2;
//		int maxDef = playerHeroVO.getBaseLevelVO().getDef() * 2;
//		int maxHp = playerHeroVO.getBaseLevelVO().getHp() * 2;
//
//		if (value > 2) {
//			value = 0;
//		}
//		if (value == 0) {
//			playerHeroVO.setTempatk(Tool.computerProp(maxAtk, playerHeroVO.getAtk()));
//		} else if (value == 1) {
//			playerHeroVO.setTempdef(Tool.computerProp(maxDef, playerHeroVO.getDef()));
//		} else {
//			playerHeroVO.setTemphp(Tool.computerProp(maxHp, playerHeroVO.getHp()));
//		}
//	}
//
//	public void heroEnhanceSave(HeroEnhanceSaveRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
//		UserCached userCached = ServerHandler.getUserCached(playerId);
//		PlayerHeroVO playerHeroVO = userCached.getPlayerHeroVOByCfgId(request.getHeroCfgId());
//		if (playerHeroVO == null) {
//			throw new GameException("不存在的英雄");
//		}
//
//		if (playerHeroVO.getLockStatus() == 0) {
//			throw new GameException("此英雄未解锁");
//		}
//
//		if (request.getResult() == 1) { // 保存属性
//			int temp = playerHeroVO.getTempatk() + playerHeroVO.getAtk();
//			if (temp < 0)
//				temp = 0;
//			playerHeroVO.setAtk(temp);
//
//			temp = playerHeroVO.getTempdef() + playerHeroVO.getDef();
//			if (temp < 0)
//				temp = 0;
//
//			playerHeroVO.setDef(temp);
//
//			temp = playerHeroVO.getTemphp() + playerHeroVO.getHp();
//			if (temp < 0)
//				temp = 0;
//			playerHeroVO.setHp(temp);
//			heroService.updateHero(playerHeroVO);
//			CommandUtil.updateHeroAndPackageCommand(playerHeroVO, userCached, commandList);
//
//		}
//
//		playerHeroVO.setTempatk(0);
//		playerHeroVO.setTempdef(0);
//		playerHeroVO.setTemphp(0);
//
//		playerHeroVO.setTempatk(0);
//		playerHeroVO.setTempdef(0);
//		playerHeroVO.setTemphp(0);
//
//		PreHeroEnhanceResponse.Builder rreHeroEnhanceResponse = PreHeroEnhanceResponse.newBuilder();
//		rreHeroEnhanceResponse.setHeroCfgId(request.getHeroCfgId());
//		rreHeroEnhanceResponse.setHp(playerHeroVO.getHp());
//		rreHeroEnhanceResponse.setAtk(playerHeroVO.getAtk());
//		rreHeroEnhanceResponse.setDef(playerHeroVO.getDef());
//
//		int maxAtk = playerHeroVO.getBaseLevelVO().getAtk() * 2;
//		int maxDef = playerHeroVO.getBaseLevelVO().getDef() * 2;
//		int maxHp = playerHeroVO.getBaseLevelVO().getHp() * 2;
//
//		rreHeroEnhanceResponse.setMaxatk(maxAtk);
//		rreHeroEnhanceResponse.setMaxdef(maxDef);
//		rreHeroEnhanceResponse.setMaxhp(maxHp);
//		rreHeroEnhanceResponse.setCostmoney(1000);
//		rreHeroEnhanceResponse.setCostrmb(100);
//
//		NettyMessageVO tempNettyMessageVO = new NettyMessageVO();
//		tempNettyMessageVO.setCommandCode(CSCommandConstant.HERO_PRE_ENHANCE);
//		tempNettyMessageVO.setData(rreHeroEnhanceResponse.build().toByteArray());
//		commandList.add(tempNettyMessageVO);
//
//	}
//
//	/**
//	 * 获取英雄传承列表 TODO
//	 */
//	public void getHeroTransList(HeroTransListReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) {
//		int heroCfgId = req.getHeroCfgId();
//		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
//		UserCached userCached = ServerHandler.getUserCached(playerId);
//		PlayerHeroVO playerHeroVO = userCached.getPlayerHeroVOByCfgId(heroCfgId);
//		if (playerHeroVO == null) {
//			throw new GameException("不存在的英雄");
//		}
//		if (playerHeroVO.getLockStatus() == 0) {
//			throw new GameException("此英雄未解锁");
//		}
//		List<PlayerHeroVO> heroList = userCached.getHeroList();
//		if (heroList != null) {
//			int maxExp = 0;
//			int maxHp = 0;
//			int maxAtk = 0;
//			int maxDef = 0;
//			HeroTransListResp.Builder resp = HeroTransListResp.newBuilder();
//			int hp = playerHeroVO.getHp();
//			int atk = playerHeroVO.getAtk();
//			int def = playerHeroVO.getDef();
//			int exp = playerHeroVO.getExp();
//			for (PlayerHeroVO playerHeroVO2 : heroList) {
//				if (playerHeroVO.getLockStatus() == 0 || playerHeroVO2.getCfgId() == heroCfgId) {
//					continue;
//				}
//				// 最大可以增加量
//				maxHp = playerHeroVO2.getBaseLevelVO().getHp() - playerHeroVO2.getHp();
//				maxAtk = playerHeroVO2.getBaseLevelVO().getAtk() - playerHeroVO2.getAtk();
//				maxDef = playerHeroVO2.getBaseLevelVO().getDef() - playerHeroVO2.getDef();
//				maxExp = playerHeroVO2.getBaseLevelVO().getExp() - playerHeroVO2.getExp();
//				maxHp = Math.max(maxHp, 0);
//				maxAtk = Math.max(maxAtk, 0);
//				maxDef = Math.max(maxDef, 0);
//				maxExp = Math.max(maxExp, 0);
//				resp.addTransAtrr(TransAtrr.newBuilder().setHeroCfgId(playerHeroVO2.getCfgId()).setHp(Math.min(hp, maxHp)).setAtk(Math.min(atk, maxAtk)).setExp(Math.min(maxExp, exp))
//						.setDef(Math.min(def, maxDef)));
//			}
//			nettyMessageVO.setData(resp.build().toByteArray());
//			commandList.add(nettyMessageVO);
//		}
//	}
//
//	public void heroTrans(HeroTransReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
//		int heroCfgId = req.getHeroCfgId();
//		int transId = req.getTranCfgId();
//		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
//		UserCached userCached = ServerHandler.getUserCached(playerId);
//		PlayerHeroVO playerHeroVO = userCached.getPlayerHeroVOByCfgId(heroCfgId);
//		PlayerHeroVO transHeroVO = userCached.getPlayerHeroVOByCfgId(transId);
//		if (playerHeroVO == null || transHeroVO == null) {
//			throw new GameException("不存在的英雄");
//		}
//		if (playerHeroVO.getLockStatus() == 0 || transHeroVO.getLockStatus() == 0) {
//			throw new GameException("此英雄未解锁");
//		}
//		if (playerHeroVO.getIsTrans() == 1) {
//			throw new GameException("该英雄已经传承过");
//		}
//
//		int hp = playerHeroVO.getHp();
//		int atk = playerHeroVO.getAtk();
//		int def = playerHeroVO.getDef();
//		int exp = playerHeroVO.getExp();
//		int maxHp = transHeroVO.getBaseLevelVO().getHp() - transHeroVO.getHp();
//		int maxAtk = transHeroVO.getBaseLevelVO().getAtk() - transHeroVO.getAtk();
//		int maxDef = transHeroVO.getBaseLevelVO().getDef() - transHeroVO.getDef();
//		int maxExp = transHeroVO.getBaseLevelVO().getExp() - transHeroVO.getExp();
//		hp = Math.min(hp, maxHp);
//		atk = Math.min(atk, maxAtk);
//		def = Math.min(def, maxDef);
//		exp = Math.min(exp, maxExp);
//		// 传出者回到初始值
//		playerHeroVO.setAtk(0);
//		playerHeroVO.setDef(0);
//		playerHeroVO.setHp(0);
//		playerHeroVO.setExp(0);
//		playerHeroVO.setIsTrans(1);// 标记为已经传承
//		// 接受者达到最新值
//		transHeroVO.setHp(hp);
//		transHeroVO.setAtk(atk);
//		transHeroVO.setDef(def);
//		transHeroVO.setExp(exp);
//		CommandUtil.updateHeroAndPackageCommand(playerHeroVO, userCached, commandList);
//		CommandUtil.updateHeroAndPackageCommand(transHeroVO, userCached, commandList);
//		heroService.updateHero(playerHeroVO);
//		heroService.updateHero(transHeroVO);
//
//	}
}
