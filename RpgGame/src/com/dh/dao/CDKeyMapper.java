package com.dh.dao;

import com.dh.game.vo.base.BaseCdKeyVO;

public interface CDKeyMapper {
	public void insertCdKey(BaseCdKeyVO baseCdKeyVO);

	public void updateBaseCdKey(BaseCdKeyVO baseCdKeyVO);

	public BaseCdKeyVO getBaseCdKey(String keyId);
}
