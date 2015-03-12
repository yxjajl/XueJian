package com.dh.processor;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.Cache.RedisMap;
import com.dh.Cache.ServerHandler;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_UNIT_TYPE;
import com.dh.game.vo.raid.YuanZhenProto.YuanZhenDetailReponse;
import com.dh.game.vo.raid.YuanZhenProto.YuanZhenDetailRequest;
import com.dh.game.vo.raid.YuanZhenProto.YuanZhenInfoResponse;
import com.dh.game.vo.raid.YuanZhenProto.YzRaid;
import com.dh.game.vo.user.PlayerHeroDefVO;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.game.vo.user.PlayerTimerVO;
import com.dh.game.vo.user.PlayerVO;
import com.dh.game.vo.user.PlayerYuanZhenVO;
import com.dh.handler.arena.ArenaRedisTool;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.VipLevelRes;
import com.dh.service.HeroService;
import com.dh.service.PlayerTimerService;
import com.dh.service.YuanZhenService;
import com.dh.util.CommandUtil;
import com.dh.util.DateUtil;
import com.dh.util.VOUtil;
import com.dh.vo.user.UserCached;

@Component
public class YuanZhenProcessor {
	@Resource
	private YuanZhenService yuanZhenService;
	@Resource
	private PlayerTimerService playerTimerService;
	@Resource
	private HeroService heroService;

	/**
	 * 进入远征UI
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void getYuanZhenData(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		if (userCached.getPlayerVO().getLevel() < 15) {
			throw new Exception("yuanzhen 15 level limit");
		}
		yuzhenui(userCached, commandList);
	}

	public static boolean haveYuanZhen(UserCached userCached) {
		if (userCached.getUserYuanZhen().getYzList().size() == 0) {
			return false;
		}

		PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();
		if (!DateUtil.isSameDay(playerTimerVO.getYzDate())) {
			return true;
		}

		if (playerTimerVO.getYzTimes() > 0) {
			return true;
		}

		int gress = userCached.getUserYuanZhen().getLastIndex();
		if (gress < YuanZhenService.MAXELILYUANZHEN) {
			return true;
		}

		return false;
	}

	public void yuzhenui(UserCached userCached, List<NettyMessageVO> commandList) throws Exception {
		yuanZhenService.loadYuanZhen(userCached);

		PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();
		if (!DateUtil.isSameDay(playerTimerVO.getYzDate())) {
			playerTimerVO.setYzTimes(VipLevelRes.getInstance().getYuanZhenReset(userCached.getPlayerVO().getVip()));
			playerTimerVO.setYzDate(new Date());
			playerTimerService.updateYZInfo(playerTimerVO);
		}

		YuanZhenInfoResponse.Builder yuanZhenInfoResponse = YuanZhenInfoResponse.newBuilder();
		yuanZhenInfoResponse.setTimes(playerTimerVO.getYzTimes());

		int gress = userCached.getUserYuanZhen().getLastIndex();

		PlayerYuanZhenVO playerYuanZhenVO = userCached.getUserYuanZhen().findPlayerYuanZhenVO(gress);

		if (playerYuanZhenVO.getStatus() == 0) {
			gress--;
		}

		if (gress > YuanZhenService.MAXNORMALYUANZHEN) {
			yuanZhenInfoResponse.setGress(gress - YuanZhenService.MAXNORMALYUANZHEN);
			yuanZhenInfoResponse.setNgress(YuanZhenService.MAXNORMALYUANZHEN);
		} else {
			yuanZhenInfoResponse.setGress(0);
			yuanZhenInfoResponse.setNgress(gress);
		}

		CommandUtil.packageNettyMessage(CSCommandConstant.YUZHENUI, yuanZhenInfoResponse.build().toByteArray(), commandList);
	}

	/**
	 * 取每个层的数据信息
	 * 
	 * @param request
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void yuanZhenDetail(YuanZhenDetailRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);

		int layer = request.getLayer();
		if (layer < 0 || layer > 1) {
			layer = 0;
		}
		yuanZhenDetail(userCached, layer, commandList);

	}

	public void yuanZhenDetail(UserCached userCached, int layer, List<NettyMessageVO> commandList) throws Exception {

		YuanZhenDetailReponse.Builder builder = YuanZhenDetailReponse.newBuilder();
		builder.setLayer(layer);

		int start = layer * YuanZhenService.MAXNORMALYUANZHEN + 1;
		int end = start + YuanZhenService.MAXNORMALYUANZHEN;
		PlayerYuanZhenVO playerYuanZhenVO = null;
		YzRaid.Builder yzRaid = null;
		for (int i = start; i < end; i++) {
			playerYuanZhenVO = userCached.getUserYuanZhen().findPlayerYuanZhenVO(i);
			yzRaid = YzRaid.newBuilder();
			yzRaid.setOtherPlayerId(playerYuanZhenVO.getOtherId());
			yzRaid.setStatus(playerYuanZhenVO.getStatus());

			if (playerYuanZhenVO.getOtherId() > 0) {
				PlayerVO otherPlayerVO = RedisMap.getPlayerVOById(playerYuanZhenVO.getOtherId());
				yzRaid.setOtherPlayerNick(otherPlayerVO.getName());
				List<PlayerHeroDefVO> hlist = ArenaRedisTool.getPlayerHeroDefList(playerYuanZhenVO.getOtherId());
				if (hlist != null && hlist.size() > 0) {
					for (PlayerHeroDefVO playerHeroDefVO : hlist) {
						yzRaid.addFinalHero(VOUtil.getFinalHero(playerHeroDefVO));
					}
				}
			}
			builder.addYzRaid(yzRaid);
		}
		// System.err.println("layer = " + layer + "," + userCached.getUserYuanZhen().getLastIndex());
		CommandUtil.packageNettyMessage(CSCommandConstant.YUZHENDETAIL, builder.build().toByteArray(), commandList);

	}

	/**
	 * 刷新远征
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void refreshYuZhen(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();
		if (playerTimerVO.getYzTimes() <= 0) {
			throw new Exception("远证次数不够了");
		}

		playerTimerVO.setYzTimes(playerTimerVO.getYzTimes() - 1);
		if (playerTimerVO.getYzTimes() < 0) {
			playerTimerVO.setYzTimes(0);
		}
		playerTimerService.updateYZInfo(playerTimerVO);

		yuanZhenService.refreshYuZhen(userCached);

		for (PlayerHeroVO playerHeroVO : userCached.getUserHero().getHeroList()) {
			if (playerHeroVO.getFinal_hp() != playerHeroVO.getYzhp()) {
				playerHeroVO.setYzhp(playerHeroVO.getFinal_hp());
				playerHeroVO.setYzanger(0);
				heroService.updateHero(playerHeroVO);
				// System.err.println("Yuanrefresh = " + playerHeroVO.getId() +
				// "," + playerHeroVO.getYzhp());
				commandList.add(CommandUtil.packageSomeProperties(PLAYER_UNIT_TYPE.UNIT_HERO, playerHeroVO.getId(), new PLAYER_PROPERTY[] { PLAYER_PROPERTY.PROPERTY_YZ_HP,
						PLAYER_PROPERTY.PROPERTY_YZ_YZANGER }, new int[] { playerHeroVO.getYzhp(), playerHeroVO.getYzanger() }));
			}
		}

		yuzhenui(userCached, commandList);
	}
}
