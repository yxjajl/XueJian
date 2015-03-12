package com.dh.processor;

import org.springframework.stereotype.Component;

@Component
public class TechProcessor {
	// @Resource
	// private TechService techService;
	// @Resource
	// private PlayerAccountService playerAccountService;
	// @Resource
	// private TasksService tasksService;
	//
	// public void getTechList(NettyMessageVO nettyMessageVO,
	// List<NettyMessageVO> commandList) {
	// int playerId = ServerHandler.get(nettyMessageVO.getChannel());
	// UserCached userCached = ServerHandler.getUserCached(playerId);
	// BaseTechVO[] baseTechVOs = techService.getBaseTechVOs(playerId,
	// userCached);// 激活,初始化一定放在最前面
	// TechListResp.Builder resp = TechListResp.newBuilder();
	// PlayerTechVO techVo = userCached.getTechVO();
	// BaseTechVO baseTechVO = null;
	// TechInfo.Builder techBuild = null;
	// int cd = 0;
	// for (int i = 0; i < TechConstants.TECH_ARRAY_LENGTH; i++) {
	// baseTechVO = baseTechVOs[i];
	// cd = techService.getCdSecond(userCached, i, techVo);
	// techBuild =
	// TechInfo.newBuilder().setType(baseTechVO.getType()).setCdTime(cd).setAddtion(baseTechVO.getParams()).setNextAddtion(baseTechVO.getNextParams()).setLevel(baseTechVO.getLevel())
	// .setCost(baseTechVO.getCost()).setCdCost(techService.calCdCost(cd,
	// playerId));
	// resp.addTechInfo(techBuild);
	// }
	// nettyMessageVO.setData(resp.build().toByteArray());
	// commandList.add(nettyMessageVO);
	// }
	//
	// public void techUp(TechUpReq req, NettyMessageVO nettyMessageVO,
	// List<NettyMessageVO> commandList) throws Exception {
	// int index = TechService.getIndexByType(req.getType());
	// if (index == -1) {
	// throw new GameException("升级对象不存在");
	// }
	// int playerId = ServerHandler.get(nettyMessageVO.getChannel());
	// UserCached userCached = ServerHandler.getUserCached(playerId);
	// if (userCached == null) {
	// commandList.add(CommonCommand.USER_NOT_EXIST);
	// return;
	// }
	// TechUpResp.Builder resp = TechUpResp.newBuilder();
	// BaseTechVO baseTechVo = techService.getBaseTechVOs(playerId,
	// userCached)[index];
	// PlayerTechVO techVo = userCached.getTechVO();
	// int cd = techService.getCdSecond(userCached, index, techVo);
	// if (cd != 0) {
	// commandList.add(CommonCommand.CD_NOT_END);
	// return;
	// }
	// if (baseTechVo.getLevel() >= userCached.getPlayerVO().getLevel()) {
	// throw new GameException("不可超过玩家等级:" +
	// userCached.getPlayerVO().getLevel());
	// }
	//
	// int money = baseTechVo.getCost();
	// if (money == 0) {
	// commandList.add(CommonCommand.MAX_LEVEL_ARRIVED);
	// return;
	// }
	// if (userCached.getPlayerAccountVO().getGmMoney() < money) {
	// commandList.add(CommonCommand.REQ_MONEY);
	// return;
	// }
	// BaseTechVO newBaseVo =
	// TechRes.getInstance().getTechByLevelType(baseTechVo.getLevel() + 1,
	// baseTechVo.getType());
	// if (newBaseVo == null) {
	// commandList.add(CommonCommand.MAX_LEVEL_ARRIVED);
	// return;
	// }
	// techVo.getBaseTechVOs()[index] = newBaseVo;
	// techService.updateTech(index, playerId, techVo);
	// techService.addTechUpCount(RecordConstants.COUNTS_INDEX_ARRAY[0],
	// playerId);
	// List<PlayerSoldierVO> soldierlist = userCached.getSoldierList();
	// if (index < 3) {// 升级英雄科技
	// List<PlayerHeroVO> heros = userCached.getHeroList();
	// if (heros != null) {
	// for (PlayerHeroVO playerHeroVO : heros) {
	// CombatUtil.heroCombat(playerHeroVO, userCached);
	// CommandUtil.updateHeroADH(playerHeroVO);
	// }
	// }
	// } else {// 升级士兵科技
	// if (soldierlist != null) {
	// for (PlayerSoldierVO playerSoldierVO : soldierlist) {// 重新计算属性
	// CombatUtil.soldierCombat(playerSoldierVO, userCached);
	// CommandUtil.packageSoldierInfo(playerSoldierVO, 0);
	// }
	// }
	// }
	// cd = techService.getCdSecond(userCached, index, techVo);
	// int cost = techService.calCdCost(cd, playerId);
	// playerAccountService.deductMoney(money, userCached.getPlayerAccountVO(),
	// commandList, "升级科技扣钱");
	// resp.setTechInfo(TechInfo.newBuilder().setAddtion(newBaseVo.getParams()).setLevel(newBaseVo.getLevel()).setNextAddtion(newBaseVo.getNextParams()).setType(newBaseVo.getType()).setCdTime(cd)
	// .setCost(newBaseVo.getCost()).setCdCost(cost));
	// nettyMessageVO.setData(resp.build().toByteArray());
	// commandList.add(nettyMessageVO);
	//
	// tasksService.changTaskByReQType(userCached,
	// TaskConstant.TASK_HAVE_TECHNOLOGY, newBaseVo.getLevel());//科技升级
	//
	// }
	//
	// /**
	// * 冷却加速
	// */
	// public void techAcc(TechCdAccReq req, NettyMessageVO nettyMessageVO,
	// List<NettyMessageVO> commandList) {
	// int index = TechService.getIndexByType(req.getType());
	// if (index == -1) {
	// throw new GameException("升级对象不存在");
	// }
	// int playerId = ServerHandler.get(nettyMessageVO.getChannel());
	// UserCached userCached = ServerHandler.getUserCached(playerId);
	// Builder resp = TechCdAccResp.newBuilder();
	// BaseTechVO newBaseVo = techService.getBaseTechVOs(playerId,
	// userCached)[index];
	// PlayerTechVO techVo = userCached.getTechVO();
	// int cd = techService.getCdSecond(userCached, index, techVo);
	// if (cd == 0) {
	// commandList.add(CommonCommand.CD_FINISHED_ERROR);
	// return;
	// }
	// int cost = techService.calCdCost(cd, playerId);
	// if (userCached.getPlayerAccountVO().getGmRmb() < cost) {
	// commandList.add(CommonCommand.REQ_RMB);
	// return;
	// }
	// try {
	// playerAccountService.deductRMB(cost, userCached.getPlayerAccountVO(),
	// commandList, "加速科技队列冷却");
	// } catch (Exception e) {
	// commandList.add(CommonCommand.OUTCOME_ERROR);
	// return;
	// }
	// techService.accTechCD(index, playerId, techVo);
	// resp.setTechInfo(TechInfo.newBuilder().setAddtion(newBaseVo.getParams()).setLevel(newBaseVo.getLevel()).setNextAddtion(newBaseVo.getNextParams()).setType(newBaseVo.getType()).setCdTime(0)
	// .setCost(newBaseVo.getCost()).setCdCost(0));
	// nettyMessageVO.setData(resp.build().toByteArray());
	// commandList.add(nettyMessageVO);
	// }

}
