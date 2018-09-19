package com.ao.scanCommunicate.protocol.packetdown;

import com.ao.scanCommunicate.protocol.Is1BaseDownPacket;
import com.ao.scanCommunicate.protocol.Is1GlobalConst;

import ahh.swallowIotServer.protocol.config.cmd.DownCommand;
import ahh.swallowIotServer.protocol.config.fields.ConstByteFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldFixValue;
import ahh.swallowIotServer.protocol.config.fields.IsWantCrc;

/**
 * 设备回应心跳下行包
 * 
 * @author aohan
 *
 */
@DownCommand(name = "设备回应心跳下行包", protocolId = Is1GlobalConst.IS1_PROTOCOL, value = Is1GlobalConst.HEARTBEAT_RESPONSE_COMMAND)
public class Is1HeartbeatResponseCommand extends Is1BaseDownPacket {

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

	public Is1HeartbeatResponseCommand() {
		this.setCmd((byte) Is1GlobalConst.HEARTBEAT_RESPONSE_COMMAND);
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
