package com.dh.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.Cache.RedisMap;
import com.dh.Cache.ServerHandler;
import com.dh.constants.CommonConstants;
import com.dh.constants.ItemConstants;
import com.dh.constants.LegionConstant;
import com.dh.constants.RaidConstant;
import com.dh.enums.GMIOEnum;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.constant.RedisKey;
import com.dh.game.vo.activity.LegionActivityProto.LegionBossCdAccrResp;
import com.dh.game.vo.activity.LegionActivityProto.LegionBossDetail;
import com.dh.game.vo.activity.LegionActivityProto.LegionBossRewardResp;
import com.dh.game.vo.base.BaseItemVO;
import com.dh.game.vo.base.BaseLegionBossVO;
import com.dh.game.vo.base.BaseLegionMemVO;
import com.dh.game.vo.base.BaseLegionShopVO;
import com.dh.game.vo.base.BaseLegionVO;
import com.dh.game.vo.base.Reward;
import com.dh.game.vo.legion.LegionProto.BaseLegionInfo;
import com.dh.game.vo.legion.LegionProto.DONATE_TYPE;
import com.dh.game.vo.legion.LegionProto.LEGION_LOG_TYPE;
import com.dh.game.vo.legion.LegionProto.LegionCreateReq;
import com.dh.game.vo.legion.LegionProto.LegionDayRewardResp;
import com.dh.game.vo.legion.LegionProto.LegionDestroyReq;
import com.dh.game.vo.legion.LegionProto.LegionDestroyResp;
import com.dh.game.vo.legion.LegionProto.LegionDonateReq;
import com.dh.game.vo.legion.LegionProto.LegionDonateResp;
import com.dh.game.vo.legion.LegionProto.LegionDonateRewardResp;
import com.dh.game.vo.legion.LegionProto.LegionEditReq;
import com.dh.game.vo.legion.LegionProto.LegionEditResp;
import com.dh.game.vo.legion.LegionProto.LegionInfoResp;
import com.dh.game.vo.legion.LegionProto.LegionJionListResp;
import com.dh.game.vo.legion.LegionProto.LegionJoinReq;
import com.dh.game.vo.legion.LegionProto.LegionJoinResp;
import com.dh.game.vo.legion.LegionProto.LegionListReq;
import com.dh.game.vo.legion.LegionProto.LegionListResp;
import com.dh.game.vo.legion.LegionProto.LegionLogsResp;
import com.dh.game.vo.legion.LegionProto.LegionMemListResp;
import com.dh.game.vo.legion.LegionProto.LegionMemManageReq;
import com.dh.game.vo.legion.LegionProto.LegionMemManageResp;
import com.dh.game.vo.legion.LegionProto.LegionShopBuyReq;
import com.dh.game.vo.legion.LegionProto.LegionShopBuyResp;
import com.dh.game.vo.legion.LegionProto.MEM_MANAGE_TYPE;
import com.dh.game.vo.legion.LegionProto.MEM_TYPE;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.user.LegionLogVO;
import com.dh.game.vo.user.LegionMemVO;
import com.dh.game.vo.user.LegionVO;
import com.dh.game.vo.user.PlayerTimerVO;
import com.dh.game.vo.user.PlayerVO;
import com.dh.main.InitLoad;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.ItemRes;
import com.dh.resconfig.LegionBossRes;
import com.dh.resconfig.LegionMemRes;
import com.dh.resconfig.LegionRes;
import com.dh.resconfig.LegionRewardRes;
import com.dh.resconfig.LegionShopRes;
import com.dh.service.KnapsackService;
import com.dh.service.LegionService;
import com.dh.service.MailService;
import com.dh.service.PlayerAccountService;
import com.dh.service.PlayerTimerService;
import com.dh.service.RewardService;
import com.dh.sync.SyncLock;
import com.dh.sync.SyncUtil;
import com.dh.util.BadWordsFilter;
import com.dh.util.CommandUtil;
import com.dh.util.CommonUtils;
import com.dh.util.DateUtil;
import com.dh.util.VOUtil;
import com.dh.vo.user.UserCached;
import com.dh.vo.user.UserLegion;

@Component
public class LegionProcessor {
	@Resource
	private KnapsackService knapsackService;
	@Resource
	private PlayerAccountService playerAccountService;
	@Resource
	private LegionService legionService;
	@Resource
	private RewardService rewardService;
	@Resource
	private PlayerTimerService playerTimerService;

	public void legionHome(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		if (userCached.getPlayerVO().getLevel() < LegionConstant.LEGION_OPEN_LEVEL) {
			throw new GameException(AlertEnum.ROLE_LEVEL_NO);
		}
		if (userCached.getPlayerVO().getLegionId() == 0) {
			commandList.add(CommandUtil.packBaseLegionInfo(MEM_TYPE.MEM_TYPE_NONE, null));
			throw new GameException(AlertEnum.LEGION_NOT_EXIST);
		}
		LegionVO legionVO = legionService.getMineLegion(userCached, commandList);
		LegionMemVO legionMemVO = LegionService.getLegionMemVO(legionVO, playerId, commandList);
		LegionInfoResp.Builder resp = VOUtil.packLegionInfo(userCached.getUserTimer().getPlayerTimerVO(), legionVO, legionMemVO);
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
		if (legionMemVO.getType() >= MEM_TYPE.MEM_TYPE_VP_VALUE) {
			commandList.add(CommandUtil.packLegionReqNotice(legionVO.getJoin_mems().size() > 0 ? 1 : 0));
		}
	}

