package com.dh.dao;

import java.util.List;

import com.dh.game.vo.user.LegionLogVO;
import com.dh.game.vo.user.LegionMemVO;
import com.dh.game.vo.user.LegionVO;

public interface LegionMapper {
	public void insertLegion(LegionVO legionVO);

	public void insertLegionMem(LegionMemVO legionMemVO);

	public void delLegion(int id);

	public List<LegionMemVO> getJoinList(int playerId);

	public void updateLegionMem(LegionMemVO legionMemVO);

	public void delLegionMem(LegionMemVO memVO);

	public void updateLegion(LegionVO legionVO);

	public List<LegionVO> getLegions();

	public List<LegionMemVO> getMems(int id);

	public List<LegionLogVO> getLogs(int legionId);

	public void insertLegionLog(LegionLogVO legionLogVO);

	public void delLegionLog(LegionLogVO legionLogVO);

	public void updateAllLegionKiller(String killerStr);
	public void destroyLegion(int id);
}
