package com.dh.dao;

import java.util.List;

import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.game.vo.user.PlayerKnapsackVO;

public interface PlayerKnapsackMapper {
	/**
	 * 背包列表
	 * 
	 * @param playerid
	 * @return
	 */
	public List<PlayerKnapsackVO> getPlayerKnapsackList(int playerid);

	/**
	 * 英雄身上的装备
	 * 
	 * @param playerid
	 * @return
	 */
	public List<PlayerKnapsackVO> getHeroEquipList(int playerid, int heroId);

	/**
	 * 更新物品信息
	 * 
	 * @param playerKnapsackVO
	 */
	public void updatePlayerKnapsackVO(PlayerKnapsackVO playerKnapsackVO);

	/**
	 * 增加物品
	 * 
	 * @param playerKnapsackVO
	 */
	public void insertPlayerKnapsackVO(PlayerKnapsackVO playerKnapsackVO);

	/**
	 * 清除物品
	 * 
	 * @param playerKnapsackVO
	 */
	public void delPlayerKnapsackVO(PlayerKnapsackVO playerKnapsackVO);

	public List<PlayerKnapsackVO> getKnapsByHeroVO(PlayerHeroVO playerHeroVO);



}
