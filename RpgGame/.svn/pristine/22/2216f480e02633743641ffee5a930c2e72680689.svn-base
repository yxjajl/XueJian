package com.dh.resconfig;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseFazhenVO;

public class FaZhenRes extends BaseRes<BaseFazhenVO> {
	private static final Logger LOGGER = Logger.getLogger(FaZhenRes.class);
	public static final String Path = filePath + "csv/cfg_array.csv";

	private FaZhenRes() {
		classz = BaseFazhenVO.class;
	}

	private static FaZhenRes INSTANCE = new FaZhenRes();

	public static FaZhenRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("FAZHEN init finished");
	}

	/**
	 * 根据玩家等级刷怪物组 <br/>
	 * 2014年7月17日
	 * 
	 * @param level
	 *            玩家等级<br/>
	 * @author dingqu-pc100
	 */
	public BaseFazhenVO getFazhenById(int id) {
		for (BaseFazhenVO baseFazhenVO : INSTANCE.getDataList()) {
			if (baseFazhenVO.getCfgId() == id) {
				return baseFazhenVO;
			}
		}
		return null;
	}

	/**
	 * 获取同类型升级
	 * 
	 * @param type
	 * @param level
	 * @return
	 */
	public BaseFazhenVO getFaZhenByTypeAndLevel(int type, int level) {
		for (BaseFazhenVO baseFazhenVO : INSTANCE.getDataList()) {
			if (baseFazhenVO.getType() == type && baseFazhenVO.getLevel() == level) {
				return baseFazhenVO;
			}
		}
		return null;
	}

	public List<BaseFazhenVO> getAllFzByLevel(int level) {
		List<BaseFazhenVO> levelList = new ArrayList<BaseFazhenVO>();
		for (BaseFazhenVO baseFazhenVO : INSTANCE.getDataList()) {
			if (baseFazhenVO.getLevel() == level) {
				levelList.add(baseFazhenVO);
			}
		}
		return levelList;

	}

	public static void main(String[] args) throws Exception {
		FaZhenRes.getInstance().loadFile(Path);
		List<BaseFazhenVO> levelList = getInstance().getAllFzByLevel(1);
		for (BaseFazhenVO baseFazhenVO : levelList) {
			System.out.println("name: " + baseFazhenVO.getName() + "level:" + baseFazhenVO.getLevel());
		}

	}
}