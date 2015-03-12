package com.dh.main;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.dh.Cache.JedisTool;
import com.dh.constants.TXConstants;
import com.dh.netty.NettyServer;
import com.dh.util.MyClassLoaderUtil;
import com.dh.util.ProperytiesUtil;
import com.dh.util.SocketUtil;

public class GateWayMain {
	private static Logger logger = Logger.getLogger(GateWayMain.class);
	public static ApplicationContext ctx = null;
	private static int serverAdd = -1;// 根据当前服务器id判断增加数目

	public static void main(String[] args) throws Exception {
		GateWayMain.run();
	}

	public static void run() throws Exception {
		configRmiHost();
		String osName = System.getProperty("os.name");
		NettyServer nettyServer = null;

		if (osName.startsWith("Windows")) {
			PropertyConfigurator.configure("config/log4j.properties");
			ctx = new FileSystemXmlApplicationContext("config/applicationContext.xml");
		} else {
			PropertyConfigurator.configure("log4j.properties");
			ctx = new FileSystemXmlApplicationContext("applicationContext.xml");

		}

		initS2sSocket();
		MyClassLoaderUtil.getInstance().loadApplicationContext(ctx);
		// MyClassLoaderUtil.getInstance().getTaskCheck().init(ctx);
		nettyServer = (NettyServer) ctx.getBean("nettyServer");
		try {
			logger.info("=============server start===============1=");
			System.err.println("GateWayMain port= " + TXConstants.netty_port);
			nettyServer.run(TXConstants.netty_port);
			logger.info("=============server start===============2=");
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void initS2sSocket() {
		try {
			initServerNum();
			String strpro = null;
			String osName = System.getProperty("os.name");
			if (osName.startsWith("Windows")) {
				strpro = "config\\server.properties";
			} else {
				strpro = "server.properties";
			}
			Properties properties = ProperytiesUtil.loadProperties(strpro);

			JedisTool.serverip = (String) properties.get("redis_server_ip");
			JedisTool.TSTPSW = (String) properties.get("redis_server_pwd");
			JedisTool.dbIndex = Integer.valueOf((String) properties.get("redis_server_db")) + serverAdd;
			TXConstants.tx_zone_id = (String) properties.get("tx_zone_id");
			TXConstants.my_server_id = Integer.valueOf((String) properties.get("my_server_id")) + serverAdd;
			TXConstants.netty_port = Integer.valueOf((String) properties.get("server.port")) + serverAdd;
		} catch (Exception e) {
			logger.error("无法连接远程服务器", e);
		} finally {
		}
		// connectCenterServer();
	}

	public static void configRmiHost() throws Exception {
		String ip = SocketUtil.getLocalIP();
		System.setProperty("java.rmi.server.hostname", ip);
		System.err.println("java.rmi.server.hostname = " + ip);
	}

	/**
	 * 通过类名字获取增量
	 * */
	private static void initServerNum() {
		// Pattern pattern = Pattern.compile("^([a-z.A-Z]+)([\\d]+)$");
		Pattern pattern = Pattern.compile("^\\S+?([\\d]+)$");
		String name = Thread.currentThread().getStackTrace()[Thread.currentThread().getStackTrace().length - 1].getClassName();
		logger.info("================" + name + "====================");
		Matcher m = pattern.matcher(name);
		if (m.matches()) {
			serverAdd = Integer.parseInt(m.group(m.groupCount()));
			serverAdd = serverAdd - 1;
		}
		if (serverAdd == -1) {
			System.err.println("服务器配置错误");
			System.exit(1);
		}
	}

}
