package com.dh.s2s.vo;

public class CommandServerSettingVO {
	short commandHeader;// 指令头
	short serverid;// 逻辑服务器id

	public short getCommandHeader() {
		return commandHeader;
	}

	public void setCommandHeader(short commandHeader) {
		this.commandHeader = commandHeader;
	}

	public short getServerid() {
		return serverid;
	}

	public void setServerid(short serverid) {
		this.serverid = serverid;
	}

}
