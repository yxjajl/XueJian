package com.dh.util;

import java.util.Hashtable;
import java.util.StringTokenizer;

public class MyHttpUtil {
	public static Hashtable<String, String> parseQueryString(String s) {
		if (s == null) {
			throw new IllegalArgumentException();
		}
		int n = s.indexOf('?');
		if (n > -1) {
			s = s.substring(n + 1);
		}

		Hashtable<String, String> ht = new Hashtable<String, String>();
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(s, "&");
		while (st.hasMoreTokens()) {
			String pair = st.nextToken();
			int pos = pair.indexOf('=');
			if (pos == -1) {
				throw new IllegalArgumentException();
			}
			String key = parseName(pair.substring(0, pos), sb);
			String val = parseName(pair.substring(pos + 1, pair.length()), sb);
			if (ht.containsKey(key)) {
				System.err.println("repeated key");
			}
			ht.put(key, val);
		}
		return ht;
	}

	private static String parseName(String s, StringBuffer sb) {
		sb.setLength(0);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '+':
				sb.append(' ');
				break;
			case '%':
				try {
					sb.append((char) Integer.parseInt(s.substring(i + 1, i + 3), 16));

					i += 2;
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException();
				} catch (StringIndexOutOfBoundsException e) {
					String rest = s.substring(i);
					sb.append(rest);
					if (rest.length() == 2) {
						i++;
					}
				}
				break;
			default:
				sb.append(c);
			}
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		// sign=ZCKQN%2F0%2FBRNxzkrmK6GiwL1hyG8%3D
		String url = "http://localhost/cgi-bin/demo_provide.cgi?amt=0&appid=15499&billno=-APPDJ10153-20120809-1150429539&fee=10&fee_acct=0&fee_coins=10&fee_coins_save=10&fee_pubcoins=0&fee_pubcoins_save=0&openid=0000000000000000000000000E1E0000&payitem=50005*2*10&providetype=3&seller_openid=000000000000000000000000008FA509&token=2854C0C5BEC0AC942C020846C0D0B33129885&ts=1344484244&uni_appamt=200&version=v3&zoneid=1";

		Hashtable<String, String> hashtable = MyHttpUtil.parseQueryString(url);
		for (String key : hashtable.keySet()) {
			System.out.println(key + "," + hashtable.get(key));
		}
	}
}
