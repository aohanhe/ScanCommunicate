package com.ao.scanCommunicate.protocol;

import ahh.swallowIotServer.protocol.config.fields.ConstByteFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldConfig;
import ahh.swallowIotServer.protocol.config.fields.IsWantCrc;
import ahh.swallowIotServer.protocol.config.fields.VarStartCharFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.VarStartCharFieldExtParam;

/**
 * 下午包头定义
 * 
 * @author aohanhe
 *
 */
public class Is1DownHeader {

	/**
	 * 报文开始字节
	 * 
	 * @return
	 */
	@FieldConfig(order = 1, type = VarStartCharFieldConfig.class)
	@VarStartCharFieldExtParam(isSkipErrorData = true, startChar = (byte) 0x48)
	@IsWantCrc(false)
	byte soi;

	/**
	 * 命令字节
	 * 
	 * @return
	 */
	@FieldConfig(order = 2, type = ConstByteFieldConfig.class)
	byte cmd;

	public byte getSoi() {
		return soi;
	}

	public void setSoi(byte soi) {
		this.soi = soi;
	}

	public byte getCmd() {
		return cmd;
	}

	public void setCmd(byte cmd) {
		this.cmd = cmd;
	}

}
