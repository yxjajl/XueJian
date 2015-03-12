package com.dh.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dh.constants.CommonCommand;
import com.dh.constants.ItemConstants;
import com.dh.dao.PlayerKnapsackMapper;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.constant.TaskConstant;
import com.dh.game.vo.base.BaseEnhanceVO;
import com.dh.game.vo.base.BaseItemVO;
import com.dh.game.vo.base.BaseProteVO;
import com.dh.game.vo.base.Reward;
import com.dh.game.vo.item.ItemProto.FitEquipResponse;
import com.dh.game.vo.login.PlayerLoginProto.KpsItemInfoUpdateResponse;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.game.vo.user.PlayerKnapsackVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.EnhanceRes;
import com.dh.resconfig.ItemRes;
import com.dh.resconfig.ProtectionRes;
import com.dh.sqlexe.SqlSaveThread;
import com.dh.util.CombatUtil;
import com.dh.util.CommandUtil;
import com.dh.util.MyClassLoaderUtil;
import com.dh.util.SqlBuild;
import com.dh.util.VOUtil;
import com.dh.vo.user.UserCached;

/**
 * 背包业务类
 * 
 * @author Administrator
 * 
 */
@Service
public class KnapsackService {
	private static Logger LOGGER = Logger.getLogger(KnapsackService.class);
	public final static byte OPERATOR_TYPE_NEW = 0; // 新增
	public final static byte OPERATOR_TYPE_UPD = 1; // 修改
	public final static byte OPERATOR_TYPE_DEL = 2; // 删除

	/** 物品所在 */
	public final static int STATIS_KNAPSACK = 0; // 在背包
	public final static int STATIS_EQUIP = 1; // 在英雄身上s

	@Resource
	private PlayerKnapsackMapper playerKnapsackMapper;
	@Resource
	private SqlBuild sqlBuild;
	@Resource
	private SqlSaveThread sqlSaveThread;

	/**
	 * 加载物品数据
	 * 
	 * @param userCached
	 * @throws Exception
	 */
	public void loadPlayerKnapsackList(UserCached userCached) throws Exception {

		List<PlayerKnapsackVO> knapsackList = playerKnapsackMapper.getPlayerKnapsackList(userCached.getPlayerId());
		userCached.getUserKnapsack().getKnapsackList().clear();
		analysisKnaspack(userCached, knapsackList, false);
		knapsackList.clear();
	}

	public void analysisKnaspack(UserCached userCached, List<PlayerKnapsackVO> knapsackList, boolean isBag) throws Exception {
		int maxKnapsackId = 0;
		// 背包
		if (knapsackList != null) {

			for (PlayerKnapsackVO playerKnapsackVO : knapsackList) {

				if (playerKnapsackVO.getItemid() > maxKnapsackId) {
					maxKnapsackId = playerKnapsackVO.getItemid();
				}

				BaseItemVO baseItemVO = ItemRes.getInstance().getBaseItemVO(playerKnapsackVO.getCfgId());
				if (baseItemVO == null) {
					LOGGER.error("没有找到此物品的基础数据" + userCached.getPlayerId() + "," + playerKnapsackVO.getCfgId());
					throw new Exception("没有找到此物品的基础数据" + userCached.getPlayerId() + "," + playerKnapsackVO.getCfgId());
				}
				playerKnapsackVO.setBaseItemVO(baseItemVO);

				if (ItemConstants.ITEM_TYPE_EQPI == baseItemVO.getType()) {
					ItemRes.getInstance().loadGem(playerKnapsackVO);
					BaseEnhanceVO baseEnhanceVO = EnhanceRes.getInstance().getBaseEnhanceVO(playerKnapsackVO.getCfgId());
					playerKnapsackVO.setBaseEnhanceVO(baseEnhanceVO);
					// 护佑
					BaseProteVO baseProteVO = ProtectionRes.getInstance().getBaseProteVO(playerKnapsackVO.getProtectLevel());
					playerKnapsackVO.setBaseProteVO(baseProteVO);

					// 计算装备的战斗力
					CombatUtil.equipCombat(playerKnapsackVO);
				}

				if (!isBag) {
					if (playerKnapsackVO.getHeroId() > 0) {
						PlayerHeroVO playerHeroVO = userCached.getUserHero().getPlayerHeroVOById(playerKnapsackVO.getHeroId());
						if (playerHeroVO == null) {
							LOGGER.error("没有找到此英雄" + userCached.getPlayerId() + "," + playerKnapsackVO.getHeroId());
							throw new Exception("没有找到此英雄" + playerKnapsackVO.getHeroId());
						}
						playerHeroVO.addEquipment(playerKnapsackVO);
					} else {
						userCached.getUserKnapsack().getKnapsackList().add(playerKnapsackVO); // 背包
					}
				}
			}

		}

		userCached.getUserKnapsack().setMaxKnapsackId(maxKnapsackId);
	}

