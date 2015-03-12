package com.dh.processor;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.Cache.RedisMap;
import com.dh.Cache.ServerHandler;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.grab.GrabProto.GrabListRequest;
import com.dh.game.vo.grab.GrabProto.GrabListResponse;
import com.dh.game.vo.grab.GrabProto.GrabRoleInfo;
import com.dh.game.vo.grab.GrabProto.RATETYPE;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.user.PlayerArenaVO;
import com.dh.game.vo.user.PlayerHeroDefVO;
import com.dh.game.vo.user.PlayerTimerVO;
import com.dh.game.vo.user.PlayerVO;
import com.dh.handler.arena.ArenaRedisTool;
import com.dh.netty.NettyMessageVO;
import com.dh.service.ArenaService;
import com.dh.service.GrabService;
import com.dh.service.PlayerAccountService;
import com.dh.service.PlayerTimerService;
import com.dh.util.CodeTool;
import com.dh.util.CommandUtil;
import com.dh.util.DateUtil;
import com.dh.util.GameUtil;
import com.dh.util.Tool;
import com.dh.util.VOUtil;
import com.dh.vo.user.UserCached;

@Component
public class GrabProcesso {
	@Resource
	private GrabService grabService;
	@Resource
	private PlayerTimerService playerTimerService;
	@Resource
	private ArenaService arenaService;
	@Resource
	private PlayerAccountService playerAccountService;

	/**
	 * 获取敌对列表
	 * 
	 * @throws Exception
	 */
	public void getEnemList(GrabListRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		// if (null ==
		// ComposeRes.getInstance().getBaseComposeVO(request.getItemCfgid())) {
		// throw new GameException(AlertEnum.ITEM_NOT_FOUND);
		// }

		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();

		if (CodeTool.isEmpty(playerTimerVO.getGrabline()) || GameUtil.getRemainTime(System.currentTimeMillis(), playerTimerVO.getGrabTime()) == 0) {
			PlayerArenaVO playerArenaVO = arenaService.getPlayerArenaVO(userCached);

			if (userCached.getUserTimer().getPlayerTimerVO().getFirstGrab() == 0) {
				playerTimerVO.setGrabline(getEnem(playerArenaVO, true));
			} else {
				playerTimerVO.setGrabline(getEnem(playerArenaVO, false));
			}

			playerTimerVO.setGrabTime(DateUtil.afterSec(new Date(), GrabService.REFRESHTIME));
			playerTimerService.updateGrabTime(playerTimerVO);
		}

		GrabListResponse.Builder response = GrabListResponse.newBuilder();
		response.setItemCfgid(request.getItemCfgid());
		userCached.getUserReward().setSpReward(request.getItemCfgid());

		String str = playerTimerVO.getGrabline();
		if (CodeTool.isNotEmpty(str)) {
			int[] arrPlayer = Tool.strToIntArr(str);
			GrabRoleInfo.Builder grabRoleInfo = null;
			for (int otherPlayerId : arrPlayer) {
				grabRoleInfo = GrabRoleInfo.newBuilder();
				PlayerArenaVO otherPlayerArenaVO = ArenaService.getPlayerArenaVOFromRedis(String.valueOf(otherPlayerId));

				grabRoleInfo.setPlayerId(otherPlayerId);
				PlayerVO otherPlayerVO = RedisMap.getPlayerVOById(otherPlayerId);
				grabRoleInfo.setName(otherPlayerVO.getName());
				grabRoleInfo.setCombat(otherPlayerArenaVO.getCombat());
				grabRoleInfo.setRate(computerRateType(userCached.getPlayerVO().getLevel(), otherPlayerVO.getLevel()));

				List<PlayerHeroDefVO> list = ArenaRedisTool.getPlayerHeroDefList(otherPlayerId);

				if (list != null) {
					for (PlayerHeroDefVO playerHeroDefVO : list) {
						// if (playerHeroDefVO.getLineStatus() == CommonConstants.ATK_LINE_START) {
						grabRoleInfo.addFinalHero(VOUtil.getFinalHero(playerHeroDefVO));
						// }
					}
				}// end if

				response.addGrabRoleInfo(grabRoleInfo);
			}// end for

		}

		response.setCostpvp(GrabService.GRAB_COST_PVP); // 夺宝每次消耗costpvp点竞技值
		response.setChangeEnempvp(GrabService.GRAB_CHANGE_PVP);
		response.setRemaintime(GameUtil.getRemainTime(System.currentTimeMillis(), playerTimerVO.getGrabTime()));// 剩余刷新时间(秒)

		CommandUtil.packageNettyMessage(CSCommandConstant.DUOBAO_LIST, response.build().toByteArray(), commandList);
	}

