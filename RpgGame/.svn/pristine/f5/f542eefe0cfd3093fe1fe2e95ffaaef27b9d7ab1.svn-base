package com.dh.processor;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dh.Cache.ServerHandler;
import com.dh.constants.MailConstants;
import com.dh.enums.GMIOEnum;
import com.dh.exception.GameException;
import com.dh.game.constant.AlertEnum;
import com.dh.game.vo.base.Reward;
import com.dh.game.vo.message.MessageProto.MailDelRequest;
import com.dh.game.vo.message.MessageProto.MailDelResponse;
import com.dh.game.vo.message.MessageProto.MailListRequest;
import com.dh.game.vo.message.MessageProto.MailListResponse;
import com.dh.game.vo.message.MessageProto.MailListResponse.Builder;
import com.dh.game.vo.message.MessageProto.RewardedRequest;
import com.dh.game.vo.message.MessageProto.RewardedResponse;
import com.dh.game.vo.message.MessageProto.eMailError;
import com.dh.game.vo.user.MailVO;
import com.dh.netty.NettyMessageVO;
import com.dh.service.MailService;
import com.dh.service.RewardService;
import com.dh.util.VOUtil;
import com.dh.vo.user.UserCached;

@Component
public class MailProcessor {
	@Resource
	private MailService mailService;
	@Resource
	private RewardService rewardService;
	private static Logger logger = Logger.getLogger(MailProcessor.class);

	/**
	 * 获取指定页码邮件
	 * 
	 * @date 2014年3月13日
	 */
	public void getMailList(MailListRequest req, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		Builder res = MailListResponse.newBuilder();

		List<MailVO> mailList = null;
		int curPage = Math.abs(req.getPageCount()) + 1; // 请求的页码
		mailService.reLoadMail(userCached);

		int mailSize = userCached.getUserMail().getMailList().size();
		int pageCount = 1;
		if (mailSize <= MailConstants.PAGE_MAILS) {
			res.setPageCount(0);
			mailList = userCached.getUserMail().getMailList();
		} else {
			pageCount = mailSize / MailConstants.PAGE_MAILS; // 总页数
			if (mailSize % MailConstants.PAGE_MAILS > 0) {
				pageCount++;
			}

			System.out.println("总页数 = pageCount " + pageCount);
			curPage = Math.min(curPage, pageCount);
			System.out.println("当前页 = curPage " + curPage);

			int start = (curPage - 1) * MailConstants.PAGE_MAILS;
			int end = Math.min(curPage * MailConstants.PAGE_MAILS, mailSize);
			mailList = userCached.getUserMail().getMailList().subList(start, end);
			res.setPageCount(curPage - 1);
		}
		res.setTotalPage(pageCount);
		for (MailVO mailVO : mailList) {
			res.addMailList(VOUtil.getMailInfo(mailVO));
		}
		NettyMessageVO resMsg = new NettyMessageVO();
		resMsg.setData(res.build().toByteArray());
		resMsg.setCommandCode(nettyMessageVO.getCommandCode());
		commandList.add(resMsg);
	}

	/**
	 * 删除指定邮件组<br/>
	 * 只统一告知是否成功,如果遇到失败,前台请再请求一下当前所有
	 * 
	 * @date 2014年3月13日
	 */
	public void delMail(MailDelRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		List<MailVO> mailList = mailService.getMailList(userCached);
		NettyMessageVO resMsg = new NettyMessageVO();
		MailDelResponse.Builder res = MailDelResponse.newBuilder();
		res.setErrorCode(eMailError.MAIL_FAIL);
		int size = mailList.size();
		if (hasAttachment(mailList, request.getIdsList())) {
			throw new GameException(AlertEnum.MAIL_ATTACHMENT_CANT_DEL);
		}
		for (Integer mailId : request.getIdsList()) {
			for (int i = mailList.size() - 1; i >= 0; i--) {
				if (mailList.get(i).getId() == mailId) {
					mailService.delMail(mailList.get(i));
					res.setErrorCode(eMailError.MAIL_OK);
					mailList.remove(i);
					break;
				}
			}
		}
		List<MailVO> appendMailList = appendMail(request.getPageCount() + 1, mailList, request.getIdsCount());
		if (appendMailList != null && appendMailList.size() > 0) {
			for (MailVO mailVO : appendMailList) {
				res.addMailInfo(VOUtil.getMailInfo(mailVO));
			}
		}
		int totalPage = size % MailConstants.PAGE_MAILS == 0 ? size / MailConstants.PAGE_MAILS : size / MailConstants.PAGE_MAILS + 1;
		res.setTotalPage(totalPage);
		res.setPageCount(request.getPageCount() * MailConstants.PAGE_MAILS == size ? Math.max(totalPage - 1, 0) : request.getPageCount());
		resMsg.setData(res.build().toByteArray());
		resMsg.setCommandCode(nettyMessageVO.getCommandCode());
		commandList.add(resMsg);
	}

