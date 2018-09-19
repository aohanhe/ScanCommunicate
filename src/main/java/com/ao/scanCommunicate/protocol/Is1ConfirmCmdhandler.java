package com.ao.scanCommunicate.protocol;

import ahh.swallowIotServer.protocol.ConfirmUpCommandHandler;

/**
 * 驿联2.0上午指令确定处理器
 * @author aohanhe
 *
 */
public class Is1ConfirmCmdhandler implements ConfirmUpCommandHandler{

	@Override
	public int confirmUpComandByValue(Object obValue) {
		byte value=(byte)obValue;
		return value&0xff;
	}

}
