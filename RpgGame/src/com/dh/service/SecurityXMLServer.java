package com.dh.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/***
 * Flash Socket通信的安全策略问题 前台flash文件要进行socket通信的时候 需要向服务器端获取crossdomain.xml文件
 * 这里就直接写在字符串里了
 * 
 * @author wup
 * 
 */
public class SecurityXMLServer implements Runnable {
	private static Logger LOGGER = Logger.getLogger(SecurityXMLServer.class);
	private ServerSocket server;
	private BufferedReader reader;
	private BufferedWriter writer;
	private String xml;

	public SecurityXMLServer() {
		/**
		 * 注意此处xml文件的内容，为纯字符串，没有xml文档的版本号
		 */
		xml = "<cross-domain-policy> " + "<allow-access-from domain=\"*\" to-ports=\"1025-29999\"/>" + "</cross-domain-policy> ";

		// 启动843端口
		createServerSocket(843);
		// new Thread(this).start();
	}

	public static void main(String[] args) {
		startXMLServer();
	}

	public static void startXMLServer() {
		ExecutorService exec = Executors.newCachedThreadPool();
		// new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
		// 50, 120L, TimeUnit.SECONDS, new
		// ArrayBlockingQueue<java.lang.Runnable>(10000));
		exec.execute(new SecurityXMLServer());
		// new SecurityXMLServer();
	}

	// 启动服务器
	private void createServerSocket(int port) {
		try {
			server = new ServerSocket(port);
			LOGGER.info("Flash secure socket listening on:" + port);
		} catch (Exception e) {
			System.exit(1);
		}
	}

	// 启动服务器线程
	public void run() {
		for (;;) {
			Socket client = null;
			try {
				// 接收客户端的连接
				client = server.accept();
				LOGGER.debug("accept SecurityXMLServer ========");
				reader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
				writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
				StringBuilder data = new StringBuilder();
				int c = 0;
				while ((c = reader.read()) != -1) {
					if (c != '\0')
						data.append((char) c);
					else
						break;
				}
				String info = data.toString();

				// String info = "<policy-file-request/>";

				// 接收到客户端的请求之后，将策略文件发送出去
				if (info.indexOf("<policy-file-request/>") >= 0) {
					writer.write(xml + "\0");
					writer.flush();
					LOGGER.debug("将安全策略文件发送至: " + client.getInetAddress());
				} else {
					writer.write(xml + "\0");
					writer.flush();
					LOGGER.debug("请求无法识别: " + client.getInetAddress());
				}

			} catch (Exception e) {
				LOGGER.error("请求error: " + client.getInetAddress(), e);
			} finally {
				try {
					if (client != null) {
						client.close();
						client = null;
					}
				} catch (IOException ex) {
					LOGGER.error("close fail " + ex.getMessage(), ex);
				}
			}
		}
	}
}