	public void legionList(LegionListReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		if (userCached.getPlayerVO().getLevel() < LegionConstant.LEGION_OPEN_LEVEL) {
			throw new GameException(AlertEnum.ROLE_LEVEL_NO);
		}
		LegionMemVO memVO = null;
		UserLegion userLegion = userCached.getUserLegion();
		if (userCached.getPlayerVO().getLegionId() > 0) {// 已经拥有军团,移除申请的军团
			LegionVO legionVO = legionService.getMineLegion(userCached, commandList);
			memVO = LegionService.getLegionMemVO(legionVO, playerId, commandList);
		}
		int page = req.getPageCount();
		page = page < 0 ? 0 : page;
		List<LegionVO> legions = LegionConstant.getLegionList(page);
		LegionListResp.Builder resp = LegionListResp.newBuilder();
		resp.setTotalAmount(LegionConstant.getLegionSize());
		resp.setPageCount(page);
		for (LegionVO legionVO : legions) {
			resp.addLegionListInfo(VOUtil.packLegionListInfo(playerId, memVO, legionVO));
		}
		resp.setJoinCountDown(userLegion.getJoinCountDown());
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	public void legionCreate(LegionCreateReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		String name = req.getLegionName();
		if (BadWordsFilter.containBadWord(name)) {
			throw new GameException(AlertEnum.COMMON_BADWORD);
		}
		int len = CommonUtils.calWordsLenth(name);
		if (len > CommonConstants.NAME_LENGTH_MAX) {// 名字长度校验
			throw new GameException(AlertEnum.NICK_LONG);
		}
		if (len < CommonConstants.NAME_LENGTH_MIN) {// 名字长度校验
			throw new GameException(AlertEnum.NICK_SHORT);
		}

		if (LegionConstant.existLegionName(name)) {// 名字重复
			throw new GameException(AlertEnum.NICK_ALREADY_USE);
		}
		SyncLock lockObject = SyncUtil.getInstance().getLock(RedisKey.PLAYERVO_MAP + playerId);
		synchronized (lockObject) {// 避免其他玩家修改
			// LegionService.freshPlayerLegionStatus(userCached);
			if (userCached.getPlayerVO().getLegionId() != 0) {
				LegionVO mineLegion = legionService.getMineLegion(userCached, commandList);
				LegionMemVO mineMem = LegionService.getLegionMemVO(mineLegion, playerId, commandList);
				commandList.add(CommandUtil.packBaseLegionInfo(MEM_TYPE.valueOf(mineMem.getType()), mineLegion));
				throw new GameException(AlertEnum.LEGION_EXIST);
			}
			if (userCached.getPlayerVO().getLevel() < LegionConstant.LEGION_CREATE_LEVEL) {
				throw new GameException(AlertEnum.ROLE_LEVEL_NO);
			}
			playerAccountService.hasEnoughMoney(userCached, LegionConstant.LEGION_CREATE_MONEY);
			LegionVO legionVO = new LegionVO();
			legionVO.setLegionName(req.getLegionName());
			legionVO.setCreateTime(new Date());
			legionVO.setLegionLevel(1);
			legionVO.setBaseLegionVO(LegionRes.getInstance().getBaseLegionByLevel(legionVO.getLegionLevel()));
			legionVO.setPlayerId(playerId);
			legionVO.setPlayerNick(userCached.getPlayerVO().getName());
			legionVO.addMoney(LegionConstant.LEGION_CREATE_MONEY / LegionConstant.LEGION_BUILD_MONEY_RATE);
			legionVO.setCombat(userCached.getPlayerVO().getCombat());
			// legionVO.setBossKiller(new int[LegionConstant.LEGION_BOSS_NUM]);
			legionService.addLegion(legionVO);
			LegionMemVO memVO = new LegionMemVO();
			memVO.setPlayerId(playerId);
			memVO.setId(legionVO.getId());
			memVO.setJoinTime(new Date());
			memVO.setType(MEM_TYPE.MEM_TYPE_CEO_VALUE);
			memVO.addDonate(LegionConstant.LEGION_CREATE_MONEY / LegionConstant.LEGION_BUILD_MONEY_RATE);

			LegionService.initMemEveryDay(legionVO, memVO);
			memVO.setBaseLegionMemVO(LegionMemRes.getInstance().getMemDonateByDonate(memVO.getDonate()));

			legionService.addMem(legionVO, memVO);
			ServerHandler.addPlayerToLegionChannel(legionVO.getId(), userCached.getChannel());
			userCached.getPlayerVO().setLegionId(legionVO.getId());
			legionService.updatePlayerVOForLegion(userCached.getPlayerVO());
			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, LegionConstant.LEGION_CREATE_MONEY, userCached.getPlayerAccountVO(), commandList, "创建军团花费");
			nettyMessageVO.setCommandCode(CSCommandConstant.LEGION_PUSH_INFO);
			BaseLegionInfo.Builder resp = VOUtil.packBaseLegionInfo(legionVO, memVO);
			nettyMessageVO.setData(resp.build().toByteArray());
			commandList.add(nettyMessageVO);
		}
	}

