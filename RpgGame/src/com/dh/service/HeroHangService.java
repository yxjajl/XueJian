package com.dh.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.dh.constants.CommonConstants;
import com.dh.dao.PlayerHeroHangMapper;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.hero.HeroTiredProto.HeroRestResponse;
import com.dh.game.vo.hero.HeroTiredProto.HeroTired;
import com.dh.game.vo.user.PlayerHeroHangVO;
import com.dh.game.vo.user.PlayerHeroVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.BuildLevelRes;
import com.dh.sqlexe.SqlSaveThread;
import com.dh.util.CommandUtil;
import com.dh.util.SqlBuild;
import com.dh.vo.user.UserCached;

@Component
public class HeroHangService {
	public final static short HERO_STATUS_OVER = 2; // 冷确完成
	public final static short HERO_STATUS_ON = 1; // 挂机中
	public final static short HERO_STATUS_OFF = 0; // 空位
	public final static long HANG_CD = 120000L;

	public final static long HERO_HANG_MIN_HUNGRY = 70;// 英雄最低休息疲劳值a

	@Resource
	private PlayerHeroHangMapper playerHeroHangMapper;
	@Resource
	private SqlBuild sqlBuild;
	@Resource
	private SqlSaveThread sqlSaveThread;

	public void loadHeroHang(UserCached userCached) {
		List<PlayerHeroHangVO> heroHangList = playerHeroHangMapper.getPlayerHang(userCached.getPlayerId());
		if (heroHangList != null) {
			for (PlayerHeroHangVO playerHeroHangVO : heroHangList) {
				if (playerHeroHangVO.getHeroId() > 0) {
					PlayerHeroVO playerHeroVO = userCached.getUserHero().getPlayerHeroVOById(playerHeroHangVO.getHeroId());
					if (playerHeroVO == null) {
						playerHeroHangVO.setHeroId(0);
						playerHeroHangVO.setIsHang(HeroHangService.HERO_STATUS_OFF);
						updatePlayerHang(playerHeroHangVO);
					} else {
						playerHeroVO.setHang_status(CommonConstants.HANG_STATUS_SLEEP);
					}
				}
			}
		}
		userCached.getUserHero().setHeroHangList(heroHangList);
	}

	/**
	 * 添加挂机队列
	 * 
	 * @param userCached
	 * @param commandList
	 * @throws Exception
	 */
	public void addPlayerHeroHangVO(UserCached userCached, int freetime, List<NettyMessageVO> commandList) throws Exception {
		PlayerHeroHangVO playerHeroHangVO = new PlayerHeroHangVO();
		playerHeroHangVO.setHeroHangId(getMaxHeroHangId(userCached.getUserHero().getHeroHangList()));
		playerHeroHangVO.setPlayerId(userCached.getPlayerId());
		playerHeroHangVO.setHeroId(0);
		playerHeroHangVO.setIsHang(HeroHangService.HERO_STATUS_OFF);
		playerHeroHangVO.setBeginTime(null);
		playerHeroHangVO.setEndTime(null);

		userCached.getUserHero().getHeroHangList().add(playerHeroHangVO);
		addPlayerHang(playerHeroHangVO);

		HeroRestResponse.Builder heroRestResponse = HeroRestResponse.newBuilder();
		HeroTired.Builder builder = HeroTired.newBuilder();
		builder.setHeroHangId(playerHeroHangVO.getHeroHangId());
		builder.setHeroId(playerHeroHangVO.getHeroId());
		builder.setIsHang(playerHeroHangVO.getIsHang());
		builder.setRemaintime(0);
		builder.setOpenLevel(BuildLevelRes.getInstance().getPositionOpenLevel(playerHeroHangVO.getHeroHangId()));

		heroRestResponse.setHeroTired(builder);

		CommandUtil.packageNettyMessage(CSCommandConstant.HERO_HUNGRY_SLEEP, heroRestResponse.build().toByteArray(), commandList);

	}

	private int getMaxHeroHangId(List<PlayerHeroHangVO> heroHangList) {
		if (heroHangList == null || heroHangList.size() == 0) {
			return 1;
		}
		return heroHangList.size() + 1;

	}

	public void updatePlayerHang(PlayerHeroHangVO playerHeroHangVO) {
		sqlSaveThread.putSql(playerHeroHangVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerHeroHangMapper.updatePlayerHang", playerHeroHangVO));
	}

	public void addPlayerHang(PlayerHeroHangVO playerHeroHangVO) {
		sqlSaveThread.putSql(playerHeroHangVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerHeroHangMapper.addPlayerHang", playerHeroHangVO));
	}

}
