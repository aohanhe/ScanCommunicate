package com.ao.scanCommunicate.protocol;

import java.util.ArrayList;
import java.util.List;


import ahh.swallowIotServer.exception.IotServerConfigException;
import ahh.swallowIotServer.protocol.config.cmd.BaseCommand;

public class Is1BaseDownPacket extends Is1DownHeader implements BaseCommand {

	// 头部分固定的长度
	public static final short headerFixLen = 3;

	@Override
	public int getCmdId() {
		// return super.getCmd();
		byte value = getCmd();
		return value & 0xff;
	}

	@Override
	public String getCmdName() {
		return null;
	}

	@Override
	public boolean isCrcOk() {
		return true;
	}

	@Override
	public boolean hasDecodeError() {
		return false;
	}

	@Override
	public List<String> getDecodeErrors() {
		return new ArrayList<String>();
	}

	@Override
	public void onCheck() throws IotServerConfigException {
		// if (this.getCmd() <= 0)
		// throw new IotServerConfigException("没有设置指令的类型值");
	}

}
