package com.dh.dao;

import java.util.List;

import com.dh.game.vo.base.BaseGMListVO;
import com.dh.vo.gm.GMIOVO;
import com.dh.vo.gm.GMOnlineVO;

public interface GMMapper {

	public void inserGMListVO(BaseGMListVO baseGMListVO);

	public void delGMListVO(BaseGMListVO baseGMListVO);

	public List<BaseGMListVO> loadBlacks();

	public int getOnlineCount();

	public GMOnlineVO getOnlineByHour(GMOnlineVO old);

	public void updateOnlineCount(GMOnlineVO onlineInfo);

	public void insertOnlineInfo(GMOnlineVO onlineInfo);

	public void addGMIOVO(GMIOVO gmioVO);
	
}
