package com.dh.resconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.BaseProto.BUILDID;
import com.dh.game.vo.base.BaseBuildLevelVO;

//
public class BuildLevelRes extends BaseRes<BaseBuildLevelVO> {
	private static final Logger LOGGER = Logger.getLogger(BuildLevelRes.class);
	public static final String Path = filePath + "csv/cfg_buildlevel.csv";
	private Map<Integer, List<BaseBuildLevelVO>> buildLevelMap = new HashMap<Integer, List<BaseBuildLevelVO>>();
	private Map<Integer, Integer> posOpenMap = new HashMap<Integer, Integer>();
	private int maxPosition = 0; // 最大蒲团数

	private BuildLevelRes() {
		classz = BaseBuildLevelVO.class;
	}

	private static BuildLevelRes INSTANCE = new BuildLevelRes();

	public static BuildLevelRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("BuildLevelRes.otherInit");
		for (BaseBuildLevelVO baseBuildLevelVO : this.getDataList()) {

			List<BaseBuildLevelVO> list = buildLevelMap.get(baseBuildLevelVO.getBuildid());
			if (list == null) {
				list = new ArrayList<BaseBuildLevelVO>();
				buildLevelMap.put(baseBuildLevelVO.getBuildid(), list);
			}

			list.add(baseBuildLevelVO);

			Integer reqLevel = posOpenMap.get(baseBuildLevelVO.getGrid());
			if (reqLevel != null && reqLevel < baseBuildLevelVO.getLevel()) {
			} else {
				posOpenMap.put(baseBuildLevelVO.getGrid(), baseBuildLevelVO.getLevel());
			}

			if (maxPosition < baseBuildLevelVO.getGrid()) {
				maxPosition = baseBuildLevelVO.getGrid();
			}
		}

		this.getDataList().clear();
	}

	public int getMaxPosition() {
		return maxPosition;
	}

	public void setMaxPosition(int maxPosition) {
		this.maxPosition = maxPosition;
	}

	public int getPositionOpenLevel(int hangId) {
		Integer value = posOpenMap.get(hangId);
		if (value == null) {
			return 0;
		}
		return value;
	}

	public static String getBuildName(BUILDID id) {
		if (BUILDID.YISITANG == id) {
			return "议事堂";
		} else if (BUILDID.YANGXINGDIAN == id) {
			return "养心殿";
		} else {
			return "建筑";
		}
	}

	/**
	 * 查等级数据
	 * 
	 * @param level
	 * @return
	 */
	public BaseBuildLevelVO getBaseBuildLevelVO(int level, int buildid) {
		List<BaseBuildLevelVO> list = buildLevelMap.get(buildid);
		if (list != null) {
			for (BaseBuildLevelVO baseBuildLevelVO : list) {
				if (baseBuildLevelVO.getLevel() == level && baseBuildLevelVO.getBuildid() == buildid) {
					return baseBuildLevelVO;
				}
			}
		}
		return null;
	}

	@Override
	protected void clear() {
		// TODO Auto-generated method stub
		super.clear();

		buildLevelMap.clear();
		posOpenMap.clear();
	}

	public static void main(String[] args) throws Exception {
		BuildLevelRes.getInstance().loadFile(BuildLevelRes.Path);
		for (BaseBuildLevelVO baseBuildLevelVO : BuildLevelRes.getInstance().getDataList()) {
			System.out.println(baseBuildLevelVO.getLevel() + "," + baseBuildLevelVO.getBuildid() + "," + baseBuildLevelVO.getCd());
		}
		
		
		BaseBuildLevelVO baseBuildLevelVO = BuildLevelRes.getInstance().getBaseBuildLevelVO(3, 1);
		System.out.println(baseBuildLevelVO.getLevel() + "," + baseBuildLevelVO.getBuildid() + "," + baseBuildLevelVO.getCd());
		System.out.println(BuildLevelRes.getInstance().getMaxPosition());

		for (int i = 1; i <= BuildLevelRes.getInstance().getMaxPosition(); i++) {
			System.out.println(i + " 开启等级: " + BuildLevelRes.getInstance().getPositionOpenLevel(i));
		}
		
		for(int i = 1;i < 20; i++) {
			 baseBuildLevelVO = BuildLevelRes.getInstance().getBaseBuildLevelVO(i, 2);
			System.out.println(baseBuildLevelVO.getLevel() + "," + baseBuildLevelVO.getGrid());
		}
	}
}