package com.dh.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class SecSocket {
	Socket socket = null;
	DataInputStream inStream = null;
	DataOutputStream outStream = null;

	public SecSocket() {
		try {
			init();
			waitData();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
	}

	void init() throws Exception {
		// socket = new Socket("127.0.0.1" , 8888);
		// inStream = new DataInputStream(socket.getInputStream());
		// outStream = new DataOutputStream(socket.getOutputStream());
		// socket.setSoTimeout(3000);
	}

	static int nn = 0;

	void nettyDate() {

	}

	void waitData() {
		// while (true)
		{
			try {
				socket = new Socket("211.149.229.104", 843); // 182.254.184.29
				inStream = new DataInputStream(socket.getInputStream());
				outStream = new DataOutputStream(socket.getOutputStream());
				socket.setSoTimeout(300000);

				outStream.writeBytes("<policy-file-request/>" + '\0');
				outStream.flush();

				// Thread.sleep(1000);
				String str1 = inStream.readLine();
				System.out.println(nn++ + "]The client receive a string : " + str1 + ",,,,," + str1.length());
				socket.close();
				inStream.close();
				outStream.close();
			} catch (SocketException e) {
				System.out.println(e.toString());
				// break;
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.toString());
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 10; i++)
			new SecSocket();
	}
}
