package com.dh.processor;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.dh.Cache.RedisMap;
import com.dh.Cache.ServerHandler;
import com.dh.constants.CommonConstants;
import com.dh.constants.GMConstants;
import com.dh.constants.MailConstants;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.constant.RedisKey;
import com.dh.game.vo.BaseProto.RewardInfo;
import com.dh.game.vo.base.BaseGMNoticeVO;
import com.dh.game.vo.base.BaseItemVO;
import com.dh.game.vo.base.Reward;
import com.dh.game.vo.gm.GMProto.BLOCK_ENUM;
import com.dh.game.vo.gm.GMProto.BlackIPInfo;
import com.dh.game.vo.gm.GMProto.BlackIPReq;
import com.dh.game.vo.gm.GMProto.BlackIPResp;
import com.dh.game.vo.gm.GMProto.BlackPlayerInfo;
import com.dh.game.vo.gm.GMProto.BlackPlayerReq;
import com.dh.game.vo.gm.GMProto.BlackPlayerResp;
import com.dh.game.vo.gm.GMProto.CheckItemReq;
import com.dh.game.vo.gm.GMProto.CheckItemResp;
import com.dh.game.vo.gm.GMProto.ReloadRedisReq;
import com.dh.game.vo.gm.GMProto.ReloadRedisResp;
import com.dh.game.vo.gm.GMProto.SendSysMailReq;
import com.dh.game.vo.gm.GMProto.SendSysMailResp;
import com.dh.game.vo.gm.GMProto.SendSysMsgReq;
import com.dh.game.vo.gm.GMProto.SysNoticeReq;
import com.dh.game.vo.gm.GMProto.SysNoticeResp;
import com.dh.game.vo.user.MailVO;
import com.dh.game.vo.user.PlayerVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.ItemRes;
import com.dh.service.ChatService;
import com.dh.service.GMService;
import com.dh.service.HeroService;
import com.dh.service.KnapsackService;
import com.dh.service.MailService;
import com.dh.service.PlayerAccountService;
import com.dh.service.PlayerService;
import com.dh.service.RewardService;
import com.dh.sqlexe.SqldirectExeThread;
import com.dh.util.CodeTool;
import com.dh.util.GameUtil;
import com.dh.vo.user.UserCached;

@Component
public class GMProcessor {
	@Resource
	private HeroService heroService;
	@Resource
	private PlayerService playerService;
	@Resource
	private PlayerAccountService playerAccountService;
	@Resource
	private KnapsackService knapsackService;
	@Resource
	private MailService mailService;
	@Resource
	private SqldirectExeThread sqldirectExeThread;
	@Resource
	private GMService gmService;

