package com.dh.resconfig;

import com.dh.game.vo.base.BaseGridVO;

public class GridRes extends BaseRes<BaseGridVO> {
	// private static final Logger LOGGER = Logger.getLogger(GridRes.class);
	public static final String Path = filePath + "csv/cfg_cloud.csv";

	private GridRes() {
		classz = BaseGridVO.class;
	}

	public void otherInit() {
		// LOGGER.info("BaseResourceVO.otherInit");
	}

	private static GridRes INSTANCE = new GridRes();

	public static GridRes getInstance() {
		return INSTANCE;
	}

	/**
	 * 格子id<br/>
	 * dingqu-pc100<br/>
	 * 2014年7月17日
	 */
	public BaseGridVO getGridById(int id) {
		for (BaseGridVO baseGridVO : INSTANCE.getDataList()) {
			if (baseGridVO.getCfgid() == id) {
				return baseGridVO;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		GridRes.getInstance().loadFile(GridRes.Path);
		BaseGridVO temp = GridRes.getInstance().getGridById(40);
		System.out.println("cfgId: " + temp.getCfgid() + ",level:" + temp.getOpenlevel());
	}

}
