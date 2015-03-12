package com.dh.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;

public class CodeTool {

	public static String arrayToString(int[] array, String splitor) {
		StringBuilder s = new StringBuilder();
		for (int i : array) {
			s.append(i).append(splitor);
		}
		return s.toString();
	}

	public static int[] StringToIntArray(String dayDoneStr, String splitor) {
		String[] array = dayDoneStr.split(splitor);
		int[] intArray = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			intArray[i] = Integer.parseInt(array[i]);
		}
		return intArray;
	}

	public static int[] StringToIntArray4Timer(String dayDoneStr, String splitor) {
		String[] array = dayDoneStr.split(splitor);
		int[] intArray = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			intArray[i] = Integer.parseInt(array[i]);
		}
		return intArray;
	}

	/**
	 * 往map里的list里放东西
	 * 
	 * @param map
	 * @param t
	 * @param reward
	 */
	public static <T, R> void putListValue(Map<T, List<R>> map, T t, R r) {
		List<R> list = map.get(t);
		if (list == null) {
			list = new ArrayList<R>();
			map.put(t, list);
		}
		list.add(r);
	}

	/**
	 * 空判定
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(List list) {
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(List list) {
		if (list == null || list.size() == 0) {
			return false;
		}
		return true;
	}

	public static boolean isNotEmpty(String str) {
		if (str == null || str.trim().length() == 0) {
			return false;
		}
		return true;
	}

	public static int equalsZero(int value, int limit, int result) {
		if (value == limit) {
			return result;
		}
		return value;
	}

	public static String getSetValue(Set<String> set, int n) {
		int i = 0;
		for (String str : set) {
			if (i == n) {
				return str;
			}
			i++;
		}
		return null;
	}

	/**
	 * 将char强转为byte只留下低八位
	 * 
	 * @param chars
	 * @return
	 */
	public static byte[] transCharsTobytes(char[] chars) {
		byte[] byteArray = new byte[chars.length];
		for (int i = 0; i < byteArray.length; i++) {
			byteArray[i] = (byte) chars[i];
		}

		return byteArray;
	}

	public static char[] decodeStrToChars(String str) {
		char[] charArray = str.toCharArray();
		return charArray;
	}

	public static String encodeCharsToStr(char[] chars) {
		return new String(chars);
	}

	public static byte[] decodeStrToBytes(String str) {
		byte[] bytes = new byte[81];
		String[] strArray = str.split(",");
		for (int i = 0; i < strArray.length; i++) {
			if (strArray[i].equalsIgnoreCase("1")) {
				bytes[i] = 1;
			}
		}
		return bytes;

	}

	public static String encodeBytesToStr(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] == 1) {
				sb.append(1);
			} else {
				sb.append("0");
			}
			sb.append(",");
		}

		return sb.toString();
	}

	public static String[] decodeStrArray(String src) {
		List<String> strList = JSONArray.parseArray(src, String.class);
		String[] strArray = strList.toArray(new String[0]);
		strList.toArray(strArray);
		return strArray;
	}

	public static List<String> decodeStrList(String src) {
		return JSONArray.parseArray(src, String.class);
	}

	public static String encodeStrArray(String[] strArray) {
		return JSONArray.toJSONString(strArray);
	}

	public static String encodeStrList(List<String> strList) {
		return JSONArray.toJSONString(strList);
	}

	public static String encodeIntArray(int[] nums) {
		return JSONArray.toJSONString(nums);
	}

	public static int[] decodeIntArray(String src) {
		List<Integer> numList = JSONArray.parseArray(src, Integer.class);
		int[] intArray = new int[numList.size()];
		for (int i = 0; i < intArray.length; i++) {
			intArray[i] = numList.get(i);
		}
		return intArray;
	}
}
