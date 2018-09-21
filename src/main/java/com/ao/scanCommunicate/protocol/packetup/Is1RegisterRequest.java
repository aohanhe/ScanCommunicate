package com.ao.scanCommunicate.protocol.packetup;

import com.ao.scanCommunicate.protocol.Is1UpHeader;
import com.ao.scanCommunicate.protocol.Is1GlobalConst;

import ahh.swallowIotServer.protocol.config.cmd.BaseCommand;
import ahh.swallowIotServer.protocol.config.cmd.UpCommand;
import ahh.swallowIotServer.protocol.config.fields.ConstByteArrayFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.ConstByteFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldFixValue;
import ahh.swallowIotServer.protocol.config.fields.FieldLen;
import ahh.swallowIotServer.protocol.config.fields.IsWantCrc;

/**
 * 注册TCP总机上行包
 * 
 * @author aohan
 *
 */
@UpCommand(name = "注册TCP总机上行", protocolId = Is1GlobalConst.IS1_PROTOCOL, value = Is1GlobalConst.REGISTER_RESPONSE)
public interface Is1RegisterRequest extends BaseCommand, Is1UpHeader {

	/**
	 * 总机编号
	 * 
	 * @return
	 */
	@FieldConfig(order = 1, type = ConstByteArrayFieldConfig.class)
	@FieldLen(8)
	byte[] getDeviceCode();

	/**
	 * crc校验位
	 * 
	 * @return
	 */
	@FieldConfig(order = 2, type = ConstByteFieldConfig.class)
	@IsWantCrc(false)
	byte getCrcBit();

	/**
	 * 报文结束字节
	 * 
	 * @return
	 */
	@FieldConfig(order = 3, type = ConstByteFieldConfig.class)
	@FieldFixValue(value = "0x57")
	@IsWantCrc(false)
	byte getEoi();

}
