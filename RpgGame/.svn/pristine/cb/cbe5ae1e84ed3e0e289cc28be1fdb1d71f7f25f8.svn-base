package com.dh.resconfig;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dh.game.vo.base.BaseMachineVO;

public class MachineRes extends BaseRes<BaseMachineVO> {
	private static final Logger LOGGER = Logger.getLogger(MachineRes.class);
	public static final String Path = filePath + "csv/cfg_machine.csv";

	private MachineRes() {
		classz = BaseMachineVO.class;
	}

	private static MachineRes INSTANCE = new MachineRes();

	public static MachineRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("machine init finished");
	}

	/**
	 * 根据玩家等级刷怪物组 <br/>
	 * 2014年7月17日
	 * 
	 * @param level
	 *            玩家等级<br/>
	 * @author dingqu-pc100
	 */
	public BaseMachineVO getMachineById(int id) {
		for (BaseMachineVO baseMachineVO : INSTANCE.getDataList()) {
			if (baseMachineVO.getCfgId() == id) {
				return baseMachineVO;
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
	public BaseMachineVO getMachineByTypeAndLevel(int type, int level) {
		for (BaseMachineVO baseMachineVO : INSTANCE.getDataList()) {
			if (baseMachineVO.getType() == type && baseMachineVO.getLevel() == level) {
				return baseMachineVO;
			}
		}
		return null;
	}

	public List<BaseMachineVO> getAllMachineByLevel(int level) {
		List<BaseMachineVO> levelList = new ArrayList<BaseMachineVO>();
		for (BaseMachineVO baseMachineVO : INSTANCE.getDataList()) {
			if (baseMachineVO.getLevel() == level) {
				levelList.add(baseMachineVO);
			}
		}
		return levelList;
	}

	public static void main(String[] args) throws Exception {
		MachineRes.getInstance().loadFile(Path);
		BaseMachineVO temp = MachineRes.getInstance().getMachineById(10014);
		System.out.println("id:" + temp.getCfgId() + "name:" + temp.getName());
	}
}