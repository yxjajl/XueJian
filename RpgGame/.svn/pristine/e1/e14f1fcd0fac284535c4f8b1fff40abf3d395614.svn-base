package com.dh.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.dao.PlayerTimerMapper;
import com.dh.game.vo.user.PlayerBuildVO;
import com.dh.sqlexe.SqlSaveThread;
import com.dh.util.SqlBuild;
import com.dh.vo.user.UserCached;

@Component
public class BuildService {
	@Resource
	private PlayerTimerMapper playerTimerMapper;
	@Resource
	private SqlBuild sqlBuild;
	@Resource
	private SqlSaveThread sqlSaveThread;

	public void loadBuild(UserCached userCached) {
		PlayerBuildVO playerBuildVO = playerTimerMapper.getBuild(userCached.getPlayerId());
		userCached.getUserTimer().setPlayerBuildVO(playerBuildVO);
	}
}
