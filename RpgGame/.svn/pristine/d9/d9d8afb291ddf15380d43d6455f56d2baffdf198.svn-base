package com.dh.service;

import org.springframework.stereotype.Component;

@Component
public class SoldierService {

//	@Resource
//	private PlayerSoldierMapper playerSoldierMapper;
//	@Resource
//	private SqlBuild sqlBuild;
//	@Resource
//	private SqlSaveThread sqlSaveThread;
//
//	/**
//	 * 加载士兵
//	 * 
//	 * @param userCached
//	 * @throws Exception
//	 */
//	public void loadSoldierList(UserCached userCached) throws Exception {
//		List<PlayerSoldierVO> soldierlist = playerSoldierMapper.getSoldierList(userCached.getPlayerId());
//		if (soldierlist != null) {
//			for (PlayerSoldierVO playerSoldierVO : soldierlist) {
//				BaseSoldierInfoVO baseSoldierInfoVO = SoldierRes.getInstance().getSoldierByCfgIDAndLevel(playerSoldierVO.getCfgId(), playerSoldierVO.getLevel());
//				playerSoldierVO.setBaseSoldierInfoVO(baseSoldierInfoVO);
//				playerSoldierVO.setLockStatus(1);
//				// 计算终极属性
//				CombatUtil.soldierCombat(playerSoldierVO, userCached);
//			}
//			userCached.setSoldierList(soldierlist);
//		} else {
//			userCached.setSoldierList(new ArrayList<PlayerSoldierVO>());
//		}
//
//		for (BaseSoldierInfoVO baseSoldierInfoVO : SoldierRes.getInstance().getLevel1SoldierList()) {
//			PlayerSoldierVO playerSoldierVO = userCached.getPlayerSoldierVOByCfgId(baseSoldierInfoVO.getCfgId());
//			if (playerSoldierVO == null) {
//				playerSoldierVO = new PlayerSoldierVO();
//				playerSoldierVO.setBaseSoldierInfoVO(baseSoldierInfoVO);
//				playerSoldierVO.setPlayerId(userCached.getPlayerId());
//				playerSoldierVO.setCfgId(baseSoldierInfoVO.getCfgId());
//				playerSoldierVO.setLevel(1);
//				playerSoldierVO.setLockStatus(0);
//				userCached.getSoldierList().add(playerSoldierVO);
//
//				// 计算终极属性
//				CombatUtil.soldierCombat(playerSoldierVO, userCached);
//			}
//		}
//	}
//
//	public void loadSoldierTeam(UserCached userCached) throws Exception {
//		PlayerSoldierTeamVO playerSoldierTeamVO = playerSoldierMapper.getSoldierTeam(userCached.getPlayerId());
//		userCached.setPlayerSoldierTeamVO(playerSoldierTeamVO);
//		playerSoldierTeamVO.setSoldierLength(playerSoldierTeamVO.getSoldierline().split(",").length);
//	}
//
//	/**
//	 * 士兵升级
//	 * 
//	 * @param playerSoldierVO
//	 * @throws Exception
//	 */
//	public void updateSoldier(PlayerSoldierVO playerSoldierVO) throws Exception {
//		sqlSaveThread.putSql(sqlBuild.getSql("com.dh.dao.PlayerSoldierMapper.updateSoldier", playerSoldierVO));
//	}
//
//	/**
//	 * 修改出阵士兵数据
//	 * 
//	 * @param playerSoldierTeamVO
//	 * @throws Exception
//	 */
//	public void updateSoldierTeam(PlayerSoldierTeamVO playerSoldierTeamVO) throws Exception {
//		// playerSoldierMapper.updateSoldierTeam(playerSoldierTeamVO);
//		sqlSaveThread.putSql(sqlBuild.getSql("com.dh.dao.PlayerSoldierMapper.updateSoldierTeam", playerSoldierTeamVO));
//	}
//
//	/**
//	 * 修改副将数据
//	 * 
//	 * @param playerSoldierTeamVO
//	 * @throws Exception
//	 */
//	public void updateAssistor(PlayerSoldierTeamVO playerSoldierTeamVO) throws Exception {
//		// playerSoldierMapper.updateSoldierTeam(playerSoldierTeamVO);
//		sqlSaveThread.putSql(sqlBuild.getSql("com.dh.dao.PlayerSoldierMapper.updateAssistor", playerSoldierTeamVO));
//	}
//
//	/**
//	 * 增加士兵,兵种不重复,push更新
//	 */
//	public void addSoldier(UserCached userCached, int cfgId, List<NettyMessageVO> commandList) {
//		BaseSoldierInfoVO baseSoldier = SoldierRes.getInstance().getSoldierByCfgID(cfgId);
//		if (baseSoldier != null) {
//			PlayerSoldierVO soldierVO = userCached.getPlayerSoldierVOByCfgId(cfgId);
//			// soldierVO.setLevel(1);
//			soldierVO.setLockStatus(1);
//			// soldierVO.setPlayerId(userCached.getPlayerId());
//			// soldierVO.setBaseSoldierInfoVO(baseSoldier);
//			CombatUtil.soldierCombat(soldierVO, userCached);
//			sqlSaveThread.putSql(sqlBuild.getSql("com.dh.dao.PlayerSoldierMapper.addSoldier", soldierVO));
//			commandList.add(CommandUtil.packageSoldierInfo(soldierVO, 1));
//		}
//	}
}
