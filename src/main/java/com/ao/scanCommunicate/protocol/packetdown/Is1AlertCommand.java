package com.ao.scanCommunicate.protocol.packetdown;

import com.ao.scanCommunicate.protocol.Is1BaseDownPacket;
import com.ao.scanCommunicate.protocol.Is1GlobalConst;

import ahh.swallowIotServer.protocol.config.cmd.DownCommand;
import ahh.swallowIotServer.protocol.config.fields.ConstByteFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.ConstIntFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldFixValue;
import ahh.swallowIotServer.protocol.config.fields.IsWantCrc;

/**
 * 获取账单下行包
 * 
 * @author aohan
 *
 */
@DownCommand(name = "设置报警值下行", protocolId = Is1GlobalConst.IS1_PROTOCOL, value = Is1GlobalConst.ALERT_COMMAND)
public class Is1AlertCommand extends Is1BaseDownPacket {

	/**
	 * 单头地址，指示单头位置
	 * 
	 * @return
	 */
	@FieldConfig(order = 1, type = ConstByteFieldConfig.class)
	byte addr;

	/**
	 * 最大值
	 * 
	 * @return
	 */
	@FieldConfig(order = 2, type = ConstIntFieldConfig.class)
	int max;

	/**
	 * 最小值
	 * 
	 * @return
	 */
	@FieldConfig(order = 3, type = ConstIntFieldConfig.class)
	int min;

	/**
	 * crc校验位
	 */
	@FieldConfig(order = 4, type = ConstByteFieldConfig.class)
	@IsWantCrc(false)
	byte crcBit;

	/**
	 * 报文结束字节
	 */
	@FieldConfig(order = 5, type = ConstByteFieldConfig.class)
	@FieldFixValue(value = "0x57")
	@IsWantCrc(false)
	byte eoi;

	public Is1AlertCommand() {
		this.setCmd((byte) Is1GlobalConst.ALERT_COMMAND);
	}

	public Is1AlertCommand(int addr, int max, int min) {
		this.setCmd((byte) Is1GlobalConst.ALERT_COMMAND);
		this.setAddr((byte) addr);
		this.setMax(max);
		this.setMin(min);
	}

	public byte getAddr() {
		return addr;
	}

	public void setAddr(byte addr) {
		this.addr = addr;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
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
