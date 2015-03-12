package com.dh.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.dh.Cache.ServerHandler;
import com.dh.constants.GMConstants;
import com.dh.constants.HeroConstants;
import com.dh.dao.GMMapper;
import com.dh.dao.PlayerGMMapper;
import com.dh.enums.GMIOEnum;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.constant.CSCommandConstant;
import com.dh.game.vo.base.BaseGMListVO;
import com.dh.game.vo.base.BaseHeroInfoVO;
import com.dh.game.vo.base.BaseItemVO;
import com.dh.game.vo.login.PlayerLoginProto.PLAYER_PROPERTY;
import com.dh.game.vo.message.MessageProto.ChatResponse;
import com.dh.game.vo.message.MessageProto.EChannel;
import com.dh.game.vo.user.PlayerGMVO;
import com.dh.netty.NettyMessageVO;
import com.dh.resconfig.HeroRes;
import com.dh.resconfig.ItemRes;
import com.dh.sqlexe.SqlSaveThread;
import com.dh.util.DateUtil;
import com.dh.util.GameUtil;
import com.dh.util.SqlBuild;
import com.dh.vo.gm.GMIOVO;
import com.dh.vo.gm.GMOnlineVO;
import com.dh.vo.user.UserCached;
import com.dh.vo.user.UserGM;

/**
 * @author dingqu-pc100
 * 
 */
@Service
public class GMService {
	private static final Logger LOGGER = Logger.getLogger(GMService.class);

	private final static String TITLE = "用  途\t<font color='red'>命令</font>\t<font color='blue'>内容</font>\t<font color='green'>数量</font>\n";
	private final static String GI = "获得物品\t<font color='red'>#GI</font>\t<font color='blue'>物品id</font>\t<font color='green'>物品数量</font>\n";
	private final static String GH = "获得英雄\t<font color='red'>#GH</font>\t<font color='blue'>英雄id</font>\t<font color='green'>英雄数量</font>\n";

	@Resource
	private KnapsackService knapsackService;
	@Resource
	private HeroService heroService;
	@Resource
	private PlayerAccountService accountService;
	// private static Date lastCalLoginDate;
	@Resource
	private PlayerService playerService;
	@Resource
	private PlayerGMMapper playerGMMapper;
	@Resource
	private SqlSaveThread sqlSaveThread;
	@Resource
	private SqlBuild sqlBuild;
	@Resource
	private GMMapper gmMapper;

	public void loadPlayerGM(UserCached userCached) {
		PlayerGMVO playerGM = playerGMMapper.getPlayerGM(userCached.getPlayerId());
		if (playerGM == null) {
			UserGM userGM = new UserGM();
			playerGM = new PlayerGMVO();
			playerGM.setPlayerId(userCached.getPlayerId());
			playerGM.setLoginRecord(1);
			userGM.setPlayerGMVO(playerGM);
			userCached.setUserGM(userGM);
			insertPlayerGM(playerGM);
		}
		userCached.getUserGM().setPlayerGMVO(playerGM);
	}

	private void addGMIOLog(int playerId, int usage, int io, int cost, int type, int content, int num) {
		String name = "";
		int price = 0;
		if (usage == GMIOEnum.OUT_BUY_ITEM.usage()) {// 购买物品
			BaseItemVO baseItem = ItemRes.getInstance().getBaseItemVO(content);
			if (baseItem == null) {
				name = "未知物品";
				price = 0;
			} else {
				name = baseItem.getName();
			}
		}
		GMIOVO gmioVO = new GMIOVO(playerId, io, usage, cost, type, content, num, name, price);
		sqlSaveThread.putSql(playerId,sqlBuild.getSql("com.dh.dao.GMMapper.addGMIOVO", gmioVO));
	}

	/**
	 * @param playerId
	 * @param usage
	 *            用途 {@link GMIOEnum}
	 * @param type
	 *            货币类型{@link RewardService}
	 * @param content
	 * @param num
	 *            消耗数量
	 */
	public void addGMOutputLog(int playerId, int usage, int cost, int type, int content, int num) {
		addGMIOLog(playerId, usage, 1, cost, type, content, num);
	}

