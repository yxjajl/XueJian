package com.dh.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 常用工具类
 * 
 * @author zqgame
 * @date 2013-9-24
 */
public class CommonUtils {
	/**
	 * 将string List转化为String
	 * 
	 * @param spliChar
	 *            分隔符
	 * */
	public static String transListToString(List<String> strs, String spliChar) {
		StringBuffer tempStr = new StringBuffer();
		if (strs != null) {
			for (String string : strs) {
				tempStr.append(string + ",");
			}
		}
		return tempStr.toString();
	}

	/**
	 * @param splitChar
	 *            分隔符
	 * */
	public static List<String> transStringToList(String str, String splitChar) {
		List<String> list = Arrays.asList(str.split(splitChar));
		List<String> strs = new ArrayList<String>(list);
		return strs;
	}

	// 将数字转化为大写
	public static String transNumber(int num) {
		switch (num) {
		case 1:
			return "一";
		case 2:
			return "二";
		case 3:
			return "三";
		case 4:
			return "四";
		case 5:
			return "五";
		case 6:
			return "六";
		case 7:
			return "七";
		case 8:
			return "八";
		case 9:
			return "九";
		case 10:
			return "十";
		default:
			return " ";
		}

	}

	/**
	 * 字数长度计算(中文2个字符,英文1个字符)
	 * 
	 * @param content
	 *            原字符串
	 * @return length 计算后的字符长度
	 * */
	public static int calWordsLenth(String content) {
		int length = 0;
		char[] strChar = content.toCharArray();
		for (int i = 0; i < strChar.length; i++) {
			if ((strChar[i] + "").getBytes().length > 1) {
				length += 2;
			} else {
				length += 1;
			}
		}
		return length;
	}

	/**
	 * 计算并截断(中文2个字符,英文1个字符),用于混合输入
	 * 
	 * @param content
	 *            原字符串
	 * @param limitLenth
	 *            最大字符串长度
	 * @return length 截断后字符串
	 * */
	public static String cutWordsLenth(String content, int limitLenth) {
		if (content.length() <= limitLenth / 2) {
			return content;
		}
		int length = 0;
		char[] strChar = content.toCharArray();
		for (int i = 0; i < strChar.length; i++) {
			if ((strChar[i] + "").getBytes().length > 1) {
				length += 2;
			} else {
				length += 1;
			}
			if (length > limitLenth) {// 多余长度自动截断
				return new String(strChar, 0, i);
			}
		}
		return content;
	}
}
