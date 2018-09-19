package com.ao.scanCommunicate.protocol.packetdown;

import com.ao.scanCommunicate.protocol.Is1BaseDownPacket;
import com.ao.scanCommunicate.protocol.Is1GlobalConst;

import ahh.swallowIotServer.protocol.config.cmd.DownCommand;
import ahh.swallowIotServer.protocol.config.fields.ConstByteFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.ConstShortFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldFixValue;
import ahh.swallowIotServer.protocol.config.fields.IsWantCrc;

/**
 * 开始充电下行包
 * 
 * @author aohan
 *
 */
@DownCommand(name = "开始充电下行", protocolId = Is1GlobalConst.IS1_PROTOCOL, value = Is1GlobalConst.CHARGE_COMMAND)
public class Is1ChargeCommand extends Is1BaseDownPacket {
	/**
	 * 单头地址，指示单头位置
	 * 
	 * @return
	 */
	@FieldConfig(order = 1, type = ConstByteFieldConfig.class)
	byte addr;

	/**
	 * 充电时长（分钟）
	 * 
	 * @return
	 */
	@FieldConfig(order = 2, type = ConstShortFieldConfig.class)
	short minutes;

	/**
	 * crc校验位
	 */
	@FieldConfig(order = 3, type = ConstByteFieldConfig.class)
	@IsWantCrc(false)
	byte crcBit;

	/**
	 * 报文结束字节
	 */
	@FieldConfig(order = 4, type = ConstByteFieldConfig.class)
	@FieldFixValue(value = "0x57")
	@IsWantCrc(false)
	byte eoi;

	public Is1ChargeCommand() {
		this.setCmd((byte) Is1GlobalConst.CHARGE_COMMAND);
	}

	public Is1ChargeCommand(int addr, short minutes) {
		this.setCmd((byte) Is1GlobalConst.CHARGE_COMMAND);
		this.setAddr((byte) addr);
		this.setMinutes(minutes);
	}

	public byte getAddr() {
		return addr;
	}

	public void setAddr(byte addr) {
		this.addr = addr;
	}

	public short getMinutes() {
		return minutes;
	}

	public void setMinutes(short minutes) {
		this.minutes = minutes;
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
