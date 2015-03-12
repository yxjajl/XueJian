package com.dh.dao;

import java.util.List;

import com.dh.game.vo.user.StreetBoxVO;
import com.dh.game.vo.user.StreetDefendLogVO;
import com.dh.game.vo.user.StreetEnemyVO;
import com.dh.game.vo.user.StreetMonsterVO;
import com.dh.game.vo.user.StreetResVO;
import com.dh.vo.user.UserStreet;

public interface StreetMapper {
	public UserStreet getUserStreet(int playerId);

	public void insertStreetRes(StreetResVO streetResVO);

	public List<StreetResVO> getUserStreetRes(int playerId);

	public void updateStreetRes(StreetResVO streetResVO);

	public void insertStreetBox(StreetBoxVO streetBoxVO);

	public void insertStreetMonster(StreetMonsterVO streetMonsterVO);

	public void delStreetBox(StreetBoxVO streetBoxVO);

	public void delStreetMonster(StreetMonsterVO streetMonsterVO);

	public void updatePlayerStreet(UserStreet userStreet);

	public void insertPlayerStreet(UserStreet userStreet);

	// #情报
	public void insertStreetDefendLog(StreetDefendLogVO streetDefendLogVO);

	public void deleteStreetDefendLog(StreetDefendLogVO streetDefendLogVO);

	// 更新江湖防御日志
	public void updateStreetDefLog(StreetDefendLogVO streetDefendLogVO);

	public List<StreetDefendLogVO> getDefendLogs(int playerId);

	public List<StreetBoxVO> getStreetBox(int playerId);

	public List<StreetMonsterVO> getStreetMonster(int playerId);

	// #仇家
	public void insertEnemy(StreetEnemyVO streetEnemyVO);

	public void deleteEnemy(StreetEnemyVO streetEnemyVO);

	public void updateEnemy(StreetEnemyVO streetEnemyVO);

	public List<StreetEnemyVO> getEnemies(int playerId);

	public List<StreetResVO> getAllRes();

}
