package com.dh.processor;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.constants.TXConstants;
import com.dh.dao.CDKeyMapper;
import com.dh.enums.GMIOEnum;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.vo.activity.ActivityProto.UseCDKeyRequest;
import com.dh.game.vo.base.BaseCdKeyVO;
import com.dh.game.vo.base.Reward;
import com.dh.game.vo.user.PlayerTimerVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.CdKeyRes;
import com.dh.service.PlayerTimerService;
import com.dh.service.RewardService;
import com.dh.sqlexe.SqlSaveThread;
import com.dh.util.SqlBuild;
import com.dh.vo.user.UserCached;

@Component
public class CdKeyProcessor {
	private static Logger logger = Logger.getLogger(CdKeyProcessor.class);
	@Resource
	private CDKeyMapper CDKeyMapper;
	@Resource
	private SqlBuild sqlBuild;
	@Resource
	private SqlSaveThread sqlSaveThread;
	@Resource
	private PlayerTimerService playerTimerService;
	@Resource
	private RewardService rewardService;

	public void useCdkey(UseCDKeyRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		String key = request.getCdkey();

		if (key == null || key.length() != 30) {
			throw new GameException(AlertEnum.INVALIDE_CDKEY);
		}
		BaseCdKeyVO baseCdKeyVO = CDKeyMapper.getBaseCdKey(key);
		if (baseCdKeyVO == null || baseCdKeyVO.getState() > 0) {
			throw new GameException(AlertEnum.INVALIDE_CDKEY);
		}

		PlayerTimerVO playerTimerVO = userCached.getUserTimer().getPlayerTimerVO();
		int cdkeyField = playerTimerVO.getCdkeyField();
		if (((cdkeyField >> baseCdKeyVO.getType()) & 1) == 1) {
			throw new GameException(AlertEnum.PASSED_CDKEY);
		}

		List<Reward> rewardList = CdKeyRes.getInstance().getItemCfgIdByType(baseCdKeyVO.getType());
		if (rewardList != null && rewardList.size() > 0) {
			rewardService.checkAndReward(userCached, rewardList, commandList, GMIOEnum.IN_CD_KEY_PACK.usage());
		} else {
			logger.error("没有此类型的奖励" + baseCdKeyVO.getType());
			throw new Exception("没有此类型的奖励" + baseCdKeyVO.getType());
		}

		cdkeyField = cdkeyField | (1 << baseCdKeyVO.getType());
		playerTimerVO.setCdkeyField(cdkeyField);
		playerTimerService.updateCDKEY(playerTimerVO);
		// reward
		baseCdKeyVO.setState(1);
		baseCdKeyVO.setHistory(TXConstants.my_server_id + "_" + playerId);
		CDKeyMapper.updateBaseCdKey(baseCdKeyVO);
		commandList.add(nettyMessageVO);
	}

	public void updateBaseCdKey(BaseCdKeyVO baseCdKeyVO) throws Exception {
		sqlSaveThread.putSql(0,sqlBuild.getSql("com.dh.dao.CDKeyMapper.updateBaseCdKey", baseCdKeyVO));
	}

}