	public void legionJoin(LegionJoinReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int legionId = req.getLeigonId();
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		if (userCached.getPlayerVO().getLegionId() != 0) {
			LegionVO mineLegion = legionService.getMineLegion(userCached, commandList);
			LegionMemVO mineMem = LegionService.getLegionMemVO(mineLegion, playerId, commandList);
			commandList.add(CommandUtil.packBaseLegionInfo(MEM_TYPE.valueOf(mineMem.getType()), mineLegion));
			throw new GameException(AlertEnum.LEGION_EXIST);
		}
		UserLegion userLegion = userCached.getUserLegion();
		if (!InitLoad.DEBUG_BETA && userLegion.getJoinCountDown() > 0) {
			throw new GameException(AlertEnum.LEGION_JOIN_CD);
		}
		LegionVO legionVO = legionService.getReqLegionById(legionId, commandList);
		LegionService.filterDestroy(legionVO);
		if (legionVO.getBaseLegionVO().getMembers() <= legionVO.getRealyMems().size()) {
			throw new GameException(AlertEnum.LEGION_HAD_FULL);
		}
		if (legionVO.getMem(userCached.getPlayerId()) != null) {
			throw new GameException(AlertEnum.LEGION_HAD_JOIN_REQ);
		}

		LegionMemVO memVO = new LegionMemVO();
		memVO.setId(legionId);
		memVO.setPlayerId(playerId);
		memVO.setType(MEM_TYPE.MEM_TYPE_REQ_VALUE);
		memVO.setJoinTime(new Date());
		// userLegion.getJoinList().add(legionVO);
		LegionService.initMemEveryDay(legionVO, memVO);
		memVO.setBaseLegionMemVO(LegionMemRes.getInstance().getMemDonateByDonate(memVO.getDonate()));

		legionService.addMem(legionVO, memVO);
		ServerHandler.broadcastLegion(legionId, CommandUtil.packLegionReqNotice(1));

		LegionJoinResp.Builder resp = LegionJoinResp.newBuilder();
		resp.setLegionId(legionId);
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	public void joinList(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		LegionVO legionVO = legionService.getMineLegion(userCached, commandList);
		LegionMemVO memVO = LegionService.getLegionMemVO(legionVO, playerId, commandList);
		LegionService.filterPermission(memVO.getType(), MEM_TYPE.MEM_TYPE_VP_VALUE);// 非管理员无法看到申请列表
		List<LegionMemVO> joins = legionVO.getJoin_mems();
		LegionJionListResp.Builder resp = LegionJionListResp.newBuilder();
		for (LegionMemVO legionMemVO : joins) {
			resp.addLegionJionInfo(VOUtil.packLegionJionInfo(legionMemVO));
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/** 成员管理 **/
	public void legionMemManage(LegionMemManageReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		int legionId = userCached.getPlayerVO().getLegionId();
		LegionVO legionVO = legionService.getMineLegion(userCached, commandList);
		LegionMemVO mineMemVO = LegionService.getLegionMemVO(legionVO, playerId, commandList);

		int mineType = mineMemVO.getType();
		List<Integer> ids = req.getPlayerIdsList();
		MEM_MANAGE_TYPE type = req.getType();
		if (type == MEM_MANAGE_TYPE.MANAGE_TYPE_AGREE) {// 同意
			LegionService.filterDestroy(legionVO);
			LegionService.filterPermission(mineType, MEM_TYPE.MEM_TYPE_VP_VALUE);
			if (legionVO.getBaseLegionVO().getMembers() <= legionVO.getRealyMems().size()) {
				throw new GameException(AlertEnum.LEGION_HAD_FULL);
			}

			List<LegionMemVO> joins = legionVO.getJoin_mems();
			boolean notExist = true;
			for (Integer integer : ids) {
				for (LegionMemVO legionMemVO : joins) {
					if (integer == legionMemVO.getPlayerId()) {
						int memId = legionMemVO.getPlayerId();
						SyncLock lockObject = SyncUtil.getInstance().getLock(RedisKey.PLAYERVO_MAP + memId);
						synchronized (lockObject) {
							PlayerVO playerVO = null;
							UserCached memCached = ServerHandler.getUserCached2(memId);
							if (memCached != null) {
								memCached.getPlayerVO().setLegionId(legionId);
								playerVO = memCached.getPlayerVO();
							} else {
								playerVO = RedisMap.getPlayerVObyId(memId);
								if (playerVO.getLegionId() > 0) {// 已经被其它军团同意
									throw new GameException(AlertEnum.LEGION_HAD_JOIN_OTHER);
								}
								playerVO.setLegionId(legionId);
							}
							legionService.updatePlayerVOForLegion(playerVO);
							legionService.addLegionLog(legionVO, LegionService.createLegionLog(LEGION_LOG_TYPE.LOG_TYPE_ADD_VALUE, legionVO, playerVO.getName()));
						}
						legionMemVO.setJoinTime(new Date());
						legionMemVO.setType(MEM_TYPE.MEM_TYPE_STAFF_VALUE);
						legionService.updateMem(legionMemVO);
						legionVO.removeJoin(legionMemVO);
						legionVO.addReallyMem(legionMemVO);
						UserCached memCache = ServerHandler.getUserCached2(memId);// 移除消息群组
						if (memCache != null) {
							ServerHandler.addPlayerToLegionChannel(legionId, memCache.getChannel());
						}
						ServerHandler.sendMessageToPlayer(CommandUtil.packBaseLegionInfo(MEM_TYPE.MEM_TYPE_STAFF, legionVO), memId);
						if (legionVO.getJoin_mems().size() == 0) {
							ServerHandler.broadcastLegion(legionId, CommandUtil.packLegionReqNotice(0));
						}
						notExist = false;
						break;
					}
				}
				if (notExist) {
					throw new GameException(AlertEnum.LEGION_HAD_JOIN_OTHER);
				}
			}
		} else if (type == MEM_MANAGE_TYPE.MANAGE_TYPE_REJECT) {// 拒绝
			LegionService.filterDestroy(legionVO);
			LegionService.filterPermission(mineType, MEM_TYPE.MEM_TYPE_VP_VALUE);
			for (Integer integer : ids) {
				List<LegionMemVO> joins = legionVO.getJoin_mems();// 删除后应该拿取最新的数据
				for (LegionMemVO legionMemVO : joins) {
					if (integer == legionMemVO.getPlayerId()) {
						legionVO.removeMem(legionMemVO);
						legionService.delMem(legionMemVO);
						if (legionVO.getJoin_mems().size() == 0) {
							ServerHandler.broadcastLegion(legionId, CommandUtil.packLegionReqNotice(0));
						}
						break;
					}
				}
			}
		} else if (type == MEM_MANAGE_TYPE.MANAGE_TYPE_UP) {// 升值:需要判断堂主和副团长数量
			LegionService.filterDestroy(legionVO);
			for (Integer integer : ids) {
				LegionMemVO editMemVO = legionVO.getMem(integer);
				if (editMemVO == null) {
					throw new GameException(AlertEnum.LEGION_HAD_REMOVE);
				}
				LegionService.filterPermission(mineType, editMemVO.getType());
				int editType = editMemVO.getType();
				if (editType >= MEM_TYPE.MEM_TYPE_VP_VALUE) {
					throw new GameException(AlertEnum.LEGION_NOT_PERMISSION);
				} else if (editType == MEM_TYPE.MEM_TYPE_STAFF_VALUE) {
					int currNum = legionVO.getManageNum(MEM_TYPE.MEM_TYPE_PM);
					int totalNum = legionVO.getBaseLegionVO().getChief();
					if (currNum >= totalNum) {
						throw new GameException(AlertEnum.LEGION_MANAGE_PM_FULL);
					}
				} else if (editType == MEM_TYPE.MEM_TYPE_PM_VALUE) {
					int currNum = legionVO.getManageNum(MEM_TYPE.MEM_TYPE_VP);
					int totalNum = legionVO.getBaseLegionVO().getDeputyleader();
					if (currNum >= totalNum) {
						throw new GameException(AlertEnum.LEGION_MANAGE_VP_FULL);
					}
				} else {
					throw new GameException(AlertEnum.LEGION_NOT_PERMISSION);
				}

				editMemVO.setType(editType + 1);
				legionService.updateMem(editMemVO);
				SyncLock lockObject = SyncUtil.getInstance().getLock(RedisKey.PLAYERVO_MAP + editMemVO.getPlayerId());
				synchronized (lockObject) {
					PlayerVO playerVO = RedisMap.getPlayerVObyId(editMemVO.getPlayerId());
					// /升职:操作者,职位,被操作玩家,新职位
					legionService.addLegionLog(
							legionVO,
							LegionService.createLegionLog(LEGION_LOG_TYPE.LOG_TYPE_UP_VALUE, legionVO, userCached.getPlayerVO().getName(), LegionConstant.getMemTitleByType(mineType),
									playerVO.getName(), LegionConstant.getMemTitleByType(editMemVO.getType())));
				}
			}
		} else if (type == MEM_MANAGE_TYPE.MANAGE_TYPE_DOWN) {// 降职
			LegionService.filterDestroy(legionVO);
			for (Integer integer : ids) {
				LegionMemVO editMemVO = legionVO.getMem(integer);
				if (editMemVO == null) {
					throw new GameException(AlertEnum.LEGION_HAD_REMOVE);
				}
				LegionService.filterPermission(mineType, editMemVO.getType());
				if (editMemVO.getType() < MEM_TYPE.MEM_TYPE_PM_VALUE) {
					throw new GameException(AlertEnum.LEGION_NOT_PERMISSION);
				}
				if (editMemVO.getType() == MEM_TYPE.MEM_TYPE_VP_VALUE) {
					int currNum = legionVO.getManageNum(MEM_TYPE.MEM_TYPE_PM);
					int totalNum = legionVO.getBaseLegionVO().getChief();
					if (currNum >= totalNum) {
						throw new GameException(AlertEnum.LEGION_MANAGE_PM_FULL);
					}
				}
				editMemVO.setType(editMemVO.getType() - 1);
				legionService.updateMem(editMemVO);
				SyncLock lockObject = SyncUtil.getInstance().getLock(RedisKey.PLAYERVO_MAP + editMemVO.getPlayerId());
				synchronized (lockObject) {
					PlayerVO playerVO = RedisMap.getPlayerVObyId(editMemVO.getPlayerId());
					// /升职:操作者,职位,被操作玩家,新职位
					legionService.addLegionLog(
							legionVO,
							LegionService.createLegionLog(LEGION_LOG_TYPE.LOG_TYPE_DOWN_VALUE, legionVO, userCached.getPlayerVO().getName(), LegionConstant.getMemTitleByType(mineType),
									playerVO.getName(), LegionConstant.getMemTitleByType(editMemVO.getType())));
				}

			}
		} else if (type == MEM_MANAGE_TYPE.MANAGE_TYPE_DEL) {// 剔除玩家
			LegionService.filterDestroy(legionVO);
			for (Integer integer : ids) {
				LegionMemVO editMemVO = legionVO.getMem(integer);
				if (editMemVO == null) {
					throw new GameException(AlertEnum.LEGION_HAD_REMOVE);
				}
				LegionService.filterPermission(mineType, editMemVO.getType() == MEM_TYPE.MEM_TYPE_VP_VALUE ? MEM_TYPE.MEM_TYPE_CEO_VALUE : MEM_TYPE.MEM_TYPE_VP_VALUE);// 副帮主只有帮主剔除,其它的只需要
				legionVO.removeMem(editMemVO);
				legionService.delMem(editMemVO);
				int memId = editMemVO.getPlayerId();
				SyncLock lockObject = SyncUtil.getInstance().getLock(RedisKey.PLAYERVO_MAP + memId);
				synchronized (lockObject) {
					PlayerVO playerVO = null;
					UserCached memCached = ServerHandler.getUserCached2(memId);
					if (memCached != null) {
						memCached.getPlayerVO().setLegionId(0);
						playerVO = memCached.getPlayerVO();
						ServerHandler.removeFromLegionChannel(legionId, memCached.getChannel());
					} else {
						playerVO = RedisMap.getPlayerVObyId(memId);
						playerVO.setLegionId(0);
					}
					legionService.updatePlayerVOForLegion(playerVO);
					legionService.addLegionLog(
							legionVO,
							LegionService.createLegionLog(LEGION_LOG_TYPE.LOG_TYPE_DEL_VALUE, legionVO, userCached.getPlayerVO().getName(), LegionConstant.getMemTitleByType(mineType),
									playerVO.getName()));
				}
				ServerHandler.sendMessageToPlayer(CommandUtil.packBaseLegionInfo(MEM_TYPE.MEM_TYPE_NONE, null), editMemVO.getPlayerId());
			}
		} else if (type == MEM_MANAGE_TYPE.MANAGE_TYPE_CEO_CHANGE) {
			for (Integer integer : ids) {
				LegionMemVO editMemVO = legionVO.getMem(integer);
				if (editMemVO == null) {
					throw new GameException(AlertEnum.LEGION_HAD_REMOVE);
				}
				LegionService.filterPermission(mineType, MEM_TYPE.MEM_TYPE_CEO_VALUE);
				mineMemVO.setType(MEM_TYPE.MEM_TYPE_STAFF_VALUE);

				editMemVO.setType(MEM_TYPE.MEM_TYPE_CEO_VALUE);
				SyncLock lockObject = SyncUtil.getInstance().getLock(RedisKey.PLAYERVO_MAP + editMemVO.getPlayerId());
				synchronized (lockObject) {
					PlayerVO playerVO = null;
					UserCached memCached = ServerHandler.getUserCached2(editMemVO.getPlayerId());
					if (memCached != null) {
						playerVO = memCached.getPlayerVO();
					} else {
						playerVO = RedisMap.getPlayerVObyId(editMemVO.getPlayerId());
					}
					legionVO.setPlayerId(editMemVO.getPlayerId());
					legionVO.setPlayerNick(playerVO.getName());
					legionService.addLegionLog(legionVO, LegionService.createLegionLog(LEGION_LOG_TYPE.LOG_TYPE_CHANGE_VALUE, legionVO, userCached.getPlayerVO().getName(), playerVO.getName()));
				}
				NettyMessageVO msg = new NettyMessageVO();
				msg.setCommandCode(CSCommandConstant.LEGION_HOME);
				LegionInfoResp.Builder resp = VOUtil.packLegionInfo(userCached.getUserTimer().getPlayerTimerVO(), legionVO, mineMemVO);
				msg.setData(resp.build().toByteArray());
				commandList.add(msg);

				legionService.updateMem(editMemVO);
				legionService.updateMem(mineMemVO);
				legionService.updateLegion(legionVO);

				break;
			}
		} else if (type == MEM_MANAGE_TYPE.MANAGE_TYPE_QUIT) {// 退出军团
			if (mineType == MEM_TYPE.MEM_TYPE_CEO_VALUE) {
				throw new GameException(AlertEnum.LEGION_NOT_PERMISSION);
			}

			legionService.delMem(mineMemVO);
			legionVO.removeMem(mineMemVO);
			userCached.getPlayerVO().setLegionId(0);
			userCached.getUserLegion().setQuitDate(DateUtil.getNow());
			legionService.updatePlayerVOForLegion(userCached.getPlayerVO());
			commandList.add(CommandUtil.packBaseLegionInfo(MEM_TYPE.MEM_TYPE_NONE, null));
			mineMemVO = null;
			ServerHandler.removeFromLegionChannel(legionId, userCached.getChannel());
			legionService.addLegionLog(legionVO, LegionService.createLegionLog(LEGION_LOG_TYPE.LOG_TYPE_QUIT_VALUE, legionVO, userCached.getPlayerVO().getName()));
		}
		LegionMemManageResp.Builder resp = LegionMemManageResp.newBuilder();
		resp.setType(req.getType());
		for (Integer integer : ids) {
			resp.addPlayerIds(integer);
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/**
	 * 成员列表
	 * 
	 * @throws Exception
	 */
	public void memList(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		LegionVO legionVO = legionService.getMineLegion(userCached, commandList);
		LegionMemListResp.Builder resp = LegionMemListResp.newBuilder();
		for (LegionMemVO legionMemVO : legionVO.getRealyMems()) {
			resp.addMemDetails(VOUtil.packLegionMemVO(legionMemVO));
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/** 军团捐献 */
	public void legionDonate(LegionDonateReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		DONATE_TYPE type = req.getDonateType();
		int num = req.getNum() < 0 ? 0 : req.getNum();
		int getNum;
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		LegionVO legionVO = legionService.getMineLegion(userCached, commandList);
		LegionService.filterDestroy(legionVO);
		LegionMemVO mineMemVO = LegionService.getLegionMemVO(legionVO, playerId, commandList);

		if (type == DONATE_TYPE.DONATE_MATERIAL) {
			if (!knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), ItemConstants.ITEM_LEGION_MATERIAL, num)) {
				throw new GameException(AlertEnum.ITEM_NUM_NOT);
			}
			legionVO.addMaterial(num);
			getNum = num * 10;//
			knapsackService.removeItem(userCached, ItemConstants.ITEM_LEGION_MATERIAL, num, commandList);
			legionService.addLegionLog(legionVO,
					LegionService.createLegionLog(LEGION_LOG_TYPE.LOG_TYPE_DONATE_MATERIAL_VALUE, legionVO, userCached.getPlayerVO().getName(), String.valueOf(num), String.valueOf(getNum)));
		} else {
			playerAccountService.hasEnoughMoney(userCached, num * LegionConstant.LEGION_BUILD_MONEY_RATE);
			getNum = num;
			legionVO.addMoney(getNum);
			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, num * LegionConstant.LEGION_BUILD_MONEY_RATE, userCached.getPlayerAccountVO(), commandList, "捐献血盟银两");
			legionService.addLegionLog(legionVO,
					LegionService.createLegionLog(LEGION_LOG_TYPE.LOG_TYPE_DONATE_MONEY_VALUE, legionVO, userCached.getPlayerVO().getName(), String.valueOf(getNum), String.valueOf(getNum)));

		}
		mineMemVO.addDonate(getNum);
		BaseLegionMemVO donateVO = LegionMemRes.getInstance().getMemDonateByDonate(mineMemVO.getDonate());// 更新玩家捐献等级
		mineMemVO.setBaseLegionMemVO(donateVO);

		BaseLegionVO baseLegionVO = LegionRes.getInstance().getBaseLegionByLevel(legionVO.getLegionLevel() + 1);
		while (baseLegionVO != null && legionVO.getMoney() >= baseLegionVO.getMoney() && legionVO.getMaterial() >= baseLegionVO.getNumber()) {
			legionVO.addLevel(baseLegionVO);
			legionService.addLegionLog(legionVO, LegionService.createLegionLog(LEGION_LOG_TYPE.LOG_TYPE_UPGRADE_VALUE, legionVO, String.valueOf(baseLegionVO.getId())));
			baseLegionVO = LegionRes.getInstance().getBaseLegionByLevel(legionVO.getLegionLevel() + 1);
		}
		legionService.updateLegion(legionVO);
		legionService.updateMem(mineMemVO);

		LegionDonateResp.Builder resp = LegionDonateResp.newBuilder();
		resp.setLevel(legionVO.getLegionLevel());
		resp.setMaterial(legionVO.getMaterial());
		resp.setMoney(legionVO.getMoney());
		resp.setDonate(mineMemVO.getDonate());
		resp.setOwnDonate(mineMemVO.getOwnDonate());
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	// 军团商店购买
	public void legionShop(LegionShopBuyReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		int buyNum = req.getNum() < 0 ? 0 : req.getNum();

		LegionVO legionVO = legionService.getMineLegion(userCached, commandList);

		LegionMemVO mineMemVO = LegionService.getLegionMemVO(legionVO, playerId, commandList);
		BaseLegionShopVO baseShop = LegionShopRes.getInstance().getLegionShopVOByCfgId(req.getCfgId());
		if (baseShop == null) {
			throw new GameException(AlertEnum.ITEM_NOT_FOUND);
		}
		int num = baseShop.getDonate() * buyNum;
		if (mineMemVO.getOwnDonate() < num) {
			throw new GameException(AlertEnum.LEGION_DONATE_NOT_ENOUGH);
		}
		if (mineMemVO.getBaseLegionMemVO().getId() < baseShop.getLevel()) {
			throw new GameException(AlertEnum.LEGION_DONATE_LEVEL_NOT_ENGOUGH);
		}
		BaseItemVO itemVO = ItemRes.getInstance().getBaseItemVO(baseShop.getItem());
		if (itemVO == null) {
			throw new GameException(AlertEnum.ITEM_NOT_FOUND);
		}
		//
		List<Reward> rewards = new ArrayList<Reward>();
		rewards.add(MailService.createReward(1, itemVO.getCfgId(), buyNum));
		rewardService.checkAndReward(userCached, rewards, commandList);
		mineMemVO.subDonate(num);
		legionService.updateMem(mineMemVO);
		LegionShopBuyResp.Builder resp = LegionShopBuyResp.newBuilder();
		resp.setOwnDonate(mineMemVO.getOwnDonate());
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/**
	 * 获取军团日志
	 */
	public void getLogs(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		LegionVO legionVO = legionService.getMineLegion(userCached, commandList);
		LegionService.getLegionMemVO(legionVO, playerId, commandList);
		LegionLogsResp.Builder resp = LegionLogsResp.newBuilder();
		for (LegionLogVO log : legionVO.getLogs()) {
			resp.addLogInfo(VOUtil.packLegionLog(log));
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/**
	 * 领取玩家贡献奖励
	 * **/
	public void rewardMem(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		LegionVO legionVO = legionService.getMineLegion(userCached, commandList);
		LegionMemVO mineMemVO = LegionService.getLegionMemVO(legionVO, playerId, commandList);
		PlayerTimerVO pt = userCached.getUserTimer().getPlayerTimerVO();
		BaseLegionMemVO baseMemVO = mineMemVO.getBaseLegionMemVO();
		int mineId = baseMemVO.getId();
		boolean notReward = true;
		for (int i = 1; i <= mineId; i++) {
			int rewardRecord = pt.getDonateReward();
			if (((rewardRecord >> (i - 1)) & 0x1) == 1) {
				continue;
			} else {
				List<Reward> rewardList = LegionRewardRes.getInstance().getRewardRateGroup(LegionMemRes.getInstance().getBaseMemByLevel(i).getLevelreward());
				rewardService.checkAndReward(userCached, rewardList, commandList, GMIOEnum.IN_LEGION_MEM.usage());
				pt.setDonateReward(pt.getDonateReward() | (0x1 << (i - 1)));
				playerTimerService.updateLegionTime(pt);
				notReward = false;
				LegionDonateRewardResp.Builder resp = LegionDonateRewardResp.newBuilder();
				resp.setDonateReward(pt.getDonateReward());
				nettyMessageVO.setData(resp.build().toByteArray());
				commandList.add(nettyMessageVO);
				break;
			}
		}
		if (notReward) {
			throw new GameException(AlertEnum.LEGION_NOT_LEVEL_REWARD);
		}
	}

	/**
	 * 领取军团奖励
	 */
	public void dayReward(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		LegionVO legionVO = legionService.getMineLegion(userCached, commandList);
		LegionService.getLegionMemVO(legionVO, playerId, commandList);
		PlayerTimerVO pt = userCached.getUserTimer().getPlayerTimerVO();
		if (pt.getDayLegionReward() == 1) {
			throw new GameException(AlertEnum.REWARD_ALREADY_GET);
		} else {
			List<Reward> rewards = LegionRewardRes.getInstance().getRewardRateGroup(legionVO.getBaseLegionVO().getDailyreward());
			rewardService.checkAndReward(userCached, rewards, commandList, GMIOEnum.IN_LEGION_DAY.usage());
			pt.setDayLegionReward(1);
			playerTimerService.updateLegionTime(pt);
			LegionDayRewardResp.Builder resp = LegionDayRewardResp.newBuilder();
			resp.setDayReward(true);
			nettyMessageVO.setData(resp.build().toByteArray());
			commandList.add(nettyMessageVO);
		}
	}

	/**
	 * 军团基本信息编辑
	 * 
	 * @throws Exception
	 */
	public void legionEdit(LegionEditReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int type = req.getType();
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		LegionEditResp.Builder resp = LegionEditResp.newBuilder();

		LegionVO legionVO = legionService.getMineLegion(userCached, commandList);
		LegionMemVO mineMemVO = LegionService.getLegionMemVO(legionVO, playerId, commandList);
		LegionService.filterDestroy(legionVO);
		LegionService.filterPermission(mineMemVO.getType(), MEM_TYPE.MEM_TYPE_VP_VALUE);

		if (type == 1) {// qq
			if (!req.getQq().matches("^\\d+$")) {
				throw new GameException(AlertEnum.COMMON_NEED_NUM);
			}
			legionVO.setQq(req.getQq());
			resp.setQq(req.getQq());
		} else if (type == 2) {
			String msg = req.getLegionNotice();
			msg = BadWordsFilter.filter(msg);
			if (CommonUtils.calWordsLenth(msg) > 100) {
				throw new GameException(AlertEnum.COMMON_TEXT_TOO_LONG);
			}
			legionVO.setNotice(msg);
			resp.setLegionNotice(msg);
		}
		legionService.updateLegion(legionVO);
		resp.setType(type);
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/**
	 * 需要向全军团推送包
	 */
	public void destroy(LegionDestroyReq req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		int type = req.getType();
		LegionDestroyResp.Builder resp = LegionDestroyResp.newBuilder();
		LegionVO legionVO = legionService.getMineLegion(userCached, commandList);
		LegionMemVO mineMemVO = LegionService.getLegionMemVO(legionVO, playerId, commandList);
		LegionService.filterPermission(mineMemVO.getType(), MEM_TYPE.MEM_TYPE_CEO_VALUE);
		if (type == 1) {// 解散血盟
			if (legionVO.getDestroyTime() != 0) {
				throw new GameException(AlertEnum.LEGION_DESTROYING);
			}
			legionVO.setDestroyTime(DateUtil.getNow());
		} else {// 取消解散
			if (legionVO.getDestroyTime() == 0) {
			} else {
				legionVO.setDestroyTime(0);
			}
		}
		legionService.updateLegion(legionVO);
		nettyMessageVO = new NettyMessageVO();
		nettyMessageVO.setCommandCode(CSCommandConstant.LEGION_DESTROY);
		int div = legionVO.getDestroyTime() == 0 ? -1 : (legionVO.getDestroyTime() + LegionConstant.LEGION_DESTROY_TIME - DateUtil.getNow());
		resp.setTimeLeft(div);
		nettyMessageVO.setData(resp.build().toByteArray());
		ServerHandler.broadcastLegion(legionVO.getId(), nettyMessageVO);
	}

	/**
	 * 获得boss详情
	 */
	public void getBossDetail(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		LegionVO legionVO = legionService.getMineLegion(userCached, commandList);
		LegionService.filterDestroy(legionVO);
		LegionMemVO mineMemVO = LegionService.getLegionMemVO(legionVO, playerId, commandList);
		if (!DateUtil.isToday(mineMemVO.getAtkBossDate())) {
			LegionService.initMemEveryDay(legionVO, mineMemVO);
			legionService.updateMem(mineMemVO);
		}
		LegionBossDetail.Builder resp = LegionBossDetail.newBuilder();
		BaseLegionBossVO bossVO = mineMemVO.getBaseLegionBossVO();
		resp.setBossId(bossVO.getId());
		int killer = legionVO.getBossStatus(bossVO.getId());
		if (killer > 0) {
			PlayerVO playerVO = RedisMap.getPlayerVObyId(killer);
			resp.setFb(playerVO.getName());
		}
		LegionService.initBossHp(killer, mineMemVO);
		if (mineMemVO.getBossStatus() == 2) {// 检验是否帮派等级已经满足条件,避免帮派升级后能攻打
			if (legionVO.getLegionLevel() >= mineMemVO.getBaseLegionBossVO().getLevel()) {
				mineMemVO.setBossStatus(1);
				legionService.updateMem(mineMemVO);
			}
		}

		resp.setHp(mineMemVO.getHp());
		resp.setBossBuf(killer > 0);

		resp.setBossStatus(mineMemVO.getBossStatus());
		resp.setLegionLevel(legionVO.getLegionLevel());
		int maxBossId = mineMemVO.getBaseLegionBossVO().getId();
		int rewardIndex = getRewardIndex(maxBossId, mineMemVO.getBossRewardId());
		resp.setRewardId(rewardIndex == 0 ? 1 : rewardIndex);
		resp.setCanReward(rewardIndex > 0 && rewardIndex < maxBossId);
		resp.setCountDown(mineMemVO.getCountDown(DateUtil.getNow()));
		int rId = LegionBossRes.getInstance().getBaseLegionBossById(rewardIndex == 0 ? 1 : rewardIndex).getGangreward();
		List<Reward> rewards = LegionRewardRes.getInstance().getRewardRateGroup(rId);
		resp.setRewardInfoList(VOUtil.packRewardInfos(rewards));
		nettyMessageVO = new NettyMessageVO();
		nettyMessageVO.setCommandCode(CSCommandConstant.LEGION_BOSS_DETAIL);
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	private int getRewardIndex(int maxBossId, int bossRewardId) {
		if (maxBossId == 1) {// 一个都没打赢
			return 0;
		}
		for (int i = 1; i < maxBossId; i++) {
			if (((bossRewardId >> (i - 1)) & 0x1) == 0) {
				return i;
			}
		}
		return maxBossId;
	}

	/** 领取boss奖励 */
	public void bossReawrd(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		LegionVO legionVO = legionService.getMineLegion(userCached, commandList);
		LegionMemVO mineMemVO = LegionService.getLegionMemVO(legionVO, playerId, commandList);

		boolean notReward = true;
		for (int i = 1; i <= mineMemVO.getBaseLegionBossVO().getId(); i++) {
			int rewardRecord = mineMemVO.getBossRewardId();
			if (((rewardRecord >> (i - 1)) & 0x1) == 1) {
				continue;
			} else {
				int rewardIndex = getRewardIndex(mineMemVO.getBaseLegionBossVO().getId(), mineMemVO.getBossRewardId());
				if (rewardIndex == 0) {
					break;
				}
				int rId = LegionBossRes.getInstance().getBaseLegionBossById(rewardIndex).getGangreward();
				List<Reward> rewards = LegionRewardRes.getInstance().getRewardRateGroup(rId);
				if (userCached.getPlayerVO().getVip() >= 5) {// VIP5奖励加倍
					rewards.addAll(LegionRewardRes.getInstance().getRewardRateGroup(rId));
				}
				rewardService.checkAndReward(userCached, rewards, commandList);
				mineMemVO.setBossRewardId(mineMemVO.getBossRewardId() | (0x1 << (i - 1)));
				legionService.updateMem(mineMemVO);
				notReward = false;
				LegionBossRewardResp.Builder resp = LegionBossRewardResp.newBuilder();
				int maxBossId = mineMemVO.getBaseLegionBossVO().getId();
				rewardIndex = getRewardIndex(maxBossId, mineMemVO.getBossRewardId());
				resp.setRewardId(rewardIndex == 0 ? 1 : rewardIndex);
				resp.setCanReward(rewardIndex > 0 && rewardIndex < maxBossId);
				rId = LegionBossRes.getInstance().getBaseLegionBossById(rewardIndex == 0 ? 1 : rewardIndex).getGangreward();
				rewards = LegionRewardRes.getInstance().getRewardRateGroup(rId);
				resp.setRewardInfoList(VOUtil.packRewardInfos(rewards));
				nettyMessageVO.setData(resp.build().toByteArray());
				commandList.add(nettyMessageVO);
				break;
			}
		}
		if (notReward) {
			throw new GameException(AlertEnum.LEGION_NOT_LEVEL_REWARD);
		}
	}

	/**
	 * 加速boss
	 * 
	 * @param nettyMessageVO
	 * @param commandList
	 * @throws Exception
	 */
	public void legionBossCdAccr(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		LegionVO legionVO = legionService.getMineLegion(userCached, commandList);
		LegionMemVO mineMemVO = LegionService.getLegionMemVO(legionVO, playerId, commandList);
		int now = DateUtil.getNow();
		if (mineMemVO.getCountDown(now) == 0) {
			throw new GameException(AlertEnum.CD_HAD_FINISHED);
		}

		if (knapsackService.hasEnoughItem(userCached.getUserKnapsack().getKnapsackList(), ItemConstants.ITEM_CDESC_LENQUE, 1)) {
			knapsackService.removeItem(userCached, ItemConstants.ITEM_CDESC_LENQUE, 1, commandList);
		} else {
			playerAccountService.hasEnoughRMBAndGift(userCached, RaidConstant.RAID_ACCRCD_COST_RMB);
			playerAccountService.deductCurrency(PLAYER_PROPERTY.PROPERTY_RMB, RaidConstant.RAID_ACCRCD_COST_RMB, userCached.getPlayerAccountVO(), commandList, "加入银两和经验副本",
					GMIOEnum.OUT_CD_LEGION_BOSS.usage());
		}
		mineMemVO.setAtkFailDate(0);
		legionService.updateMem(mineMemVO);
		nettyMessageVO.setData(LegionBossCdAccrResp.newBuilder().setCd(0).build().toByteArray());
		commandList.add(nettyMessageVO);
	}

}
