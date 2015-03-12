package com.dh.main.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dh.resconfig.BaseRes;
import com.dh.util.RandomUtil;

public class LoadHeroRes extends BaseRes<LoadHeroVO> {
	private static final Logger LOGGER = Logger.getLogger(LoadHeroRes.class);
	public static final String Path = filePath + "csv/cfg_loaddata.csv";

	private LoadHeroRes() {
		classz = LoadHeroVO.class;
	}

	private static LoadHeroRes INSTANCE = new LoadHeroRes();

	public static LoadHeroRes getInstance() {
		return INSTANCE;
	}

	public void otherInit() {
		LOGGER.info("LoadHeroRes.otherInit");
	}

	public List<LoadHeroVO> random() {
		List<LoadHeroVO> result = new ArrayList<LoadHeroVO>(5);
		List<Integer> oldList = new ArrayList<Integer>();
		for (int i = 0; i < 5; i++) {
			int random = RandomUtil.randomInt(getDataList().size());
			LoadHeroVO loadHeroVO = getDataList().get(random);
			while (oldList.contains(loadHeroVO.getCfgid())) {
				random = RandomUtil.randomInt(getDataList().size());
				loadHeroVO = getDataList().get(random);
			}
			result.add(loadHeroVO);
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		LoadHeroRes.getInstance().loadFile(LoadHeroRes.Path);
//		 for (LoadHeroVO loadHeroVO : LoadHeroRes.getInstance().getDataList()) {
//		 System.out.println(loadHeroVO.getLevel() + "," + loadHeroVO.getName());
//		 }
		int cumCombat = 0;
		for (LoadHeroVO loadHeroVO : LoadHeroRes.getInstance().random()) {
			cumCombat += loadHeroVO.getCombat();
			System.out.println(loadHeroVO.getCfgid() + "," + loadHeroVO.getCombat() + "," + loadHeroVO.getLevel() + "," + loadHeroVO.getStar());
		}
		System.out.println("cumCombat = " + cumCombat);
	}
}
