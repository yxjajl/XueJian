package com.dh.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dh.game.vo.user.PlayerBuildVO;

public interface TechMapper {
	public void updateTech(@Param(value = "index") int index, @Param(value = "playerId") int playerId, @Param(value = "level") int level, @Param(value = "cdTime") Date cdTime);

	/**
	 * 查询玩家科技 TODO
	 */
	public List<Map<String, Object>> getTechs(int playerId);

	public void insertTech(@Param(value = "index") int index, @Param(value = "playerId") int playerId, @Param(value = "level") int level, @Param(value = "cdTime") Date cdTime);

	public void insertPlayerBuild(PlayerBuildVO playerBuildVO);
}
