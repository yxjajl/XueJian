package com.dh.resconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

import com.dh.util.CsvAble;
import com.dh.util.ReflectUtil;

public class BaseRes<T> extends CsvAble {
	private List<T> dataList = new ArrayList<T>();
	private Map<Integer, T> dataMap = new HashMap<Integer, T>();
	protected Class<T> classz;

	@Override
	protected void read(CSVReader reader) {
		if (reader != null) {
			String[] nextLine = null;
			int iCount = 0;

			T info = null;
			try {
				while ((nextLine = reader.readNext()) != null) {
					if (iCount == 0) {
						this.filedName = nextLine;
					} else if (iCount == 1) {
						this.flags = nextLine;
					} else if (iCount > 2) {

						if (!isSpaceLine(nextLine)) {
							// 从第二行开始
							info = classz.newInstance();
							for (int n = 0; n < filedName.length; n++) {
								if (filedName[n].trim().length() > 0 && flags[n].trim().length() == 0) {
									ReflectUtil.setter(filedName[n].trim(), info, (nextLine[n].trim()));
								}
							}

							dataList.add(info);
						}
					}
					iCount++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		otherInit();
	}

	public List<T> getDataList() {
		return dataList;
	}

	@Override
	protected void clear() {
		dataList.clear();
		dataMap.clear();
	}

	protected boolean isSpaceLine(String[] nextLine) {
		boolean result = true;
		for (String str : nextLine) {
			if (str.length() > 0) {
				result = false;
				break;
			}
		}

		return result;
	}

	/**
	 * 对数据进行一些其它的处理
	 */
	protected void otherInit() {
		// TODO Auto-generated method stub
	}

	public static void main(String[] args) throws Exception {
		// for (BaseCityInfo baseCityInfo : BaseRes.getInstance().getDataList())
		// {
		// System.out.println(baseCityInfo.getId() + "," +
		// baseCityInfo.getLevel() + "," + baseCityInfo.getOpen_taskid());
		// }
		// CityRes instance = CityRes.getInstance();
		// BaseCityInfo bb = new BaseCityInfo();
		// CityRes.setter2("name", bb, "rick");
		// CityRes.setter2("id", bb, "111");
		// System.out.println(bb.getId() + "," + bb.getName());
		// System.out.println(CityRes.getFiled("name", BaseCityInfo.class));
		// System.out.println(CityRes.geSetterByFieldName("name",
		// BaseCityInfo.class, CityRes.getFiled("name",
		// BaseCityInfo.class).getType()));

	}

	public Map<Integer, T> getDataMap() {
		return dataMap;
	}

}
