package com.ao.scanCommunicate.protocol.packetdown;

import com.ao.scanCommunicate.protocol.Is1BaseDownPacket;
import com.ao.scanCommunicate.protocol.Is1GlobalConst;

import ahh.swallowIotServer.protocol.config.cmd.DownCommand;
import ahh.swallowIotServer.protocol.config.fields.ConstByteFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldFixValue;
import ahh.swallowIotServer.protocol.config.fields.IsWantCrc;

/**
 * 设备回应充电完成下行指令包
 * 
 * @author aohan
 *
 */
@DownCommand(name = "设备回应充电完成下行指令包", protocolId = Is1GlobalConst.IS1_PROTOCOL, value = Is1GlobalConst.STOP_RESPONSE_COMMAND)
public class Is1StopResponseCommand extends Is1BaseDownPacket {

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

	public Is1StopResponseCommand() {
		this.setCmd((byte) Is1GlobalConst.STOP_RESPONSE_COMMAND);
	}

	public Is1StopResponseCommand(int addr) {
		this.setCmd((byte) Is1GlobalConst.STOP_RESPONSE_COMMAND);
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
