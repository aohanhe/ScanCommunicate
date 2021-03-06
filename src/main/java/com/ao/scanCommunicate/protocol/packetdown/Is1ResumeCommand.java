package com.ao.scanCommunicate.protocol.packetdown;

import com.ao.scanCommunicate.protocol.Is1BaseDownPacket;
import com.ao.scanCommunicate.protocol.Is1GlobalConst;

import ahh.swallowIotServer.protocol.config.cmd.DownCommand;
import ahh.swallowIotServer.protocol.config.fields.ConstByteFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldFixValue;
import ahh.swallowIotServer.protocol.config.fields.IsWantCrc;

/**
 * 恢复充电下行包
 * 
 * @author aohan
 *
 */
@DownCommand(name = "恢复充电下行", protocolId = Is1GlobalConst.IS1_PROTOCOL, value = Is1GlobalConst.RESUME_COMMAND)
public class Is1ResumeCommand extends Is1BaseDownPacket {

	/**
	 * 单头地址，指示单头位置
	 * 
	 * @return
	 */
	@FieldConfig(order = 1, type = ConstByteFieldConfig.class)
	byte addr;

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

	public Is1ResumeCommand() {
		this.setCmd((byte) Is1GlobalConst.RESUME_COMMAND);
	}

	public Is1ResumeCommand(int addr) {
		this.setCmd((byte) Is1GlobalConst.RESUME_COMMAND);
		this.setAddr((byte) addr);
	}

	public byte getAddr() {
		return addr;
	}

	public void setAddr(byte addr) {
		this.addr = addr;
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
