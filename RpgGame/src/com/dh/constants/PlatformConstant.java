package com.dh.constants;

public class PlatformConstant {
	public final static String QZONE = "qzone";
	public final static String PENGYOU = "pengyou";
	public final static String P9CB = "9cb";

	public static boolean isTXPlatform(String pf) {
		return QZONE.equalsIgnoreCase(pf) || PENGYOU.equalsIgnoreCase(pf);
	}

	public static boolean is9CBPlatform(String pf) {
		return P9CB.equalsIgnoreCase(pf);
	}
}
