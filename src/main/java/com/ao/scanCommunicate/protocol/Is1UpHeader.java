package com.ao.scanCommunicate.protocol;

import ahh.swallowIotServer.protocol.config.fields.ConstByteFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.FieldConfig;
import ahh.swallowIotServer.protocol.config.fields.IsWantCrc;
import ahh.swallowIotServer.protocol.config.fields.VarStartCharFieldConfig;
import ahh.swallowIotServer.protocol.config.fields.VarStartCharFieldExtParam;

/**
 * 上行指包头定义
 * 
 * @author aohanhe
 *
 */
public interface Is1UpHeader {
	/**
	 * 报文开始字节
	 * 
	 * @return
	 */
	@FieldConfig(order = 1, type = VarStartCharFieldConfig.class)
	@VarStartCharFieldExtParam(isSkipErrorData = true, startChar = (byte) 0x48)
	@IsWantCrc(false)
	byte getSoi();

	/**
	 * 命令字节
	 * 
	 * @return
	 */
	@FieldConfig(order = 2, type = ConstByteFieldConfig.class)
	byte getCmd();

}
