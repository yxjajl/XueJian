package com.dh.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import au.com.bytecode.opencsv.CSVReader;

public abstract class CsvAble {
	public static final byte UTF8HADER1 = (byte) 0xEF;
	public static final byte UTF8HADER2 = (byte) 0xBB;
	public static final byte UTF8HADER3 = (byte) 0xBF;
	public static String filePath = CsvAble.class.getClassLoader().getResource("").getPath().replaceAll("%20", " ");
	protected String[] filedName;
	protected String[] flags;

	/**
	 * 加载配置文件，并读取其中类容
	 * 
	 * @param path
	 * @return boolean
	 * @author:zqgame
	 * @date:2012-12-12
	 */
	public boolean loadFile(String path) {
		// 清理
		clear();
		CSVReader reader = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(path);
			if (in.markSupported()) {
				byte[] inHeader = new byte[3];
				in.read(inHeader, 0, 3);

				if (inHeader[0] != UTF8HADER1 || inHeader[1] != UTF8HADER2 || inHeader[2] != UTF8HADER3) {
					in.reset();
				}
			}
			InputStreamReader inputReader = new InputStreamReader(in, "GBK");
			reader = new CSVReader(inputReader);
			// 开始读
			read(reader);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			// 关闭流
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean reLoadFile(String path, String name) {

		if (name.trim().length() == 0 || this.getClass().getSimpleName().equalsIgnoreCase(name)) {
			return loadFile(path);
		}
		return false;
	}

	/**
	 * 
	 * 读取配置文件类容 读取每一行, 行从0开始，并滤过第一行描述说明
	 */

	protected abstract void read(CSVReader reader);

	// 清理
	protected abstract void clear();
}