	/**
	 * @param playerId
	 * @param usage
	 *            来源{@link GMIOEnum}
	 * @param type
	 *            货币类型{@link RewardService}
	 * @param content
	 * @param num
	 *            消耗数量
	 */
	public void addGMInputLog(int playerId, int usage, int cost, int type, int content, int num) {
		addGMIOLog(playerId, usage, 2, cost, type, content, num);
	}

	public void getAndUpOnline() {
		int hour = (int) (System.currentTimeMillis() / TimeUnit.HOURS.toMillis(1));
		// int newCount = gmMapper.getOnlineCount();
		int newCount = ServerHandler.getOnlineCount();
		GMOnlineVO old = new GMOnlineVO();
		old.setHour(hour);
		GMOnlineVO onlineInfo = gmMapper.getOnlineByHour(old);
		if (onlineInfo == null) {
			onlineInfo = new GMOnlineVO();
			onlineInfo.setHour(hour);
			onlineInfo.setCount(newCount);
			gmMapper.insertOnlineInfo(onlineInfo);
			// sqlSaveThread.putSql(sqlBuild.getSql("com.dh.dao.GMMapper.insertOnlineInfo",
			// onlineInfo));

		} else if (onlineInfo.getCount() < newCount) {// 需要更新
			onlineInfo.setCount(newCount);
			gmMapper.updateOnlineCount(onlineInfo);
			// sqlSaveThread.putSql(sqlBuild.getSql("com.dh.dao.GMMapper.updateOnlineCount",
			// onlineInfo));
		}
	}

	public void loadBlacks() {
		List<BaseGMListVO> blacks = gmMapper.loadBlacks();
		for (BaseGMListVO baseGMListVO : blacks) {
			GMConstants.addGMlist(baseGMListVO);
		}
	}

	public void addBlackIP(String mac, String ip, String reason, int type) {
		if (!GMConstants.isBlackIP(mac)) {
			BaseGMListVO gmListVO = GMConstants.addBlack(mac, ip, reason, type);
			sqlSaveThread.putSql(0,sqlBuild.getSql("com.dh.dao.GMMapper.inserGMListVO", gmListVO));
		}
	}

	public boolean removeBlackIP(String mac, String ip) {
		BaseGMListVO black = GMConstants.getBlackByMac(mac);
		if (black != null) {
			sqlSaveThread.putSql(0,sqlBuild.getSql("com.dh.dao.PlayerGMMapper.delGMListVO", black));
			GMConstants.removeBlack(mac);
			return true;
		}
		return false;
	}

	public void gmLogin(UserCached userCached) {
		try {
			Date regDate = userCached.getPlayerVO().getRegisterdate();
			int day = DateUtil.dateDayDifference(System.currentTimeMillis(), regDate.getTime());
			PlayerGMVO gmVO = userCached.getUserGM().getPlayerGMVO();
			if (day < 31) {
				int record = gmVO.getLoginRecord() | (0x1 << day);
				gmVO.setLoginRecord(record);
			}
			gmVO.setIp(GameUtil.getIpString(userCached.getChannel().remoteAddress().toString()));
			updatePlayerGM(gmVO);
		} catch (Exception e) {
			LOGGER.error("gm登录错误:" + e.getCause(), e);
		}
	}

	public void gmLoginOut(UserCached userCached) {
		// GMConstants.subAmount(1);
	}

	// public boolean gm(String msg, UserCached userCached, List<NettyMessageVO>
	// commandList) {
	// try {
	// String[] args = msg.split(" ");
	// switch (args[0].toLowerCase()) {
	// case "#h":
	// case "#help":
	// return showHelp(userCached);
	// case "#gi":
	// case "#获得物品":
	// return getItem(userCached, args, commandList);
	// case "#gh":
	// case "#获得英雄":
	// return getHero(userCached, args, commandList);
	// case "#gr":
	// return getRMB(userCached, args, commandList);
	// case "#gs":// 获得银币
	// return getMoney(userCached, args, commandList);
	// default:
	// break;
	// }
	// } catch (Exception e) {
	// LOGGER.error("gm工具错误" + e.getCause(), e);
	// }
	//
	// return false;
	// }

