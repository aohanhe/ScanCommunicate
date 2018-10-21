package com.ao.scanCommunicate.protocol.packetup;

import com.ao.scanCommunicate.protocol.Is1UpHeader;
import com.ao.scanCommunicate.protocol.Is1GlobalConst;

import ahh.swallowIotServer.protocol.config.cmd.BaseCommand;
import ahh.swallowIotServer.protocol.config.cmd.UpCommand;
import ahh.swallowIotServer.protocol.config.fields.ConstByteFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.ConstIntFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.ConstShortFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldFixValue;
import ahh.swallowIotServer.protocol.config.fields.IsWantCrc;

/**
 * 充电完成上行包
 * 
 * @author aohan
 *
 */
@UpCommand(name = "充电完成上行", protocolId = Is1GlobalConst.IS1_PROTOCOL, value = Is1GlobalConst.FINISH_RESPONSE)
public interface Is1FinishResponse extends BaseCommand, Is1UpHeader {

	/**
	 * 单头地址，指示单头位置
	 * 
	 * @return
	 */
	@FieldConfig(order = 1, type = ConstByteFieldConfig.class)
	byte getAddr();

	/**
	 * 充电时长（分钟）
	 * 
	 * @return
	 */
	@FieldConfig(order = 2, type = ConstShortFieldConfig.class)
	short getMinutes();

	/**
	 * 充电量
	 * 
	 * @return
	 */
	@FieldConfig(order = 3, type = ConstIntFieldConfig.class)
	String getEnergy();

	/**
	 * crc校验位
	 * 
	 * @return
	 */
	@FieldConfig(order = 4, type = ConstByteFieldConfig.class)
	@IsWantCrc(false)
	byte getCrcBit();

	/**
	 * 报文结束字节
	 * 
	 * @return
	 */
	@FieldConfig(order = 5, type = ConstByteFieldConfig.class)
	@FieldFixValue(value = "0x57")
	@IsWantCrc(false)
	byte getEoi();

}
