package com.ao.scanCommunicate.protocol.packetdown;

import com.ao.scanCommunicate.protocol.Is1BaseDownPacket;
import com.ao.scanCommunicate.protocol.Is1GlobalConst;

import ahh.swallowIotServer.protocol.config.cmd.DownCommand;
import ahh.swallowIotServer.protocol.config.fields.ConstByteFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldFixValue;
import ahh.swallowIotServer.protocol.config.fields.IsWantCrc;

/**
 * 注册TCP总机下行包
 * 
 * @author aohan
 *
 */
@DownCommand(name = "注册TCP总机下行", protocolId = Is1GlobalConst.IS1_PROTOCOL, value = Is1GlobalConst.REGISTER_COMMAND)
public class Is1RegisterCommand extends Is1BaseDownPacket {

	/**
	 * 验证结果,0：失败，1：成功
	 */
	@FieldConfig(order = 1, type = ConstByteFieldConfig.class)
	byte authResult;

	/**
	 * crc校验位
	 */
	@FieldConfig(order = 2, type = ConstByteFieldConfig.class)
	@IsWantCrc(false)
	byte crcBit;

	/**
	 * 报文结束字节
	 */
	@FieldConfig(order = 3, type = ConstByteFieldConfig.class)
	@FieldFixValue(value = "0x57")
	@IsWantCrc(false)
	byte eoi;

	public Is1RegisterCommand() {
		this.setCmd((byte) Is1GlobalConst.REGISTER_COMMAND);
	}

	public Is1RegisterCommand(int authResult) {
		this.setCmd((byte) Is1GlobalConst.REGISTER_COMMAND);
		this.setAuthResult((byte) authResult);
	}

	public byte getAuthResult() {
		return authResult;
	}

	public void setAuthResult(byte authResult) {
		this.authResult = authResult;
	}

	public byte getCrcBit() {
		return crcBit;
	}

	public void setCrcBit(byte crcBit) {
		this.crcBit = crcBit;
	}

	public byte getEoi() {
		return eoi;
	}

	public void setEoi(byte eoi) {
		this.eoi = eoi;
	}
}
