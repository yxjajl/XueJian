package com.dh.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dh.sqlexe.SqlSaveThread;
import com.dh.util.SqlBuild;

/**
 * 英雄装备操作
 * 
 * @author Administrator
 * 
 */
@Service
public class HeroEquipService {

	@Resource
	private SqlBuild sqlBuild;
	@Resource
	private SqlSaveThread sqlSaveThread;

	// /**
	// * 根据类型找装备
	// *
	// * @param playerHeroVO
	// * @param type
	// * @return
	// * @throws Exception
	// */
	// public PlayerKnapsackVO findplayerKnapsackByType(PlayerHeroVO playerHeroVO, int type) throws Exception {
	// List<PlayerKnapsackVO> list = playerHeroVO.getEquipList();
	// for (PlayerKnapsackVO playerKnapsackVO : list) {
	// if (playerKnapsackVO.getType() == type) {
	// return playerKnapsackVO;
	// }
	// }
	// return null;
	// }
	//
	// /**
	// * 找装备 通过id
	// *
	// * @param playerHeroVO
	// * @param itemId
	// * @return
	// * @throws Exception
	// */
	// public PlayerKnapsackVO findplayerKnapsackByItemId(PlayerHeroVO playerHeroVO, int itemId) throws Exception {
	// List<PlayerKnapsackVO> list = playerHeroVO.getEquipList();
	// for (PlayerKnapsackVO playerKnapsackVO : list) {
	// if (playerKnapsackVO.getItemid() == itemId) {
	// return playerKnapsackVO;
	// }
	// }
	// return null;
	// }
	//
	// /**
	// * 移除英雄装备
	// *
	// * @param playerHeroVO
	// * @param playerKnapsackVO
	// * @param commandList
	// * @throws Exception
	// */
	// public void deletePlayerKnapsackVO(PlayerHeroVO playerHeroVO, PlayerKnapsackVO playerKnapsackVO, List<NettyMessageVO> commandList) throws Exception {
	// sqlSaveThread.putSql(sqlBuild.getSql("com.dh.dao.PlayerKnapsackMapper.delPlayerEquip", playerKnapsackVO));
	//
	// // commandList.add(packageNewItemInfo(playerKnapsackVO, playerHeroVO.getHeroid(), KnapsackService.OPERATOR_TYPE_DEL));
	// }
	//
	// /**
	// * 添加英雄装备
	// *
	// * @param playerHeroVO
	// * @param playerKnapsackVO
	// * @param commandList
	// * @throws Exception
	// */
	// public void addPlayerKnapsackVO(PlayerHeroVO playerHeroVO, PlayerKnapsackVO playerKnapsackVO, List<NettyMessageVO> commandList) throws Exception {
	// playerKnapsackVO.setStatus(KnapsackService.STATIS_EQUIP);// 装备在身上
	// playerHeroVO.getEquipList().add(playerKnapsackVO);
	// sqlSaveThread.putSql(sqlBuild.getSql("com.dh.dao.PlayerKnapsackMapper.insertPlayerEquip", playerKnapsackVO));
	//
	// // commandList.add(packageNewItemInfo(playerKnapsackVO, playerHeroVO.getHeroid(), KnapsackService.OPERATOR_TYPE_NEW));
	// }

	// public NettyMessageVO packageNewItemInfo(PlayerKnapsackVO playerKnapsackVO, int heroId, int mode) {
	// ItemInfoUpdateResponse.Builder builder = ItemInfoUpdateResponse.newBuilder();
	// builder.setStatus(mode);
	// builder.setHeroId(heroId);
	// builder.addItems(VOUtil.getItemInfo(playerKnapsackVO));
	//
	// NettyMessageVO nettyMessageVO = new NettyMessageVO();
	// nettyMessageVO.setCommandCode(CSCommandConstant.UPDATE_ITEMINFO_DW);
	// nettyMessageVO.setData(builder.build().toByteArray());
	//
	// return nettyMessageVO;
	// }
}