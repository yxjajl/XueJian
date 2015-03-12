package com.dh.resconfig;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseSkillVO;

public class SkillRes extends BaseRes<BaseSkillVO> {
	private static final Logger LOGGER = Logger.getLogger(SkillRes.class);
	public static final String Path = filePath + "csv/cfg_skill.csv";

	private SkillRes() {
		classz = BaseSkillVO.class;
	}

	private static SkillRes INSTANCE = new SkillRes();

	public static SkillRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("SkillRes.otherInit");
	}

	/**
	 * 查等级数据
	 * 
	 * @param level
	 * @return
	 */
	public BaseSkillVO getBaseSkillVO(int id) {
		for (BaseSkillVO BaseSkillVO : SkillRes.getInstance().getDataList()) {
			if (BaseSkillVO.getId() == id) {
				return BaseSkillVO;
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		SkillRes.getInstance().loadFile(SkillRes.Path);
		for (BaseSkillVO BaseSkillVO : SkillRes.getInstance().getDataList()) {
			System.out.println(BaseSkillVO.getId() + "," + BaseSkillVO.getDec());
		}
	}
}