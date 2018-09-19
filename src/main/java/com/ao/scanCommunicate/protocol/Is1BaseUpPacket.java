package com.ao.scanCommunicate.protocol;

public interface Is1BaseUpPacket {
	/**
	 * crc校验
	 * 
	 * @return
	 */
	// byte crc();

	/**
	 * 判断是否通过crc校验
	 * 
	 * @return
	 */
	// boolean passCrc();

	/**
	 * crc校验位
	 * 
	 * @return
	 */
	// @FieldConfig(order = 20, type = ConstByteFieldConfig.class)
	// @IsWantCrc(false)
	// byte getCrc();

	/**
	 * 报文结束字节
	 * 
	 * @return
	 */
	// @FieldConfig(order = 30, type = ConstByteFieldConfig.class)
	// @FieldFixValue(value = "0x57")
	// @IsWantCrc(false)
	// byte getEoi();
}