	/**
	 * 保存数据
	 * 
	 * @param newList
	 * @param updateList
	 * @param delList
	 * @param commandList
	 */
	public void saveKnapsack(List<PlayerKnapsackVO> newList, List<PlayerKnapsackVO> updateList, List<PlayerKnapsackVO> delList, List<NettyMessageVO> commandList) {
		if (newList != null) {
			for (PlayerKnapsackVO playerKnapsackVO : newList) {

				if (ItemConstants.ITEM_TYPE_EQPI == playerKnapsackVO.getBaseItemVO().getType()) {
					ItemRes.getInstance().loadGem(playerKnapsackVO);
					BaseEnhanceVO baseEnhanceVO = EnhanceRes.getInstance().getBaseEnhanceVO(playerKnapsackVO.getCfgId());
					playerKnapsackVO.setBaseEnhanceVO(baseEnhanceVO);
					// 护佑
					BaseProteVO baseProteVO = ProtectionRes.getInstance().getBaseProteVO(playerKnapsackVO.getProtectLevel());
					playerKnapsackVO.setBaseProteVO(baseProteVO);

					// 计算装备的战斗力
					CombatUtil.equipCombat(playerKnapsackVO);

				}

				sqlSaveThread.putSql(playerKnapsackVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerKnapsackMapper.insertPlayerKnapsackVO", playerKnapsackVO));
				commandList.add(packageNewItemInfo(playerKnapsackVO, OPERATOR_TYPE_NEW));
			}

			newList.clear();
			newList = null;
		}

		if (updateList != null) {
			for (PlayerKnapsackVO playerKnapsackVO : updateList) {
				// 重新计算装备战斗力
				if (ItemConstants.ITEM_TYPE_EQPI == playerKnapsackVO.getBaseItemVO().getType()) {
					CombatUtil.equipCombat(playerKnapsackVO);
				}
				sqlSaveThread.putSql(playerKnapsackVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerKnapsackMapper.updatePlayerKnapsackVO", playerKnapsackVO));
				if (playerKnapsackVO.getHeroId() == 0) {
					commandList.add(packageNewItemInfo(playerKnapsackVO, OPERATOR_TYPE_UPD));
				}
			}

			updateList.clear();
			updateList = null;
		}

		if (delList != null) {
			for (PlayerKnapsackVO playerKnapsackVO : delList) {
				sqlSaveThread.putSql(playerKnapsackVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerKnapsackMapper.delPlayerKnapsackVO", playerKnapsackVO));
				commandList.add(packageNewItemInfo(playerKnapsackVO, OPERATOR_TYPE_DEL));
			}
			delList.clear();
			delList = null;
		}
	}

	/**
	 * 查看背包空间
	 * 
	 * @param userCached
	 * @param cfgid
	 * @param num
	 * @param maxnum
	 * @return
	 */
	public boolean hasKnapsackGird(UserCached userCached, int n) {
		int maxGird = userCached.getPlayerAccountVO().getKnapsack();
		int curGird = userCached.getUserKnapsack().getKnapsackList().size();
		return maxGird >= (curGird + n);
	}

	// 根据物品所需存的数量和最大叠加上限
	public boolean hasKnapsackGird(UserCached userCached, int number, int maxNum) {
		int n = number / maxNum;
		if (number % maxNum > 0) {
			n++;
		}
		return hasKnapsackGird(userCached, n);
	}

	/**
	 * 把脱下的装备放包里(因为数据是存在t_player_item表里，所以只需要更新状态
	 * 
	 * @param userCached
	 * @param playerKnapsackVO
	 * @param commandList
	 */
	public void takeOnEquipment(UserCached userCached, PlayerHeroVO playerHeroVO, NettyMessageVO nettyMessageVO, PlayerKnapsackVO playerKnapsackVO, List<NettyMessageVO> commandList) throws Exception {

		if (playerHeroVO.getLevel() < playerKnapsackVO.getBaseItemVO().getReqLevel()) {
			throw new GameException(AlertEnum.HERO_LEVEL_NO);// 英雄等级不足
		}

		if (playerHeroVO.getBaseHeroInfoVO().getRace() != playerKnapsackVO.getBaseItemVO().getRace()) {
			throw new GameException(AlertEnum.RACE_NOT_MATCH);// 英雄职业不匹配
		}

		PlayerKnapsackVO sameEquipment = playerHeroVO.getSameTypeEquipment(playerKnapsackVO);
		if (sameEquipment != null) {
			takeOffEquipment(userCached, playerHeroVO, nettyMessageVO, sameEquipment, commandList);
		}

		playerKnapsackVO.setStatus(STATIS_EQUIP);
		playerKnapsackVO.setHeroId(playerHeroVO.getId());
		playerKnapsackVO.setPosition(0);
		playerHeroVO.addEquipment(playerKnapsackVO);
		userCached.getUserKnapsack().getKnapsackList().remove(playerKnapsackVO);

		// 更新数据库
		sqlSaveThread.putSql(playerKnapsackVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerKnapsackMapper.updatePlayerKnapsackVO", playerKnapsackVO));
		// 告诉前端从背包里拿掉了一个物品穿在了英雄身上

		FitEquipResponse.Builder response = FitEquipResponse.newBuilder();
		response.setHeroId(playerHeroVO.getId());
		response.setItemId(playerKnapsackVO.getItemid());

		CommandUtil.packageNettyMessage(CSCommandConstant.HERO_ON_EQUIP, response.build().toByteArray(), commandList);

	}

	/**
	 * 脱装备
	 * 
	 * @param userCached
	 * @param playerKnapsackVO
	 * @param commandList
	 */
	public void takeOffEquipment(UserCached userCached, PlayerHeroVO playerHeroVO, NettyMessageVO nettyMessageVO, PlayerKnapsackVO playerKnapsackVO, List<NettyMessageVO> commandList) throws Exception {
		// 判断有没有 背包空间
		if (!hasKnapsackGird(userCached, 1, 1)) {
			commandList.add(CommonCommand.KNAPSACK_FULL);
			return;
		}
		playerHeroVO.removeEquipment(playerKnapsackVO); // 从身上移除装备
		playerKnapsackVO.setStatus(STATIS_KNAPSACK); // 更改状态
		playerKnapsackVO.setHeroId(0);
		playerKnapsackVO.setPosition(getPos(userCached));
		userCached.getUserKnapsack().getKnapsackList().add(playerKnapsackVO); // 放进背包
		// 更新数据库
		sqlSaveThread.putSql(playerKnapsackVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerKnapsackMapper.updatePlayerKnapsackVO", playerKnapsackVO));
		// 告诉前台从英雄身上脱下了一个装备放在在背包里

		FitEquipResponse.Builder response = FitEquipResponse.newBuilder();
		response.setHeroId(playerHeroVO.getId());
		response.setItemId(playerKnapsackVO.getItemid());

		CommandUtil.packageNettyMessage(CSCommandConstant.HERO_OFF_EQUIP, response.build().toByteArray(), commandList);

	}

	/**
	 * 脱掉英雄的所有装备
	 * 
	 * @param userCached
	 * @param playerHeroVO
	 * @param commandList
	 */
	public void takeOffEquipment(UserCached userCached, PlayerHeroVO playerHeroVO, List<NettyMessageVO> commandList) throws Exception {
		if (playerHeroVO.getEquipList() != null && playerHeroVO.getEquipList().size() > 0) {
			int size = playerHeroVO.getEquipList().size();
			for (int i = 0; i < size; i++) {
				PlayerKnapsackVO playerKnapsackVO = playerHeroVO.getEquipList().get(0);
				playerHeroVO.removeEquipment(playerKnapsackVO); // 从身上移除装备
				playerKnapsackVO.setStatus(STATIS_KNAPSACK); // 更改状态
				playerKnapsackVO.setHeroId(0);
				playerKnapsackVO.setPosition(getPos(userCached));
				userCached.getUserKnapsack().getKnapsackList().add(playerKnapsackVO); // 放进背包
				// 更新数据库
				sqlSaveThread.putSql(userCached.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerKnapsackMapper.updatePlayerKnapsackVO", playerKnapsackVO));
				// 告诉前台从英雄身上脱下了一个装备放在在背包里

				FitEquipResponse.Builder response = FitEquipResponse.newBuilder();
				response.setHeroId(playerHeroVO.getId());
				response.setItemId(playerKnapsackVO.getItemid());

				CommandUtil.packageNettyMessage(CSCommandConstant.HERO_OFF_EQUIP, response.build().toByteArray(), commandList);

			}
		}
		// 判断有没有 背包空间

		playerHeroVO.getEquipList().clear(); // 从身上移除装备

	}

	/**
	 * 增加物品
	 * 
	 * @param userCached
	 * @param baseItemVO
	 * @param number
	 * @param commandList
	 */
	public void addNewItem(UserCached userCached, BaseItemVO baseItemVO, final int num, List<NettyMessageVO> commandList) throws Exception {
		List<PlayerKnapsackVO> newList = new ArrayList<PlayerKnapsackVO>();
		List<PlayerKnapsackVO> updateList = new ArrayList<PlayerKnapsackVO>();

		int number = num;
		if (baseItemVO.getMaxnum() == 1) {
			for (int i = 0; i < number; i++) {
				int pos = getPos(userCached);
				int itemid = getItemid(userCached);
				PlayerKnapsackVO newPlayerKnapsackVO = baseItemVOToPlayerKnapsackVO(baseItemVO, userCached.getPlayerId(), itemid, pos, 1);
				userCached.getUserKnapsack().getKnapsackList().add(newPlayerKnapsackVO);
				newList.add(newPlayerKnapsackVO);
			}

		} else if (number % baseItemVO.getMaxnum() == 0) {
			int n = number / baseItemVO.getMaxnum();
			for (int i = 0; i < n; i++) {
				int pos = getPos(userCached);
				int itemid = getItemid(userCached);
				PlayerKnapsackVO newPlayerKnapsackVO = baseItemVOToPlayerKnapsackVO(baseItemVO, userCached.getPlayerId(), itemid, pos, baseItemVO.getMaxnum());
				userCached.getUserKnapsack().getKnapsackList().add(newPlayerKnapsackVO);
				newList.add(newPlayerKnapsackVO);
			}
		} else {
			while (number > 0) {
				PlayerKnapsackVO playerKnapsackVO = findplayerKnapsackByCfg(userCached.getUserKnapsack().getKnapsackList(), baseItemVO.getCfgId(), baseItemVO.getMaxnum());// find
				if (playerKnapsackVO == null) {
					int pos = getPos(userCached);
					int itemid = getItemid(userCached);
					int ss = Math.min(baseItemVO.getMaxnum(), number);
					PlayerKnapsackVO newPlayerKnapsackVO = baseItemVOToPlayerKnapsackVO(baseItemVO, userCached.getPlayerId(), itemid, pos, ss);
					newList.add(newPlayerKnapsackVO);
					userCached.getUserKnapsack().getKnapsackList().add(newPlayerKnapsackVO);
					number = number - ss;
				} else { // 添加到已有格子中
					int ss = baseItemVO.getMaxnum() - playerKnapsackVO.getNumber();
					if (ss >= number) { // 有足够的空位置
						playerKnapsackVO.setNumber(playerKnapsackVO.getNumber() + number);
						updateList.add(playerKnapsackVO);
						number = 0;
					} else {
						playerKnapsackVO.setNumber(baseItemVO.getMaxnum());
						updateList.add(playerKnapsackVO);
						number = number - ss;
					}
				}
			} // end while
		} // end else

		saveKnapsack(newList, updateList, null, commandList);

		if (ItemConstants.ITEM_TYPE_EQPI == baseItemVO.getType()) {
			// 拥有N件M星以上装备
			MyClassLoaderUtil.getInstance().getTaskCheck().changTaskByReQType(userCached, TaskConstant.TASK_NEQ_NSTAR, commandList);
		}
	}

	/**
	 * 根据配置id 移除物品
	 * 
	 * @param userCached
	 * @param cfgId
	 * @param number
	 * @param commandList
	 * @param updateList
	 * @param delList
	 */
	public void removeItem(UserCached userCached, int cfgId, int number, List<NettyMessageVO> commandList) {
		List<PlayerKnapsackVO> updateList = new ArrayList<PlayerKnapsackVO>();
		List<PlayerKnapsackVO> delList = new ArrayList<PlayerKnapsackVO>();
		deleteItem(userCached, cfgId, number, updateList, delList);
		saveKnapsack(null, updateList, delList, commandList);
	}

	private void deleteItem(UserCached userCached, int cfgId, int number, List<PlayerKnapsackVO> updateList, List<PlayerKnapsackVO> delList) {

		PlayerKnapsackVO playerKnapsackVO = null;
		while (number > 0) {
			playerKnapsackVO = findplayerKnapsackByCfg(userCached.getUserKnapsack().getKnapsackList(), cfgId, -1);
			if (playerKnapsackVO.getNumber() > number) {
				playerKnapsackVO.setNumber(playerKnapsackVO.getNumber() - number);
				updateList.add(playerKnapsackVO);
				number = 0;
			} else {
				number = number - playerKnapsackVO.getNumber();
				userCached.getUserKnapsack().getKnapsackList().remove(playerKnapsackVO);
				delList.add(playerKnapsackVO);
			}
		}
	}

	/**
	 * 删除多个物品
	 * 
	 * @param userCached
	 * @param cfgId
	 * @param number
	 * @param commandList
	 */
	public void removeItem(UserCached userCached, int[] cfgId, int[] number, List<NettyMessageVO> commandList) {
		List<PlayerKnapsackVO> updateList = new ArrayList<PlayerKnapsackVO>();
		List<PlayerKnapsackVO> delList = new ArrayList<PlayerKnapsackVO>();
		for (int i = 0; i < cfgId.length; i++) {
			if (cfgId[i] > 0) {
				deleteItem(userCached, cfgId[i], number[i], updateList, delList);
			}
		}
		saveKnapsack(null, updateList, delList, commandList);
	}

	public void removeItem(UserCached userCached, int[] cfgId, int[] number, int count, List<NettyMessageVO> commandList) {
		List<PlayerKnapsackVO> updateList = new ArrayList<PlayerKnapsackVO>();
		List<PlayerKnapsackVO> delList = new ArrayList<PlayerKnapsackVO>();
		for (int i = 0; i < cfgId.length; i++) {
			if (cfgId[i] > 0) {
				deleteItem(userCached, cfgId[i], number[i] * count, updateList, delList);
			}
		}
		saveKnapsack(null, updateList, delList, commandList);
	}

	/**
	 * 移除
	 * 
	 * @date 2014年3月14日
	 */
	public void removeItem(UserCached userCached, PlayerKnapsackVO playerKnapsackVO, int number, List<NettyMessageVO> commandList) {
		List<PlayerKnapsackVO> updateList = new ArrayList<PlayerKnapsackVO>();
		List<PlayerKnapsackVO> delList = new ArrayList<PlayerKnapsackVO>();

		if (playerKnapsackVO.getNumber() > number) {
			playerKnapsackVO.setNumber(playerKnapsackVO.getNumber() - number);
			updateList.add(playerKnapsackVO);
		} else {
			userCached.getUserKnapsack().getKnapsackList().remove(playerKnapsackVO);
			delList.add(playerKnapsackVO);
		}
		saveKnapsack(null, updateList, delList, commandList);
	}

	/**
	 * 是否有足够的物品
	 * 
	 * @param dataList
	 * @param cfgId
	 * @param num
	 * @return
	 */
	public boolean hasEnoughItem(List<PlayerKnapsackVO> dataList, int cfgId, int num) {

		if (num == 0) {
			return true;
		}

		for (PlayerKnapsackVO playerKnapsackVO : dataList) {
			if (playerKnapsackVO.getCfgId() == cfgId && STATIS_KNAPSACK == playerKnapsackVO.getStatus()) {
				num = num - playerKnapsackVO.getNumber();
				if (num <= 0) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean hasEnoughItem(List<PlayerKnapsackVO> dataList, int[] cfgId, int[] num) {
		return hasEnoughItem(dataList, cfgId, num, 1);
	}

	public boolean hasEnoughItem(List<PlayerKnapsackVO> dataList, int[] cfgId, int[] num, int count) {
		for (int i = 0; i < cfgId.length; i++) {
			if (cfgId[i] > 0) {
				if (!hasEnoughItem(dataList, cfgId[i], num[i] * count)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 某类物品的个数
	 * 
	 * @param dataList
	 * @param cfgId
	 * @return
	 */
	public int findKnaspackNumByCfgId(List<PlayerKnapsackVO> dataList, int cfgId) {
		int num = 0;
		for (PlayerKnapsackVO playerKnapsackVO : dataList) {
			if (playerKnapsackVO.getCfgId() == cfgId && STATIS_KNAPSACK == playerKnapsackVO.getStatus()) {
				num += playerKnapsackVO.getNumber();
			}
		}
		return num;
	}

	/**
	 * 查看有没有足够的空间放奖励
	 * 
	 * @param rewardList
	 * @param userCached
	 * @return 如果格子不够返回false,够返回true
	 */
	public boolean checkKnapsakFull(List<Reward> rewardList, UserCached userCached) {
		int n = 0;
		for (Reward reward : rewardList) {
			if (reward.getType() == RewardService.REWARD_TYPE_GOODS) {
				BaseItemVO baseItemVO = ItemRes.getInstance().getBaseItemVO(reward.getContent());
				int x = reward.getNumber() / baseItemVO.getMaxnum();
				if (reward.getNumber() % baseItemVO.getMaxnum() > 0) {
					x++;
				}

				n += x;
			}
		}

		return hasKnapsackGird(userCached, n);
	}

	/**
	 * 根据配置id,找物品
	 * 
	 * @param dataList
	 * @param cfgId
	 * @param maxnum
	 * @return
	 */
	public PlayerKnapsackVO findplayerKnapsackByCfg(List<PlayerKnapsackVO> dataList, int cfgId, int maxnum) {
		for (PlayerKnapsackVO playerKnapsackVO : dataList) {
			// 找未叠满的物品格 如果是扣除物品则maxnum填-1
			if (playerKnapsackVO.getCfgId() == cfgId && STATIS_KNAPSACK == playerKnapsackVO.getStatus() && (maxnum < 0 || playerKnapsackVO.getNumber() < maxnum)) {
				return playerKnapsackVO;
			}
		}
		return null;
	}

	/**
	 * 根据唯一标识找物品
	 * 
	 * @param dataList
	 * @param cfgId
	 * @param maxnum
	 * @return
	 */
	public PlayerKnapsackVO findplayerKnapsackById(List<PlayerKnapsackVO> dataList, int itemId) {
		for (PlayerKnapsackVO playerKnapsackVO : dataList) {
			if (playerKnapsackVO.getItemid() == itemId) {
				return playerKnapsackVO;
			}
		}
		return null;
	}

	/**
	 * 取到背包的一个空位置
	 * 
	 * @param userCached
	 * @return
	 * @throws Exception
	 */
	public int getPos(UserCached userCached) throws Exception {
		int size = userCached.getPlayerAccountVO().getKnapsack();
		byte[] temp = new byte[size];
		for (PlayerKnapsackVO playerKnapsackVO : userCached.getUserKnapsack().getKnapsackList()) {
			if (playerKnapsackVO.getStatus() == STATIS_KNAPSACK) { // 背包
				temp[playerKnapsackVO.getPosition()] = 1;
			}
		}

		for (int i = 0; i < size; i++) {
			if (temp[i] == 0) {
				return i;
			}
		}

		LOGGER.error("" + userCached.getPlayerAccountVO().getKnapsack() + "," + userCached.getUserKnapsack().getKnapsackList().size());
		throw new Exception("error pos - 1");
	}

	/**
	 * 取最大物品id + 1
	 * 
	 * @param userCached
	 * @return
	 */
	public int getItemid(UserCached userCached) {
		int maxItemid = userCached.getUserKnapsack().getMaxKnapsackId();
		maxItemid++;
		userCached.getUserKnapsack().setMaxKnapsackId(maxItemid);
		return maxItemid;
	}

	/**
	 * 将基础数据转成用户数据
	 * 
	 * @param baseItemVO
	 * @param playerid
	 * @param itemid
	 * @param pos
	 * @return
	 */
	public static PlayerKnapsackVO baseItemVOToPlayerKnapsackVO(BaseItemVO baseItemVO, int playerid, int itemid, int pos, int number) {
		PlayerKnapsackVO playerKnapsackVO = new PlayerKnapsackVO();
		playerKnapsackVO.setBaseItemVO(baseItemVO);
		playerKnapsackVO.setItemid(itemid);
		playerKnapsackVO.setPlayerId(playerid);
		playerKnapsackVO.setHeroId(0);
		playerKnapsackVO.setCfgId(baseItemVO.getCfgId());

		playerKnapsackVO.setStatus(STATIS_KNAPSACK);
		playerKnapsackVO.setPosition(pos);
		playerKnapsackVO.setNumber(number);
		playerKnapsackVO.setEnhance(0);
		playerKnapsackVO.setGem1(0);
		playerKnapsackVO.setGem2(-1);
		playerKnapsackVO.setGem3(-1);
		playerKnapsackVO.setGem4(-1);
		ItemRes.getInstance().loadGem(playerKnapsackVO);
		return playerKnapsackVO;
	}

	public NettyMessageVO packageNewItemInfo(PlayerKnapsackVO playerKnapsackVO, int mode) {
		KpsItemInfoUpdateResponse.Builder kpsItemInfoUpdateResponse = KpsItemInfoUpdateResponse.newBuilder();
		kpsItemInfoUpdateResponse.setStatus(mode);
		kpsItemInfoUpdateResponse.setHeroId(playerKnapsackVO.getHeroId());
		//
		kpsItemInfoUpdateResponse.addItems(VOUtil.getItemInfo(playerKnapsackVO));

		NettyMessageVO nettyMessageVO = new NettyMessageVO();
		nettyMessageVO.setCommandCode(CSCommandConstant.KNAPSACK_UPDATE);
		nettyMessageVO.setData(kpsItemInfoUpdateResponse.build().toByteArray());

		return nettyMessageVO;
	}
}
