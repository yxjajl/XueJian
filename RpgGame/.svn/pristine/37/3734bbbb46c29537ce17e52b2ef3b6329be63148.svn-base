package com.dh.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class SocketUtil {
	public static InetAddress getInetAddress() {

		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.out.println("unknown host!");
		}
		return null;

	}

	public static String getHostIp(InetAddress netAddress) {
		if (null == netAddress) {
			return null;
		}
		String ip = netAddress.getHostAddress(); // get the ip address
		return ip;
	}

	public static String getHostName(InetAddress netAddress) {
		if (null == netAddress) {
			return null;
		}
		String name = netAddress.getHostName(); // get the host address
		return name;
	}

	public static String getLocalIP() throws Exception {
		Enumeration<NetworkInterface> e1 = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
		while (e1.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface) e1.nextElement();
			if (ni.getName().equals("lo")) {
				continue;
			}
			// System.err.print(ni.getName());
			// System.err.print(": ");
			Enumeration<InetAddress> e2 = ni.getInetAddresses();
			while (e2.hasMoreElements()) {
				InetAddress ia = (InetAddress) e2.nextElement();
				if (ia instanceof Inet6Address)
					continue; // omit IPv6 address
				if (CodeTool.isEmpty(ia.getHostAddress())) {
					continue;
				} else {
					return ia.getHostAddress();
				}
				// System.err.print(ia.getHostAddress());
				// if (e2.hasMoreElements()) {
				// System.err.print(", ");
				// }
			}
		}

		return null;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(SocketUtil.getLocalIP());
		// InetAddress netAddress = getInetAddress();
		// System.out.println("host ip:" + getHostIp(netAddress));
		// System.out.println("host name:" + getHostName(netAddress));
		// Properties properties = System.getProperties();
		// Set<String> set = properties.stringPropertyNames(); //
		// 获取java虚拟机和系统的信息。
		// for (String name : set) {
		// System.out.println(name + ":" + properties.getProperty(name));
		// }
	}
}
