package com.dh.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.dao.PlayerRaidMapper;
import com.dh.game.vo.base.BaseRaidInfo;
import com.dh.game.vo.user.PlayerEliteRaidVO;
import com.dh.game.vo.user.PlayerRaidVO;
import com.dh.resconfig.RaidRes;
import com.dh.sqlexe.SqlSaveThread;
import com.dh.util.DateUtil;
import com.dh.util.SqlBuild;
import com.dh.vo.user.UserCached;

@Component
public class RaidService {
	public static final int ELRAID_INIT_TIMES = 20; // 精英副本初始挑战次数
	@Resource
	private PlayerRaidMapper playerRaidMapper;
	@Resource
	private SqlBuild sqlBuild;
	@Resource
	private SqlSaveThread sqlSaveThread;

	/**
	 * 加载副本
	 * 
	 * @param userCached
	 * @throws Exception
	 */
	public void loadRaidList(UserCached userCached) throws Exception {
		// 普通副本
		List<PlayerRaidVO> raidList = playerRaidMapper.getRaidList(userCached.getPlayerId());
		if (raidList != null) {
			userCached.getUserRaid().setRaidList(raidList);
		} else {
			userCached.getUserRaid().setRaidList(new ArrayList<PlayerRaidVO>());
		}
		// 精英副本
		List<PlayerEliteRaidVO> eliteRaidList = playerRaidMapper.getEliteRaidList(userCached.getPlayerId());

		userCached.getUserRaid().getEliteRaidList().clear();
		if (raidList != null) {
			userCached.getUserRaid().getEliteRaidList().addAll(eliteRaidList);
		}

		bucongEliRaid(userCached);

	}

	public void bucongEliRaid(UserCached userCached) {
		for (PlayerRaidVO playerRaidVO : userCached.getUserRaid().getRaidList()) {
			if (playerRaidVO.getScore() <= 0) {
				continue;
			}
			BaseRaidInfo baseRaidInfo = RaidRes.getInstance().getNextBaseRaidInfo(playerRaidVO.getRaidid(), RaidRes.RAID_MODE_ELI);
			if (baseRaidInfo != null && userCached.getUserRaid().getPlayerEliteRaidVO(baseRaidInfo.getRaidid()) == null) {
				this.addPlayerEliteRaidVO(userCached, baseRaidInfo);
			}
		}
	}

	public int getCountTimesAndClear(UserCached userCached) {
		int count = 0;
		Date curDate = new Date();
		PlayerEliteRaidVO playerEliteRaidVO = userCached.getUserRaid().getFirstPlayerEliteRaidVO();
		if (playerEliteRaidVO != null) {
			if (!DateUtil.isSameDay(playerEliteRaidVO.getPassdate(), curDate, 4)) {// 每天４点刷新
				playerEliteRaidVO.setTimes(ELRAID_INIT_TIMES);
				playerEliteRaidVO.setPassdate(curDate);
				updateEliteRaid(playerEliteRaidVO);
			}
			count += playerEliteRaidVO.getTimes();
		}
		return count;
	}

	/**
	 * 新增副本
	 * 
	 * @param userCached
	 * @param baseRaidInfo
	 */
	public void addPlayerRaidVO(UserCached userCached, BaseRaidInfo baseRaidInfo) {
		PlayerRaidVO playerRaidVO = new PlayerRaidVO();
		playerRaidVO.setPlayerId(userCached.getPlayerId());
		playerRaidVO.setRaidid(baseRaidInfo.getRaidid());
		playerRaidVO.setScore((short) 0);
		playerRaidVO.setScorereward1((short) 0);
		playerRaidVO.setScorereward2((short) 0);
		playerRaidVO.setFivereward((short) 0);
		// userCached.getUserRaid().getRaidList().add(playerRaidVO);
		userCached.getUserRaid().appendRaid(playerRaidVO, baseRaidInfo);
		sqlSaveThread.putSql(playerRaidVO.getPlayerId(), sqlBuild.getSql("com.dh.dao.PlayerRaidMapper.addRaid", playerRaidVO));
	}

	/**
	 * 新增副本
	 * 
	 * @param userCached
	 * @param baseRaidInfo
	 */
	public void addPlayerEliteRaidVO(UserCached userCached, BaseRaidInfo baseRaidInfo) {
		PlayerEliteRaidVO playerEliteRaidVO = new PlayerEliteRaidVO();
		playerEliteRaidVO.setPlayerId(userCached.getPlayerId());
		playerEliteRaidVO.setRaidid(baseRaidInfo.getRaidid());
		playerEliteRaidVO.setScore(0);
		playerEliteRaidVO.setTimes(ELRAID_INIT_TIMES);
		playerEliteRaidVO.setPassdate(new Date());
		playerEliteRaidVO.setFivereward(0);
		// userCached.getUserRaid().getRaidList().add(playerRaidVO);
		userCached.getUserRaid().appendEliteRaid(playerEliteRaidVO, baseRaidInfo);
		sqlSaveThread.putSql(playerEliteRaidVO.getPlayerId(), sqlBuild.getSql("com.dh.dao.PlayerRaidMapper.addEliteRaid", playerEliteRaidVO));
	}

	public void updateRaid(PlayerRaidVO playerRaidVO) {
		sqlSaveThread.putSql(playerRaidVO.getPlayerId(), sqlBuild.getSql("com.dh.dao.PlayerRaidMapper.updateRaid", playerRaidVO));
	}

	public void updateEliteRaid(PlayerEliteRaidVO playerEliteRaidVO) {
		sqlSaveThread.putSql(playerEliteRaidVO.getPlayerId(), sqlBuild.getSql("com.dh.dao.PlayerRaidMapper.updateEliteRaid", playerEliteRaidVO));
	}
}
