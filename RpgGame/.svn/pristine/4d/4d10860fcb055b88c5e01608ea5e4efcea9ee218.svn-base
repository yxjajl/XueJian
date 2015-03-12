package com.dh.processor;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.constant.TaskConstant;
import com.dh.game.vo.base.EnhanceCostVO;
import com.dh.game.vo.hero.HeroSkillProto.HeroSkillUpLevelRequest;
import com.dh.game.vo.hero.HeroSkillProto.HeroSkillUpLevelResponse;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.SkillLevelCostRes;
import com.dh.service.HeroService;
import com.dh.service.PlayerAccountService;
import com.dh.util.CommandUtil;
import com.dh.util.MyClassLoaderUtil;
import com.dh.vo.user.UserCached;

@Component
public class HeroSkillProcessor {
	private static Logger logger = Logger.getLogger(HeroSkillProcessor.class);

	@Resource
	private HeroService heroService;
	@Resource
	private PlayerAccountService playerAccountService;

	/**
	 * 主动技能升级
	 * 
	 * @param request
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void skillUpLevel(HeroSkillUpLevelRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		PlayerHeroVO playerHeroVO = userCached.getUserHero().getPlayerHeroVOById(request.getHeroId());
		if (playerHeroVO == null) {
			throw new GameException(AlertEnum.HERO_NOT_FOUND);
		}

		int skillLevel = playerHeroVO.getSkillLevel();

		EnhanceCostVO enhanceCostVO = SkillLevelCostRes.getInstance().getEnhanceCostVO(skillLevel);
		if (enhanceCostVO == null || enhanceCostVO.getCostMoney() == 0) {
			throw new GameException(AlertEnum.SKILL_LEVEL_LIMIT);
		}

		playerAccountService.hasEnoughMoney(userCached, enhanceCostVO.getCostMoney());

		playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, enhanceCostVO.getCostMoney(), userCached.getPlayerAccountVO(), commandList, "英雄技能升级");

		skillLevel++;
		playerHeroVO.setSkillLevel(skillLevel);

		heroService.updateHero(playerHeroVO);

		HeroSkillUpLevelResponse.Builder builder = HeroSkillUpLevelResponse.newBuilder();
		builder.setHeroId(request.getHeroId());
		builder.setSkillLevel(skillLevel);
		// heroService.updateHeroCombat(userCached, playerHeroVO, commandList);
		CommandUtil.packageNettyMessage(CSCommandConstant.HERO_SKILL_UPLEVEL, builder.build().toByteArray(), commandList);

		MyClassLoaderUtil.getInstance().getTaskCheck().changTaskByReQType(userCached, TaskConstant.TASK_SKILL_UPLEVEL, playerHeroVO.getCfgId(), skillLevel, commandList);
	}

	// /**
	// * 技能替换
	// *
	// * @param request
	// * @param nettyMessageVO
	// * @param commandList
	// * @throws Exception
	// */
	// public void skillReplace(SkillReplaceRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
	// int playerId = ServerHandler.get(nettyMessageVO.getChannel());
	// UserCached userCached = ServerHandler.getUserCached(playerId);
	//
	// PlayerHeroVO playerHeroVO = userCached.getUserHero().getPlayerHeroVOById(request.getHeroId());
	// if (playerHeroVO == null || request.getHeroId() != userCached.getUserHero().getReplaceSkillplayerId()) {
	// throw new GameException(AlertEnum.HERO_NOT_FOUND);
	// }
	//
	// userCached.getUserHero().setReplaceSkillplayerId(0);
	//
	// int count = 0;
	// int n = 0;
	// for (PassivesSkillVO psv : playerHeroVO.getNpassivesSkill()) {
	// if (psv == null) {
	// if (playerHeroVO.getLevel() >= PassivesSkillRes.openLevel[n]) {
	// count++;
	// }
	// } else {
	// count++;
	// }
	// n++;
	// }
	//
	// int cc = 0;
	// for (int value : request.getStatusList()) {
	// if (value > 0) {
	// cc++;
	// }
	// }
	//
	// if (cc > count) {
	// throw new Exception("技能数不正确 上传22" + cc + ",需要" + count);
	// }
	//
	// if (request.getStatusList() == null || request.getStatusList().size() != playerHeroVO.getNpassivesSkill().length) {
	// int x = 0;
	// if (request.getStatusList() != null) {
	// x = request.getStatusList().size();
	// }
	// throw new Exception("技能数不正确 上传" + x + ",需要" + playerHeroVO.getNpassivesSkill().length);
	// }
	//
	// for (int i = 0; i < playerHeroVO.getNpassivesSkill().length; i++) {
	// int value = request.getStatusList().get(i);
	// if (value > 0) {
	// PassivesSkillVO psv = userCached.getUserHero().findReplacePassiveSkillVO(value);
	// if (psv == null) {
	// psv = findPassivesSkillVO(playerHeroVO.getNpassivesSkill(), value);
	// if (psv == null) {
	// throw new Exception("技能没有出现在刷新列表");
	// }
	// }
	// playerHeroVO.getNpassivesSkill()[i] = psv;
	// } else {
	// playerHeroVO.getNpassivesSkill()[i] = null;
	// }
	// }
	//
	// playerHeroVO.setNpassivesSkill(playerHeroVO.getNpassivesSkill());
	//
	// heroService.updateHero(playerHeroVO);
	//
	// SkillReplaceResponse.Builder builder = SkillReplaceResponse.newBuilder();
	// builder.setHeroId(request.getHeroId());
	//
	// int i = 0;
	// int num = 0;
	// for (PassivesSkillVO psv : playerHeroVO.getNpassivesSkill()) {
	// if (psv == null) {
	// if (playerHeroVO.getLevel() >= PassivesSkillRes.openLevel[i]) {
	// builder.addPassivesSkill(0);
	// } else {
	// builder.addPassivesSkill(-PassivesSkillRes.openLevel[i]);
	// }
	// } else {
	// builder.addPassivesSkill(psv.getId());
	// num++;
	// }
	// i++;
	//
	// }
	// CommandUtil.packageNettyMessage(CSCommandConstant.HERO_SKILL_REPLACE, builder.build().toByteArray(), commandList);
	//
	// MyClassLoaderUtil.getInstance().getTaskCheck().changTaskByReQTypeAcc2(userCached, TaskConstant.TASK_NPSKILL, 0, num, commandList);
	// }
	//
	// public static PassivesSkillVO findPassivesSkillVO(PassivesSkillVO[] arr, int id) {
	// for (PassivesSkillVO passivesSkillVO : arr) {
	// if (passivesSkillVO != null && passivesSkillVO.getId() == id) {
	// return passivesSkillVO;
	// }
	// }
	//
	// return null;
	// }
	//
	// /**
	// * 技能刷新
	// *
	// * @param nettyMessageVO
	// * @param commandList
	// * @throws Exception
	// */
	// public void skillRefresh(SkillRefreshRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
	// int playerId = ServerHandler.get(nettyMessageVO.getChannel());
	// UserCached userCached = ServerHandler.getUserCached(playerId);
	//
	// PlayerHeroVO playerHeroVO = userCached.getUserHero().getPlayerHeroVOById(request.getHeroId());
	// if (playerHeroVO == null) {
	// throw new GameException(AlertEnum.HERO_NOT_FOUND);
	// }
	//
	// int maxLevel = 0;
	//
	// for (PassivesSkillVO temp : playerHeroVO.getNpassivesSkill()) {
	// if (temp != null && temp.getLevel() > maxLevel) {
	// maxLevel = temp.getLevel();
	// }
	// }
	// if (maxLevel > 5) {
	// maxLevel = 5;
	// }
	//
	// int costMoneh = (int) Math.pow(10, (2 + maxLevel));
	//
	// playerAccountService.hasEnoughMoney(userCached, costMoneh);
	// playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, costMoneh, userCached.getPlayerAccountVO(), commandList, "刷被动技能");
	// PassivesSkillVO[] replaceList = PassivesSkillRes.getInstance().random(playerHeroVO.getPassivesSkill(), playerHeroVO.getLevel(), playerHeroVO.getNpassivesSkill());
	//
	// userCached.getUserHero().setReplaceSkillplayerId(request.getHeroId());
	// userCached.getUserHero().setReplaceList(replaceList);
	//
	// SkillRefreshResponse.Builder builder = SkillRefreshResponse.newBuilder();
	// builder.setHeroId(request.getHeroId());
	//
	// for (PassivesSkillVO passivesSkillVO : replaceList) {
	// if (passivesSkillVO == null) {
	// builder.addSkillid(0);
	// } else {
	// builder.addSkillid(passivesSkillVO.getId());
	// }
	// }
	//
	// CommandUtil.packageNettyMessage(CSCommandConstant.HERO_SKILL_REFRESH, builder.build().toByteArray(), commandList);
	//
	// }

}
