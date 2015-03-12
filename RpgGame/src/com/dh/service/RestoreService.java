package com.dh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dh.Cache.JedisTool;
import com.dh.dao.BaseInfoMapper;
import com.dh.game.constant.RedisKey;
import com.dh.game.vo.base.BaseSystemIDVO;

/**
 * 数据库数据恢复
 * 
 * @author dingqu-pc100
 * 
 */
@Service
public class RestoreService {
	@Resource
	private BaseInfoMapper baseInfoMapper;
	private static Map<String, BaseSystemIDVO> SYSTEM_IDS_MAP = new HashMap<String, BaseSystemIDVO>();
	public final static String LEGION_KEY="LEGIONID";
	
	
	/**
	 * 将数据库数据加载到redis
	 */
	public void loadBaseDataFromDB() {
		int maxPlayerId = baseInfoMapper.getMaxPlayerId();
		JedisTool.set(RedisKey.MAX_PLAYER_ID, maxPlayerId + "");
		// 加载自增长起始id
		List<BaseSystemIDVO> ids = baseInfoMapper.getBaseSystemIds();
		for (BaseSystemIDVO baseSystemIDVO : ids) {
			SYSTEM_IDS_MAP.put(baseSystemIDVO.getSkey(), baseSystemIDVO);
		}
	}

	public int getStartIdByKey(String skey) {
		BaseSystemIDVO baseSystemIDVO = SYSTEM_IDS_MAP.get(skey);
		if (baseSystemIDVO != null) {
			return baseSystemIDVO.getGid();
		}
		return 0;
	}

}
