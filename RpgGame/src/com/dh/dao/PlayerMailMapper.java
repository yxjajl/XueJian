package com.dh.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import com.dh.game.vo.user.MailVO;

public interface PlayerMailMapper {
	public List<MailVO> getMailList(int playerId);

	public List<MailVO> reloadMails(MailVO mailVO);

	public MailVO getMailById(MailVO mailVO);

	public void insertMail(MailVO mailVO);

	public void updateMail(MailVO mailVO);

	public void markMailRead(MailVO mailVO);

	public void markMailReward(MailVO mailVO);

	public void markMailValid(MailVO mailVO);

	public void delMail(MailVO mailVO);

	public int getMaxMailId(int playerId);

	public void delInvalidMail(int lifeDay);

	public List<HashMap<Integer, Integer>> getMaxMailIds();
}
