package com.dh.netty;

import io.netty.channel.Channel;

public class NettyMessageVO {
	private short commandCode;
	private byte[] data;
	private Channel channel;
	private int userid;
	private long now;

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public short getCommandCode() {
		return commandCode;
	}

	public void setCommandCode(short commandCode) {
		this.commandCode = commandCode;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getDataLength() {
		if (data == null)
			return 0;
		return  data.length;
	}

	public long getNow() {
		return now;
	}

	public void setNow(long now) {
		this.now = now;
	}

}
