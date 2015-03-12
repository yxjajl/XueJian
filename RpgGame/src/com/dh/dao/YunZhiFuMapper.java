package com.dh.dao;

import java.util.List;

import com.dh.game.vo.user.RechargeInfoVO;

public interface YunZhiFuMapper {
	public RechargeInfoVO getRechargeInfo(String token);

	public void insertRechargeInfo(RechargeInfoVO rechargeInfoVO);

	public void updateRechargeInfo(RechargeInfoVO rechargeInfoVO);

	public List<RechargeInfoVO> getTop20Recharge(int serverId);
}
