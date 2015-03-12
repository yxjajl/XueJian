package com.dh.constants;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.dh.Cache.ServerHandler;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.vo.raid.ManyPeopleProto.ManyPeopleListResp;
import com.dh.game.vo.raid.ManyPeopleProto.ManyPeopleRaidTeamInfo;
import com.dh.game.vo.user.PlayerVO;
import com.dh.handler.raid.ManyPeopleTeam;
import com.dh.netty.NettyMessageVO;
import com.dh.sync.LockConstant;
import com.dh.sync.SyncLock;
import com.dh.sync.SyncUtil;

public class ManyPeopleRaidConstants {
	public final static int TEAM_STATUS_CREATE = 0;// 组队状态
	public final static int TEAM_STATUS_BATTLE = 1;// 战斗状态
	// 副本id, 副本队伍列表
	public final static HashMap<Integer, Map<Integer, ManyPeopleTeam>> MANPEOPLETEAMMAP = new HashMap<Integer, Map<Integer, ManyPeopleTeam>>();

	//
	public static Collection<ManyPeopleTeam> teamList(int raidid) {
		SyncLock syncLock = SyncUtil.getInstance().getLock(LockConstant.LOCK_MANYPEOPLE + raidid);
		synchronized (syncLock) {
			Map<Integer, ManyPeopleTeam> map = MANPEOPLETEAMMAP.get(raidid);
			if (map == null) {
				map = new HashMap<Integer, ManyPeopleTeam>();
				MANPEOPLETEAMMAP.put(raidid, map);
			} else {
				return map.values();
			}
		}
		return null;
	}

	// 创建副本
	public static void createTeam(int raidid, int levelLimit, boolean isAutoStart, String password, PlayerVO masterPlayerVO) {
		SyncLock syncLock = SyncUtil.getInstance().getLock(LockConstant.LOCK_MANYPEOPLE + raidid);
		ManyPeopleTeam manyPeopleTeam = null;
		synchronized (syncLock) {

			Map<Integer, ManyPeopleTeam> map = MANPEOPLETEAMMAP.get(raidid);
			if (map == null) {
				map = new HashMap<Integer, ManyPeopleTeam>();
				MANPEOPLETEAMMAP.put(raidid, map);
			} else {
				manyPeopleTeam = map.get(masterPlayerVO.getPlayerId());
			}

			if (masterPlayerVO != null) {
				System.err.println("error");
			}

			manyPeopleTeam = newManyPeopleTeam(generationTeamId(), raidid, levelLimit, isAutoStart, password, masterPlayerVO);
			map.put(raidid, manyPeopleTeam);
		}
	}

	public static void joinTeam(int raidTypeId, int teamid, String password, PlayerVO clientPlayerVO) throws Exception {
		ManyPeopleTeam manyPeopleTeam = null;
		Map<Integer, ManyPeopleTeam> map = MANPEOPLETEAMMAP.get(raidTypeId);
		if (map == null) {
			throw new GameException(AlertEnum.TEAM_NOT_EXIST);
		} else {
			manyPeopleTeam = map.get(teamid);
		}

		if (manyPeopleTeam == null) {
			throw new GameException(AlertEnum.TEAM_NOT_EXIST);
		}

		synchronized (manyPeopleTeam) {
			if (manyPeopleTeam.getPassword().equals(password)) {
				throw new GameException(AlertEnum.TEAM_PASS_ERR);
			}

			if (clientPlayerVO.getLevel() < manyPeopleTeam.getLevelLimit()) {
				throw new GameException(AlertEnum.TEAM_COND_ERR);
			}
		}

		manyPeopleTeam.join(clientPlayerVO);
	}

	private static ManyPeopleTeam newManyPeopleTeam(int teamId, int raidid, int levelLimit, boolean isAutoStart, String password, PlayerVO masterPlayerVO) {
		ManyPeopleTeam mpt = new ManyPeopleTeam();

		mpt.setTeamId(teamId);
		mpt.setAutoStart(isAutoStart);
		mpt.setRaidid(raidid);
		mpt.setLevelLimit(levelLimit);
		mpt.setPassword(password);
		// mpt.setMasterPlayerVO(masterPlayerVO);

		return mpt;
	}

	// 广播消息
	public static void brokeMessage(List<PlayerVO> playerList, int otherPlayerId, NettyMessageVO nettyMessageVO) {
		if (playerList != null) {
			for (PlayerVO playerVO : playerList)
				if (playerVO.getPlayerId() != otherPlayerId) {
					ServerHandler.sendMessageToPlayer(nettyMessageVO, playerVO.getPlayerId());
				}
		}
	}

	// =========================================================================
	private static AtomicInteger teamidSerial = new AtomicInteger(0);

	public static int generationTeamId() {
		if (teamidSerial.get() >= 9999999) {
			teamidSerial.set(0);
		}
		return teamidSerial.incrementAndGet();
	}

	public static ManyPeopleRaidTeamInfo.Builder toManyPeopleRaidTeamInfo(ManyPeopleTeam manyPeopleTeam) {
		ManyPeopleRaidTeamInfo.Builder manyPeopleRaidTeamInfo = ManyPeopleRaidTeamInfo.newBuilder();
		manyPeopleRaidTeamInfo.setTeamId(manyPeopleTeam.getTeamId());
		manyPeopleRaidTeamInfo.setNick("abc");
		manyPeopleRaidTeamInfo.setNumber(0);
		manyPeopleRaidTeamInfo.setPersonLimit(0);
		manyPeopleRaidTeamInfo.setIsLock(manyPeopleTeam.getPassword() != null);
		manyPeopleRaidTeamInfo.setReqLevel(manyPeopleTeam.getLevelLimit());

		return manyPeopleRaidTeamInfo;
	}

	// 队伍信息列表
	public static ManyPeopleListResp.Builder getManyPeopleListResp(Collection<ManyPeopleTeam> collection) {
		ManyPeopleListResp.Builder manyPeopleListResp = ManyPeopleListResp.newBuilder();
		if (collection != null) {
			for (ManyPeopleTeam manyPeopleTeam : collection) {
				manyPeopleListResp.addTeamInfo(toManyPeopleRaidTeamInfo(manyPeopleTeam));
			}
		}
		manyPeopleListResp.setTimes(100);
		return manyPeopleListResp;
	}

}
