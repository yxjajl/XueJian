package com.dh.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 禁词过滤器，目前只支持中文和英文的禁词
 * 
 * @author Max林炳忠
 * 
 */
@SuppressWarnings("resource")
public class BadWordsFilter {

	public static HashMap<String, HashSet<String>> table = new HashMap<String, HashSet<String>>();

	public static List<String> badWords = new ArrayList<String>();

	// 初始化禁词表，以HashMap结合HashSet存储，以禁词第一个字作为HashMap的key，后续字符作为HashSet的value
	static {
		try {
			String path = BadWordsFilter.class.getClassLoader().getResource("badwords.txt").getPath().replace("%20", " ");
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), "UTF-8"));
			String str = null;
			while ((str = reader.readLine()) != null) {
				str = str.trim();
				if ("".equals(str)) {
					continue;
				}
				badWords.add(str);
				HashSet<String> nodes = null;
				String start = str.substring(0, 1);
				if (table.containsKey(start)) {
					nodes = table.get(start);
					if (nodes == null) {
						nodes = new HashSet<String>();
					}
					nodes.add(str.substring(1));
					table.put(start, nodes);
				} else {
					if (str.length() == 1) {
						table.put(start, null);
					} else {
						nodes = new HashSet<String>();
						nodes.add(str.substring(1));
						table.put(start, nodes);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean containBadWord(String input) {
		for (String str : BadWordsFilter.badWords) {
			if (input.contains(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 过滤算法的实现<br/>
	 * 大写会自动转化为小写
	 * 
	 * @param input
	 * @return
	 */
	public static String filter(String input) {
		StringBuilder str = new StringBuilder(input.toLowerCase());
		int step = 0;
		int len = str.length();
		// 逐个检索，只扫描一次即可
		while (step < len) {
			String now = str.substring(step, step + 1);
			// 当前字符时某个禁词的第一字
			if (table.containsKey(now)) {
				HashSet<String> nodes = table.get(now);
				// 禁词只有一个字符直接覆盖即可
				if (nodes == null) {
					str = str.replace(step, step + 1, "*");
					step++;
				}
				// 多个字的禁词
				else {
					int max = Integer.MIN_VALUE;
					boolean flag = false;
					for (String node : nodes) {
						if (node.length() >= max && len - step - 1 >= node.length()) {
							max = node.length();
							if (node.equals(str.substring(step + 1, step + node.length() + 1))) {
								for (int i = 0; i < 1 + node.length(); i++) {
									str = str.replace(step + i, step + i + 1, "*");
								}
								flag = true;
								break;
							}
						}
					}
					if (flag) {
						step += max + 1;
						continue;
					}
					// 禁词较长，被比较的字符串较短，退出
					if (max == Integer.MIN_VALUE) {
						step++;
						continue;
					}
					int count = 0;
					int tmp = step + 1;
					HashMap<Integer, String> map = new HashMap<Integer, String>();
					StringBuilder sub = new StringBuilder();
					ArrayList<String> letters = new ArrayList<String>();
					// 过滤掉特殊字符和英文字母
					while (true) {
						if (count >= max || tmp >= len) {
							break;
						}
						if (str.charAt(tmp) >= 0 && str.charAt(tmp) <= 255 || str.charAt(tmp) >= 65281 && str.charAt(tmp) <= 65374) {
							// 记录英文字母
							if (str.charAt(tmp) >= 65 && str.charAt(tmp) <= 90 || str.charAt(tmp) >= 97 && str.charAt(tmp) <= 122) {
								letters.add(str.charAt(tmp) + "");
							}
							// 过滤掉所有的特殊字符和英文字母
							map.put(tmp, str.charAt(tmp) + "");
						}
						// 为中文或其他时
						else {
							sub.append(str.substring(tmp, tmp + 1));
							count++;
						}
						tmp++;
					}
					// 中文为空，并有英文字母，这时对英文字母进行检索
					if (sub.length() == 0 && letters.size() > 0) {
						for (int i = 0; i < letters.size() && i < max; i++) {
							sub.append(letters.get(i));
						}
						letters.clear();
						// 去掉英文字母
						while (true) {
							flag = false;
							for (Integer key : map.keySet()) {
								if (map.get(key).toCharArray()[0] >= 65 && map.get(key).toCharArray()[0] <= 90 || map.get(key).toCharArray()[0] >= 97 && map.get(key).toCharArray()[0] <= 122) {
									map.remove(key);
									flag = true;
									break;
								}
							}
							if (flag == false) {
								break;
							}
						}
					}
					// 检索比较
					flag = false;
					for (String node : nodes) {
						if (node.length() > sub.length()) {
							continue;
						}
						if (node.equals(sub.substring(0, node.length()))) {
							for (int i = step; i <= step + node.length(); i++) {
								// 跳过特殊字符或英文字母
								if (map.containsKey(i)) {
									step++;
								}
								str = str.replace(i, i + 1, "*");
							}
							step += node.length();
							flag = true;
							break;
						}
					}
					if (flag == false) {
						step++;
					}
				}
			} else {
				step++;
			}
		}
		return str.toString();
	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		System.out.println(filter("国民党"));
		System.out.println(System.currentTimeMillis());
		System.out.println(BadWordsFilter.containBadWord("国民党"));
	}
}