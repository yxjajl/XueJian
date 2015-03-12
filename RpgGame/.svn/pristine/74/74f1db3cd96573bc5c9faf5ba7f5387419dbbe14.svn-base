package com.dh.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import com.dh.game.constant.AlertEnum;

public class ProperytiesUtil {
	private static ResourceBundle ALERT_RESOURCE_BUNDLE = null;

	static {
		Locale myLocale = Locale.CHINA;// 获得系统默认的国家/语言环境
		ResourceBundle bundle = ResourceBundle.getBundle("alert", myLocale);
		ALERT_RESOURCE_BUNDLE = bundle;
	}

	public static Properties loadProperties(String path) throws Exception {
		InputStream in = new BufferedInputStream(new FileInputStream(path));
		Properties p = new Properties();
		p.load(in);
		return p;
	}

	public static String getAlertMsg(AlertEnum alertEnum) {
		String str = "";
		try {
			str = new String(ALERT_RESOURCE_BUNDLE.getString(alertEnum.toString()).getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			str = alertEnum.name();
		}
		return str;
	}
	
	public static String getAlertMsg(AlertEnum alertEnum,String...parameter) {
		String str = "";
		try {
			str = new String(ALERT_RESOURCE_BUNDLE.getString(alertEnum.toString()).getBytes("ISO8859-1"), "UTF-8");
			str = MessageFormat.format(str, parameter);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			str = alertEnum.name();
		}
		return str;
	}

	// public static String getAlertMsg(String key) {
	// String str = "";
	// try {
	// str = new
	// String(ALERT_RESOURCE_BUNDLE.getString(key).getBytes("ISO8859-1"),
	// "UTF-8");
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// str = key;
	// }
	// return str;
	// }

	public static void main(String[] args) throws Exception {
		System.out.println(ProperytiesUtil.getAlertMsg(AlertEnum.NOT_ENOUGH_VIPLEVEL));
	}
}
