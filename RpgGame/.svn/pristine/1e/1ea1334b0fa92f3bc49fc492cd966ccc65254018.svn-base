package com.dh.resconfig;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.dh.game.vo.base.BaseTechVO;
import com.dh.util.CsvAble;

public class TechRes extends CsvAble {
	private static final Logger LOGGER = Logger.getLogger(SoldierRes.class);
	public static final String Path = filePath + "csv/cfg_technology.csv";

	private List<BaseTechVO> techList = new ArrayList<BaseTechVO>();

	private TechRes() {
	}

	private static TechRes instance = new TechRes();

	public static TechRes getInstance() {
		return instance;
	}

	@Override
	protected void read(CSVReader reader) {
		if (reader != null) {
			String[] nextLine = null;
			int iCount = 0;
			BaseTechVO baseTechVO = null;
			try {
				while ((nextLine = reader.readNext()) != null) {
					if (iCount > 1) {
						// 从第3行开始
						baseTechVO = new BaseTechVO();
						int n = 0;
						baseTechVO.setId(Integer.parseInt(nextLine[n++].trim()));
						baseTechVO.setName((nextLine[n++].trim()));
						baseTechVO.setUrl(Integer.parseInt(nextLine[n++].trim()));
						baseTechVO.setType(Integer.parseInt(nextLine[n++].trim()));
						baseTechVO.setLevel(Integer.parseInt(nextLine[n++].trim()));
						baseTechVO.setParams(Integer.parseInt(nextLine[n++].trim()));
						baseTechVO.setCost(Integer.parseInt(nextLine[n++].trim()));
						baseTechVO.setCd(Integer.parseInt(nextLine[n++].trim()));
						baseTechVO.setDesc((nextLine[n++].trim()));
						techList.add(baseTechVO);
						if (techList.size() > 1) {// 下一级升级可以提升量
							techList.get(techList.size() - 2).setNextParams(baseTechVO.getParams());
						}
					}
					iCount++;
				}
			} catch (Exception e) {
				LOGGER.error("csvable read error!!!path:" + Path, e);
			}

		}

	}

	/**
	 * 通过科技类型和等级获取基本科技信息
	 * 
	 * @param level
	 * @param type
	 * @return
	 */
	public BaseTechVO getTechByLevelType(int level, int type) {
		for (BaseTechVO baseTechVO : techList) {
			if (baseTechVO.getType() == type && baseTechVO.getLevel() == level) {
				return baseTechVO;
			}
		}
		return null;
	}

	@Override
	protected void clear() {
		techList.clear();
	}

	public static void main(String[] args) {
		TechRes.getInstance().loadFile(TechRes.Path);
		System.out.println(TechRes.getInstance().getTechByLevelType(10, 10).getNextParams());
	}
}