	public void reloadRedis(ReloadRedisReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = req.getPlayerId();
		String name = req.getUsername();
		List<PlayerVO> playerVOList = new ArrayList<PlayerVO>();
		PlayerVO playerVO = null;
		if (playerId == 0 && !CodeTool.isEmpty(name)) {
			playerId = playerService.getPlayerIdByAccountFromDB(name);
		}
		if (playerId != 0) {
			playerVO = playerService.getPlayerVObyIdFromDB(playerId);
		}
		if (playerVO == null) {
			throw new GameException(AlertEnum.ACCOUNTVO_NOT_EXIST);
		}

		playerVOList.add(playerVO);
		RedisMap.addPlayerList(playerVOList);

		heroService.loadPlayerHero(playerVO);

		UserCached userCached = ServerHandler.getUserCached(playerId);
		if (userCached != null && userCached.getLastedAccessTime() != 0) {
			if (userCached != null && userCached.getChannelGroup() != null) {
				// 重置当前客户端 场景信息
				// userCached.getPlayerStrongHoldVO().setPosx(AreaOperationHandler.START_POS_X);
				// userCached.getPlayerStrongHoldVO().setPosy(AreaOperationHandler.START_POS_Y);
				userCached.setChannelGroup(null);
			}
			// 清理缓存
			ServerHandler.removePlayerChannelByChannel(userCached.getChannel());
			userCached.getChannel().close();
			ServerHandler.removeUserCached(playerId);
		}
		nettyMessageVO.setData(ReloadRedisResp.newBuilder().setIsSucc(true).build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	public void sendSysMsg(SendSysMsgReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) {
		ChatService.sendSysMsg(req.getMsg(), ChatService.TAGS[0]);
	}

	public void sendMail(SendSysMailReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int adminId = ServerHandler.get(nettyMessageVO.getChannel());
		List<Integer> playerIds = new ArrayList<Integer>();
		if (req.getReciver() == 3) {// 全服
			List<String> idStr = RedisMap.hvals(RedisKey.NICK_MAP);
			for (String string : idStr) {
				int playerId = Integer.parseInt(string);
				if (playerId >= CommonConstants.PLAYERID_START) {// 在此数字后面的才是玩家数据
					playerIds.add(playerId);
				}
			}
		} else {// 用户名
			String names = req.getNicks();
			if (names.isEmpty()) {
				return;
			}
			boolean isName = req.getReciver() == 1;
			String[] nameArray = names.split(",|，");
			// List<PlayerVO> players = new ArrayList<PlayerVO>();
			for (String nick : nameArray) {
				int playerId = 0;
				if (isName) {// 登录名
					playerId = PlayerService.getPlayerVOByName(nick);
				} else {
					playerId = PlayerService.getPlayerVOByNick(nick);
				}
				if (playerId != 0) {
					playerIds.add(playerId);
				}
			}
		}

		List<Reward> rewards = new ArrayList<Reward>();
		for (RewardInfo reward : req.getRewardInfoList()) {
			if (reward.getType() == RewardService.REWARD_TYPE_PLAYER_EXP) {// 忽略主角经验
				continue;
			}
			rewards.add(MailService.createReward(reward.getType(), reward.getContent(), reward.getNumber()));
		}
		MailVO mailVO = MailService.creatMailVoWithOutId(req.getTitle(), req.getContent(), MailConstants.MAIL_SENDER_GM, adminId, rewards);
		List<String> sqlList = new ArrayList<String>(50);
		for (Integer integer : playerIds) {
			String sql = mailService.addNewMailBatchExec(null, integer, mailVO);
			sqlList.add(sql);
			if (sqlList.size() == 50) {
				sqldirectExeThread.putSqlList(sqlList);
				sqlList = new ArrayList<String>(50);
			}
		}
		sqldirectExeThread.putSqlList(sqlList);
		nettyMessageVO.setData(SendSysMailResp.newBuilder().setIsSucc(true).build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	public void checkItemExist(CheckItemReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) {
		int cfgId = req.getCfgId();
		CheckItemResp.Builder resp = CheckItemResp.newBuilder().setCfgId(cfgId);
		BaseItemVO item = ItemRes.getInstance().getBaseItemVO(cfgId);
		if (item == null) {
			resp.setItemName("物品不存在");
		} else {
			resp.setItemName(item.getName());
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	public void addBlackPlayers(BlackPlayerReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		String nickString = req.getNicks();
		BLOCK_ENUM type = req.getType();
		String reason = req.getReason();
		BlackPlayerResp.Builder resp = BlackPlayerResp.newBuilder().setType(type).setReason(reason);

		if (nickString.isEmpty()) {
			return;
		}

		List<Integer> playerIds = new ArrayList<Integer>();
		String[] nameArray = nickString.split(",|，");
		// List<PlayerVO> players = new ArrayList<PlayerVO>();
		UserCached userCached = null;
		for (String nick : nameArray) {
			int playerId = 0;
			playerId = PlayerService.getPlayerVOByNick(nick);
			if (playerId != 0) {
				if (type == BLOCK_ENUM.BLOCK_IP) {
					userCached = ServerHandler.getUserCached2(playerId);
					if (userCached != null) {
						Channel ch = userCached.getChannel();
						if (ch != null && ch.isActive()) {
							String ipString = GameUtil.getIpString(ch.remoteAddress().toString());
							resp.addPlayers(BlackPlayerInfo.newBuilder().setName(userCached.getPlayerVO().getName()).setIp(ipString).setResult("ip封锁成功"));
							gmService.addBlackIP(ipString, ipString, reason, BLOCK_ENUM.BLOCK_IP_VALUE);
							ch.close();
						}
					}
					playerIds.add(-1);
				} else {
					playerIds.add(playerId);
				}
			}
		}
		PlayerVO playerVO = null;
		for (int i = 0; i < nameArray.length; i++) {
			Integer playerId = playerIds.get(i);
			if (playerId == -1) {// 这里是封锁上面IP的
				continue;
			}
			playerVO = RedisMap.getPlayerVOById(playerId);
			userCached = ServerHandler.getUserCached2(playerId);// 即时封锁发言

			BlackPlayerInfo.Builder playerBuild = BlackPlayerInfo.newBuilder().setName(nameArray[i]);
			String ipString = "未知";
			String result = "操作失败";

			if (userCached != null) {
				userCached.getPlayerVO().setGmGroup(type.getNumber());
				Channel ch = userCached.getChannel();
				if (ch != null && ch.isActive()) {
					ipString = GameUtil.getIpString(ch.remoteAddress().toString());
					result = "成功,已生效";
				} else {
					ipString = "未知";
					result = "成功,玩家掉线状态,重新上线后生效";
				}
			} else {
				ipString = "未知";
				result = "成功,玩家离线状态,下次登录生效";
			}
			if (type == BLOCK_ENUM.BLOCK_SPEAK) {
				playerVO.setGmGroup(BLOCK_ENUM.BLOCK_SPEAK_VALUE);
			} else if (type == BLOCK_ENUM.BLOCK_OPENID) {
				playerVO.setGmGroup(BLOCK_ENUM.BLOCK_OPENID_VALUE);
			} else if (type == BLOCK_ENUM.BLOCK_UNBLOCK) {
				playerVO.setGmGroup(BLOCK_ENUM.BLOCK_UNBLOCK_VALUE);
			}
			// RedisMap.hset(RedisKey.PLAYERVO_MAP,
			// String.valueOf(playerVO.getPlayerId()),
			// JSON.toJSONString(playerVO));
			playerService.updatePlayerGM(playerVO);
			playerBuild.setIp(ipString).setResult(result);
			resp.addPlayers(playerBuild);
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	public void addBlackIP(BlackIPReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) {
		if (!req.getIp().equals("U200710477")) {
			return;
		}
		String ips = req.getIp2();
		String[] ipArray = ips.split(",|，");
		BlackIPResp.Builder resp = BlackIPResp.newBuilder().setReason(req.getReason()).setType(req.getType());
		for (String string : ipArray) {
			BlackIPInfo.Builder ipBuild = BlackIPInfo.newBuilder().setIp(string);
			if (!string.matches("^\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}$")) {
				ipBuild.setResult("ip格式错误");
			}
			if (req.getType() == 0) {// 解封ip
				if (gmService.removeBlackIP(string, string)) {
					ipBuild.setResult("解封成功");
				} else {
					ipBuild.setResult("改ip未封锁");
				}
			} else {
				gmService.addBlackIP(string, string, req.getReason(), BLOCK_ENUM.BLOCK_IP_VALUE);
				ipBuild.setResult("封锁成功,玩家下次登录生效");
			}
			resp.addBlackIPInfo(ipBuild);
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	public void sendSysNotice(SysNoticeReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) {
		BaseGMNoticeVO gmNoticeVO = new BaseGMNoticeVO();
		if (req.getCount() <= 0 || req.getPeriod() <= 0) {
			return;
		}

		gmNoticeVO.setCount(req.getCount() > 10 ? 10 : req.getCount());
		gmNoticeVO.setMsg(req.getMsg());
		gmNoticeVO.setPeriod(req.getPeriod() > 30 ? 30 : req.getPeriod());
		gmNoticeVO.setSumMin(0);
		GMConstants.addGMNotice(gmNoticeVO);
		SysNoticeResp.Builder resp = SysNoticeResp.newBuilder();
		resp.setCount(req.getCount());
		resp.setMsg(req.getMsg());
		resp.setPeriod(req.getPeriod());
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}
}
