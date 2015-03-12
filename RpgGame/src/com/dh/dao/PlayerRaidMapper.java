package com.dh.dao;

import java.util.List;

import com.dh.game.vo.user.PlayerEliteRaidVO;
import com.dh.game.vo.user.PlayerRaidVO;

public interface PlayerRaidMapper {
	public void addRaid(PlayerRaidVO playerRaidVO); // 添加副本

	public List<PlayerRaidVO> getRaidList(int playerId);// 取副本列表

	public void updateRaid(PlayerRaidVO playerRaidVO);// 修改状态和积分

	public List<PlayerEliteRaidVO> getEliteRaidList(int playerId); // 精英副本

	public void addEliteRaid(PlayerEliteRaidVO playerEliteRaidVO);// 添加精英副本

	public void updateEliteRaid(PlayerEliteRaidVO playerEliteRaidVO); // 修改

}
