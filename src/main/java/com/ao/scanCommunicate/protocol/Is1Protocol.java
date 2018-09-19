package com.ao.scanCommunicate.protocol;


import ahh.swallowIotServer.protocol.config.ConfirmUpCmdhandler;
import ahh.swallowIotServer.protocol.config.HasHeaderProtocol;
import ahh.swallowIotServer.protocol.config.NetType;
import ahh.swallowIotServer.protocol.config.ProtocoCrcHandler;
import ahh.swallowIotServer.protocol.config.header.DownHeader;
import ahh.swallowIotServer.protocol.config.header.UpHeader;
import ahh.swallowIotServer.protocol.hasheader.BaseHasHeaderProtocol;

/**
 * 智能插座协议1.0处理类
 * 
 * @author aohanhe
 *
 */
@HasHeaderProtocol(name = "智能插座协议1.0", netType = NetType.TCP, portConfig = "protocl.is1.port", value = Is1GlobalConst.IS1_PROTOCOL)
@UpHeader(Is1UpHeader.class)
@DownHeader(Is1DownHeader.class)
@ConfirmUpCmdhandler(confirmCmdhandler = Is1ConfirmCmdhandler.class, value = "cmd")
@ProtocoCrcHandler(value = Is1CrcHandler.class, upCrcFieldName = "crcBit", downCrcFieldName = "crcBit")
public class Is1Protocol extends BaseHasHeaderProtocol implements IGetProtocolRmControl {
	public static short SEQ_NOTSYNC = (short) 0xFFFF;

	private Is1Controller control = new Is1Controller();

	@Override
	public BaseController getDeviceControl() {
		return control;
	}
}
