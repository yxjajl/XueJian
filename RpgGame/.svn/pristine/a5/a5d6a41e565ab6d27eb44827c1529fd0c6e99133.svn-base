package com.dh.resconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.PassivesSkillVO;

public class PassivesSkillRes extends BaseRes<PassivesSkillVO> {
	private final static Logger LOGGER = Logger.getLogger(PassivesSkillRes.class);
	public final static String Path = filePath + "csv/cfg_passiveskill.csv";
	public final static int[] openLevel = { 1, 2, 4 };

	private Map<Integer, List<PassivesSkillVO>> skillmap = new HashMap<Integer, List<PassivesSkillVO>>();

	private PassivesSkillRes() {
		classz = PassivesSkillVO.class;
	}

	@Override
	protected void clear() {
		super.clear();
		skillmap.clear();
	}

	private static PassivesSkillRes INSTANCE = new PassivesSkillRes();

	public static PassivesSkillRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("PassivesSkillRes.otherInit");
		for (PassivesSkillVO passivesSkillVO : this.getDataList()) {
			List<PassivesSkillVO> list = skillmap.get(passivesSkillVO.getId());
			if (list == null) {
				list = new ArrayList<PassivesSkillVO>();
				skillmap.put(passivesSkillVO.getId(), list);
			}
			list.add(passivesSkillVO);
		}

	}

	public PassivesSkillVO getPassivesSkillVO(int cfgId, int level) {
		if (cfgId <= 0) {
			return null;
		}
		List<PassivesSkillVO> list = skillmap.get(cfgId);
		if (list != null && level >= 1 && level < list.size()) {
			return list.get(level - 1);
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		// BuffRes.getInstance().loadFile(BuffRes.Path);
		PassivesSkillRes.getInstance().loadFile(PassivesSkillRes.Path);
		// for (PassivesSkillVO passivesSkillVO : PassivesSkillRes.getInstance().getDataList()) {
		// System.out.println(passivesSkillVO.getId() + "," + passivesSkillVO.getDec());
		// }
	}
}