	/**
	 * 刷新对手
	 * 
	 * @param request
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void getEnemList2(GrabListRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		// if (null ==
		// ComposeRes.getInstance().getBaseComposeVO(request.getItemCfgid())) {
		// throw new GameException(AlertEnum.ITEM_NOT_FOUND);
		// }

		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		playerAccountService.hasEnoughPvP(userCached, GrabService.GRAB_CHANGE_PVP);

		PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();

		PlayerArenaVO playerArenaVO = arenaService.getPlayerArenaVO(userCached);
		playerTimerVO.setGrabline(getEnem(playerArenaVO, false));
		playerTimerVO.setGrabTime(DateUtil.afterSec(new Date(), GrabService.REFRESHTIME));
		playerTimerService.updateGrabTime(playerTimerVO);

		GrabListResponse.Builder response = GrabListResponse.newBuilder();
		response.setItemCfgid(request.getItemCfgid());

		String str = playerTimerVO.getGrabline();
		if (CodeTool.isNotEmpty(str)) {
			int[] arrPlayer = Tool.strToIntArr(str);
			GrabRoleInfo.Builder grabRoleInfo = null;
			for (int otherPlayerId : arrPlayer) {
				grabRoleInfo = GrabRoleInfo.newBuilder();
				PlayerArenaVO otherPlayerArenaVO = ArenaService.getPlayerArenaVOFromRedis(String.valueOf(otherPlayerId));

				grabRoleInfo.setPlayerId(otherPlayerId);
				PlayerVO otherPlayerVO = RedisMap.getPlayerVOById(otherPlayerId);
				grabRoleInfo.setName(otherPlayerVO.getName());
				grabRoleInfo.setCombat(otherPlayerArenaVO.getCombat());
				grabRoleInfo.setRate(computerRateType(userCached.getPlayerVO().getLevel(), otherPlayerVO.getLevel()));

				List<PlayerHeroDefVO> list = ArenaRedisTool.getPlayerHeroDefList(otherPlayerId);

				if (list != null && list.size() > 0) {
					for (PlayerHeroDefVO playerHeroDefVO : list) {
						grabRoleInfo.addFinalHero(VOUtil.getFinalHero(playerHeroDefVO));
					}
				}// end if

				response.addGrabRoleInfo(grabRoleInfo);
			}// end for

		}

		response.setCostpvp(GrabService.GRAB_COST_PVP); // 夺宝每次消耗costpvp点竞技值
		response.setChangeEnempvp(GrabService.GRAB_CHANGE_PVP);
		response.setRemaintime(GameUtil.getRemainTime(System.currentTimeMillis(), playerTimerVO.getGrabTime()));// 剩余刷新时间(秒)

		playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_PVP, GrabService.GRAB_CHANGE_PVP, userCached.getPlayerAccountVO(), commandList, "换一批夺宝对手");

		CommandUtil.packageNettyMessage(CSCommandConstant.DUOBAO_LIST, response.build().toByteArray(), commandList);
	}

	/**
	 * 刷一批对手
	 * 
	 * @param playerArenaVO
	 * @return
	 */
	public String getEnem(PlayerArenaVO playerArenaVO, boolean isfirst) {
		int count = ArenaService.getPlayerArenaCountFromRedis();
		int start = 0;
		if (isfirst) {
			start = count - 10;
		}
		int end = count - 1;
		return ArenaService.getEnem(start, end, playerArenaVO.getOrdernum());
	}

	/**
	 * 记算掉宝率
	 * 
	 * @param mylevel
	 * @param otherLevel
	 * @return
	 */
	public static RATETYPE computerRateType(int mylevel, int otherLevel) {
		int rate = computerRate(mylevel, otherLevel);

		if (rate >= 5 && rate < 10) {
			return RATETYPE.LOW;
		} else if (rate >= 11 && rate < 20) {
			return RATETYPE.MEDIUM;
		} else if (rate >= 21 && rate < 30) {
			return RATETYPE.HIGH;
		} else if (rate >= 31 && rate < 40) {
			return RATETYPE.HIGHER;
		} else if (rate >= 41 && rate < 50) {
			return RATETYPE.HIGHEST;
		}
		return RATETYPE.LOW;
	}

	public static int computerRate(int mylevel, int otherLevel) {
		int rate = 30 + (otherLevel - mylevel) * 1;// （取值范围5%~50%）
		rate = Math.max(5, rate);
		rate = Math.min(50, rate);

		return rate;
	}
}