	public static boolean hasAttachment(List<MailVO> mails, List<Integer> ids) {
		for (Integer integer : ids) {
			for (MailVO mailVO : mails) {
				if (mailVO.getId() == integer && !mailVO.getRewards().isEmpty() && mailVO.getIsReward() == 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 标记为已读
	 * 
	 * @date 2014年3月13日
	 */
	public void mailRead(int id, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		List<MailVO> mailList = mailService.getMailList(userCached);
		for (MailVO mailVO : mailList) {
			if (mailVO.getId() == id) {
				mailVO.setIsRead(1);
				mailService.updateMailAttr(1, mailVO);
				break;
			}
		}
	}

	/**
	 * 领取奖励
	 * 
	 * @date 2014年3月13日
	 */
	public void mailRewarded(RewardedRequest request, NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		List<MailVO> mailList = mailService.getMailList(userCached);

		RewardedResponse.Builder resp = RewardedResponse.newBuilder();
		boolean unFull = false;
		List<Integer> ids = request.getIdList();
		for (Integer id : ids) {
			for (int i = mailList.size() - 1; i >= 0; i--) {
				MailVO mailVO = mailList.get(i);
				if (mailVO.getId() == id) {
					if (mailVO.getIsReward() != 1 && mailVO.getRewards() != null && !mailVO.getRewards().isEmpty()) {
						List<Reward> rewards = MailService.decodeRewardString(mailVO.getRewards());
						int usage;
						if (mailVO.getSenderName().equals(MailConstants.MAIL_SENDER_KNAP_FULL)) {
							usage = GMIOEnum.IN_MAIL_KNAPFULL.usage();
						} else if (mailVO.getSenderName().equals(MailConstants.MAIL_SENDER_GM)) {
							usage = GMIOEnum.IN_MAIL_GM.usage();
						} else {
							usage = GMIOEnum.IN_MAIL_OTHER.usage();
						}
						unFull = rewardService.reward(userCached, rewards, commandList, usage);
						if (unFull) {
							mailVO.setIsReward(1);
							mailVO.setIsRead(1);
							mailVO.setLifeTime((int) (MailConstants.M_PT_READED_LIFETIME / 1000));
							mailVO.setRewardsProto(null);
							mailService.updateMail(mailVO);
						}
					}
					resp.addMailInfo(VOUtil.getMailInfo(mailVO));

				}
			}
		}
		nettyMessageVO.setData(resp.build().toByteArray());
		commandList.add(nettyMessageVO);
	}

	/**
	 * 从nextPage页取Size条数据
	 * 
	 * @param nextPage
	 * @param mailList
	 * @param size
	 * @return
	 */
	public static List<MailVO> appendMail(int nextPage, List<MailVO> mailList, int number) {
		nextPage++;
		if (mailList.size() == 0 || number == 0 || nextPage < 0) {
			return null;
		}
		int mailSize = mailList.size();
		int pageCount = mailSize / MailConstants.PAGE_MAILS; // 总页数
		if (mailSize % MailConstants.PAGE_MAILS > 0) {
			pageCount++;
		}
		if (pageCount < nextPage) {
			return null;
		}

		int start = (nextPage - 1) * MailConstants.PAGE_MAILS;
		int end = Math.min(mailSize, start + number); // 最后一条数据

		return mailList.subList(start, end);
	}

	/**
	 * 根据id取页码
	 * 
	 * @param id
	 * @param mailList
	 * @return
	 */
	public static int getMailPageById(int num, List<MailVO> mailList) {
		int page = -1;
		if (num > -1) {
			page = num / MailConstants.PAGE_MAILS;
			if (num % MailConstants.PAGE_MAILS > 0) {
				page++;
			}
			page--; // 页码从0开始
		}

		return page;
	}

	public void rewardAll(NettyMessageVO nettyMessageVO, List<NettyMessageVO> commandList) throws Exception {
		int playerId = ServerHandler.get(nettyMessageVO.getChannel());
		UserCached userCached = ServerHandler.getUserCached(playerId);
		List<MailVO> mailList = mailService.getMailList(userCached);
		for (MailVO mailVO : mailList) {
			if (mailVO.getIsReward() == 0 && mailVO.getRewards() != null && !mailVO.getRewards().isEmpty()) {// 未领奖,奖励不为空,
				List<Reward> rewards = MailService.decodeRewardString(mailVO.getRewards());
				if (rewards != null && !rewards.isEmpty()) {
					int usage;
					if (mailVO.getSenderName().equals(MailConstants.MAIL_SENDER_KNAP_FULL)) {
						usage = GMIOEnum.IN_MAIL_KNAPFULL.usage();
					} else if (mailVO.getSenderName().equals(MailConstants.MAIL_SENDER_GM)) {
						usage = GMIOEnum.IN_MAIL_GM.usage();
					} else {
						usage = GMIOEnum.IN_MAIL_OTHER.usage();
					}
					boolean isSucc = rewardService.reward(userCached, rewards, commandList, usage);
					if (isSucc) {
						mailVO.setIsReward(1);
						mailService.updateMailAttr(2, mailVO);
						MailService.decodeRewardString2proto(mailVO);
					} else {
						break;
					}

				}
			}
		}
		// 模拟请求
		MailListRequest req = MailListRequest.newBuilder().setPageCount(0).build();
		getMailList(req, nettyMessageVO, commandList);

	}
}