	public boolean getMoney(UserCached userCached, String[] args, List<NettyMessageVO> commandList) {
		try {
			accountService.addCurrency(PLAYER_PROPERTY.PROPERTY_MONEY, Integer.parseInt(args[1]), userCached.getPlayerAccountVO(), commandList, "gm工具增加");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean getRMB(UserCached userCached, String[] args, List<NettyMessageVO> commandList) {
		try {
			accountService.addCurrency(PLAYER_PROPERTY.PROPERTY_RMB, Integer.parseInt(args[1]), userCached.getPlayerAccountVO(), commandList, "gm工具增加");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean getHero(UserCached userCached, String[] args, List<NettyMessageVO> commandList) throws Exception {
		if (args.length < 3) {
			return showHelp(userCached);
		}
		int cfgId = Integer.parseInt(args[1]);
		int num = Integer.parseInt(args[2]);
		BaseHeroInfoVO baseHeroInfoVO = HeroRes.getInstance().getBaseHeroInfoVO(cfgId);
		if (baseHeroInfoVO == null) {
			throw new GameException(AlertEnum.HERO_NOT_FOUND);
		} else {
			if (userCached.getUserHero().getHeroList().size() + num > HeroConstants.HERO_MAX_NUM) {
				throw new GameException(AlertEnum.HERO_NUM_LIMIT);
			}
			heroService.addPlayerHero(baseHeroInfoVO, 1, userCached, false, commandList);
		}
		return true;
	}

	public boolean getItem(UserCached userCached, String[] args, List<NettyMessageVO> commandList) throws Exception {
		if (args.length < 3) {
			return showHelp(userCached);
		}
		int cfgId = Integer.parseInt(args[1]);
		int num = Integer.parseInt(args[2]);
		BaseItemVO baseItemVO = ItemRes.getInstance().getBaseItemVO(cfgId);

		// if (baseItemVO == null) {
		// commandList.add(CommonCommand.ITEM_NOT_EXIST);
		// return false;
		// }
		// if (!knapsackService.hasKnapsackGird(userCached, num,
		// baseItemVO.getMaxnum())) {
		// commandList.add(CommonCommand.KNAPSACK_FULL);
		// return false;
		// }
		knapsackService.addNewItem(userCached, baseItemVO, num, commandList);
		return true;

	}

	private boolean showHelp(UserCached userCached) {
		StringBuilder sb = new StringBuilder("GM范例:#gi 10019 1\n");
		sb.append(TITLE);
		sb.append(GI);
		sb.append(GH);
		sendToPlayer(sb.toString(), userCached);
		return true;
	}

	public void updatePlayerGM(PlayerGMVO playerGMVO) {
		// playerGMMapper.updatePlayerGM(playerGMVO);
		sqlSaveThread.putSql(playerGMVO.getPlayerId(),sqlBuild.getSql("com.dh.dao.PlayerGMMapper.updatePlayerGM", playerGMVO));
	}

	public void insertPlayerGM(PlayerGMVO playerGMVO) {
		playerGMMapper.insertGM(playerGMVO);
		// sqlSaveThread.putSql(sqlBuild.getSql("com.dh.dao.PlayerGMMapper.insertGM",
		// playerGMVO));
	}

	private static void sendToPlayer(String msg, UserCached userCached) {
		NettyMessageVO nettyMessageVO = new NettyMessageVO();
		nettyMessageVO.setCommandCode(CSCommandConstant.CMD_CHAT);
		ChatResponse.Builder resp = ChatResponse.newBuilder().setEChannel(EChannel.CHAT_WORLD).setMsg(msg).setName(userCached.getPlayerVO().getName());
		nettyMessageVO.setData(resp.build().toByteArray());
		ServerHandler.sendMessageToPlayer(nettyMessageVO, userCached.getChannel());
	}

}
