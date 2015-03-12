package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseHoleVO;

public class HoleRes extends BaseRes<BaseHoleVO> {
	private static final Logger LOGGER = Logger.getLogger(HoleRes.class);
	public static final String Path = filePath + "csv/cfg_hole.csv";

	private HoleRes() {
		classz = BaseHoleVO.class;
	}

	private static HoleRes INSTANCE = new HoleRes();

	public static HoleRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("HoleRes.otherInit");
	}

	/**
	 * 根据孔位置查开起条件
	 * 
	 * @param level
	 * @return
	 */
	public BaseHoleVO getBaseHoleVO(int pos) {
		if (pos < 0 || pos >= this.getDataList().size()) {
			return null;
		}

		return getDataList().get(pos);
	}

	public static void main(String[] args) throws Exception {
		HoleRes.getInstance().loadFile(HoleRes.Path);
		for(BaseHoleVO baseHoleVO:HoleRes.getInstance().getDataList()) {
			System.out.println(baseHoleVO.getCfgid()+"," + baseHoleVO.getItemid());
		}
	}
}