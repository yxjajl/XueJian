package com.dh.rmi;

import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.TaskMarketV3;
import com.dh.Cache.ServerHandler;
import com.dh.constants.TXConstants;
import com.dh.game.vo.user.PlayerVO;
import com.dh.processor.RechargeProcessor;
import com.dh.service.MailService;
import com.dh.service.PlayerService;
import com.dh.sync.LockConstant;
import com.dh.sync.SyncLock;
import com.dh.sync.SyncUtil;
import com.dh.vo.user.UserCached;

@Component
public class TxRecharge implements TxRechargeInterface {
	private static Logger logger = Logger.getLogger(TxRecharge.class);
	@Resource
	private RechargeProcessor rechargeProcessor;
	@Resource
	private MailService mailService;
	@Resource
	private PlayerService playerService;

	public String test(String msg) {
		String str = ("hello " + msg + ":" + TXConstants.my_server_id);
		System.err.println(str);
		logger.error(str);
		return str;
	}

	@Override
	public boolean congzi(int playerId, int goldIngot) {
		System.err.println("congzi:" + playerId + "," + goldIngot);
		logger.error("congzi" + playerId + "," + goldIngot);
		boolean success = true;
		try {
			rechargeProcessor.recharge(playerId, goldIngot);
		} catch (Exception e) {
			logger.error("TxRecharge: " + e.getMessage(), e);
			success = false;
		}
		return success;
	}

	@Override
	public boolean congzi(String account, int goldIngot) {
		logger.error("congzi account =" + account + "," + goldIngot);
		boolean success = true;
		try {
			PlayerVO playerVO = PlayerService.getPlayerVOByAccount(account);
			rechargeProcessor.recharge(playerVO.getPlayerId(), goldIngot);
		} catch (Exception e) {
			logger.error("TxRecharge: " + e.getMessage(), e);
			success = false;
		}
		return success;
	}

	public String taskProcess(HashMap<String, String> map) {

		String cmd = map.get("cmd");
		String sstep = map.get("step");
		String account = map.get("openid");
		String sItemId = map.get("payitem");

		System.err.println("taskProcess:" + cmd + "," + sstep + "," + account + "," + sItemId);
		logger.error("taskProcess:" + cmd + "," + sstep + "," + account + "," + sItemId);
		int step = Integer.valueOf(sstep);
		int itemCfgId = 0;
		if (sItemId != null && sItemId.trim().length() > 0) {
			itemCfgId = Integer.valueOf(sItemId);
		}

		int ret = TaskMarketV3.reg_succ;
		String msg = "OK";
		String zoneid = TXConstants.tx_zone_id;

		PlayerVO playerVO = null;

		try {
			playerVO = PlayerService.getPlayerVOByAccount(account);
		} catch (Exception e) {
			logger.error("redis get playervo error", e);
			ret = TaskMarketV3.reg_reward_fail;
			msg = "奖励发放失败";
			String str = "{\"ret\":" + ret + ",\"msg\":\"" + msg + "\",\"zoneid\":\"" + zoneid + "\"}";
			return str;
		}

		if (playerVO != null) {
			if (playerVO.getHonor() >= step) {
				ret = TaskMarketV3.reg_sended;
				msg = "该步骤奖励已发放过";
			} else {
				if ("award".equals(cmd)) { // 平台通知开发者直接给给用户发货，开发者返回发货是否成功
					if (step == 1 || step == 4) {// 只有第1和第4步才能直接发奖励
						if (!taskMarketSend(playerVO, step, itemCfgId)) {
							ret = TaskMarketV3.reg_reward_fail;
							msg = "奖励发放失败";
						}
					}
				} else if ("check_award".equals(cmd)) { // 开发者需要查询任务步骤是否完成，若步骤已完成，直接给用户发货
					if (step == 2) {
						if (playerVO.getLevel() >= 30) {
							if (!taskMarketSend(playerVO, step, itemCfgId)) {
								ret = TaskMarketV3.reg_reward_fail;
								msg = "奖励发放失败";
							}
						} else {
							ret = TaskMarketV3.reg_nofinish;
							msg = "用户尚未完成本步骤";
						}
					}
				} else if ("check".equals(cmd)) { // 开发者仅需要查询任务步骤是否完成，返回步骤完成状态
					if (step == 3) { // 第三步是发送　平台　礼金，只需check
						if (playerVO.getLevel() < 40) {
							ret = TaskMarketV3.reg_nofinish;
							msg = "用户尚未完成本步骤";
						}
					} else if (step == 2) {
						if (playerVO.getLevel() >= 30) {
						} else {
							ret = TaskMarketV3.reg_nofinish;
							msg = "用户尚未完成本步骤";
						}
					}
				}

				if (ret == TaskMarketV3.reg_succ) {
					// 更新step
					SyncLock syncLock = SyncUtil.getInstance().getLock(LockConstant.LOCK_PLAYERVO + playerVO.getPlayerId());
					synchronized (syncLock) {

						UserCached userCached = ServerHandler.getUserCached2(playerVO.getPlayerId());
						if (userCached != null) {
							playerVO = userCached.getPlayerVO();
						}

						playerVO.setHonor(step);
						playerService.updatePlayerVO(playerVO);
					}
				}

			}
		} else {
			ret = TaskMarketV3.reg_nouser;
			msg = "用户尚未在应用内创建角色";
		}

		String str = "{\"ret\":" + ret + ",\"msg\":\"" + msg + "\",\"zoneid\":\"" + zoneid + "\"}";
		return str;
	}

	public boolean taskMarketSend(PlayerVO playerVO, int step, int itemCfgId) {
		if (playerVO == null) {
			return false;
		}
		String title = "";
		switch (step) {
		case 1:
			title = "血剑新秀礼包";
			break;
		case 2:
			title = "血剑大侠礼包";
			break;
		case 3:
			break;
		case 4:
			title = "血剑豪杰礼包";
			break;
		}

		try {
			mailService.addNewMailTaskMarket(playerVO.getPlayerId(), title, itemCfgId, 1);
		} catch (Exception e) {
			logger.error("taskMarketSend error ", e);
			return false;
		}

		return true;
	}

}
